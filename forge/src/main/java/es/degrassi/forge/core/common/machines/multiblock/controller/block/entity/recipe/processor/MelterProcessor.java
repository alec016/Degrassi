package es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.recipe.processor;

import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.MelterControllerEntity;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.recipe.MelterRecipe;
import es.degrassi.forge.core.init.RecipeRegistration;
import java.util.ArrayList;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

public class MelterProcessor extends MultiblockProcessor<MelterRecipe, MelterControllerEntity> {
  public MelterProcessor(MelterControllerEntity entity, boolean reset) {
    super(entity, reset);
  }

  @Override
  public MelterProcessor init() {
    initialized = true;
    Level level = entity == null ? Objects.requireNonNull(Minecraft.getInstance().level) : entity.getLevel();
    RecipeManager recipeManager = level == null ? Minecraft.getInstance().level.getRecipeManager() : level.getRecipeManager();
    recipes = new ArrayList<>();
    recipes.addAll(recipeManager.getAllRecipesFor(RecipeRegistration.MELTER_TYPE.get()));

    if(this.futureRecipeID != null && this.entity.getLevel() != null) {
      this.recipes.forEach(recipe -> {
        if (recipe.getId().equals(futureRecipeID))
          setRecipe(recipe);
      });
      this.futureRecipeID = null;
    }
    return this;
  }
}
