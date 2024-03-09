package es.degrassi.forge.api.core.common;

import es.degrassi.forge.core.common.recipe.MachineRecipe;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IProcessor<T extends MachineRecipe> extends INBTSerializable<CompoundTag> {
  List<T> getRecipes();
  void processTick();
  void processStart();
  void processEnd();
  void tick();
  MachineRecipe getOldRecipe();

  void setRecipe(T recipe);
  void searchForRecipe(List<? extends IComponent> components);
}
