package es.degrassi.forge.integration.jei.categories;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.init.gui.renderer.ProgressComponent;
import es.degrassi.forge.init.recipe.recipes.UpgradeMakerRecipe;
import es.degrassi.forge.init.registration.BlockRegister;
import es.degrassi.forge.integration.jei.JEIDegrassiPlugin;
import es.degrassi.forge.integration.jei.renderer.ProgressGuiElementJeiRenderer;
import es.degrassi.forge.util.TextureSizeHelper;
import es.degrassi.forge.util.storage.ProgressStorage;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UpgradeMakerRecipeCategory implements IRecipeCategory<UpgradeMakerRecipe> {
  public static final ResourceLocation UID = new DegrassiLocation("upgrade_maker");
   public static final ResourceLocation TEXTURE = new DegrassiLocation("textures/gui/jei/upgrade_maker_gui.png");
   public static final ResourceLocation FILLED_PROGRESS = new DegrassiLocation("textures/gui/jei/upgrade_maker_progress_filled.png");

  private final IDrawable background;
  private final IDrawable icon;
  private ProgressGuiElementJeiRenderer progress;
  private ProgressComponent progressComponent;
  private ProgressStorage progressStorage;

  public UpgradeMakerRecipeCategory(@NotNull IJeiHelpers helper) {
    IGuiHelper helper1 = helper.getGuiHelper();
    this.background = helper1.drawableBuilder(TEXTURE, 0, 0, 136, 86).setTextureSize(136, 86).build();
    this.icon = helper1.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegister.UPGRADE_MAKER.get()));
  }

  @Override
  public @NotNull Component getTitle() {
    return Component.literal("Upgrading");
  }

  @Override
  public @NotNull RecipeType<UpgradeMakerRecipe> getRecipeType() {
    return JEIDegrassiPlugin.UPGRADE_MAKER_TYPE;
  }

  @Override
  public @NotNull IDrawable getBackground() {
    return background;
  }

  @Override
  public @NotNull IDrawable getIcon() {
    return icon;
  }

  @Override
  public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull UpgradeMakerRecipe recipe, @NotNull IFocusGroup focuses) {
    builder.addSlot(RecipeIngredientRole.INPUT, 46, 19).addIngredients(recipe.getIngredients().get(0));
    builder.addSlot(RecipeIngredientRole.INPUT, 46, 55).addIngredients(recipe.getIngredients().get(1));
    builder.addSlot(RecipeIngredientRole.OUTPUT, 112, 37).addItemStack(recipe.getResultItem());
    builder.addSlot(RecipeIngredientRole.INPUT, 26, 8).addIngredients(ForgeTypes.FLUID_STACK, List.of(recipe.getFluid())).setFluidRenderer(recipe.getFluid().getAmount(), false, 16, 70);
  }

  @Override
  public void draw(@NotNull UpgradeMakerRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull PoseStack stack, double mouseX, double mouseY) {
    IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
    stack.pushPose();
    // animated progress bar
    if (progress == null) {
      progress = new ProgressGuiElementJeiRenderer();
    }
    if(progressStorage == null) {
       progressStorage = new ProgressStorage(20) {
        @Override
        public void onProgressChanged() {
          if(progress > maxProgress) resetProgress();
        }
      };
    }
    if (progressComponent == null) {
      progressComponent = new ProgressComponent(
        66,
        36,
        progressStorage,
        TextureSizeHelper.getTextureWidth(FILLED_PROGRESS),
        TextureSizeHelper.getTextureHeight(FILLED_PROGRESS),
        FILLED_PROGRESS
      );
    }
    progress.renderElementInJEI(stack, progressComponent, recipe, mouseX, mouseY, 66, 36);
    stack.popPose();
  }
}
