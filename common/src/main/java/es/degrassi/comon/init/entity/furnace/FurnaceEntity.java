package es.degrassi.comon.init.entity.furnace;

import es.degrassi.comon.init.recipe.furnace.FurnaceRecipe;
import es.degrassi.comon.init.recipe.helpers.furnace.FurnaceRecipeHelper;
import es.degrassi.comon.util.storage.AbstractEnergyStorage;
import es.degrassi.comon.util.storage.ProgressStorage;
import es.degrassi.network.furnace.FurnaceEnergyPacket;
import es.degrassi.network.furnace.FurnaceProgressPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public abstract class FurnaceEntity extends BlockEntity {
  public ItemStackHandler itemHandler;
  public AbstractEnergyStorage ENERGY_STORAGE;
  private final Component name;
  public final ContainerData data;
  public ProgressStorage progressStorage;
  public FurnaceRecipe recipe;

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
            new FurnaceEnergyPacket(this.energy, this.capacity, this.maxReceive, getBlockPos())
              .sendToChunkListeners(level.getChunkAt(getBlockPos()));
        }
      };
    this.progressStorage = new ProgressStorage(0) {
      @Override
      public void onProgressChanged() {
        setChanged();
        if (level != null && !level.isClientSide())
          new FurnaceProgressPacket(this.progress, this.maxProgress, getBlockPos())
            .sendToChunkListeners(level.getChunkAt(getBlockPos()));
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
    if (FurnaceRecipeHelper.hasRecipe(furnaceEntity)) {
      furnaceEntity.progressStorage.increment(false);
      FurnaceRecipeHelper.extractEnergy(furnaceEntity);
      if (furnaceEntity.progressStorage.getProgress() >= furnaceEntity.progressStorage.getMaxProgress()) FurnaceRecipeHelper.craftItem(furnaceEntity);
    } else {
      furnaceEntity.resetProgress();
    }
    setChanged(level, pos, state);
  }

  public void resetProgress() {
    this.progressStorage.resetProgress();
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
  }

  @Override
  public void load(@NotNull CompoundTag nbt) {
    super.load(nbt);
    itemHandler.deserializeNBT(nbt.getCompound("furnace.inventory"));
    ENERGY_STORAGE.setEnergy(nbt.getInt("furnace.energy"));
    ENERGY_STORAGE.setCapacity(nbt.getInt("furnace.capacity"));
    progressStorage.setProgress(nbt.getInt("furnace.progress"));
    progressStorage.setMaxProgress(nbt.getInt("furnace.maxProgress"));
  }

  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }
}
