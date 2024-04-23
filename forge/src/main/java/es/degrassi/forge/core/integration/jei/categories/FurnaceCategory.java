package es.degrassi.forge.core.integration.jei.categories;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IElement;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.common.component.ExperienceComponent;
import es.degrassi.forge.core.common.component.ProgressComponent;
import es.degrassi.forge.core.common.element.EnergyElement;
import es.degrassi.forge.core.common.element.ExperienceElement;
import es.degrassi.forge.core.common.element.ItemElement;
import es.degrassi.forge.core.common.element.ProgressElement;
import es.degrassi.forge.core.common.machines.block.FurnaceBlock;
import es.degrassi.forge.core.common.machines.entity.FurnaceEntity;
import es.degrassi.forge.core.common.machines.screen.FurnaceScreen;
import es.degrassi.forge.core.common.processor.FurnaceProcessor;
import es.degrassi.forge.core.common.recipe.FurnaceRecipe;
import es.degrassi.forge.core.common.requirement.ItemRequirement;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.integration.jei.DegrassiRecipeTypes;
import es.degrassi.forge.core.integration.jei.wrapper.EnergyWrapper;
import es.degrassi.forge.core.integration.jei.wrapper.ExperienceWrapper;
import es.degrassi.forge.core.integration.jei.wrapper.IngredientTypes;
import es.degrassi.forge.core.integration.jei.wrapper.ProgressWrapper;
import es.degrassi.forge.core.tiers.Furnace;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import oshi.util.tuples.Pair;

public class FurnaceCategory implements IRecipeCategory<FurnaceRecipe> {
  public static FurnaceProcessor defaultProcessor = new FurnaceProcessor(new FurnaceEntity(BlockPos.ZERO, BlockRegistration.FURNACE.get(Furnace.IRON).defaultBlockState(), Furnace.IRON));
  static {
    defaultProcessor.init();
  }
  private final IDrawable background, icon;
  private final FurnaceBlock block;

  private static final Map<FurnaceRecipe, List<Pair<IElement<?>, IComponent>>> mapRecipeElement = new HashMap<>();

