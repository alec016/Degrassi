package es.degrassi.forge.core.integration.jei.categories;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.common.utils.DegrassiLogger;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IElement;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.api.core.common.RequirementMode;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.common.component.ExperienceComponent;
import es.degrassi.forge.core.common.component.FluidComponent;
import es.degrassi.forge.core.common.component.ItemComponent;
import es.degrassi.forge.core.common.component.ProgressComponent;
import es.degrassi.forge.core.common.element.EnergyElement;
import es.degrassi.forge.core.common.element.ExperienceElement;
import es.degrassi.forge.core.common.element.FluidElement;
import es.degrassi.forge.core.common.element.ItemElement;
import es.degrassi.forge.core.common.element.ProgressElement;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.processor.MachineProcessor;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import es.degrassi.forge.core.common.requirement.EnergyRequirement;
import es.degrassi.forge.core.common.requirement.ExperienceRequirement;
import es.degrassi.forge.core.common.requirement.FluidRequirement;
import es.degrassi.forge.core.common.requirement.ItemRequirement;
import es.degrassi.forge.core.integration.jei.wrapper.EnergyWrapper;
import es.degrassi.forge.core.integration.jei.wrapper.ExperienceWrapper;
import es.degrassi.forge.core.integration.jei.wrapper.FluidWrapper;
import es.degrassi.forge.core.integration.jei.wrapper.IngredientTypes;
import es.degrassi.forge.core.integration.jei.wrapper.ProgressWrapper;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import oshi.util.tuples.Pair;

public abstract class AbstractCategory<R extends MachineRecipe<R>, P extends MachineProcessor<R, E>, E extends MachineEntity<R>> implements IRecipeCategory<R> {
  private static final Map<MachineRecipe<?>, List<Pair<IElement<?>, Pair<IRequirement<?>, IComponent>>>> mapRecipeElement = new HashMap<>();
  private final Map<TextureMap, ResourceLocation> textures = new LinkedHashMap<>();
  private final Map<PositionMap, Pair<Integer, Integer>> XY = new LinkedHashMap<>();

  private final String titleKey;
  @Getter
  private final RecipeType<R> recipeType;
  @Getter
  protected IDrawable background, icon;

  @Setter
  private boolean
    animateProgress = true,
    animateEnergy = false,
    animateExperience = false,
    animateFluid = false;

  protected AbstractCategory(String titleKey, RecipeType<R> recipeType) {
    this.titleKey = titleKey;
    this.recipeType = recipeType;

    addDefaultTextures();
  }

  @Override
  public Component getTitle() {
    return Component.translatable(titleKey);
  }

