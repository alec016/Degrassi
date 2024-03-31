package es.degrassi.forge.core.common.component;

import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.network.component.EnergyPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.energy.IEnergyStorage;

public class EnergyComponent implements IComponent, IEnergyStorage {
  private int energy;
  private final ComponentManager manager;
  private int capacity, maxInput, maxOutput;
  private final MachineEntity<?> entity;
  private final String id;
  private ComponentIOMode mode;
  public EnergyComponent(ComponentManager manager, int capacity, MachineEntity<?> entity, String id) {
    this(manager, capacity, capacity, entity, id);
  }
  public EnergyComponent(ComponentManager manager, int capacity, MachineEntity<?> entity, String id, ComponentIOMode mode) {
    this(manager, capacity, capacity, entity, id, mode);
  }
  public EnergyComponent(ComponentManager manager, int capacity, int transfer, MachineEntity<?> entity, String id) {
    this(manager, capacity, transfer, transfer, entity, id, ComponentIOMode.ALL);
  }
  public EnergyComponent(ComponentManager manager, int capacity, int transfer, MachineEntity<?> entity, String id, ComponentIOMode mode) {
    this(manager, capacity, transfer, transfer, entity, id, mode);
  }

  public EnergyComponent(ComponentManager manager, int capacity, int maxInput, int maxOutput, MachineEntity<?> entity, String id) {
    this(manager, capacity, maxInput, maxOutput, entity, id, ComponentIOMode.ALL);
  }

  public EnergyComponent(ComponentManager manager, int capacity, int maxInput, int maxOutput, MachineEntity<?> entity, String id, ComponentIOMode mode) {
    this.manager = manager;
    this.capacity = capacity;
    this.maxInput = mode.receiveWillAll() ? Math.min(capacity, maxInput) : 0;
    this.maxOutput = mode.extractWithAll() ? Math.min(capacity, maxOutput) : 0;
    this.entity = entity;
    this.id = id;
    this.mode = mode;
  }

  @Override
  public ComponentManager getManager() {
    return manager;
  }

  public ComponentIOMode getMode() {
    return mode;
  }

  public void setMode(ComponentIOMode mode) {
    this.mode = mode;
  }

  @Override
  public void markDirty() {
    entity.setChanged();
    if(entity.getLevel() != null && !entity.getLevel().isClientSide())
      new EnergyPacket(energy, id, entity.getBlockPos())
        .sendToChunkListeners(entity.getLevel().getChunkAt(entity.getBlockPos()));
  }

  @Override
  public void serialize(CompoundTag nbt) {
    CompoundTag tag = new CompoundTag();
    tag.putInt("energy", energy);
    tag.putInt("capacity", capacity);
    tag.putInt("max_input", maxInput);
    tag.putInt("max_output", maxOutput);
    tag.putString("mode", mode.serialize());
    nbt.put(id, tag);
  }

  @Override
  public void deserialize(CompoundTag nbt) {
    if (nbt.contains(id)) {
      CompoundTag tag = nbt.getCompound(id);
      this.energy = tag.getInt("energy");
      this.capacity = tag.getInt("capacity");
      this.maxInput = tag.getInt("max_input");
      this.maxOutput = tag.getInt("max_output");
      this.mode = ComponentIOMode.deserialize(tag.getString("mode"));
    }
  }

  public void setEnergy(int energy) {
    if (energy == this.energy) return;
    this.energy = energy;
    markDirty();
  }

  @Override
  public int receiveEnergy(int energy, boolean simulate) {
    if (mode.extract()) return 0;
    int toReceive = Math.min(this.capacity - this.energy, Math.min(energy, maxInput));
    if (!simulate) {
      this.energy += toReceive;
    }
    markDirty();
    return toReceive;
  }

  @Override
  public int extractEnergy(int energy, boolean simulate) {
    if (mode.receive()) return 0;
    int toExtract = Math.min(this.energy, Math.min(energy, this.maxOutput));
    if (!simulate) {
      this.energy -= toExtract;
    }
    markDirty();
    return toExtract;
  }

  @Override
  public int getEnergyStored() {
    return energy;
  }

  @Override
  public int getMaxEnergyStored() {
    return capacity;
  }

  @Override
  public boolean canExtract() {
    return maxOutput > 0 && mode.extractWithAll();
  }

  @Override
  public boolean canReceive() {
    return maxInput > 0 && mode.receiveWillAll();
  }

  public String getId() {
    return id;
  }

  public int toComparatorPower() {
    return (int) (subSized() * 15);
  }

  public float subSized() {
    return this.capacity > 0 ? (float) this.energy / this.capacity : 0;
  }

  public int getMaxInput() {
    return mode.receiveWillAll() ? maxInput : 0;
  }

  public int getMaxOutput() {
    return mode.extractWithAll() ? maxOutput : 0;
  }

  public void setMaxInput(int maxInput) {
    this.maxInput = maxInput;
  }

  public void setMaxOutput(int maxOutput) {
    this.maxOutput = maxOutput;
  }

  @Override
  public String toString() {
    return "EnergyComponent{" +
      "energy=" + energy +
      ", capacity=" + capacity +
      ", maxInput=" + maxInput +
      ", maxOutput=" + maxOutput +
      ", id='" + id + '\'' +
      '}';
  }

  public EnergyComponent setCapacity(int capacity) {
    if (this.capacity != capacity)
      this.capacity = capacity;
    return this;
  }

  public EnergyComponent setTransfer(int energyTransfer) {
    if (mode.receiveWillAll()) setMaxInput(energyTransfer);
    if (mode.extractWithAll()) setMaxOutput(energyTransfer);
    return this;
  }
}
