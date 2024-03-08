package es.degrassi.forge.api.core.common;

import es.degrassi.forge.core.common.recipe.MachineRecipe;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IProcessor<T extends MachineRecipe> extends INBTSerializable<CompoundTag> {
  List<T> getRecipes();
  void processTick(List<? extends IComponent> components);
  void processStart(List<? extends IComponent> components);
  void processEnd(List<? extends IComponent> components);
  MachineRecipe getOldRecipe();
  void setRecipe(MachineRecipe recipe);
}
