package es.degrassi.forge.integration.jei.categories;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.init.block.FurnaceBlock;
import es.degrassi.forge.init.gui.component.*;
import es.degrassi.forge.init.gui.element.EnergyGuiElement;
import es.degrassi.forge.init.gui.element.ProgressGuiElement;
import es.degrassi.forge.init.recipe.recipes.FurnaceRecipe;
import es.degrassi.forge.integration.jei.DegrassiJEIRecipeTypes;
import es.degrassi.forge.integration.jei.ingredients.DegrassiTypes;
import es.degrassi.forge.integration.jei.renderer.EnergyJeiRenderer;
import es.degrassi.forge.requirements.IRequirement;
import es.degrassi.forge.util.*;
import es.degrassi.forge.integration.jei.renderer.ProgressJeiRenderer;
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
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class FurnaceRecipeCategory implements IRecipeCategory<FurnaceRecipe> {
  private static final Map<String, ResourceLocation> UIDS = Maps.newHashMap();
  static {
    UIDS.put("default", createUID(""));
  }

  public static ResourceLocation createUID(String furnace) {
    if (furnace.isEmpty()) {
      ResourceLocation UID = new DegrassiLocation("generator");
      UIDS.put("default", UID);
      return UID;
    }
    if (UIDS.get(furnace) != null) return UIDS.get(furnace);
    ResourceLocation UID = new DegrassiLocation(furnace + "furnace");
    UIDS.put(furnace, UID);
    return UID;
  }

  public static final ResourceLocation TEXTURE = new DegrassiLocation("textures/gui/jei/furnace_gui.png");
  public static final ResourceLocation FILLED_PROGRESS = new DegrassiLocation("textures/gui/jei/furnace_progress_filled.png");

  private final IDrawable background;
  private final IDrawable icon;
  private final Map<FurnaceRecipe, EnergyGuiElement> energyComponents = Maps.newHashMap();
  private final Map<FurnaceRecipe, ProgressGuiElement> progressComponents = Maps.newHashMap();
  private ProgressJeiRenderer progress;
  private EnergyJeiRenderer energy;
  private final FurnaceBlock block;

  public FurnaceRecipeCategory(@NotNull IJeiHelpers helper, FurnaceBlock block) {
    IGuiHelper helper1 = helper.getGuiHelper();
    this.background = helper1.drawableBuilder(TEXTURE, 0, 0, 126, 73).setTextureSize(126, 73).build();
    this.icon = helper1.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(block));
    this.block = block;
  }

  @Override
  public @NotNull Component getTitle() {
    return Component.translatable(block.getDescriptionId());
  }

  @Override
  public @NotNull RecipeType<FurnaceRecipe> getRecipeType() {
    return switch (block.getTier()) {
      case IRON -> DegrassiJEIRecipeTypes.IRON_FURNACE_TYPE;
      case GOLD -> DegrassiJEIRecipeTypes.GOLD_FURNACE_TYPE;
      case DIAMOND -> DegrassiJEIRecipeTypes.DIAMOND_FURNACE_TYPE;
      case EMERALD -> DegrassiJEIRecipeTypes.EMERALD_FURNACE_TYPE;
      case NETHERITE -> DegrassiJEIRecipeTypes.NETHERITE_FURNACE_TYPE;
    };
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
  public void draw(@NotNull FurnaceRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull PoseStack stack, double mouseX, double mouseY) {
    IRecipeCategory.super.draw(recipe, recipeSlotsView, stack, mouseX, mouseY);
    stack.pushPose();
    // animated progress bar
    initRenderers(recipe);
    progress.renderElementInJEI(stack, progressComponents.get(recipe), recipe, mouseX, mouseY, 53, 18);
    energy.renderElementInJEI(stack, energyComponents.get(recipe), recipe, mouseX, mouseY, energyComponents.get(recipe).getX(), energyComponents.get(recipe).getY());
    stack.popPose();
  }

  private void initRenderers(@NotNull FurnaceRecipe recipe) {
    if (progress == null) {
      progress = new ProgressJeiRenderer(42, 18);
      initRenderers(recipe);
      return;
    }
    if (energy == null) {
      energy = new EnergyJeiRenderer(111, 16);
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
        53,
        18,
        progressComponent,
        TextureSizeHelper.getTextureWidth(FILLED_PROGRESS),
        TextureSizeHelper.getTextureHeight(FILLED_PROGRESS),
        FILLED_PROGRESS
      ));
      initRenderers(recipe);
      return;
    }
    if (energyComponents.get(recipe) == null) {
      energyComponents.put(recipe, new EnergyGuiElement(
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
      initRenderers(recipe);
      return;
    }
    progressComponents.get(recipe).getStorage().increment();
  }
}