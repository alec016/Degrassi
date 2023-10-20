package es.degrassi.comon.util.storage;

import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class EfficiencyStorage implements INBTSerializable<Tag> {
  protected double efficiency;
  protected int capacity;

  public EfficiencyStorage() {
    this(100, 0);
  }
  public EfficiencyStorage(int capacity)
  {
    this(capacity, 0);
  }

  public EfficiencyStorage(int capacity, double efficiency) {
    this.capacity = capacity;
    this.efficiency = Math.max(0 , Math.min(capacity, efficiency));
  }

  @Override
  public Tag serializeNBT() {
    return DoubleTag.valueOf(this.getEfficiency());
  }

  @Override
  public void deserializeNBT(Tag nbt) {
    if (!(nbt instanceof DoubleTag doubleNbt))
      throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
    this.efficiency = doubleNbt.getAsDouble();
  }

  public double extractEfficiency(double maxExtract, boolean simulate) {
    if (!canExtract()) return 0;

    double efficiencyExtracted = Math.min(efficiency, maxExtract);
    if (!simulate)
      efficiency -= efficiencyExtracted;
    onEfficiencyChanged();
    return efficiencyExtracted;
  }

  public double getEfficiency() {
    return this.efficiency * 100;
  }

  public int getMaxEfficiencyStored() {
    return this.capacity;
  }

  public boolean canExtract() {
    return true;
  }

  public void setEfficiency(double efficiency) {
    if (efficiency > this.capacity) return;
    this.efficiency = efficiency;
    onEfficiencyChanged();
  }
  public abstract void onEfficiencyChanged();
}
