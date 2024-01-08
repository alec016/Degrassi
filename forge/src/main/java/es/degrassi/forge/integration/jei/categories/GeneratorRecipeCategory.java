package es.degrassi.forge.integration.jei.categories;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.init.block.generators.*;
import es.degrassi.forge.init.gui.renderer.EnergyInfoArea;
import es.degrassi.forge.init.gui.renderer.ProgressComponent;
import es.degrassi.forge.init.recipe.recipes.GeneratorRecipe;
import es.degrassi.forge.integration.jei.*;
import es.degrassi.forge.integration.jei.ingredients.DegrassiTypes;
import es.degrassi.forge.integration.jei.renderer.EnergyJeiRenderer;
import es.degrassi.forge.integration.jei.renderer.ProgressJeiRenderer;
import es.degrassi.forge.requirements.IRequirement;
import es.degrassi.forge.util.TextureSizeHelper;
import es.degrassi.forge.util.storage.AbstractEnergyStorage;
import es.degrassi.forge.util.storage.ProgressStorage;
import mezz.jei.api.constants.*;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.*;
import mezz.jei.api.recipe.*;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class GeneratorRecipeCategory implements IRecipeCategory<GeneratorRecipe> {
  private static final Map<String, ResourceLocation> UIDS = Maps.newHashMap();

  static {
    UIDS.put("default", createUID(""));
  }

  public static ResourceLocation createUID(@NotNull String generator) {
    if (generator.isEmpty()) {
      ResourceLocation UID = new DegrassiLocation("generator");
      UIDS.put("default", UID);
      return UID;
    }
    if (UIDS.get(generator) != null) return UIDS.get(generator);
    ResourceLocation UID = new DegrassiLocation(generator + "_generator");
    UIDS.put(generator, UID);
    return UID;
  }

  public final ResourceLocation FILLED_PROGRESS;
  public final ResourceLocation TEXTURE;

  private final IDrawable background;
  private final IDrawable icon;
  private final Map<GeneratorRecipe, EnergyInfoArea> energyComponents = Maps.newHashMap();
  private final Map<GeneratorRecipe, ProgressComponent> progressComponents = Maps.newHashMap();
  private ProgressJeiRenderer progress;
  private EnergyJeiRenderer energy;
  private final GeneratorBlock block;

  private @NotNull IDrawable createBackground(@NotNull IJeiHelpers helper) {
    int width = TextureSizeHelper.getTextureWidth(TEXTURE);
    int height = TextureSizeHelper.getTextureHeight(TEXTURE);
    return helper.getGuiHelper().drawableBuilder(TEXTURE, 0, 0, width, height).setTextureSize(width, height).build();
  }

  private @NotNull IDrawable createIcon(@NotNull IJeiHelpers helper, GeneratorBlock block) {
    return helper.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(block));
  }

  public GeneratorRecipeCategory(IJeiHelpers helper, GeneratorBlock block, ResourceLocation background, ResourceLocation filled_progress) {
    this.TEXTURE = background;
    this.FILLED_PROGRESS = filled_progress;
    this.background = createBackground(helper);
    this.icon = createIcon(helper, block);
    this.block = block;
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
  public void setRecipe(@NotNull IRecipeLayoutBuilder builder, GeneratorRecipe recipe, @NotNull IFocusGroup focuses) {
    if (!recipe.canUseRecipe(block.kjs$getId())) return;
    initRenderers(recipe);
    builder.addSlot(RecipeIngredientRole.INPUT, 53, 35).addIngredients((Ingredient) recipe.getIngredients().get(0));
    builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 134, 9)
      .addIngredient(DegrassiTypes.ENERGY, energyComponents.get(recipe))
      .setCustomRenderer(DegrassiTypes.ENERGY, energy)
      .setSlotName("energy")
      .addTooltipCallback(
        (slot, tooltip) -> {
          tooltip.clear();
          tooltip.addAll(energyComponents.get(recipe).getTooltips());
        }
      );
    builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 82, 26)
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
  public void draw(GeneratorRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull PoseStack stack, double mouseX, double mouseY) {
    if (!recipe.canUseRecipe(block.kjs$getId())) return;
    IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
    stack.pushPose();
    // animated progress bar
    initRenderers(recipe);
    progress.renderElementInJEI(stack, progressComponents.get(recipe), recipe, mouseX, mouseY, progressComponents.get(recipe).getX(), progressComponents.get(recipe).getY());
    energy.renderElementInJEI(stack, energyComponents.get(recipe), recipe, mouseX, mouseY, energyComponents.get(recipe).getX(), energyComponents.get(recipe).getY());
    stack.popPose();
  }

  private void initRenderers(GeneratorRecipe recipe) {
    if (progress == null) progress = new ProgressJeiRenderer(43, 34);
    if (energy == null) energy = new EnergyJeiRenderer(34, 70);
    ProgressStorage progressStorage = new ProgressStorage(recipe.getTime()) {
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
      82, 26,
      progressStorage,
      TextureSizeHelper.getTextureWidth(FILLED_PROGRESS),
      TextureSizeHelper.getTextureHeight(FILLED_PROGRESS),
      FILLED_PROGRESS
    ).setDirection(ProgressComponent.Orientation.TOP));
    energyComponents.put(recipe, new EnergyInfoArea(
      134, 9,
      energyStorage,
      111,
      16,
      null,
      IRequirement.ModeIO.OUTPUT
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

  @Override
  public @NotNull RecipeType<GeneratorRecipe> getRecipeType() {
    if (block instanceof JewelryGenerator) {
      return DegrassiJEIRecipeTypes.JEWELRY_GENERATOR_TYPE;
    } else if (block instanceof CombustionGenerator) {
      return DegrassiJEIRecipeTypes.COMBUSTION_GENERATOR_TYPE;
    }

    return DegrassiJEIRecipeTypes.GENERATOR_TYPE;
  }

  @Override
  public @NotNull Component getTitle() {
    return Component.translatable(block.getDescriptionId());
  }
}