  public void setRecipeRequirements(IRecipeLayoutBuilder builder, R recipe, ComponentManager manager, List<IElement<?>> elements, P processor, R originalRecipe) {
    List<Pair<IElement<?>, Pair<IRequirement<?>, IComponent>>> list = new LinkedList<>();
    recipe.getRequirements().forEach(req -> {
      if (req instanceof ItemRequirement requirement) {
        if (requirement.getComponent() != null) {
          ItemComponent component = requirement.getComponent();
          elements.stream().filter(element -> element instanceof ItemElement).map(el -> (ItemElement) el).forEach(element -> {
            ItemElement e = new ItemElement(null, getX(positionFromRequirement(requirement)), getY(positionFromRequirement(requirement)), Component.literal("item"), element.getId(), getTexture(TextureMap.ITEM), true);
            builder
              .addSlot(roleFromMode(requirement.getMode()), e.getX() + 1, e.getY() + 1)
              .setSlotName(component.getId())
              .addItemStack(new ItemStack(requirement.getItem(), requirement.getAmount()))
              .setSlotName(element.getId());
            list.add(new Pair<>(e, new Pair<>(requirement, component)));
          });
        }
      }else if (req instanceof FluidRequirement requirement) {
        if (requirement.getComponent() != null) {
          FluidComponent component = requirement.getComponent();
          elements.stream().filter(element -> element instanceof FluidElement).map(element -> (FluidElement) element).forEach(element -> {
            FluidWrapper fluid = new FluidWrapper(
              getX(positionFromRequirement(requirement)),
              getY(positionFromRequirement(requirement)),
              getTexture(TextureMap.FLUID),
              requirement,
              recipe,
              element.getId(),
              animateFluid
            );
            builder
              .addSlot(roleFromMode(requirement.getMode()), fluid.getX(), fluid.getY())
              .setCustomRenderer(IngredientTypes.FLUID, fluid)
              .addIngredient(IngredientTypes.FLUID, component)
              .setSlotName(element.getId());
            list.add(new Pair<>(fluid, new Pair<>(requirement, component)));
          });
        }
      } else if (req instanceof EnergyRequirement requirement) {
        if (requirement.getComponent() != null) {
          EnergyComponent component = requirement.getComponent();
          elements.stream().filter(element -> element instanceof EnergyElement).map(element -> (EnergyElement) element).forEach(element -> {
            EnergyWrapper energy = new EnergyWrapper(
              getX(positionFromRequirement(requirement)),
              getY(positionFromRequirement(requirement)),
              getTexture(TextureMap.ENERGY_EMPTY),
              getTexture(TextureMap.ENERGY_FILLED),
              element.getDirection(),
              requirement,
              recipe,
              animateEnergy,
              element.getId()
            );
            builder
              .addSlot(roleFromMode(requirement.getMode()), energy.getX(), energy.getY())
              .setCustomRenderer(IngredientTypes.ENERGY, energy)
              .addIngredient(IngredientTypes.ENERGY, component)
              .setSlotName(element.getId());
            list.add(new Pair<>(energy, new Pair<>(requirement, component)));
          });
        }
      } else if (req instanceof ExperienceRequirement requirement) {
        if (requirement.getComponent() != null) {
          ExperienceComponent component = requirement.getComponent();
          elements.stream().filter(element -> element instanceof ExperienceElement).map(element -> (ExperienceElement) element).forEach(element -> {
            ExperienceWrapper experience = new ExperienceWrapper(
              getX(positionFromRequirement(requirement)),
              getY(positionFromRequirement(requirement)),
              getTexture(TextureMap.EXPERIENCE_EMPTY),
              getTexture(TextureMap.EXPERIENCE_FILLED),
              element.getDirection(),
              requirement,
              recipe,
              animateExperience,
              element.getId()
            );
            builder
              .addSlot(RecipeIngredientRole.RENDER_ONLY, experience.getX(), experience.getY())
              .setCustomRenderer(IngredientTypes.EXPERIENCE, experience)
              .addIngredient(IngredientTypes.EXPERIENCE, component)
              .setSlotName(element.getId());
            list.add(new Pair<>(experience, new Pair<>(requirement, component)));
          });
        }
      }
    });

    elements.stream().filter(element -> element instanceof ProgressElement).map(element -> (ProgressElement) element).findFirst().ifPresent(element -> {
      ProgressWrapper progress = new ProgressWrapper(
        getX(PositionMap.PROGRESS),
        getY(PositionMap.PROGRESS),
        getTexture(TextureMap.PROGRESS_EMPTY),
        getTexture(TextureMap.PROGRESS_FILLED),
        element.getDirection(),
        recipe,
        animateProgress
      );
      manager.getComponent(progress.getId()).map(component -> (ProgressComponent) component).stream().findFirst().ifPresent(component -> {
        builder
          .addSlot(RecipeIngredientRole.RENDER_ONLY, progress.getX(), progress.getY())
          .setCustomRenderer(IngredientTypes.PROGRESS, progress)
          .addIngredient(IngredientTypes.PROGRESS, component)
          .setSlotName(element.getId());
        list.add(new Pair<>(progress, new Pair<>(null, component)));
      });
    });

    mapRecipeElement.put(originalRecipe, list);
  }

