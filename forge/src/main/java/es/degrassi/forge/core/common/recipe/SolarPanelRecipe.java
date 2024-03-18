package es.degrassi.forge.core.common.recipe;

import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IRequirement;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class SolarPanelRecipe extends MachineRecipe<SolarPanelRecipe> {
  public SolarPanelRecipe(int time, List<IRequirement<?>> requirements) {
    super(time, requirements);
  }

  @Override
  public boolean matches(List<? extends IComponent> components) {
    return false;
  }

  @Override
  public SolarPanelRecipe copy() {
    return null;
  }

  @Override
  public ResourceLocation getId() {
    return null;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return null;
  }

  @Override
  public RecipeType<?> getType() {
    return null;
  }
}
