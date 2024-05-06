package es.degrassi.forge.core.common.component;

import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.requirement.EnergyRequirement;
import es.degrassi.forge.core.network.component.EnergyPacket;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.energy.IEnergyStorage;

@Getter
@Setter
public class EnergyComponent implements IComponent, IEnergyStorage {
  protected int energy;
  private final ComponentManager manager;
  protected int capacity;
  private int maxInput;
  private int maxOutput;
  private final MachineEntity<?> entity;
  private final String id;
  private ComponentIOMode mode;

  public EnergyComponent(ComponentManager manager, int capacity, int maxInput, int maxOutput, MachineEntity<?> entity, String id, ComponentIOMode mode) {
    this.manager = manager;
    this.capacity = capacity;
    this.maxInput = Math.min(capacity, maxInput);
    this.maxOutput = Math.min(capacity, maxOutput);
    this.entity = entity;
    this.id = id;
    this.mode = mode;
    this.energy = 0;
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
    if (mode.output()) return 0;
    int toReceive = Math.min(this.capacity - this.getEnergyStored(), Math.min(energy, maxInput));
    if (!simulate) {
      this.energy += toReceive;
    }
    markDirty();
    return toReceive;
  }

  @Override
  public int extractEnergy(int energy, boolean simulate) {
    if (mode.input()) return 0;
    int toExtract = Math.min(this.getEnergyStored(), Math.min(energy, this.maxOutput));
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
    return getMode().outputWithAll() && maxOutput > 0;
  }

  @Override
  public boolean canReceive() {
    return getMode().inputWillAll() && maxInput > 0;
  }

  @Override
  public void fill(IRequirement<?> requirement) {
    if (requirement instanceof EnergyRequirement req) {
      this.energy = this.capacity = req.getAmount();
      markDirty();
    }
  }

  public int toComparatorPower() {
    return (int) (subSized() * 15);
  }

  public float subSized() {
    return this.capacity > 0 ? (float) this.energy / this.capacity : 0;
  }

  public EnergyComponent setCapacity(int capacity) {
    if (this.capacity != capacity)
      this.capacity = capacity;
    return this;
  }

  // recipe stuff
  public int receiveRecipeEnergy(int energy, boolean simulate) {
    int toReceive = Math.min(this.capacity - this.energy, Math.min(energy, maxInput));
    if (!simulate) {
      this.energy += toReceive;
    }
    markDirty();
    return toReceive;
  }

  public int extractRecipeEnergy(int energy, boolean simulate) {
    int toExtract = Math.min(this.energy, Math.min(energy, this.maxOutput));
    if (!simulate) {
      this.energy -= toExtract;
    }
    markDirty();
    return toExtract;
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

  public void setTransfer(int transferCache) {
    if (mode.inputWillAll()) setMaxInput(transferCache);
    if (mode.outputWithAll()) setMaxOutput(transferCache);
  }

  @Override
  public String getTypeString() {
    return "energy";
  }
}
