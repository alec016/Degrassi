package es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.recipe;

import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.recipe.BaseMultiblockControllerRecipe;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

@Getter
@SuppressWarnings("unchecked")
public class MelterRecipe extends BaseMultiblockControllerRecipe<MelterRecipe> {
  private final ResourceLocation id;

  public MelterRecipe(ResourceLocation id, int time, List<? extends IRequirement<?>> requirements) {
    super(time, (List<IRequirement<?>>) requirements);
    this.id = id;
  }

  @Override
  public boolean matches(List<? extends IComponent> components) {
    List<IComponent> componentMatches = new ArrayList<>();
    AtomicInteger count = new AtomicInteger(0);
    getRequirements().forEach(requirement -> components.forEach(component -> {
      if (component.getId().equals(requirement.getId()) && requirement.matches(component, getTime())) {
        count.getAndIncrement();
        componentMatches.add(component);
      }
    }));
    tickRequirements.clear();
    endRequirements.clear();
    startRequirements.clear();
    if (count.get() == getRequirements().size()) {
      getRequirements().forEach(requirement -> componentMatches.forEach(component -> {
        if (requirement.getId().equals(component.getId())) {
          if (requirement.getMode().isPerTick()) tickRequirements.put(requirement, component);
          else {
            if (requirement.getMode().isInput()) startRequirements.put(requirement, component);
            else endRequirements.put(requirement, component);
          }
        }
      }));
      return true;
    }
    return false;
  }

  @Override
  public MelterRecipe copy() {
    MelterRecipe recipe = new MelterRecipe(id, getTime(), getRequirements().stream().map(IRequirement::copy).toList());
    recipe.startRequirements.putAll(startRequirements);
    recipe.endRequirements.putAll(endRequirements);
    recipe.tickRequirements.putAll(tickRequirements);
    return recipe;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return null;
  }

  @Override
  public RecipeType<?> getType() {
    return null;
  }

  @Override
  public String toString() {
    return "Melter" + super.toString();
  }
}
