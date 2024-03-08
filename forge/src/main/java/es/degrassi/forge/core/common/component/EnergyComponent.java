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
  private final int capacity, maxInput, maxOutput;
  private final MachineEntity<?> entity;
  private final String id;
  public EnergyComponent(ComponentManager manager, int capacity, MachineEntity<?> entity, String id) {
    this(manager, capacity, capacity, entity, id);
  }
  public EnergyComponent(ComponentManager manager, int capacity, int transfer, MachineEntity<?> entity, String id) {
    this(manager, capacity, transfer, transfer, entity, id);
  }
  public EnergyComponent(ComponentManager manager, int capacity, int maxInput, int maxOutput, MachineEntity<?> entity, String id) {
    this.manager = manager;
    this.capacity = capacity;
    this.maxInput = Math.min(capacity, maxInput);
    this.maxOutput = Math.min(capacity, maxOutput);
    this.entity = entity;
    this.id = id;
  }

  @Override
  public ComponentManager getManager() {
    return manager;
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
    nbt.put(id, tag);
  }

  @Override
  public void deserialize(CompoundTag nbt) {
    if (nbt.contains(id)) {
      CompoundTag tag = nbt.getCompound(id);
      this.energy = tag.getInt("energy");
    }
  }

  public void setEnergy(int energy) {
    if (energy == this.energy) return;
    this.energy = energy;
    markDirty();
  }

  @Override
  public int receiveEnergy(int energy, boolean simulate) {
    int toReceive = Math.min(this.capacity - this.energy, Math.min(energy, maxInput));
    if (!simulate) {
      this.energy += toReceive;
    }
    markDirty();
    return toReceive;
  }

  @Override
  public int extractEnergy(int energy, boolean simulate) {
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
    return maxOutput > 0;
  }

  @Override
  public boolean canReceive() {
    return maxInput > 0;
  }

  public String getId() {
    return id;
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
}
