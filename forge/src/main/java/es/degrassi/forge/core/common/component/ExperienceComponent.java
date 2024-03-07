package es.degrassi.forge.core.common.component;

import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.network.component.ExperiencePacket;
import net.minecraft.nbt.CompoundTag;

public class ExperienceComponent implements IComponent {
  private float experience;
  private final ComponentManager manager;
  private final float capacity;
  private final MachineEntity entity;
  private final String id;
  public ExperienceComponent(ComponentManager manager, float capacity, MachineEntity entity, String id) {
    this.manager = manager;
    this.capacity = capacity;
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
      new ExperiencePacket(experience, id, entity.getBlockPos())
        .sendToChunkListeners(entity.getLevel().getChunkAt(entity.getBlockPos()));
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void serialize(CompoundTag nbt) {
    CompoundTag tag = new CompoundTag();
    tag.putFloat("experience", experience);
    nbt.put(id, tag);
  }

  @Override
  public void deserialize(CompoundTag nbt) {
    if (nbt.contains(id)) {
      CompoundTag tag = nbt.getCompound(id);
      this.experience = tag.getFloat("experience");
    }
  }

  public void setExperience(float experience) {
    if (experience == this.experience) return;
    this.experience = experience;
    markDirty();
  }

  public float receiveExperience(float experience, boolean simulate) {
    float toReceive = Math.min(this.capacity - this.experience, experience);
    if (!simulate) {
      this.experience += toReceive;
    }
    markDirty();
    return toReceive;
  }

  public float extractExperience(float experience, boolean simulate) {
    float toExtract = Math.min(this.experience, experience);
    if (!simulate) {
      this.experience -= toExtract;
    }
    markDirty();
    return toExtract;
  }

  public float getExperienceStored() {
    return experience;
  }

  public float getCapacity() {
    return capacity;
  }

  public boolean canExtract() {
    return true;
  }

  public boolean canReceive() {
    return true;
  }

  @Override
  public String toString() {
    return "ExperienceComponent{" +
      "experience=" + experience +
      ", capacity=" + capacity +
      ", id='" + id + '\'' +
      '}';
  }
}
