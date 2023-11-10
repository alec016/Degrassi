package es.degrassi.forge.integration.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import es.degrassi.forge.init.registration.RecipeRegistry;
import es.degrassi.forge.integration.kubejs.recipes.DegrassiRecipeSchemas;
import es.degrassi.forge.integration.kubejs.recipes.builder.AbstractRecipeBuilderJS;
import es.degrassi.forge.util.IntRange;
import org.jetbrains.annotations.NotNull;

public class DegrassiKubeJSPlugin extends KubeJSPlugin {

  @Override
  public void registerEvents() {
    DegrassiEvents.register();
  }

  @Override
  public void registerRecipeSchemas(@NotNull RegisterRecipeSchemasEvent event) {
    event.register(RecipeRegistry.FURNACE_RECIPE_TYPE.getId(), DegrassiRecipeSchemas.FURNACE_MACHINE);
    event.register(RecipeRegistry.MELTER_RECIPE_TYPE.getId(), DegrassiRecipeSchemas.MELTER_MACHINE);
    event.register(RecipeRegistry.UPGRADE_MAKER_RECIPE_TYPE.getId(), DegrassiRecipeSchemas.UPGRADE_MACHINE);
  }

  @Override
  public void clearCaches() {
    AbstractRecipeBuilderJS.IDS.clear();
  }

  @Override
  public void registerTypeWrappers(ScriptType type, @NotNull TypeWrappers typeWrappers) {
    typeWrappers.register(IntRange.class, (ctx, o) -> IntRange.of(o));
  }
}
