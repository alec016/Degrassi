package es.degrassi.forge.api.core.common;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IProcessor extends INBTSerializable<CompoundTag> {
  void processTick();
  void processStart();
  void processEnd();
//  MachineRecipe getOldRecipe();
//  void setRecipe(MachineRecipe recipe);
}
