package es.degrassi.forge.init.entity.furnace;

import es.degrassi.forge.init.block.furnace.FurnaceBlock;
import es.degrassi.forge.init.entity.*;
import es.degrassi.forge.init.entity.type.IEnergyEntity;
import es.degrassi.forge.init.entity.type.IItemEntity;
import es.degrassi.forge.init.entity.type.IProgressEntity;
import es.degrassi.forge.init.entity.type.IRecipeEntity;
import es.degrassi.forge.init.handlers.ItemWrapperHandler;
import es.degrassi.forge.init.recipe.IDegrassiRecipe;
import es.degrassi.forge.init.recipe.recipes.FurnaceRecipe;
import es.degrassi.forge.init.recipe.helpers.RecipeHelpers;
import es.degrassi.forge.init.registration.ItemRegister;
import es.degrassi.forge.network.EnergyPacket;
import es.degrassi.forge.network.ItemPacket;
import es.degrassi.forge.network.ProgressPacket;
import es.degrassi.forge.util.storage.AbstractEnergyStorage;
import es.degrassi.forge.util.storage.ExperienceStorage;
import es.degrassi.forge.util.storage.ProgressStorage;
import es.degrassi.forge.network.furnace.FurnaceExperiencePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public abstract class FurnaceEntity extends BaseEntity implements IEnergyEntity, IRecipeEntity, IItemEntity, IProgressEntity {
  private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
  private LazyOptional<AbstractEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

  public ItemStackHandler itemHandler;
  public AbstractEnergyStorage ENERGY_STORAGE;
  private final Component name;
  public final ContainerData data;

  public FurnaceBlock delegate;
  public ProgressStorage progressStorage;
  public FurnaceRecipe recipe;
  public ExperienceStorage xp;

  private final Map<Direction, LazyOptional<ItemWrapperHandler>> itemWrapperHandlerMap = Map.of(
    Direction.UP, LazyOptional.of(() -> new ItemWrapperHandler(
      itemHandler,
      i -> i == 3,
      (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s)
    )),
    Direction.DOWN, LazyOptional.of(() -> new ItemWrapperHandler(
      itemHandler,
      i -> i == 3,
      (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s)
    )),
    Direction.NORTH, LazyOptional.of(() -> new ItemWrapperHandler(
      itemHandler,
      i -> i == 3,
      (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s)
    )),
    Direction.SOUTH, LazyOptional.of(() -> new ItemWrapperHandler(
      itemHandler,
      i -> i == 3,
      (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s)
    )),
    Direction.EAST, LazyOptional.of(() -> new ItemWrapperHandler(
      itemHandler,
      i -> i == 3,
      (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s)
    )),
    Direction.WEST, LazyOptional.of(() -> new ItemWrapperHandler(
      itemHandler,
      i -> i == 3,
      (i, s) -> itemHandler.isItemValid(0, s) || itemHandler.isItemValid(1, s) || itemHandler.isItemValid(2, s)
    ))
  );

  public FurnaceEntity(
    BlockEntityType<?> blockEntityType,
    BlockPos pos,
    BlockState state,
    Component name,
    int defaultCapacity,
    int defaultTransfer
  ) {
    super(blockEntityType, pos, state);
    this.name = name;
    this.data = new ContainerData() {
      @Override
      public int get(int index) {
        return switch (index) {
          case 0 -> FurnaceEntity.this.progressStorage.getProgress();
          case 1 -> FurnaceEntity.this.progressStorage.getMaxProgress();
          default -> 0;
        };
      }

      @Override
      public void set(int index, int value) {
        switch (index) {
          case 0 -> FurnaceEntity.this.progressStorage.setProgress(value);
          case 1 -> FurnaceEntity.this.progressStorage.setMaxProgress(value);
        }
      }

      @Override
      public int getCount() {
        return 2;
      }
    };
    this.ENERGY_STORAGE = new AbstractEnergyStorage(defaultCapacity, defaultTransfer) {
        @Override
        public boolean canExtract() {
          return false;
        }

        @Override
        public void onEnergyChanged() {
          setChanged();
          if (level != null && level.getServer() != null && !level.isClientSide())
            new EnergyPacket(this.energy, this.capacity, this.maxReceive, getBlockPos())
              .sendToChunkListeners(level.getChunkAt(getBlockPos()));
        }
      };
    this.progressStorage = new ProgressStorage(0) {
      @Override
      public void onProgressChanged() {
        setChanged();
        if (level != null && !level.isClientSide())
          new ProgressPacket(this.progress, this.maxProgress, getBlockPos())
            .sendToChunkListeners(level.getChunkAt(getBlockPos()));
      }
    };
    this.xp = new ExperienceStorage(0) {
      @Override
      public void onExperienceChanged() {
        setChanged();
        if(level != null && !level.isClientSide() && level.getServer() != null) {
          if (this.xp >= 9843) {
            dropExperience();
          }
          new FurnaceExperiencePacket(this.xp, pos)
            .sendToChunkListeners(level.getChunkAt(getBlockPos()));
        }
      }
    };
    this.itemHandler = new ItemStackHandler(4) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
        if (level != null && !level.isClientSide() && level.getServer() != null) {
          new ItemPacket(this, worldPosition)
            .sendToAll(level.getServer());
          //.sendToChunkListeners(level.getChunkAt(pos));
        }
      }

      @Override
      public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return switch(slot) {
          case 0 -> stack.is(ItemRegister.SPEED_UPGRADE.get());
          case 1 -> stack.is(ItemRegister.ENERGY_UPGRADE.get());
          case 2 -> true;
          case 3 -> false;
          default -> isItemValid(slot, stack);
        };
      }
    };
  }

  public Component getName() {
    return this.name;
  }

  public void setProgress(int progress) {
    this.progressStorage.setProgress(progress);
    setChanged();
  }

  public void setMaxProgress(int maxProgress) {
    this.progressStorage.setMaxProgress(maxProgress);
  }

  public void setEnergyLevel(int energy) {
    this.ENERGY_STORAGE.setEnergy(energy);
    setChanged();
  }

  public void setCapacityLevel(int capacity) {
    ENERGY_STORAGE.setCapacity(capacity);
    setChanged();
  }

  public void setTransferRate(int transfer) {
    ENERGY_STORAGE.setTransfer(transfer);
    setChanged();
  }

  public AbstractEnergyStorage getEnergyStorage() {
    return ENERGY_STORAGE;
  }

  public void setHandler(@NotNull ItemStackHandler handler) {
    for (int i = 0; i < handler.getSlots(); i++) {
      itemHandler.setStackInSlot(i, handler.getStackInSlot(i));
    }
    setChanged();
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
    @NotNull FurnaceEntity furnaceEntity
  ) {
    if (level.isClientSide()) return;
    if (RecipeHelpers.FURNACE.hasRecipe(furnaceEntity)) {
      furnaceEntity.progressStorage.increment(false);
      RecipeHelpers.FURNACE.extractEnergy(furnaceEntity);
      if (furnaceEntity.progressStorage.getProgress() >= furnaceEntity.progressStorage.getMaxProgress()) RecipeHelpers.FURNACE.craftItem(furnaceEntity);
    } else {
      furnaceEntity.resetProgress();
    }
    setChanged(level, pos, state);
  }

  public void resetProgress() {
    this.progressStorage.resetProgressAndMaxProgress();
    this.recipe = null;
    setChanged();
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag nbt) {
    super.saveAdditional(nbt);
    nbt.put("furnace.inventory", itemHandler.serializeNBT());
    nbt.putInt("furnace.energy", ENERGY_STORAGE.getEnergyStored());
    nbt.putInt("furnace.capacity", ENERGY_STORAGE.getMaxEnergyStored());
    nbt.putInt("furnace.progress", progressStorage.getProgress());
    nbt.putInt("furnace.maxProgress", progressStorage.getMaxProgress());
    nbt.putFloat("furnace.xp", xp.getXp());
  }

  @Override
  public void load(@NotNull CompoundTag nbt) {
    super.load(nbt);
    itemHandler.deserializeNBT(nbt.getCompound("furnace.inventory"));
    ENERGY_STORAGE.setEnergy(nbt.getInt("furnace.energy"));
    ENERGY_STORAGE.setCapacity(nbt.getInt("furnace.capacity"));
    progressStorage.setProgress(nbt.getInt("furnace.progress"));
    progressStorage.setMaxProgress(nbt.getInt("furnace.maxProgress"));
    xp.setXp(nbt.getFloat("furnace.xp"));
  }

  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  public void setXp(float xp) {
    this.xp.setXp(xp);
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ENERGY) return lazyEnergyHandler.cast();
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
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    lazyItemHandler.invalidate();
    lazyEnergyHandler.invalidate();
  }

  @Override
  public @NotNull CompoundTag getUpdateTag() {
    CompoundTag nbt = super.getUpdateTag();
    nbt.put("furnace.inventory", itemHandler.serializeNBT());
    nbt.putInt("furnace.energy", ENERGY_STORAGE.getEnergyStored());
    nbt.putInt("furnace.capacity", ENERGY_STORAGE.getMaxEnergyStored());
    nbt.putInt("furnace.progress", progressStorage.getProgress());
    nbt.putInt("furnace.maxProgress", progressStorage.getMaxProgress());
    return nbt;
  }

  @Override
  public void handleUpdateTag(CompoundTag tag) {
    load(tag);
  }

  public void dropExperience() {
    if(level != null && level.getServer() != null) {
      ExperienceOrb.award(Objects.requireNonNull(level.getServer().getLevel(level.dimension())), Vec3.atCenterOf(getBlockPos()), (int) Math.floor(this.xp.getXp()));
      this.xp.extractXp((float) Math.floor(this.xp.getXp()));
    }
  }

  @Override
  public IDegrassiRecipe getRecipe() {
    return this.recipe;
  }

  @Override
  public ProgressStorage getProgressStorage(){
    return this.progressStorage;
  }
}
