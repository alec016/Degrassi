package es.degrassi.forge.util.storage;

import es.degrassi.forge.init.gui.IComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractEnergyStorage implements IEnergyStorage, INBTSerializable<CompoundTag>, IComponent {
  protected int energy;
  protected int capacity;
  protected int maxReceive;
  protected int maxExtract;

  public AbstractEnergyStorage (int capacity) {
    this(capacity, capacity, capacity, 0);
  }
  public AbstractEnergyStorage(int capacity, int maxTransfer) {
    this(capacity, maxTransfer, maxTransfer, 0);
  }
  public AbstractEnergyStorage(int capacity, int maxReceive, int maxExtract) {
    this(capacity, maxReceive, maxExtract, 0);
  }

  public AbstractEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy) {
    this.capacity = capacity;
    this.maxReceive = maxReceive;
    this.maxExtract = maxExtract;
    this.energy = Math.max(0 , Math.min(capacity, energy));
  }

  @Override
  public int extractEnergy(int maxExtract, boolean simulate) {
    if (!canExtract())
      return 0;

    int energyExtracted = Math.min(energy, Math.min(this.maxExtract, (int) maxExtract));
    if (!simulate)
      energy -= energyExtracted;
    onEnergyChanged();
    return (int) energyExtracted;
  }

  @Override
  public int receiveEnergy(int maxReceive, boolean simulate) {
    if (!canReceive())
      return 0;

    int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
    if (!simulate)
      energy += energyReceived;
    onEnergyChanged();
    return (int) energyReceived;
  }

  @Override
  public int getEnergyStored() {
    return energy;
  }

  @Override
  public int getMaxEnergyStored(){
    return capacity;
  }

  @Override
  public boolean canExtract() {
    return this.maxExtract > 0;
  }

  public void setEnergy(int energy) {
    if (this.energy != energy) this.energy = energy;
    onEnergyChanged();
  }

  @Override
  public boolean canReceive() {
    return this.maxReceive > 0;
  }

  public abstract void onEnergyChanged();

  public void setMaxExtract(int maxExtract) {
    this.maxExtract = maxExtract;
    onEnergyChanged();
  }

  public void setMaxReceive(int maxReceive) {
    this.maxReceive = maxReceive;
    onEnergyChanged();
  }

  public int getMaxExtract() {
    return this.maxExtract;
  }

  public int getMaxReceive() {
    return this.maxReceive;
  }

  public void setTransfer(int transfer) {
    this.maxExtract = transfer;
    this.maxReceive = transfer;
    onEnergyChanged();
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
    onEnergyChanged();
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag nbt = new CompoundTag();
    nbt.putInt("energy", this.getEnergyStored());
    nbt.putInt("capacity", this.getMaxEnergyStored());
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    if (nbt == null)
      throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
    this.energy = nbt.getInt("energy");
    this.capacity = nbt.getInt("capacity");
  }

  public void setEnergyToCapacity() {
    if (this.energy > this.capacity)
      this.energy = this.capacity;
    onEnergyChanged();
  }
}
