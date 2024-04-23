package es.degrassi.forge.core.integration.jei;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.common.utils.TextureSizeHelper;
import es.degrassi.forge.core.common.machines.block.FurnaceBlock;
import es.degrassi.forge.core.common.machines.screen.FurnaceScreen;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.integration.jei.categories.FurnaceCategory;
import es.degrassi.forge.core.integration.jei.helper.EnergyIngredientHelper;
import es.degrassi.forge.core.integration.jei.helper.ExperienceIngredientHelper;
import es.degrassi.forge.core.integration.jei.wrapper.EnergyWrapper;
import es.degrassi.forge.core.integration.jei.wrapper.ExperienceWrapper;
import es.degrassi.forge.core.integration.jei.wrapper.IngredientTypes;
import es.degrassi.forge.core.integration.jei.wrapper.ProgressWrapper;
import es.degrassi.forge.core.integration.jei.helper.ProgressIngredientHelper;
import es.degrassi.forge.core.tiers.Furnace;
import java.util.ArrayList;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@JeiPlugin
public class DegrassiJEIPlugin implements IModPlugin {

  @Override
  public ResourceLocation getPluginUid() {
    return new DegrassiLocation("jei_plugin");
  }

  @Override
  public void registerIngredients(IModIngredientRegistration registration) {
    registration.register(IngredientTypes.PROGRESS, new ArrayList<>(), new ProgressIngredientHelper(), ProgressWrapper.DUMMY);
    registration.register(IngredientTypes.ENERGY, new ArrayList<>(), new EnergyIngredientHelper(), EnergyWrapper.DUMMY);
    registration.register(IngredientTypes.EXPERIENCE, new ArrayList<>(), new ExperienceIngredientHelper(), ExperienceWrapper.DUMMY);
  }

  @Override
  public void registerCategories(IRecipeCategoryRegistration registration) {
    registerFurnaceCategory(registration);
  }

  @Override
  public void registerRecipes(IRecipeRegistration registration) {
    registerFurnaceRecipe(registration);
  }

  @Override
  public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
    registerFurnaceCatalysts(registration);
  }

  @Override
  public void registerGuiHandlers(IGuiHandlerRegistration registration) {
    registerFurnaceGuiHandler(registration);
  }

  // FURNACE
  private void registerFurnaceRecipe(IRecipeRegistration registration) {
    registration.addRecipes(DegrassiRecipeTypes.FURNACE, FurnaceCategory.defaultProcessor.getRecipes());
  }
  private void registerFurnaceCategory(IRecipeCategoryRegistration registration) {
    IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
    registration.addRecipeCategories(new FurnaceCategory(guiHelper, (FurnaceBlock) BlockRegistration.FURNACE.get(Furnace.IRON)));
  }
  private void registerFurnaceGuiHandler(IGuiHandlerRegistration registration) {
    registration.addRecipeClickArea(
      FurnaceScreen.class,
      66,
      33,
      TextureSizeHelper.getTextureWidth(new DegrassiLocation("textures/gui/furnace_progress_filled.png")),
      TextureSizeHelper.getTextureHeight(new DegrassiLocation("textures/gui/furnace_progress_filled.png")),
      DegrassiRecipeTypes.FURNACE
    );
  }
  private void registerFurnaceCatalysts(IRecipeCatalystRegistration registration) {
    for (Furnace tier : Furnace.values()) {
      registration.addRecipeCatalyst(
        new ItemStack(BlockRegistration.FURNACE.get(tier)),
        DegrassiRecipeTypes.FURNACE,
        RecipeTypes.SMELTING,
        RecipeTypes.BLASTING,
        RecipeTypes.SMOKING
      );
    }
  }
}
