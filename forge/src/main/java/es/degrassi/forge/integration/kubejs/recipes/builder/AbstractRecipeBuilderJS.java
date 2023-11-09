package es.degrassi.forge.integration.kubejs.recipes.builder;

import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.init.recipe.IDegrassiRecipe;
import es.degrassi.forge.init.recipe.builder.AbstractRecipeBuilder;
import es.degrassi.forge.integration.kubejs.recipes.DegrassiRecipeSchemas;
import es.degrassi.forge.requirements.IRequirement;
import es.degrassi.forge.util.Utils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.helpers.MessageFormatter;

import java.util.*;

@SuppressWarnings("unchecked")
public abstract class AbstractRecipeBuilderJS<
  T extends AbstractRecipeBuilder<? extends IDegrassiRecipe>,
  E extends AbstractRecipeBuilderJS<T, E>
> extends RecipeJS implements RecipeBuilderJS<E> {
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
      this.id = new DegrassiLocation(this.typeID.getPath() + "/server_scripts/" + uniqueID);
    }
  }
  public abstract T makeBuilder();

  @Override
  public E addRequirement(IRequirement<?> requirement) {
    setValue(DegrassiRecipeSchemas.REQUIREMENTS, Utils.addToArray(getValue(DegrassiRecipeSchemas.REQUIREMENTS), requirement));
    return (E) this;
  }

  @Override
  public String getFromToString() {
    return Objects.requireNonNull(createRecipe()).toString();
  }

  @Override
  public E error(String error, Object... args) {
    throw new RecipeExceptionJS(MessageFormatter.arrayFormat(error, args).getMessage());
  }
}
