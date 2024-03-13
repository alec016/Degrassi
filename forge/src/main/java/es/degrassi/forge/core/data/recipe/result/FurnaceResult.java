package es.degrassi.forge.core.data.recipe.result;

import com.google.gson.JsonObject;
import es.degrassi.forge.core.common.RequirementManager;
import es.degrassi.forge.core.common.requirement.EnergyRequirement;
import es.degrassi.forge.core.common.requirement.ExperienceRequirement;
import es.degrassi.forge.core.common.requirement.ItemRequirement;
import es.degrassi.forge.core.data.recipe.Result;
import es.degrassi.forge.core.init.RecipeRegistration;
import es.degrassi.forge.core.init.RequirementRegistration;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;

public class FurnaceResult extends Result {
  public FurnaceResult(ResourceLocation id, RequirementManager manager, int time, Advancement.Builder advancement, ResourceLocation advancementId) {
    super(id, manager, time, advancement, advancementId, RecipeRegistration.FURNACE_SERIALIZER.get());
  }

  @Override
  public void serializeRecipeData(JsonObject json) {
    super.serializeRecipeData(json);

    manager.getByType(RequirementRegistration.ENERGY.get()).stream().map(req -> (EnergyRequirement) req)
      .findFirst().ifPresent(req -> req.toJson(json));
    manager.getByType(RequirementRegistration.ITEM.get()).stream().map(req -> (ItemRequirement) req)
      .toList().forEach(req -> req.toJson(json));
    manager.getByType(RequirementRegistration.EXPERIENCE.get()).stream().map(req -> (ExperienceRequirement) req)
      .findFirst().ifPresent(req -> req.toJson(json));
  }
}
