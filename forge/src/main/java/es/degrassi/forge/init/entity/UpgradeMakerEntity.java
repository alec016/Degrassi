package es.degrassi.forge.init.entity;

import es.degrassi.forge.init.block.FurnaceBlock;
import es.degrassi.forge.init.block.UpgradeMaker;
import es.degrassi.forge.init.entity.type.*;
import es.degrassi.forge.init.gui.component.*;
import es.degrassi.forge.init.handlers.ItemWrapperHandler;
import es.degrassi.forge.init.recipe.helpers.RecipeHelpers;
import es.degrassi.forge.init.recipe.recipes.UpgradeMakerRecipe;
import es.degrassi.forge.init.registration.EntityRegister;
import es.degrassi.forge.init.registration.ItemRegister;
import es.degrassi.forge.integration.config.DegrassiConfig;
import es.degrassi.forge.network.EnergyPacket;
import es.degrassi.forge.network.FluidPacket;
import es.degrassi.forge.network.ItemPacket;
import es.degrassi.forge.network.ProgressPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class UpgradeMakerEntity extends BaseEntity implements IEnergyEntity, IRecipeEntity<UpgradeMakerRecipe>, IFluidEntity, IProgressEntity, IItemEntity {
  public UpgradeMaker delegate;
  private UpgradeMakerRecipe recipe;
  private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
  private LazyOptional<EnergyComponent> lazyEnergyHandler = LazyOptional.empty();
  private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();
  private final EnergyComponent ENERGY_STORAGE = new EnergyComponent(getComponentManager(), DegrassiConfig.get().upgradeMakerConfig.upgrade_maker_capacity, DegrassiConfig.get().upgradeMakerConfig.upgrade_maker_transfer) {
    @Override
    public boolean canExtract() {
      return false;
    }

    @Override
    public void onChanged() {
      super.onChanged();
      if (level != null && level.getServer() != null && !level.isClientSide())
        new EnergyPacket(this.energy, this.capacity, this.maxReceive, getBlockPos())
          .sendToChunkListeners(level.getChunkAt(getBlockPos()));
    }
  };
  private final ProgressComponent progressComponent = new ProgressComponent(getComponentManager(), 0) {
    @Override
    public void onChanged() {
      super.onChanged();
      if (level != null && !level.isClientSide())
        new ProgressPacket(this.progress, this.maxProgress, getBlockPos())
          .sendToChunkListeners(level.getChunkAt(getBlockPos()));
    }
  };
  private final ItemStackHandler itemHandler = new ItemStackHandler(5) {
    @Override
    protected void onContentsChanged(int slot) {
      getComponentManager().markDirty();
      if (level != null && !level.isClientSide() && level.getServer() != null) {
        new ItemPacket(this, worldPosition)
          .sendToAll(level.getServer());
      }
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
      return switch(slot) {
        case 0 -> stack.is(ItemRegister.SPEED_UPGRADE.get());
        case 1 -> stack.is(ItemRegister.ENERGY_UPGRADE.get());
        case 2 -> {
          AtomicBoolean has = new AtomicBoolean(false);
          RecipeHelpers.UPGRADE_MAKER.recipes.forEach(recipe -> {
            if (recipe.getIngredients().get(0).getItems()[0].is(stack.getItem())) has.set(true);
          });
          yield has.get();
        }
        case 3 -> {
          AtomicBoolean has = new AtomicBoolean(false);
          RecipeHelpers.UPGRADE_MAKER.recipes.forEach(recipe -> {
            if (recipe.getIngredients().get(1).getItems()[0].is(stack.getItem())) has.set(true);
          });
          yield has.get();
        }
        default -> false;
      };
    }
  };
  private final FluidComponent fluidStorage = new FluidComponent(getComponentManager(), 10000) {
    @Override
    public void onChanged() {
      super.onChanged();
      if (level != null && !level.isClientSide()) {
        if(level.getServer() != null) {
          new FluidPacket(this.getFluid(), worldPosition)
            .sendToAll(level.getServer());
        }
      }
    }
    @Override
    public boolean isFluidValid(FluidStack stack) {
      AtomicBoolean has = new AtomicBoolean(false);
      RecipeHelpers.UPGRADE_MAKER.recipes.forEach(recipe -> {
        if (recipe.getFluid().isFluidEqual(stack)) has.set(true);
      });
      return has.get();
    }
  };

  private final Map<Direction, LazyOptional<ItemWrapperHandler>> itemWrapperHandlerMap = Map.of(
    Direction.UP, LazyOptional.of(() -> new ItemWrapperHandler(
      getComponentManager(),
      itemHandler,
      i -> i == 4,
      (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s) || itemHandler.isItemValid(3, s)
    )),
    Direction.DOWN, LazyOptional.of(() -> new ItemWrapperHandler(
      getComponentManager(),
      itemHandler,
      i -> i == 4,
      (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s) || itemHandler.isItemValid(3, s)
    )),
    Direction.NORTH, LazyOptional.of(() -> new ItemWrapperHandler(
      getComponentManager(),
      itemHandler,
      i -> i == 4,
      (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s) || itemHandler.isItemValid(3, s)
    )),
    Direction.SOUTH, LazyOptional.of(() -> new ItemWrapperHandler(
      getComponentManager(),
      itemHandler,
      i -> i == 4,
      (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s) || itemHandler.isItemValid(3, s)
    )),
    Direction.EAST, LazyOptional.of(() -> new ItemWrapperHandler(
      getComponentManager(),
      itemHandler,
      i -> i == 4,
      (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s) || itemHandler.isItemValid(3, s)
    )),
    Direction.WEST, LazyOptional.of(() -> new ItemWrapperHandler(
      getComponentManager(),
      itemHandler,
      i -> i == 4,
      (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s) || itemHandler.isItemValid(3, s)
    ))
  );

  public UpgradeMakerEntity(BlockPos blockPos, BlockState blockState) {
    super(EntityRegister.UPGRADE_MAKER.get(), blockPos, blockState);
  }

  public Component getName() {
    return Component.translatable(
      "block.degrassi.upgrade_maker"
    );
  }

  public void drops() {
    SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
    for (int i = 0; i < itemHandler.getSlots(); i++) {
      inventory.setItem(i, itemHandler.getStackInSlot(i));
    }

    assert this.level != null;
    Containers.dropContents(this.level, this.worldPosition, inventory);
  }

  public static void tick(
    @NotNull Level level,
    BlockPos pos,
    BlockState state,
    @NotNull UpgradeMakerEntity entity
  ) {
    if (level.isClientSide()) {
      return;
    }
    if (RecipeHelpers.UPGRADE_MAKER.hasRecipe(entity)) {
      if(!entity.getRecipe().isInProgress() && entity.getProgressStorage().getProgress() == 0) {
        entity.getRecipe().startProcess(entity);
      } else {
        entity.getRecipe().tick(entity);
      }
      if (entity.getProgressStorage().getProgress() >= entity.getProgressStorage().getMaxProgress()) {
        entity.getRecipe().endProcess(entity);
      }
    }
    entity.getComponentManager().markDirty();
  }

  @Override
  public EnergyComponent getEnergyStorage() {
    return ENERGY_STORAGE;
  }

  @Override
  public void setEnergyLevel(int energy) {
    this.ENERGY_STORAGE.setEnergy(energy);
    getComponentManager().markDirty();
  }

  @Override
  public void setCapacityLevel(int capacity) {
    this.ENERGY_STORAGE.setCapacity(capacity);
    getComponentManager().markDirty();
  }

  @Override
  public void setTransferRate(int transfer) {
    this.ENERGY_STORAGE.setTransfer(transfer);
    getComponentManager().markDirty();
  }


  @Override
  public UpgradeMakerRecipe getRecipe() {
    return recipe;
  }

  @Override
  public void setRecipe(UpgradeMakerRecipe recipe) {
    this.recipe = recipe;
  }

  @Override
  public ProgressComponent getProgressStorage() {
    return progressComponent;
  }

  @Override
  public void setProgress(int progress) {
    this.progressComponent.setProgress(progress);
    getComponentManager().markDirty();
  }

  @Override
  public void setMaxProgress(int maxProgress) {
    this.progressComponent.setMaxProgress(maxProgress);
    getComponentManager().markDirty();
  }

  @Override
  public void resetProgress() {
    this.progressComponent.resetProgress();
    this.recipe = null;
    getComponentManager().markDirty();
  }

  @Override
  public void setHandler(@NotNull ItemStackHandler handler) {
    for (int i = 0; i < handler.getSlots(); i++) {
      itemHandler.setStackInSlot(i, handler.getStackInSlot(i));
    }
    getComponentManager().markDirty();
  }
  public ItemStackHandler getItemHandler() {
    return itemHandler;
  }

  @Override
  public FluidComponent getFluidStorage() {
    return fluidStorage;
  }

  @Override
  public void setFluidHandler(@NotNull FluidTank storage) {
    this.fluidStorage.setFluid(storage.getFluid());
    this.fluidStorage.setCapacity(storage.getCapacity());
    setChanged();
  }

  @Override
  public void setFluid(FluidStack fluid) {
    this.fluidStorage.setFluid(fluid);
    getComponentManager().markDirty();
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ENERGY) return lazyEnergyHandler.cast();
    if (cap == ForgeCapabilities.FLUID_HANDLER) return lazyFluidHandler.cast();
    if (cap == ForgeCapabilities.ITEM_HANDLER) {
      if (side == null) return lazyItemHandler.cast();
      if (itemWrapperHandlerMap.containsKey(side)) {
        Direction localDir = this.getBlockState().getValue(FurnaceBlock.FACING);
        if (side == Direction.UP || side == Direction.DOWN) return itemWrapperHandlerMap.get(side).cast();

        return switch(localDir) {
          default -> itemWrapperHandlerMap.get(side.getOpposite()).cast();
          case EAST -> itemWrapperHandlerMap.get(side.getClockWise()).cast();
          case SOUTH -> itemWrapperHandlerMap.get(side).cast();
          case WEST -> itemWrapperHandlerMap.get(side.getCounterClockWise()).cast();
        };
      }
    }

    return super.getCapability(cap, side);
  }

  @Override
  public void onLoad() {
    super.onLoad();
    lazyItemHandler = LazyOptional.of(() -> itemHandler);
    lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
    lazyFluidHandler = LazyOptional.of(() -> fluidStorage);
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    lazyItemHandler.invalidate();
    lazyEnergyHandler.invalidate();
    lazyFluidHandler.invalidate();
  }

  @Override
  public @NotNull CompoundTag getUpdateTag() {
    CompoundTag nbt = super.getUpdateTag();
    nbt.put("upgrade_maker.inventory", itemHandler.serializeNBT());
    nbt.put("upgrade_maker.energy", ENERGY_STORAGE.serializeNBT());
    nbt.put("upgrade_maker.progress", progressComponent.serializeNBT());
    Tag fluidTag = fluidStorage.serializeNBT();
    nbt.put("upgrade_maker.fluid", fluidTag);
    return nbt;
  }

  @Override
  public void saveAdditional(@NotNull CompoundTag nbt) {
    nbt.put("upgrade_maker.inventory", itemHandler.serializeNBT());
    nbt.put("upgrade_maker.energy", ENERGY_STORAGE.serializeNBT());
    nbt.put("upgrade_maker.progress", progressComponent.serializeNBT());
    CompoundTag fluidTag = nbt.getCompound("upgrade_maker.fluid");
    fluidStorage.deserializeNBT(fluidTag);
    super.saveAdditional(nbt);
  }

  @Override
  public void load(@NotNull CompoundTag nbt) {
    super.load(nbt);
    itemHandler.deserializeNBT(nbt.getCompound("upgrade_maker.inventory"));
    ENERGY_STORAGE.deserializeNBT(nbt.getCompound("upgrade_maker.energy"));
    progressComponent.deserializeNBT(nbt.getCompound("upgrade_maker.progress"));
    CompoundTag fluidTag = nbt.getCompound("upgrade_maker.fluid");
    fluidStorage.deserializeNBT(fluidTag);
  }

  @Override
  public void handleUpdateTag(CompoundTag tag) {
    load(tag);
  }

}
