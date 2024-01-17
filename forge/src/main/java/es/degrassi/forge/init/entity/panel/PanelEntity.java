package es.degrassi.forge.init.entity.panel;

import es.degrassi.forge.init.entity.BaseEntity;
import es.degrassi.forge.init.entity.type.IEfficiencyEntity;
import es.degrassi.forge.init.entity.type.IEnergyEntity;
import es.degrassi.forge.init.entity.type.IEnergyEntity.IGenerationEntity;
import es.degrassi.forge.init.entity.type.IItemEntity;
import es.degrassi.forge.init.gui.component.*;
import es.degrassi.forge.network.EfficiencyPacket;
import es.degrassi.forge.network.EnergyPacket;
import es.degrassi.forge.network.GenerationPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public abstract class PanelEntity extends BaseEntity implements IEnergyEntity, IItemEntity, IGenerationEntity, IEfficiencyEntity {
  public static final float RAIN_MULTIPLIER = 0.6F, THUNDER_MULTIPLIER = 0.4F;
  public EnergyComponent ENERGY_STORAGE;
  protected GenerationComponent currentGen;
  public ItemStackHandler itemHandler;
  private final Component name;

  protected EfficiencyComponent efficiency;
  protected LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
  protected LazyOptional<EnergyComponent> lazyEnergyHandler = LazyOptional.empty();

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
    this.efficiency= new EfficiencyComponent(getComponentManager()) {
      @Override
      public void onChanged() {
        super.onChanged();
        if (level != null && !level.isClientSide()) {
          new EfficiencyPacket(this.getEfficiency(), worldPosition)
            .sendToChunkListeners(level.getChunkAt(getBlockPos()));
        }
      }
    };
    this.ENERGY_STORAGE = createEnergyStorage(this);
    this.currentGen = createGenerationStorage(this);
  }

  @Contract(value = "_ -> new", pure = true)
  protected static @NotNull GenerationComponent createGenerationStorage(PanelEntity entity) {
    return new GenerationComponent(entity.getComponentManager()) {
      @Override
      public void onChanged() {
        super.onChanged();
        if (entity.level != null && !entity.level.isClientSide()) {
          new GenerationPacket(this.getGeneration(), entity.worldPosition)
            .sendToChunkListeners(entity.level.getChunkAt(entity.getBlockPos()));
        }
      }
    };
  }

  @Contract(value = "_ -> new", pure = true)
  protected static @NotNull EnergyComponent createEnergyStorage(@NotNull PanelEntity entity) {
    return new EnergyComponent(entity.getComponentManager(), entity.defaultCapacity, entity.defaultTransfer) {
      @Override
      public boolean canReceive() {
        return false;
      }

      @Override
      public void onChanged() {
        super.onChanged();
        if (entity.level != null && !entity.level.isClientSide())
          new EnergyPacket(
            this.energy,
            this.capacity,
            this.maxExtract,
            entity.getBlockPos()
          ).sendToChunkListeners(entity.level.getChunkAt(entity.getBlockPos()));
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

  public GenerationComponent getGenerationStorage() {
    return this.currentGen;
  }

  public void receiveEnergy() {
    int energy = ENERGY_STORAGE.getEnergyStored() + currentGen.getGeneration();
    if (energy <= ENERGY_STORAGE.getMaxEnergyStored())
      ENERGY_STORAGE.setEnergy(energy);
    getComponentManager().markDirty();
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
    nbt.put("panel.energy", ENERGY_STORAGE.serializeNBT());
  }

  @Override
  public void load(@NotNull CompoundTag nbt) {
    super.load(nbt);
    itemHandler.deserializeNBT(nbt.getCompound("panel.inventory"));
    ENERGY_STORAGE.deserializeNBT(nbt.getCompound("panel.energy"));
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
    this.ENERGY_STORAGE.setCapacity(capacity);
    setEnergyToCapacity();
    getComponentManager().markDirty();
  }

  public void setTransferRate(int transfer) {
    ENERGY_STORAGE.setTransfer(transfer);
    getComponentManager().markDirty();
  }

  public void setEnergyToCapacity() {
    ENERGY_STORAGE.setEnergyToCapacity();
    getComponentManager().markDirty();
  }

  public void setEnergyLevel(int energy) {
    this.ENERGY_STORAGE.setEnergy(energy);
    getComponentManager().markDirty();
  }

  public EnergyComponent getEnergyStorage() {
    return ENERGY_STORAGE;
  }

  public void setHandler(@NotNull ItemStackHandler handler) {
    for (int i = 0; i < handler.getSlots(); i++) {
      itemHandler.setStackInSlot(i, handler.getStackInSlot(i));
    }
    getComponentManager().markDirty();
  }


  public EfficiencyComponent getCurrentEfficiency() {
    return this.efficiency;
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ENERGY) return lazyEnergyHandler.cast();
    if (cap == ForgeCapabilities.ITEM_HANDLER) return lazyItemHandler.cast();

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
    nbt.put("panel.inventory", itemHandler.serializeNBT());
    nbt.put("panel.energy", ENERGY_STORAGE.serializeNBT());
    return nbt;
  }

  @Override
  public void handleUpdateTag(CompoundTag tag) {
    load(tag);
  }
}
