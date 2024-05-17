package es.degrassi.forge.core.common.recipe;

import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IRequirement;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

/**
 * DO NOT USE
 */
public abstract class SolarPanelRecipe extends MachineRecipe<SolarPanelRecipe> {
  public SolarPanelRecipe(int time, List<IRequirement<?>> requirements) {
    super(time, requirements);
  }
}
