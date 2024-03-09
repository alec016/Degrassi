package es.degrassi.forge.core.common.component;

import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.network.component.ProgressPacket;
import net.minecraft.nbt.CompoundTag;

public class ProgressComponent implements IComponent {
  public static final String id = "progress";
  private int progress, maxProgress;
  private final ComponentManager manager;
  private final MachineEntity<?> entity;
  public ProgressComponent(ComponentManager manager, MachineEntity<?> entity) {
    this.manager = manager;
    this.entity = entity;
  }
  @Override
  public ComponentManager getManager() {
    return manager;
  }

  @Override
  public void markDirty() {
    entity.setChanged();
    if(entity.getLevel() != null && !entity.getLevel().isClientSide())
      new ProgressPacket(progress, maxProgress, id, entity.getBlockPos())
        .sendToChunkListeners(entity.getLevel().getChunkAt(entity.getBlockPos()));
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void serialize(CompoundTag nbt) {
    CompoundTag tag = new CompoundTag();
    tag.putInt("progress", progress);
    tag.putInt("maxProgress", maxProgress);
    nbt.put(id, tag);
  }

  @Override
  public void deserialize(CompoundTag nbt) {
    if (nbt.contains(id)) {
      CompoundTag tag = nbt.getCompound(id);
      this.progress = tag.getInt("progress");
      this.maxProgress = tag.getInt("maxProgress");
    }
  }

  public void setMaxProgress(int maxProgress) {
    this.maxProgress = maxProgress;
    markDirty();
  }

  public void setProgress(int progress) {
    if (progress == this.progress) return;
    this.progress = progress;
    markDirty();
  }

  public void tick() {
    this.progress++;
    markDirty();
  }

  public void resetProgress () {
    this.progress = 0;
    markDirty();
  }

  public int getProgress() {
    return progress;
  }

  public int getMaxProgress() {
    return maxProgress;
  }

  public float getProgressPercentage() {
    return progress / (maxProgress * 1F);
  }

  public boolean hasEnded() {
    return this.progress >= this.maxProgress;
  }

  @Override
  public String toString() {
    return "ProgressComponent{" +
      "progress=" + progress +
      ", maxProgress=" + maxProgress +
      ", id='" + id + "'" +
      '}';
  }
}