  public FurnaceCategory(IGuiHelper guiHelper, FurnaceBlock block) {
    this.block = block;
    this.background = guiHelper.drawableBuilder(FurnaceScreen.getJeiBackground(), 0, 0, 126, 73).setTextureSize(126, 73).build();
    this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(block));
  }

  public static RecipeType<FurnaceRecipe> getByTier(Furnace tier) {
    return DegrassiRecipeTypes.FURNACE;
  }

  @Override
  public RecipeType<FurnaceRecipe> getRecipeType() {
    return getByTier(block.getTier());
  }

  @Override
  public Component getTitle() {
    return Component.translatable(block.getDescriptionId());
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
  public void setRecipe(IRecipeLayoutBuilder builder, FurnaceRecipe recipe, IFocusGroup focuses) {
    List<Pair<IElement<?>, IComponent>> list = new LinkedList<>();
    List<IComponent> components = defaultProcessor.getEntity().getComponentManager().get();
    List<IElement<?>> elements = defaultProcessor.getEntity().getElementManager().get();
    FurnaceRecipe.separateRequirements(recipe, components);
    recipe.getStartRequirements().forEach((requirement, component) -> {
      // mode INPUT
      if (requirement instanceof ItemRequirement itemRequirement) {
        IElement<?> element = new ItemElement(null, 13, 14, Component.literal("item"), "input", new DegrassiLocation("textures/gui/base_slot.png"));
        builder.addSlot(RecipeIngredientRole.INPUT, element.getX() + 1, element.getY() + 1).addItemStack(new ItemStack(itemRequirement.getItem(), itemRequirement.getAmount()));
        list.add(new Pair<>(element, component));
      }
    });
    recipe.getEndRequirements().forEach((requirement, component) -> {
      // mode OUTPUT
      if (requirement instanceof ItemRequirement itemRequirement) {
        IElement<?> element = new ItemElement(null, 93, 14, Component.literal("item"), "output", new DegrassiLocation("textures/gui/base_slot.png"));
        builder.addSlot(RecipeIngredientRole.OUTPUT, element.getX() + 1, element.getY() + 1).addItemStack(new ItemStack(itemRequirement.getItem(), itemRequirement.getAmount()));
        list.add(new Pair<>(element, component));
      }
    });

    elements.forEach(element -> {
      if (element instanceof EnergyElement energyElement) {
        EnergyWrapper energy = new EnergyWrapper(
          7,
          50,
          new DegrassiLocation("textures/gui/jei/furnace_energy_empty.png"),
          new DegrassiLocation("textures/gui/jei/furnace_energy_filled.png"),
          energyElement.getDirection(),
          recipe,
          false
        );
        defaultProcessor.getEntity().getComponentManager().getComponent(energy.getId()).map(component -> (EnergyComponent) component).ifPresent(component -> {
          EnergyComponent copy = new EnergyComponent(null, component.getMaxEnergyStored(), component.getMaxInput(), component.getMaxOutput(), component.getEntity(), component.getId(), component.getMode());
          builder
            .addSlot(RecipeIngredientRole.RENDER_ONLY, energy.getX(), energy.getY())
            .setCustomRenderer(IngredientTypes.ENERGY, energy)
            .addIngredient(IngredientTypes.ENERGY, copy)
            .setSlotName("energy");
          list.add(new Pair<>(energy, copy));
        });
      }
      if (element instanceof ExperienceElement experienceElement) {
        ExperienceWrapper experience = new ExperienceWrapper(
          31,
          37,
          new DegrassiLocation("textures/gui/base_experience_empty.png"),
          new DegrassiLocation("textures/gui/base_experience_filled.png"),
          experienceElement.getDirection(),
          recipe,
          false
        );
        defaultProcessor.getEntity().getComponentManager().getComponent(experience.getId()).map(component -> (ExperienceComponent) component).ifPresent(component -> {
          ExperienceComponent copy = new ExperienceComponent(null, component.getCapacity(), component.getEntity(), component.getId(), component.getMode());
          builder
            .addSlot(RecipeIngredientRole.RENDER_ONLY, experience.getX(), experience.getY())
            .setCustomRenderer(IngredientTypes.EXPERIENCE, experience)
            .addIngredient(IngredientTypes.EXPERIENCE, copy)
            .setSlotName("experience");
          list.add(new Pair<>(experience, copy));
        });
      }
      if (element instanceof ProgressElement progressElement) {
        ProgressWrapper progress = new ProgressWrapper(
          41, 14,
          new DegrassiLocation("textures/gui/jei/furnace_progress_empty.png"),
          new DegrassiLocation("textures/gui/jei/furnace_progress_filled.png"),
          progressElement.getDirection(),
          recipe,
          true
        );
        defaultProcessor.getEntity().getComponentManager().getComponent(progress.getId()).map(component -> (ProgressComponent) component).ifPresent(component -> {
          ProgressComponent copy = new ProgressComponent(component.getManager(), component.getEntity());
          builder
            .addSlot(RecipeIngredientRole.RENDER_ONLY, progress.getX(), progress.getY())
            .setCustomRenderer(IngredientTypes.PROGRESS, progress)
            .addIngredient(IngredientTypes.PROGRESS, copy)
            .setSlotName("progress");
          list.add(new Pair<>(progress, copy));
        });
      }
    });

    mapRecipeElement.put(recipe, list);
  }

  @Override
  public void draw(FurnaceRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
    IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    List<Pair<IElement<?>, IComponent>> list = mapRecipeElement.get(recipe);
    guiGraphics.pose().pushPose();
    list.forEach(pair -> pair.getA().renderInJei(guiGraphics, recipe, mouseX, mouseY, pair.getB()));
    guiGraphics.pose().popPose();
  }
}
