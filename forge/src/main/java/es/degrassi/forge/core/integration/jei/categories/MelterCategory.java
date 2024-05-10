package es.degrassi.forge.core.integration.jei.categories;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IElement;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.common.component.FluidComponent;
import es.degrassi.forge.core.common.component.ItemComponent;
import es.degrassi.forge.core.common.component.ProgressComponent;
import es.degrassi.forge.core.common.element.EnergyElement;
import es.degrassi.forge.core.common.element.FluidElement;
import es.degrassi.forge.core.common.element.ItemElement;
import es.degrassi.forge.core.common.element.ProgressElement;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.MelterController;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.MelterControllerEntity;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.recipe.MelterRecipe;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.recipe.processor.MelterProcessor;
import es.degrassi.forge.core.common.machines.multiblock.controller.client.screen.MelterScreen;
import es.degrassi.forge.core.common.requirement.FluidRequirement;
import es.degrassi.forge.core.common.requirement.ItemRequirement;
import es.degrassi.forge.core.integration.jei.DegrassiRecipeTypes;
import es.degrassi.forge.core.integration.jei.wrapper.EnergyWrapper;
import es.degrassi.forge.core.integration.jei.wrapper.FluidWrapper;
import es.degrassi.forge.core.integration.jei.wrapper.IngredientTypes;
import es.degrassi.forge.core.integration.jei.wrapper.ProgressWrapper;
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
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import oshi.util.tuples.Pair;

public class MelterCategory implements IRecipeCategory<MelterRecipe> {
  public static MelterProcessor defaultProcessor = new MelterProcessor(MelterControllerEntity.DUMMY, false);
  static {
    defaultProcessor.init();
  }

  private final IDrawable background, icon;

  private static final Map<MelterRecipe, List<Pair<IElement<?>, IComponent>>> mapRecipeElement = new HashMap<>();

