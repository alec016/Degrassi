package es.degrassi.forge.init.gui.component;

import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

@SuppressWarnings("unused")
public class GenerationComponent implements INBTSerializable<Tag>, IComponent {
  protected int generation;
  private final ComponentManager manager;

  public GenerationComponent(ComponentManager manager) {
    this(manager, 0);
  }

  public GenerationComponent(ComponentManager manager, int generation) {
    this.generation = Math.max(0 , generation);
    this.manager = manager;
  }

  @Override
  public Tag serializeNBT() {
    return IntTag.valueOf(this.getGeneration());
  }

  @Override
  public void deserializeNBT(Tag nbt) {
    if (!(nbt instanceof IntTag intNbt))
      throw new IllegalArgumentException("Can not deserialize to an instance that isn't the default implementation");
    this.generation = intNbt.getAsInt();
  }

  public int extractGeneration(int maxExtract, boolean simulate) {
    if (!canExtract()) return 0;

    int generationExtracted = Math.min(generation, maxExtract);
    if (!simulate)
      generation -= generationExtracted;
    onChanged();
    return generationExtracted;
  }

  public int getGeneration() {
    return this.generation;
  }

  public boolean canExtract() {
    return true;
  }

  public void setGeneration(int generation) {
    this.generation = generation;
    onChanged();
  }

  @Override
  public ComponentManager getManager() {
    return manager;
  }

  public void onChanged() {
    manager.markDirty();
  }
}
