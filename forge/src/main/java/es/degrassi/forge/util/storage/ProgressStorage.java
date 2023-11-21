package es.degrassi.forge.util.storage;

import es.degrassi.forge.init.gui.IComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.io.DataOutput;

@SuppressWarnings("unused")
public abstract class ProgressStorage implements INBTSerializable<CompoundTag>, IComponent {
  protected int progress;
  protected int maxProgress;

  public ProgressStorage() {
    this(78);
  }

  public ProgressStorage(int maxProgress) {
    this(0, maxProgress);
  }

  public ProgressStorage(int progress, int maxProgress) {
    this.progress = Math.max(0, Math.min(progress, maxProgress));
    this.maxProgress = maxProgress;
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag nbt = new CompoundTag();
    nbt.putInt("progress", progress);
    nbt.putInt("maxProgress", maxProgress);
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    if (nbt == null)
      throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
    this.progress = nbt.getInt("progress");
    this.maxProgress = nbt.getInt("maxProgress");
  }

  public static ProgressStorage fromNBT(CompoundTag nbt) {
    if (nbt == null)
      throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
    int progress = nbt.getInt("progress");
    int maxProgress = nbt.getInt("maxProgress");
    return new ProgressStorage(progress, maxProgress) {
      @Override
      public void onProgressChanged() {
      }
    };
  }

  public void resetProgress() {
    this.progress = 0;
    onProgressChanged();
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
    onProgressChanged();
  }

  public boolean canIncrement() {
    return this.progress < this.maxProgress;
  }

  public void setMaxProgress(int maxProgress) {
    this.maxProgress = maxProgress;
    onProgressChanged();
  }

  public void setProgress(int progress) {
    if (progress <= maxProgress)
      this.progress = progress;
    onProgressChanged();
  }

  public int getProgress() {
    return progress;
  }

  public int getMaxProgress() {
    return maxProgress;
  }

  public abstract void onProgressChanged();

  public void resetProgressAndMaxProgress() {
    this.maxProgress = 0;
    resetProgress();
  }
}
