package es.degrassi.forge.init.gui.component;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;

@SuppressWarnings("unused")
public class ProgressComponent implements IComponent {
  protected int progress;
  protected int maxProgress;
  private final ComponentManager manager;

  public ProgressComponent(ComponentManager manager) {
    this(manager, 78);
  }

  public ProgressComponent(ComponentManager manager, int maxProgress) {
    this(manager, 0, maxProgress);
  }

  public ProgressComponent(ComponentManager manager, int progress, int maxProgress) {
    this.progress = Math.max(0, Math.min(progress, maxProgress));
    this.maxProgress = maxProgress;
    this.manager = manager;
  }

  @Override
  public Tag serializeNBT() {
    CompoundTag nbt = new CompoundTag();
    nbt.putInt("progress", progress);
    nbt.putInt("maxProgress", maxProgress);
    return nbt;
  }

  @Override
  public void deserializeNBT(Tag tag) {
    if (tag == null)
      throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
    if (tag instanceof CompoundTag nbt) {
      this.progress = nbt.getInt("progress");
      this.maxProgress = nbt.getInt("maxProgress");
    }
  }

  public void resetProgress() {
    this.progress = 0;
    onChanged();
  }

  public void increment() {
    increment(1);
  }

  public void increment(boolean simulate) {
    increment(1, simulate);
  }

  public void increment(int increment, boolean simulate) {
    if (!simulate) increment(increment);
  }

  public void increment(int increment) {
    if (canIncrement()) this.progress += increment;
    onChanged();
  }

  public boolean canIncrement() {
    return this.progress < this.maxProgress;
  }

  public void setMaxProgress(int maxProgress) {
    this.maxProgress = maxProgress;
    onChanged();
  }

  public void setProgress(int progress) {
    if (progress <= maxProgress)
      this.progress = progress;
    onChanged();
  }

  public int getProgress() {
    return progress;
  }

  public int getMaxProgress() {
    return maxProgress;
  }

  public void resetProgressAndMaxProgress() {
    this.maxProgress = 0;
    resetProgress();
  }

  @Override
  public ComponentManager getManager() {
    return manager;
  }

  @Override
  public void onChanged() {
    manager.markDirty();
  }
}