  @Override
  public void draw(R recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
    List<Pair<IElement<?>, Pair<IRequirement<?>, IComponent>>> list = mapRecipeElement.get(recipe);
    guiGraphics.pose().pushPose();
    list.forEach(pair -> pair.getA().renderInJei(guiGraphics, pair.getB().getA(), recipe, mouseX, mouseY, pair.getB().getB()));
    guiGraphics.pose().popPose();
  }

  protected final AbstractCategory<R, P, E> addTexture(TextureMap key, ResourceLocation value) {
    textures.put(key, value);
    return this;
  }

  protected final ResourceLocation getTexture(TextureMap key) {
    return textures.get(key);
  }

  protected final AbstractCategory<R, P, E> add(PositionMap key) {
    return addXY(key, 0, 0);
  }

  protected final AbstractCategory<R, P, E> addX(PositionMap key, int x) {
    return addXY(key, x, 0);
  }

  protected final AbstractCategory<R, P, E> addY(PositionMap key, int y) {
    return addXY(key, 0, y);
  }

  protected final AbstractCategory<R, P, E> addXY(PositionMap key, int x, int y) {
    XY.put(key, new Pair<>(x, y));
    return this;
  }

  protected final int getX(PositionMap key) {
    return XY.get(key).getA();
  }

  protected final int getY(PositionMap key) {
    return XY.get(key).getB();
  }

  protected final PositionMap positionFromRequirement(IRequirement<?> requirement) {
    if (requirement instanceof EnergyRequirement) return PositionMap.ENERGY;
    if (requirement instanceof ExperienceRequirement) return PositionMap.EXPERIENCE;
    if (requirement instanceof ItemRequirement) return requirement.getMode().isInput() ? PositionMap.ITEM_INPUT : PositionMap.ITEM_OUTPUT;
    if (requirement instanceof FluidRequirement) return requirement.getMode().isInput() ? PositionMap.FLUID_INPUT : PositionMap.FLUID_OUTPUT;
    return null;
  }

  protected final RecipeIngredientRole roleFromMode(RequirementMode mode) {
    return mode.isInput() ? RecipeIngredientRole.INPUT : RecipeIngredientRole.OUTPUT;
  }

  protected final void addDefaultTextures() {
    addTexture(TextureMap.ITEM, new DegrassiLocation("textures/gui/base_slot.png"))
      .addTexture(TextureMap.EXPERIENCE_EMPTY, new DegrassiLocation("textures/gui/base_experience_empty.png"))
      .addTexture(TextureMap.EXPERIENCE_FILLED, new DegrassiLocation("textures/gui/base_experience_filled.png"))
      .addTexture(TextureMap.ENERGY_EMPTY, new DegrassiLocation("textures/gui/energy_storage_empty.png"))
      .addTexture(TextureMap.ENERGY_FILLED, new DegrassiLocation("textures/gui/energy_storage_filled.png"))
      .addTexture(TextureMap.FLUID, new DegrassiLocation("textures/gui/base_fluid_storage.png"))
      .addTexture(TextureMap.PROGRESS_EMPTY, new DegrassiLocation("textures/gui/base_progress_empty.png"))
      .addTexture(TextureMap.PROGRESS_FILLED, new DegrassiLocation("textures/gui/base_progress_filled.png"));
  }

  protected abstract P getProcessor();

  protected enum TextureMap {
    ENERGY_EMPTY,
    ENERGY_FILLED,
    ITEM,
    FLUID,
    EXPERIENCE_EMPTY,
    EXPERIENCE_FILLED,
    PROGRESS_EMPTY,
    PROGRESS_FILLED
  }

  protected enum PositionMap {
    ENERGY,
    ITEM_INPUT,
    ITEM_OUTPUT,
    FLUID_INPUT,
    FLUID_OUTPUT,
    EXPERIENCE,
    PROGRESS
  }
}
