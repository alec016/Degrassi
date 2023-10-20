package es.degrassi.forge.integration.jei;

import es.degrassi.Degrassi;
import es.degrassi.comon.init.recipe.panel.PanelRecipe;
import es.degrassi.forge.init.registration.BlockRegister;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class PanelCategory implements IRecipeCategory<PanelRecipe> {
  public static final ResourceLocation TEXTURE = new ResourceLocation(Degrassi.MODID, "textures/gui/base_background.png");
  public static final ResourceLocation PROGRESS_FILLED = new ResourceLocation(Degrassi.MODID, "textures/gui/base_background.png");

  private final IDrawable background;
  private final IDrawable icon;
  private final IGuiHelper helper;

  public PanelCategory(@NotNull IGuiHelper helper) {
    this.background = helper.drawableBuilder(TEXTURE, 0, 0, 176, 85).setTextureSize(256, 166).build();
    this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegister.SP1_BLOCK.get()));
    this.helper = helper;
  }

  @Override
  public RecipeType<PanelRecipe> getRecipeType() {
    return null;
  }

  @Override
  public IDrawable getBackground() {
    return background;
  }

  @Override
  public IDrawable getIcon() {
    return icon;
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, PanelRecipe recipe, IFocusGroup focuses) {
  }
}
