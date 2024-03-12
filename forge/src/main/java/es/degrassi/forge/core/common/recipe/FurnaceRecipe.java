package es.degrassi.forge.core.common.recipe;

import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.init.RecipeRegistration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

public class FurnaceRecipe extends MachineRecipe<FurnaceRecipe> {
  private final ResourceLocation id;

  @SuppressWarnings("unchecked")
  public FurnaceRecipe(ResourceLocation id, int time, List<? extends IRequirement<?>> requirements) {
    super(time, (List<IRequirement<?>>) requirements);
    this.id = id;
  }

  @Override
  public @NotNull ResourceLocation getId() {
    return id;
  }

  @Override
  public @NotNull RecipeSerializer<FurnaceRecipe> getSerializer() {
    return RecipeRegistration.FURNACE_SERIALIZER.get();
  }

  @Override
  public @NotNull RecipeType<FurnaceRecipe> getType() {
    return RecipeRegistration.FURNACE_TYPE.get();
  }

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
  public FurnaceRecipe copy() {
    FurnaceRecipe recipe = new FurnaceRecipe(getId(), getTime(), getRequirements().stream().map(IRequirement::copy).toList());
    recipe.startRequirements.putAll(startRequirements);
    recipe.endRequirements.putAll(endRequirements);
    recipe.tickRequirements.putAll(tickRequirements);
    return recipe;
  }

  @Override
  public String toString() {
    return "Furnace" + super.toString();
  }
}
