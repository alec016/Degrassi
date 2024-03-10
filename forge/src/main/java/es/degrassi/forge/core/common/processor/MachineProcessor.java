package es.degrassi.forge.core.common.processor;

import es.degrassi.forge.api.core.common.CraftingResult;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IProcessor;
import es.degrassi.forge.api.utils.DegrassiLogger;
import es.degrassi.forge.core.common.component.ProgressComponent;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

public abstract class MachineProcessor<T extends MachineRecipe> implements IProcessor<T> {
  protected List<T> recipes;
  protected final MachineEntity<T> entity;
  protected boolean initialized = false;
  protected T currentRecipe;
  //Recipe that was processed when the machine was unloaded, and we need to resume
  protected ResourceLocation futureRecipeID;
  protected Phase phase = Phase.NONE;
  private final boolean resetOnError;
  public MachineProcessor(MachineEntity<T> entity, boolean reset) {
    this.entity = entity;
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
   *  @Override
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
    currentRecipe.getTickRequirements().forEach((requirement, component) -> {
      CraftingResult result = requirement.processTick(component);
      if (!result.isSuccess()) entity.setErrored(result.getMessage());
    });
  }

  @Override
  public void processStart() {
    currentRecipe.getStartRequirements().forEach((requirement, component) -> {
      CraftingResult result = requirement.processStart(component);
      if (!result.isSuccess()) entity.setErrored(result.getMessage());
    });
  }

  @Override
  public void processEnd() {
    currentRecipe.getEndRequirements().forEach((requirement, component) -> {
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
    recipes.forEach(recipe -> {
      if (r.get() != null) return;
      if (recipe.matches(components)) r.set(recipe);
    });
    if (r.get() != null) {
      setRecipe(r.get());
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
