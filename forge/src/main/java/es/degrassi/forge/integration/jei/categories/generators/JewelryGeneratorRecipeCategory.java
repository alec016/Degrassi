package es.degrassi.forge.integration.jei.categories.generators;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.init.recipe.recipes.generators.JewelryGeneratorRecipe;
import es.degrassi.forge.init.registration.BlockRegister;
import es.degrassi.forge.integration.jei.DegrassiJEIRecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class JewelryGeneratorRecipeCategory extends GeneratorRecipeCategory<JewelryGeneratorRecipe>{
  public static final ResourceLocation UID = new DegrassiLocation("jewelry");
  public static final ResourceLocation TEXTURE = new DegrassiLocation("textures/gui/jei/jewelry_generator_gui.png");

  public JewelryGeneratorRecipeCategory(@NotNull IJeiHelpers helper) {
    super(createBackground(helper), createIcon(helper));
  }

  private static @NotNull IDrawable createBackground(@NotNull IJeiHelpers helper) {
    return helper.getGuiHelper().drawableBuilder(TEXTURE, 0, 0, 126, 73).setTextureSize(126, 73).build();
  }

  private static @NotNull IDrawable createIcon(@NotNull IJeiHelpers helper) {
    return helper.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegister.IRON_FURNACE_BLOCK.get()));
  }

  @Override
  public @NotNull RecipeType<JewelryGeneratorRecipe> getRecipeType() {
    return DegrassiJEIRecipeTypes.JEWELRY_GENERATOR_TYPE;
  }

  @Override
  public @NotNull Component getTitle() {
    return Component.literal("Jewelry Generator");
  }

  @Override
  public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull JewelryGeneratorRecipe recipe, @NotNull IFocusGroup focuses) {

  }
}
