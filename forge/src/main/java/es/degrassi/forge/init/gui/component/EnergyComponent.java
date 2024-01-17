package es.degrassi.forge.init.gui.component;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;

import net.minecraftforge.energy.IEnergyStorage;

@SuppressWarnings("unused")
public class EnergyComponent implements IEnergyStorage, IComponent {
  protected int energy;
  protected int capacity;
  protected int maxReceive;
  protected int maxExtract;
  private final ComponentManager manager;

  public EnergyComponent(ComponentManager manager, int capacity) {
    this(manager, capacity, capacity, capacity, 0);
  }
  public EnergyComponent(ComponentManager manager, int capacity, int maxTransfer) {
    this(manager, capacity, maxTransfer, maxTransfer, 0);
  }
  public EnergyComponent(ComponentManager manager, int capacity, int maxReceive, int maxExtract) {
    this(manager, capacity, maxReceive, maxExtract, 0);
  }

  public EnergyComponent(ComponentManager manager, int capacity, int maxReceive, int maxExtract, int energy) {
    this.capacity = capacity;
    this.maxReceive = maxReceive;
    this.maxExtract = maxExtract;
    this.energy = Math.max(0 , Math.min(capacity, energy));
    this.manager = manager;
  }

  @Override
  public int extractEnergy(int maxExtract, boolean simulate) {
    if (!canExtract())
      return 0;

    int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
    if (!simulate)
      energy -= energyExtracted;
    onChanged();
    return energyExtracted;
  }

  @Override
  public int receiveEnergy(int maxReceive, boolean simulate) {
    if (!canReceive())
      return 0;

    int energyReceived = Math.min(capacity - energy, Math.min(this.maxReceive, maxReceive));
    if (!simulate)
      energy += energyReceived;
    onChanged();
    return energyReceived;
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
    onChanged();
  }

  @Override
  public boolean canReceive() {
    return this.maxReceive > 0;
  }

  public void setMaxExtract(int maxExtract) {
    this.maxExtract = maxExtract;
    onChanged();
  }

  public void setMaxReceive(int maxReceive) {
    this.maxReceive = maxReceive;
    onChanged();
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
    onChanged();
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
    onChanged();
  }

  @Override
  public Tag serializeNBT() {
    CompoundTag nbt = new CompoundTag();
    nbt.putInt("energy", this.getEnergyStored());
    nbt.putInt("capacity", this.getMaxEnergyStored());
    return nbt;
  }

  @Override
  public void deserializeNBT(Tag tag) {
    if (tag == null)
      throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
    if (tag instanceof CompoundTag nbt) {
      this.energy = nbt.getInt("energy");
      this.capacity = nbt.getInt("capacity");
    }
  }

  public void setEnergyToCapacity() {
    if (this.energy > this.capacity)
      this.energy = this.capacity;
    onChanged();
  }


  public ComponentManager getManager () {
    return manager;
  }

  public void onChanged() {
    manager.markDirty();
  }
}
