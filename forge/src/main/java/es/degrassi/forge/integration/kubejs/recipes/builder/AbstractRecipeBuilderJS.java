package es.degrassi.forge.integration.kubejs.recipes.builder;

import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import es.degrassi.forge.init.recipe.IDegrassiRecipe;
import es.degrassi.forge.init.recipe.builder.AbstractRecipeBuilder;
import es.degrassi.forge.integration.kubejs.recipes.DegrassiRecipeSchemas;
import es.degrassi.forge.integration.kubejs.recipes.FurnaceRecipeSchema;
import es.degrassi.forge.requirements.IRequirement;
import es.degrassi.forge.util.Utils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.Nullable;
import org.slf4j.helpers.MessageFormatter;

import java.util.*;

public abstract class AbstractRecipeBuilderJS<T extends AbstractRecipeBuilder<?  extends IDegrassiRecipe>> extends RecipeJS implements RecipeBuilderJS {
  public static final Map<ResourceLocation, List<Integer>> IDS = new HashMap<>();
  private final ResourceLocation typeID;
  public AbstractRecipeBuilderJS(ResourceLocation typeID) {
    this.typeID = typeID;
  }

  @Override
  public void afterLoaded() {
    super.afterLoaded();
    if(this.newRecipe) {
      int uniqueID = IDS.computeIfAbsent(this.typeID, id -> new ArrayList<>()).size();
      IDS.get(this.typeID).add(uniqueID);
      this.id = new ResourceLocation("kubejs", this.typeID.getPath() + "/server_scripts/" + uniqueID);
    }
  }
  public abstract T makeBuilder();

  @Override
  public @Nullable Recipe<?> createRecipe() {
    if (this.removed) return null;
    if (!this.newRecipe) return getOriginalRecipe();

    T builder = makeBuilder();

    for (IRequirement<?> requirement : getValue(FurnaceRecipeSchema.REQUIREMENTS))
      builder.addRequirement(requirement);

    return builder.build(getOrCreateId());
  }

  @Override
  public String getFromToString() {
    return Objects.requireNonNull(createRecipe()).toString();
  }

  @Override
  public AbstractRecipeBuilderJS<T> addRequirement(IRequirement<?> requirement) {
    setValue(DegrassiRecipeSchemas.REQUIREMENTS, Utils.addToArray(getValue(DegrassiRecipeSchemas.REQUIREMENTS), requirement));
    return this;
  }

  @Override
  public AbstractRecipeBuilderJS<T> error(String error, Object... args) {
    throw new RecipeExceptionJS(MessageFormatter.arrayFormat(error, args).getMessage());
  }
}
