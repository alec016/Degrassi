package es.degrassi.forge.init.gui.component;

import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.Tag;

@SuppressWarnings("unused")
public class EfficiencyComponent implements IComponent {
  protected double efficiency;
  protected int capacity;
  private final ComponentManager manager;

  public EfficiencyComponent(ComponentManager manager) {
    this(manager, 100, 0);
  }
  public EfficiencyComponent(ComponentManager manager, int capacity)
  {
    this(manager, capacity, 0);
  }

  public EfficiencyComponent(ComponentManager manager, int capacity, double efficiency) {
    this.manager = manager;
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
    onChanged();
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
    onChanged();
  }

  public ComponentManager getManager () {
    return manager;
  }

  public void onChanged() {
    manager.markDirty();
  }
}
