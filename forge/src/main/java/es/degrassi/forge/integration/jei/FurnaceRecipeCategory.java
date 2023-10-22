package es.degrassi.forge.integration.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.Degrassi;
import es.degrassi.comon.init.gui.screen.renderer.ProgressComponent;
import es.degrassi.comon.init.recipe.furnace.FurnaceRecipe;
import es.degrassi.comon.util.TextureSizeHelper;
import es.degrassi.comon.util.storage.ProgressStorage;
import es.degrassi.forge.init.registration.BlockRegister;
import es.degrassi.forge.integration.jei.renderer.ProgressGuiElementJeiRenderer;
import mezz.jei.api.constants.VanillaTypes;
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

public class FurnaceRecipeCategory implements IRecipeCategory<FurnaceRecipe> {
  public static final ResourceLocation UID = new ResourceLocation(Degrassi.MODID, "furnace");
  public static final ResourceLocation TEXTURE = new ResourceLocation(Degrassi.MODID, "textures/gui/furnace_gui.png");
  public static final ResourceLocation FILLED_PROGRESS = new ResourceLocation(Degrassi.MODID, "textures/gui/furnace_progress_filled.png");
  public static final ResourceLocation FILLED_ENERGY = new ResourceLocation(Degrassi.MODID, "textures/gui/furnace_energy_storage_filled.png");

  private final IDrawable background;
  private final IDrawable icon;
  private final IGuiHelper helper;
  private ProgressGuiElementJeiRenderer progress;
  private ProgressComponent progressComponent;
  private ProgressStorage progressStorage;

  public FurnaceRecipeCategory(@NotNull IJeiHelpers helper) {
    this.helper = helper.getGuiHelper();
    this.background = this.helper.drawableBuilder(TEXTURE, 0, 0, 176, 95).setTextureSize(176, 180).build();
    this.icon = this.helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegister.IRON_FURNACE_BLOCK.get()));
  }

  @Override
  public @NotNull Component getTitle() {
    return Component.literal("Degrassi Furnace");
  }

  @Override
  public @NotNull RecipeType<FurnaceRecipe> getRecipeType() {
    return JEIDegrassiPlugin.FURNACE_TYPE;
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
  public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull FurnaceRecipe recipe, @NotNull IFocusGroup focuses) {
    builder.addSlot(RecipeIngredientRole.INPUT, 44, 34).addIngredients(recipe.getIngredients().get(0));
    builder.addSlot(RecipeIngredientRole.OUTPUT, 134, 34).addItemStack(recipe.getResultItem());
  }

  @Override
  public void draw(@NotNull FurnaceRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull PoseStack stack, double mouseX, double mouseY) {
    IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);

    stack.pushPose();
    // energy bar
    helper.drawableBuilder(
      FILLED_ENERGY,
      0,
      0,
      TextureSizeHelper.getTextureWidth(FILLED_ENERGY),
      TextureSizeHelper.getTextureHeight(FILLED_ENERGY)
    ).setTextureSize(
      TextureSizeHelper.getTextureWidth(FILLED_ENERGY),
      TextureSizeHelper.getTextureHeight(FILLED_ENERGY)
    ).build().draw(stack, 7 , 72);
    stack.popPose();
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
        33,
        progressStorage,
        TextureSizeHelper.getTextureWidth(FILLED_PROGRESS),
        TextureSizeHelper.getTextureHeight(FILLED_PROGRESS),
        FILLED_PROGRESS
      );
    }
    progress.renderElementInJEI(stack, progressComponent, recipe, mouseX, mouseY, 66, 33);
    stack.popPose();
  }
}
