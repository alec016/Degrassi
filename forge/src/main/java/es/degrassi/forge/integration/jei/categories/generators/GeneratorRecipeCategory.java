package es.degrassi.forge.integration.jei.categories.generators;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.init.gui.renderer.EnergyInfoArea;
import es.degrassi.forge.init.gui.renderer.ProgressComponent;
import es.degrassi.forge.init.recipe.recipes.generators.GeneratorRecipe;
import es.degrassi.forge.integration.jei.ingredients.DegrassiTypes;
import es.degrassi.forge.integration.jei.renderer.EnergyJeiRenderer;
import es.degrassi.forge.integration.jei.renderer.ProgressJeiRenderer;
import es.degrassi.forge.requirements.IRequirement;
import es.degrassi.forge.util.TextureSizeHelper;
import es.degrassi.forge.util.storage.AbstractEnergyStorage;
import es.degrassi.forge.util.storage.ProgressStorage;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public abstract class GeneratorRecipeCategory<R extends GeneratorRecipe> implements IRecipeCategory<R> {
  public static final ResourceLocation FILLED_PROGRESS = new DegrassiLocation("textures/gui/jei/generator_progress_filled.png");

  private final IDrawable background;
  private final IDrawable icon;
  private final Map<R, EnergyInfoArea> energyComponents = Maps.newHashMap();
  private final Map<R, ProgressComponent> progressComponents = Maps.newHashMap();
  private ProgressJeiRenderer progress;
  private EnergyJeiRenderer energy;

  public GeneratorRecipeCategory(IDrawable background, IDrawable icon) {
    this.background = background;
    this.icon = icon;
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
  public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull R recipe, @NotNull IFocusGroup focuses) {
    initRenderers(recipe);
    builder.addSlot(RecipeIngredientRole.INPUT, 31, 19).addIngredients(recipe.getIngredients().get(0));
    builder.addSlot(RecipeIngredientRole.OUTPUT, 101, 19).addItemStack(recipe.getResultItem());
    builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 7, 50)
      .addIngredient(DegrassiTypes.ENERGY, energyComponents.get(recipe))
      .setCustomRenderer(DegrassiTypes.ENERGY, energy)
      .setSlotName("energy")
      .addTooltipCallback(
        (slot, tooltip) -> {
          tooltip.clear();
          tooltip.addAll(energyComponents.get(recipe).getTooltips());
        }
      );
    builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 53, 18)
      .setCustomRenderer(DegrassiTypes.PROGRESS, progress)
      .addIngredient(DegrassiTypes.PROGRESS, progressComponents.get(recipe))
      .addTooltipCallback(
        (slot, tooltip) -> {
          tooltip.clear();
          tooltip.addAll(progress.getJEITooltips(progressComponents.get(recipe), recipe));
        }
      );
  }

  @Override
  public void draw(@NotNull R recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull PoseStack stack, double mouseX, double mouseY) {
    IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
    stack.pushPose();
    // animated progress bar
    initRenderers(recipe);
    progress.renderElementInJEI(stack, progressComponents.get(recipe), recipe, mouseX, mouseY, 53, 18);
    energy.renderElementInJEI(stack, energyComponents.get(recipe), recipe, mouseX, mouseY, energyComponents.get(recipe).getX(), energyComponents.get(recipe).getY());
    stack.popPose();
  }

  private void initRenderers(R recipe) {
    if (progress == null) progress = new ProgressJeiRenderer(42, 18);
    if (energy == null) energy = new EnergyJeiRenderer(111, 16);
    ProgressStorage progressStorage = new ProgressStorage(20) {
      @Override
      public void onProgressChanged() {
        if(progress > maxProgress) resetProgress();
      }
    };
    AbstractEnergyStorage energyStorage = new AbstractEnergyStorage(recipe.getEnergyRequired()) {
      @Override
      public void onEnergyChanged() {}
    };
    progressComponents.put(recipe, new ProgressComponent(
      56,
      18,
      progressStorage,
      TextureSizeHelper.getTextureWidth(FILLED_PROGRESS),
      TextureSizeHelper.getTextureHeight(FILLED_PROGRESS),
      FILLED_PROGRESS
    ));
    energyComponents.put(recipe, new EnergyInfoArea(
      7,
      50,
      energyStorage,
      111,
      16,
      null,
      IRequirement.ModeIO.INPUT
    ) {
      @Override
      public List<Component> getTooltips() {
        return List.of(
          mode == IRequirement.ModeIO.INPUT
            ? Component.translatable("degrassi.gui.element.energy.input").withStyle(ChatFormatting.ITALIC)
            : Component.translatable("degrassi.gui.element.energy.output").withStyle(ChatFormatting.ITALIC),
          Component.translatable(
            "degrassi.gui.element.energy.jei",
            Component.translatable(
              "degrassi.gui.element.energy.jei.total",
              recipe.getEnergyRequired() * recipe.getTime(),
              Component.translatable("unit.energy.forge")
            ),
            Component.literal(" @ ").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY),
            Component.translatable(
              "degrassi.gui.element.energy.jei.perTick",
              recipe.getEnergyRequired(),
              Component.translatable("unit.energy.forge"),
              "/t"
            )
          )
        );
      }
    });
  }
}
