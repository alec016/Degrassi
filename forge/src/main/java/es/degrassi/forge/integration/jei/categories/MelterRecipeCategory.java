package es.degrassi.forge.integration.jei.categories;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.init.gui.component.*;
import es.degrassi.forge.init.gui.element.EnergyGuiElement;
import es.degrassi.forge.init.gui.element.ProgressGuiElement;
import es.degrassi.forge.init.recipe.recipes.MelterRecipe;
import es.degrassi.forge.init.registration.BlockRegister;
import es.degrassi.forge.integration.jei.DegrassiJEIRecipeTypes;
import es.degrassi.forge.integration.jei.ingredients.DegrassiTypes;
import es.degrassi.forge.integration.jei.renderer.EnergyJeiRenderer;
import es.degrassi.forge.integration.jei.renderer.ProgressJeiRenderer;
import es.degrassi.forge.requirements.IRequirement;
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
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class MelterRecipeCategory implements IRecipeCategory<MelterRecipe> {
  public static final ResourceLocation UID = new DegrassiLocation("melter");
  public static final ResourceLocation TEXTURE = new DegrassiLocation("textures/gui/jei/melter_gui.png");
  public static final ResourceLocation FILLED_PROGRESS = new DegrassiLocation("textures/gui/jei/melter_progress_filled.png");

  private final IDrawable background;
  private final IDrawable icon;
  private final Map<MelterRecipe, EnergyGuiElement> energyComponents = Maps.newHashMap();
  private final Map<MelterRecipe, ProgressGuiElement> progressComponents = Maps.newHashMap();
  private ProgressJeiRenderer progress;
  private EnergyJeiRenderer energy;

  public MelterRecipeCategory(@NotNull IJeiHelpers helper) {
    IGuiHelper helper1 = helper.getGuiHelper();
    this.background = helper1.drawableBuilder(TEXTURE, 0, 0, 140, 84).setTextureSize(140, 84).build();
    this.icon = helper1.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(BlockRegister.MELTER_BLOCK.get()));
  }

  @Override
  public @NotNull Component getTitle() {
    return Component.literal("Melting");
  }

  @Override
  public @NotNull RecipeType<MelterRecipe> getRecipeType() {
    return DegrassiJEIRecipeTypes.MELTER_TYPE;
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
  public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull MelterRecipe recipe, @NotNull IFocusGroup focuses) {
    initRenderers(recipe);
    builder.addSlot(RecipeIngredientRole.INPUT, 50, 34).addIngredients(recipe.getIngredients().get(0));
    builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 7).addIngredients(ForgeTypes.FLUID_STACK, List.of(recipe.getFluid())).setFluidRenderer(recipe.getFluid().getAmount(), false, 16, 70);
    builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 8, 7)
      .addIngredient(DegrassiTypes.ENERGY, energyComponents.get(recipe))
      .setCustomRenderer(DegrassiTypes.ENERGY, energy)
      .setSlotName("energy")
      .addTooltipCallback(
        (slot, tooltip) -> {
          tooltip.clear();
          tooltip.addAll(energyComponents.get(recipe).getTooltips());
        }
      );
    builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 70, 33)
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
  public void draw(@NotNull MelterRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull PoseStack stack, double mouseX, double mouseY) {
    IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
    stack.pushPose();
    // animated progress bar
    initRenderers(recipe);
    progress.renderElementInJEI(stack, progressComponents.get(recipe), recipe, mouseX, mouseY, 70, 33);
    energy.renderElementInJEI(stack, energyComponents.get(recipe), recipe, mouseX, mouseY, energyComponents.get(recipe).getX(), energyComponents.get(recipe).getY());
    stack.popPose();
  }

  private void initRenderers(@NotNull MelterRecipe recipe) {
    if (progress == null) {
      progress = new ProgressJeiRenderer(42, 18);
      initRenderers(recipe);
      return;
    }
    if (energy == null) {
      energy = new EnergyJeiRenderer(16, 70);
      initRenderers(recipe);
      return;
    }
    ProgressComponent progressComponent = new ProgressComponent(new ComponentManager(null), recipe.getTime()) {
      @Override
      public void onChanged() {
        if(progress >= maxProgress) resetProgress();
      }
    };
    EnergyComponent energyStorage = new EnergyComponent(new ComponentManager(null), recipe.getEnergyRequired()) {
      @Override
      public void onChanged() {}
    };
    if (progressComponents.get(recipe) == null) {
      progressComponents.put(recipe, new ProgressGuiElement(
        70,
        33,
        progressComponent,
        FILLED_PROGRESS,
        false, false
      ));
      initRenderers(recipe);
      return;
    }
    if (energyComponents.get(recipe) == null) {
      energyComponents.put(recipe, new EnergyGuiElement(
        8,
        7,
        energyStorage,
        null,
        IRequirement.ModeIO.INPUT,
        true
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
      initRenderers(recipe);
      return;
    }
    progressComponents.get(recipe).getStorage().increment();
  }
}
