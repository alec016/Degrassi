package es.degrassi.forge.init.recipe;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IDegrassiRecipe extends Recipe<SimpleContainer> {

  int getTime();

  boolean showInJei();

  int getEnergyRequired();

  boolean isModified();

  void modify();

  IDegrassiRecipe copy();

  void setTime(int i);

  void setEnergyRequired(int i);

  default FluidStack getFluid() {
    return FluidStack.EMPTY;
  }
}
