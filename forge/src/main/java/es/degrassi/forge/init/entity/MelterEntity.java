package es.degrassi.forge.init.entity;

import es.degrassi.forge.init.block.FurnaceBlock;
import es.degrassi.forge.init.block.Melter;
import es.degrassi.forge.init.entity.renderer.LerpedFloat;
import es.degrassi.forge.init.entity.type.*;
import es.degrassi.forge.init.gui.component.*;
import es.degrassi.forge.init.handlers.ItemWrapperHandler;
import es.degrassi.forge.init.recipe.helpers.RecipeHelpers;
import es.degrassi.forge.init.recipe.recipes.MelterRecipe;
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

public class MelterEntity extends BaseEntity implements IEnergyEntity, IRecipeEntity<MelterRecipe>, IProgressEntity, IItemEntity, IFluidEntity {
  public Melter delegate;
  private LerpedFloat fluidLevel;
  protected boolean forceFluidLevelUpdate;
  protected boolean queuedSync;
  private MelterRecipe recipe;
  private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
  private LazyOptional<EnergyComponent> lazyEnergyHandler = LazyOptional.empty();
  private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();
  private final EnergyComponent ENERGY_STORAGE = new EnergyComponent(getComponentManager(), DegrassiConfig.get().melterConfig.melter_capacity, DegrassiConfig.get().melterConfig.melter_transfer) {
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
  private final ItemStackHandler itemHandler = new ItemStackHandler(4) {
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
          RecipeHelpers.MELTER.recipes.forEach(recipe -> {
            if (recipe.getIngredients().get(0).getItems()[0].is(stack.getItem())) has.set(true);
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
      } else if(level != null && level.isClientSide()) {
        if (fluidLevel == null)
          fluidLevel = LerpedFloat.linear()
            .startWithValue(getFillState());
        fluidLevel.chase(getFillState(), .5f, LerpedFloat.Chaser.EXP);
      }
    }
    @Override
    public boolean isFluidValid(FluidStack stack) {
      AtomicBoolean has = new AtomicBoolean(false);
      RecipeHelpers.MELTER.recipes.forEach(recipe -> {
        if (recipe.getFluid().isFluidEqual(stack)) has.set(true);
      });
      return has.get();
    }
  };

  private final Map<Direction, LazyOptional<ItemWrapperHandler>> itemWrapperHandlerMap = Map.of(
    Direction.UP, LazyOptional.of(() -> new ItemWrapperHandler(
      getComponentManager(),
      itemHandler,
      i -> i == 3,
      (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s)
    )),
    Direction.DOWN, LazyOptional.of(() -> new ItemWrapperHandler(
      getComponentManager(),
      itemHandler,
      i -> i == 3,
      (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s)
    )),
    Direction.NORTH, LazyOptional.of(() -> new ItemWrapperHandler(
      getComponentManager(),
      itemHandler,
      i -> i == 3,
      (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s)
    )),
    Direction.SOUTH, LazyOptional.of(() -> new ItemWrapperHandler(
      getComponentManager(),
      itemHandler,
      i -> i == 3,
      (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s)
    )),
    Direction.EAST, LazyOptional.of(() -> new ItemWrapperHandler(
      getComponentManager(),
      itemHandler,
      i -> i == 3,
      (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s)
    )),
    Direction.WEST, LazyOptional.of(() -> new ItemWrapperHandler(
      getComponentManager(),
      itemHandler,
      i -> i == 3,
      (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s)
    ))
  );

  public MelterEntity(BlockPos pos, BlockState state) {
    super(
      EntityRegister.MELTER.get(),
      pos,
      state
    );
    forceFluidLevelUpdate = true;
    queuedSync = true;
  }

  public Component getName() {
    return Component.translatable(
      "block.degrassi.melter"
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
    @NotNull MelterEntity entity
  ) {
    if (level.isClientSide()) {
      if (entity.fluidLevel != null)
        entity.fluidLevel.tickChaser();
      entity.getComponentManager().markDirty();
      return;
    }
    if (RecipeHelpers.MELTER.hasRecipe(entity)) {
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
  public MelterRecipe getRecipe() {
    return recipe;
  }

  @Override
  public void setRecipe(MelterRecipe recipe) {
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
    getComponentManager().markDirty();
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
    nbt.put("melter.inventory", itemHandler.serializeNBT());
    nbt.put("melter.energy", ENERGY_STORAGE.serializeNBT());
    nbt.put("melter.progress", progressComponent.serializeNBT());
    CompoundTag fluidTag = (CompoundTag) fluidStorage.serializeNBT();
    nbt.put("melter.fluid", fluidTag);
    return nbt;
  }

  @Override
  public void saveAdditional(@NotNull CompoundTag nbt) {
    nbt.put("melter.inventory", itemHandler.serializeNBT());
    nbt.put("melter.energy", ENERGY_STORAGE.serializeNBT());
    nbt.put("melter.progress", progressComponent.serializeNBT());
    CompoundTag fluidTag = (CompoundTag) fluidStorage.serializeNBT();
    nbt.put("melter.fluid", fluidTag);
    nbt.putBoolean("ForceFluidLevel", true);
    nbt.putBoolean("LazySync", true);
    super.saveAdditional(nbt);
  }

  @Override
  public void load(@NotNull CompoundTag nbt) {
    super.load(nbt);
    itemHandler.deserializeNBT(nbt.getCompound("melter.inventory"));
    ENERGY_STORAGE.deserializeNBT(nbt.getCompound("melter.energy"));
    progressComponent.deserializeNBT(nbt.getCompound("melter.progress"));
    fluidStorage.deserializeNBT(nbt.getCompound("melter.fluid"));
    if (nbt.contains("ForceFluidLevel") || fluidLevel == null)
      fluidLevel = LerpedFloat.linear()
        .startWithValue(getFillState());
    if (nbt.contains("LazySync"))
      fluidLevel.chase(fluidLevel.getChaseTarget(), 0.125f, LerpedFloat.Chaser.EXP);
  }

  @Override
  public void handleUpdateTag(CompoundTag tag) {
    load(tag);
  }

  public float getFillState() {
    return (float) fluidStorage.getFluidAmount() / fluidStorage.getCapacity();
  }

  public LerpedFloat getFluidLevel() {
    return fluidLevel;
  }

  public void setFluidLevel(LerpedFloat fluidLevel) {
    this.fluidLevel = fluidLevel;
    getComponentManager().markDirty();
  }

  public ItemStack getRenderStack() {
    return itemHandler.getStackInSlot(2).isEmpty() ? ItemStack.EMPTY : itemHandler.getStackInSlot(2);
  }

}
