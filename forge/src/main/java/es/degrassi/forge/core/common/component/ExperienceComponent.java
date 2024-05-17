package es.degrassi.forge.core.common.component;

import com.google.gson.JsonObject;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.requirement.ExperienceRequirement;
import es.degrassi.forge.core.network.component.ExperiencePacket;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;

@Getter
@Setter
public class ExperienceComponent implements IComponent {
  private float experience, capacity;
  private final ComponentManager manager;
  private final MachineEntity<?> entity;
  private final String id;
  private ComponentIOMode mode;
  public ExperienceComponent(ComponentManager manager, float capacity, MachineEntity<?> entity, String id, ComponentIOMode mode) {
    this.manager = manager;
    this.capacity = capacity;
    this.entity = entity;
    this.id = id;
    this.mode = mode;
  }

  @Override
  public void markDirty() {
    entity.setChanged();
    if(entity.getLevel() != null && !entity.getLevel().isClientSide())
      new ExperiencePacket(experience, capacity, id, entity.getBlockPos())
        .sendToChunkListeners(entity.getLevel().getChunkAt(entity.getBlockPos()));
  }

  @Override
  public void fill(IRequirement<?> requirement) {
    if (requirement instanceof ExperienceRequirement req) {
      this.capacity = req.getXp();
      if (req.getMode().isInput()) this.experience = capacity;
        markDirty();
    }
  }

  @Override
  public CompoundTag serialize() {
    CompoundTag tag = new CompoundTag();
    tag.putFloat("experience", experience);
    tag.putFloat("capacity", capacity);
    tag.putString("mode", mode.serialize());
    tag.putString("id", id);
    return tag;
  }

  @Override
  public void deserialize(CompoundTag nbt) {
    this.experience = nbt.getFloat("experience");
    this.capacity = nbt.getFloat("capacity");
    this.mode = ComponentIOMode.deserialize(nbt.getString("mode"));
  }

  public void setExperience(float experience) {
    if (experience == this.experience) return;
    this.experience = experience;
    markDirty();
  }

  public float receiveExperience(float experience, boolean simulate) {
    if (mode.output()) return 0;
    float toReceive = Math.min(this.capacity - this.experience, experience);
    if (!simulate) {
      this.experience += toReceive;
    }
    markDirty();
    return toReceive;
  }

  public float extractExperience(float experience, boolean simulate) {
    if(mode.input()) return 0;
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

  public void setCapacity(float capacity) {
    this.capacity = capacity;
    markDirty();
  }

  public boolean canExtract() {
    return this.experience > 0;
  }

  public boolean canReceive() {
    return this.experience < this.capacity;
  }

  // recipe stuff
  public float receiveRecipeExperience(float experience, boolean simulate) {
    float toReceive = Math.min(this.capacity - this.experience, experience);
    if (!simulate) {
      this.experience += toReceive;
    }
    markDirty();
    return toReceive;
  }

  public float extractRecipeExperience(float experience, boolean simulate) {
    float toExtract = Math.min(this.experience, experience);
    if (!simulate) {
      this.experience -= toExtract;
    }
    markDirty();
    return toExtract;
  }

  @Override
  public String getTypeString() {
    return "experience";
  }

  @Override
  public String toString() {
    return asJson().toString();
  }

  public JsonObject asJson() {
    JsonObject json = new JsonObject();
    json.addProperty("experience", experience);
    json.addProperty("capacity", capacity);
    json.addProperty("id", id);
    json.addProperty("type", "experience");
    return json;
  }

  public ExperienceComponent copy(MachineEntity<?> entity, ComponentManager manager) {
    return new ExperienceComponent(manager, capacity, entity, id, mode);
  }
}
