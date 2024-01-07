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

@SuppressWarnings("rawtypes")
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

  public static final ResourceLocation FILLED_PROGRESS = new DegrassiLocation("textures/gui/jei/generator_progress_filled.png");
  public static final ResourceLocation TEXTURE = new DegrassiLocation("textures/gui/jei/generator_gui.png");

  private final IDrawable background;
  private final IDrawable icon;
  private final Map<GeneratorRecipe<?>, EnergyInfoArea> energyComponents = Maps.newHashMap();
  private final Map<GeneratorRecipe<?>, ProgressComponent> progressComponents = Maps.newHashMap();
  private ProgressJeiRenderer progress;
  private EnergyJeiRenderer energy;
  private final GeneratorBlock block;

  private static @NotNull IDrawable createBackground(@NotNull IJeiHelpers helper) {
    return helper.getGuiHelper().drawableBuilder(TEXTURE, 0, 0, 126, 73).setTextureSize(126, 73).build();
  }

  private static @NotNull IDrawable createIcon(@NotNull IJeiHelpers helper, GeneratorBlock block) {
    return helper.getGuiHelper().createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(block));
  }

  public GeneratorRecipeCategory(IJeiHelpers helper, GeneratorBlock block) {
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
    builder.addSlot(RecipeIngredientRole.INPUT, 31, 19).addIngredients((Ingredient) recipe.getIngredients().get(0));
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
  public void draw(GeneratorRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull PoseStack stack, double mouseX, double mouseY) {
    if (!recipe.canUseRecipe(block.kjs$getId())) return;
    IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
    stack.pushPose();
    // animated progress bar
    initRenderers(recipe);
    progress.renderElementInJEI(stack, progressComponents.get(recipe), recipe, mouseX, mouseY, 53, 18);
    energy.renderElementInJEI(stack, energyComponents.get(recipe), recipe, mouseX, mouseY, energyComponents.get(recipe).getX(), energyComponents.get(recipe).getY());
    stack.popPose();
  }

  private void initRenderers(GeneratorRecipe<?> recipe) {
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

  @Override
  public @NotNull RecipeType<GeneratorRecipe> getRecipeType() {
    if (block instanceof JewelryGenerator) {
      return DegrassiJEIRecipeTypes.JEWELRY_GENERATOR_TYPE;
    }

    return DegrassiJEIRecipeTypes.GENERATOR_TYPE;
//    return switch(block.kjs$getId()) {
//      case "degrassi:jewelry_generator" -> DegrassiJEIRecipeTypes.JEWELRY_GENERATOR_TYPE;
//      default -> DegrassiJEIRecipeTypes.GENERATOR_TYPE;
//    };
  }

  @Override
  public @NotNull Component getTitle() {
    return Component.translatable(block.getDescriptionId());
  }
}
