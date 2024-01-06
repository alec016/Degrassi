package es.degrassi.forge.util.storage;

import es.degrassi.forge.init.gui.IComponent;
import net.minecraft.nbt.*;
import net.minecraftforge.common.util.INBTSerializable;

@SuppressWarnings("unused")
public abstract class ExperienceStorage implements INBTSerializable<Tag>, IComponent {
  protected int xp;

  public ExperienceStorage() {
    this(0);
  }

  public ExperienceStorage(int xp) {
    this.xp = xp;
  }

  public int getXp() {
    return this.xp;
  }

  public void setXp(int xp) {
    this.xp = xp;
    onExperienceChanged();
  }

  public void addXp(int xp) {
    this.xp += xp;
    onExperienceChanged();
  }

  public void extractXp(int xp) {
    if(canExtract(xp)) {
      this.xp -= xp;
    }
  }

  public boolean canExtract(int xp) {
    return this.xp >= xp;
  }

  @Override
  public Tag serializeNBT() {
    return IntTag.valueOf(this.getXp());
  }

  @Override
  public void deserializeNBT(Tag nbt) {
    if (!(nbt instanceof IntTag intNbt))
      throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
    this.xp = intNbt.getAsInt();
  }

  public abstract void onExperienceChanged();
}
