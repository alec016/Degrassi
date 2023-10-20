package es.degrassi.comon.util.storage;

import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class GenerationStorage implements INBTSerializable<Tag> {
  protected int generation;

  public GenerationStorage() {
    this(0);
  }

  public GenerationStorage(int generation) {
    this.generation = Math.max(0 , generation);
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
    onGenerationChanged();
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
    onGenerationChanged();
  }
  public abstract void onGenerationChanged();
}
