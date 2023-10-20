package es.degrassi.forge.integration.jei;

import es.degrassi.Degrassi;
import es.degrassi.comon.init.recipe.furnace.FurnaceRecipe;
import es.degrassi.comon.init.recipe.helpers.furnace.FurnaceRecipeHelper;
import es.degrassi.forge.init.registration.BlockRegister;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class JEIDegrassiPlugin implements IModPlugin {
  public static RecipeType<FurnaceRecipe> FURNACE_TYPE = new RecipeType<>(
    FurnaceRecipeCategory.UID,
    FurnaceRecipe.class
  );

  @Override
  public @NotNull ResourceLocation getPluginUid() {
    return new ResourceLocation(Degrassi.MODID, "jei_plugin");
  }

  @Override
  public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalyst(
      new ItemStack(BlockRegister.IRON_FURNACE_BLOCK.get()),
      FURNACE_TYPE,
      RecipeTypes.SMELTING,
      RecipeTypes.BLASTING,
      RecipeTypes.SMOKING
    );
    registration.addRecipeCatalyst(
      new ItemStack(BlockRegister.GOLD_FURNACE_BLOCK.get()),
      FURNACE_TYPE,
      RecipeTypes.SMELTING,
      RecipeTypes.BLASTING,
      RecipeTypes.SMOKING
    );
    registration.addRecipeCatalyst(
      new ItemStack(BlockRegister.DIAMOND_FURNACE_BLOCK.get()),
      FURNACE_TYPE,
      RecipeTypes.SMELTING,
      RecipeTypes.BLASTING,
      RecipeTypes.SMOKING
    );
    registration.addRecipeCatalyst(
      new ItemStack(BlockRegister.EMERALD_FURNACE_BLOCK.get()),
      FURNACE_TYPE,
      RecipeTypes.SMELTING,
      RecipeTypes.BLASTING,
      RecipeTypes.SMOKING
    );
    registration.addRecipeCatalyst(
      new ItemStack(BlockRegister.NETHERITE_FURNACE_BLOCK.get()),
      FURNACE_TYPE,
      RecipeTypes.SMELTING,
      RecipeTypes.BLASTING,
      RecipeTypes.SMOKING
    );
  }

  @Override
  public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
    registration.addRecipeCategories(
      new FurnaceRecipeCategory(registration.getJeiHelpers().getGuiHelper())
    );
  }

  @Override
  public void registerRecipes(@NotNull IRecipeRegistration registration) {
    // RecipeManager manager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

    FurnaceRecipeHelper.init();

    registration.addRecipes(FURNACE_TYPE, FurnaceRecipeHelper.recipes);
  }
}
