package es.degrassi.forge.core.common.processor;

import es.degrassi.forge.api.core.common.CraftingResult;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IProcessor;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.component.ProgressComponent;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

public abstract class MachineProcessor<T extends MachineRecipe> implements IProcessor<T> {
  protected List<T> recipes;
  protected final MachineEntity<T> entity;
  protected boolean initialized = false;
  protected T currentRecipe;
  //Recipe that was processed when the machine was unloaded, and we need to resume
  protected ResourceLocation futureRecipeID;
  protected final RecipeType<T> recipeType;
  protected Phase phase = Phase.NONE;
  private final boolean resetOnError;
  private final Map<IRequirement<?>, IComponent> tickRequirements = new LinkedHashMap<>();
  private final Map<IRequirement<?>, IComponent> startRequirements = new LinkedHashMap<>();
  private final Map<IRequirement<?>, IComponent> endRequirements = new LinkedHashMap<>();
  public MachineProcessor(MachineEntity<T> entity, RecipeType<T> recipeType, boolean reset) {
    this.entity = entity;
    this.recipeType = recipeType;
    this.resetOnError = reset;
  }

  public MachineEntity<T> getEntity() {
    return entity;
  }

  /**
   * Initialize the recipes list
   * adding every single recipe that the processor can handle.
   * <br />
   * <br />
   * If the futureRecipeID is not null
   * search for the recipe that matches
   * and set the recipe to that one.
   * <br />
   * <br />
   * Example
   * <pre>{@code
   *  protected void init() {
   *    initialized = true;
   *    recipes = Objects.requireNonNull(entity.getLevel()).getRecipeManager().getAllRecipesFor(recipeType);
   *    if(this.futureRecipeID != null && this.entity.getLevel() != null) {
   *      this.entity.getLevel().getRecipeManager()
   *        .byKey(this.futureRecipeID)
   *        .filter(recipe -> recipe instanceof MachineRecipe)
   *        .map(recipe -> (T) recipe)
   *        .ifPresent(this::setRecipe);
   *      this.futureRecipeID = null;
   *    }
   *  }
   * }
   * </pre>
   */
  protected abstract void init();

  @Override
  public List<T> getRecipes() {
    return recipes;
  }

  public void tick () {
    if (!initialized) init();
    if (currentRecipe != null)
      switch (phase) {
        case NONE -> {
          ProgressComponent component = (ProgressComponent) entity.getComponentManager().getComponent(ProgressComponent.id).orElse(null);
          if (component == null) return;
          component.setMaxProgress(currentRecipe.getTime());
          processStart();
          setPhase(Phase.STARTED);
        }
        case STARTED -> {
          if (entity.getStatus().isError()) return;
          ProgressComponent component = (ProgressComponent) entity.getComponentManager().getComponent(ProgressComponent.id).orElse(null);
          if (component == null) return;
          processTick();
          setPhase(Phase.TICKING);
        }
        case TICKING -> {
          if (entity.getStatus().isError()) return;
          ProgressComponent component = (ProgressComponent) entity.getComponentManager().getComponent(ProgressComponent.id).orElse(null);
          if (component == null) return;
          if (component.hasEnded()) {
            setPhase(Phase.END);
            return;
          }
          processTick();
          if (entity.getStatus().isError()) return;
          component.tick();
        }
        case END -> {
          if (entity.getStatus().isError()) return;
          ProgressComponent component = (ProgressComponent) entity.getComponentManager().getComponent(ProgressComponent.id).orElse(null);
          if (component == null) return;
          processEnd();
          component.resetProgress();
          component.setMaxProgress(0);
          entity.resetErrorMessage();
          setPhase(Phase.NONE);
          entity.setIdle();
        }
      }
    else entity.setIdle();
  }

  @Override
  public void processTick() {
    tickRequirements.forEach((requirement, component) -> {
      CraftingResult result = requirement.processTick(component);
      if (!result.isSuccess()) entity.setErrored(result.getMessage());
    });
  }

  @Override
  public void processStart() {
    startRequirements.forEach((requirement, component) -> {
      CraftingResult result = requirement.processStart(component);
      if (!result.isSuccess()) entity.setErrored(result.getMessage());
    });
  }

  @Override
  public void processEnd() {
    endRequirements.forEach((requirement, component) -> {
      CraftingResult result = requirement.processEnd(component);
      if (!result.isSuccess()) entity.setErrored(result.getMessage());
    });
    setRecipe(null);
  }

  @Override
  public T getOldRecipe() {
    return currentRecipe;
  }

  @Override
  public void setRecipe(T recipe) {
    this.currentRecipe = recipe;
    entity.setChanged();
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag nbt = new CompoundTag();
    nbt.putString("phase", phase.toString());
    if(this.currentRecipe != null)
      nbt.putString("recipe", this.currentRecipe.getId().toString());
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    phase = Phase.value(nbt.getString("phase"));
    if(nbt.contains("recipe", Tag.TAG_STRING))
      this.futureRecipeID = new ResourceLocation(nbt.getString("recipe"));
  }

  @Override
  public void searchForRecipe(List<? extends IComponent> components) {
    if (!initialized) init();
    AtomicReference<T> r = new AtomicReference<>(null);
    List<IComponent> componentMatches = new ArrayList<>();
    recipes.forEach(recipe -> {
      if (r.get() != null) return;
      AtomicInteger count = new AtomicInteger(0);
      recipe.getRequirements().forEach(requirement -> components.forEach(component -> {
        if (component.getId().equals(requirement.getId())) {
          if (requirement.matches(component, recipe.getTime())) {
            count.getAndIncrement();
            componentMatches.add(component);
          }
        }
      }));
      if (count.get() == recipe.getRequirements().size()) r.set(recipe);
      else componentMatches.clear();
    });
    if (r.get() != null) {
      setRecipe(r.get());
      r.get().getRequirements().forEach(requirement -> componentMatches.forEach(component -> {
        if (requirement.getId().equals(component.getId())) {
          if (requirement.getMode().isPerTick()) tickRequirements.put(requirement, component);
          else {
            if (requirement.getMode().isInput()) startRequirements.put(requirement, component);
            else if (requirement.getMode().isOutput()) endRequirements.put(requirement, component);
          }
        }
      }));
      entity.setRunning();
    }
  }

  public boolean shouldReset() {
    return resetOnError;
  }

  public void setPhase(Phase phase) {
    this.phase = phase;
    entity.setChanged();
  }

  public enum Phase {
    STARTED,
    TICKING,
    END,
    NONE;

    public static Phase value(String value) {
      if (value.equalsIgnoreCase("started")) return STARTED;
      if (value.equalsIgnoreCase("ticking")) return TICKING;
      if (value.equalsIgnoreCase("end")) return END;
      if (value.equalsIgnoreCase("none")) return NONE;
      return null;
    }
  }
}
