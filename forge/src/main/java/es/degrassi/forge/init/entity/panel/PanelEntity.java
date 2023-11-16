package es.degrassi.forge.init.entity.panel;

import es.degrassi.forge.init.entity.BaseEntity;
import es.degrassi.forge.init.entity.type.IEfficiencyEntity;
import es.degrassi.forge.init.entity.type.IEnergyEntity;
import es.degrassi.forge.init.entity.type.IEnergyEntity.IGenerationEntity;
import es.degrassi.forge.init.entity.type.IItemEntity;
import es.degrassi.forge.network.EnergyPacket;
import es.degrassi.forge.util.storage.AbstractEnergyStorage;
import es.degrassi.forge.util.storage.GenerationStorage;
import es.degrassi.forge.network.GenerationPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public abstract class PanelEntity extends BaseEntity implements IEnergyEntity, IItemEntity, IGenerationEntity, IEfficiencyEntity {
  public static final float RAIN_MULTIPLIER = 0.6F, THUNDER_MULTIPLIER = 0.4F;
  public AbstractEnergyStorage ENERGY_STORAGE;
  protected GenerationStorage currentGen;
  public ItemStackHandler itemHandler;
  private final Component name;
  protected final int defaultCapacity;
  protected final int defaultTransfer;
  protected int effCacheTime;
  protected double effCache;
  protected int transferCache;
  protected int transferCacheTime;
  protected int genCache;
  protected int genCacheTime;
  protected int capacityCache;
  protected int capacityCacheTime;
  public boolean cache$seeSky;
  public byte cache$seeSkyTimer;

  public PanelEntity(
    BlockEntityType<?> blockEntityType,
    BlockPos pos,
    BlockState state,
    Component name,
    int defaultCapacity,
    int defaultTransfer
  ) {
    super(blockEntityType, pos, state);
    this.name = name;
    this.defaultTransfer = defaultTransfer;
    this.defaultCapacity = defaultCapacity;
    this.ENERGY_STORAGE = createEnergyStorage(this);
    this.currentGen = createGenerationStorage(this);
  }

  @Contract(value = "_ -> new", pure = true)
  protected static @NotNull GenerationStorage createGenerationStorage(PanelEntity entity) {
    return new GenerationStorage() {
      @Override
      public void onGenerationChanged() {
        entity.setChanged();
        if (entity.level != null && !entity.level.isClientSide()) {
          new GenerationPacket(this.getGeneration(), entity.worldPosition)
            .sendToChunkListeners(entity.level.getChunkAt(entity.getBlockPos()));
        }
      }
    };
  }

  @Contract(value = "_ -> new", pure = true)
  protected static @NotNull AbstractEnergyStorage createEnergyStorage(@NotNull PanelEntity panelEntity) {
    return new AbstractEnergyStorage(panelEntity.defaultCapacity, panelEntity.defaultTransfer) {
      @Override
      public boolean canReceive() {
        return false;
      }

      @Override
      public void onEnergyChanged() {
        panelEntity.setChanged();
        if (panelEntity.level != null && !panelEntity.level.isClientSide())
          new EnergyPacket(
            this.energy,
            this.capacity,
            this.maxExtract,
            panelEntity.getBlockPos()
          ).sendToChunkListeners(panelEntity.level.getChunkAt(panelEntity.getBlockPos()));
      }
    };
  }

  public Component getName() {
    return this.name;
  }

  public int getCurrentGeneration() {
    return currentGen.getGeneration();
  }

  public abstract int getGeneration();

  public GenerationStorage getGenerationStorage() {
    return this.currentGen;
  }

  public void receiveEnergy() {
    int energy = ENERGY_STORAGE.getEnergyStored() + currentGen.getGeneration();
    if (energy <= ENERGY_STORAGE.getMaxEnergyStored())
      ENERGY_STORAGE.setEnergy(energy);
    setChanged();
  }

  public boolean doesSeeSky() {
    if (cache$seeSkyTimer < 1) {
      cache$seeSkyTimer = 20;
      cache$seeSky =
        level != null &&
        level.getBrightness(LightLayer.SKY, worldPosition) > 0 &&
        level.canSeeSky(worldPosition.above());
    }
    return cache$seeSky;
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag nbt) {
    super.saveAdditional(nbt);
    nbt.put("panel.inventory", itemHandler.serializeNBT());
    nbt.putInt("panel.energy", ENERGY_STORAGE.getEnergyStored());
    nbt.putInt("panel.capacity", ENERGY_STORAGE.getMaxEnergyStored());
  }

  @Override
  public void load(@NotNull CompoundTag nbt) {
    super.load(nbt);
    itemHandler.deserializeNBT(nbt.getCompound("panel.inventory"));
    ENERGY_STORAGE.setEnergy(nbt.getInt("panel.energy"));
    ENERGY_STORAGE.setCapacity(nbt.getInt("panel.capacity"));
  }

  @Override
  public ClientboundBlockEntityDataPacket getUpdatePacket() {
    return ClientboundBlockEntityDataPacket.create(this);
  }

  public void drops() {
    SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
    for (int i = 0; i < itemHandler.getSlots(); i++) {
      inventory.setItem(i, itemHandler.getStackInSlot(i));
    }

    assert this.level != null;
    Containers.dropContents(this.level, this.worldPosition, inventory);
  }

  public void setCapacityLevel(int capacity) {
    ENERGY_STORAGE.setCapacity(capacity);
    setEnergyToCapacity();
    setChanged();
  }

  public void setTransferRate(int transfer) {
    ENERGY_STORAGE.setTransfer(transfer);
    setChanged();
  }

  public void setEnergyToCapacity() {
    ENERGY_STORAGE.setEnergyToCapacity();
    setChanged();
  }

  public void setEnergyLevel(int energy) {
    this.ENERGY_STORAGE.setEnergy(energy);
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
}
