package es.degrassi.comon.util.storage;

import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class ExperienceStorage implements INBTSerializable<Tag> {
  protected float xp;

  public ExperienceStorage() {
    this(0);
  }

  public ExperienceStorage(float xp) {
    this.xp = xp;
  }

  public float getXp() {
    return this.xp;
  }

  public void setXp(float xp) {
    this.xp = xp;
    onExperienceChanged();
  }

  public void addXp(float xp) {
    this.xp += xp;
    onExperienceChanged();
  }

  public void extractXp(float xp) {
    if(canExtract(xp)) {
      this.xp -= xp;
    }
  }

  public boolean canExtract(float xp) {
    return this.xp >= xp;
  }

  @Override
  public Tag serializeNBT() {
    return FloatTag.valueOf(this.getXp());
  }

  @Override
  public void deserializeNBT(Tag nbt) {
    if (!(nbt instanceof FloatTag intNbt))
      throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
    this.xp = intNbt.getAsFloat();
  }

  public abstract void onExperienceChanged();
}