  public MelterCategory(IGuiHelper guiHelper, MelterController block) {
    this.background = guiHelper.drawableBuilder(MelterScreen.getJeiBackground(), 0, 0, 140, 84).setTextureSize(140, 84).build();
    this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(block));
  }

  @Override
  public RecipeType<MelterRecipe> getRecipeType() {
    return DegrassiRecipeTypes.MELTER;
  }

  @Override
  public Component getTitle() {
    return Component.translatable("degrassi.jei.recipe.melter");
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
  public void setRecipe(IRecipeLayoutBuilder builder, MelterRecipe recipe, IFocusGroup focuses) {
    List<Pair<IElement<?>, IComponent>> list = new LinkedList<>();
    List<IElement<?>> elements = defaultProcessor.getEntity().getElementManager().get();

    elements.forEach(element -> {
      if (element instanceof ItemElement itemElement && element.jei()) {
        recipe.getRequirements().forEach(req -> {
          if (req.getId().isEmpty() && req instanceof ItemRequirement itemRequirement) {
            defaultProcessor.getEntity().getComponentManager().getItemHandler().getComponents().forEach(component -> {
              ItemComponent copy = new ItemComponent(null, component.getId(), component.isWhitelist(), component.getEntity(), component.getMode(), component.getFilter().toArray(Item[]::new));
              if (component.getMode().input() && req.getMode().isInput()) {
                IElement<?> e = new ItemElement(null, 35, 33, Component.literal("item"), copy.getId(), new DegrassiLocation("textures/gui/base_slot.png"), true);
                builder.addSlot(RecipeIngredientRole.INPUT, e.getX() + 1, e.getY() + 1).addItemStack(new ItemStack(itemRequirement.getItem(), itemRequirement.getAmount()));
                list.add(new Pair<>(e, copy));
              } else if (component.getMode().output() && req.getMode().isOutput()) {
                IElement<?> e = new ItemElement(null, 93, 14, Component.literal("item"), copy.getId(), new DegrassiLocation("textures/gui/base_slot.png"), true);
                builder.addSlot(RecipeIngredientRole.OUTPUT, e.getX() + 1, e.getY() + 1).addItemStack(new ItemStack(itemRequirement.getItem(), itemRequirement.getAmount()));
                list.add(new Pair<>(e, copy));
              }
            });
          } else if (req instanceof ItemRequirement itemRequirement && element.getId().equals(req.getId()))
            defaultProcessor.getEntity().getComponentManager().getComponent(itemElement.getId()).map(component -> (ItemComponent) component).ifPresent(component -> {
              ItemComponent copy = new ItemComponent(null, component.getId(), component.isWhitelist(), component.getEntity(), component.getMode(), component.getFilter().toArray(Item[]::new));
              if (copy.getMode().input()) {
                IElement<?> e = new ItemElement(null, 35, 33, Component.literal("item"), copy.getId(), new DegrassiLocation("textures/gui/base_slot.png"), true);
                builder.addSlot(RecipeIngredientRole.INPUT, e.getX() + 1, e.getY() + 1).addItemStack(new ItemStack(itemRequirement.getItem(), itemRequirement.getAmount()));
                list.add(new Pair<>(e, copy));
              } else if (copy.getMode().output()) {
                IElement<?> e = new ItemElement(null, 93, 14, Component.literal("item"), copy.getId(), new DegrassiLocation("textures/gui/base_slot.png"), true);
                builder.addSlot(RecipeIngredientRole.OUTPUT, e.getX() + 1, e.getY() + 1).addItemStack(new ItemStack(itemRequirement.getItem(), itemRequirement.getAmount()));
                list.add(new Pair<>(e, copy));
              }
            });
        });
      }
      if (element instanceof FluidElement fluidElement && element.jei()) {
        boolean animated = false;
        recipe.getRequirements().forEach(req -> {
          if (req.getId().isEmpty() && req instanceof FluidRequirement fluidRequirement) {
            defaultProcessor.getEntity().getComponentManager().getFluidHandler().getComponents().forEach(component -> {
              FluidComponent copy = new FluidComponent(null, component.getId(), component.isWhitelist(), component.getCapacity(), component.getEntity(), component.getMode(), component.getFilter().toArray(Fluid[]::new));
              if (component.getMode().input() && req.getMode().isInput()) {
                FluidWrapper fluid = new FluidWrapper(115, 6, new DegrassiLocation("textures/gui/jei/melter_fluid.png"), recipe, copy.getId(), animated);
                builder
                  .addSlot(RecipeIngredientRole.RENDER_ONLY, fluid.getX(), fluid.getY())
                  .setCustomRenderer(IngredientTypes.FLUID, fluid)
                  .addIngredient(IngredientTypes.FLUID, copy)
                  .setSlotName("fluid");
                list.add(new Pair<>(fluid, copy));
              } else if (component.getMode().output() && req.getMode().isOutput()) {
                FluidWrapper fluid = new FluidWrapper(115, 6, new DegrassiLocation("textures/gui/jei/melter_fluid.png"), recipe, copy.getId(), animated);
                builder
                  .addSlot(RecipeIngredientRole.RENDER_ONLY, fluid.getX(), fluid.getY())
                  .setCustomRenderer(IngredientTypes.FLUID, fluid)
                  .addIngredient(IngredientTypes.FLUID, copy)
                  .setSlotName("fluid");
                list.add(new Pair<>(fluid, copy));
              }
            });
          } else if (req instanceof FluidRequirement fluidRequirement && element.getId().equals(req.getId()))
            defaultProcessor.getEntity().getComponentManager().getComponent(fluidElement.getId()).map(component -> (FluidComponent) component).ifPresent(component -> {
              FluidComponent copy = new FluidComponent(null, component.getId(), component.isWhitelist(), component.getCapacity(), component.getEntity(), component.getMode(), component.getFilter().toArray(Fluid[]::new));
              if (component.getMode().input() && req.getMode().isInput()) {
                FluidWrapper fluid = new FluidWrapper(115, 6, new DegrassiLocation("textures/gui/jei/melter_fluid.png"), recipe, copy.getId(), animated);
                builder
                  .addSlot(RecipeIngredientRole.RENDER_ONLY, fluid.getX(), fluid.getY())
                  .setCustomRenderer(IngredientTypes.FLUID, fluid)
                  .addIngredient(IngredientTypes.FLUID, copy)
                  .setSlotName("fluid");
                list.add(new Pair<>(fluid, copy));
              } else if (component.getMode().output() && req.getMode().isOutput()) {
                FluidWrapper fluid = new FluidWrapper(115, 6, new DegrassiLocation("textures/gui/jei/melter_fluid.png"), recipe, copy.getId(), animated);
                builder
                  .addSlot(RecipeIngredientRole.RENDER_ONLY, fluid.getX(), fluid.getY())
                  .setCustomRenderer(IngredientTypes.FLUID, fluid)
                  .addIngredient(IngredientTypes.FLUID, copy)
                  .setSlotName("fluid");
                list.add(new Pair<>(fluid, copy));
              }
            });
        });
      }
      if (element instanceof EnergyElement energyElement && element.jei()) {
        EnergyWrapper energy = new EnergyWrapper(
          7,
          6,
          new DegrassiLocation("textures/gui/jei/melter_energy_empty.png"),
          new DegrassiLocation("textures/gui/jei/melter_energy_filled.png"),
          energyElement.getDirection(),
          recipe,
          false,
          element.getId()
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
      if (element instanceof ProgressElement progressElement && element.jei()) {
        ProgressWrapper progress = new ProgressWrapper(
          62, 33,
          new DegrassiLocation("textures/gui/jei/melter_progress_empty.png"),
          new DegrassiLocation("textures/gui/jei/melter_progress_filled.png"),
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
  public void draw(MelterRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
//    IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    List<Pair<IElement<?>, IComponent>> list = mapRecipeElement.get(recipe);
    guiGraphics.pose().pushPose();
    list.forEach(pair -> pair.getA().renderInJei(guiGraphics, recipe, mouseX, mouseY, pair.getB()));
    guiGraphics.pose().popPose();
  }
}
