package es.degrassi.forge.init.gui.component;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.IntTag;

@SuppressWarnings("unused")
public class ExperienceComponent implements IComponent {
  protected int xp;
  private final ComponentManager manager;

  public ExperienceComponent(ComponentManager manager) {
    this(manager, 0);
  }

  public ExperienceComponent(ComponentManager manager, int xp) {
    this.xp = xp;
    this.manager = manager;
  }

  public int getXp() {
    return this.xp;
  }

  public void setXp(int xp) {
    this.xp = xp;
    onChanged();
  }

  public void addXp(int xp) {
    this.xp += xp;
    onChanged();
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

  @Override
  public ComponentManager getManager() {
    return manager;
  }

  public void onChanged() {
    manager.markDirty();
  }
}
