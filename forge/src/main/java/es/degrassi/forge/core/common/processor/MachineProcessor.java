package es.degrassi.forge.core.common.processor;

import com.google.gson.JsonObject;
import es.degrassi.common.utils.Utils;
import es.degrassi.forge.api.core.common.CraftingResult;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IProcessor;
import es.degrassi.forge.core.common.component.ProgressComponent;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

@Getter
public abstract class MachineProcessor<T extends MachineRecipe<T>, E extends MachineEntity<T>> implements IProcessor<T> {
  protected List<T> recipes;
  protected final E entity;
  protected boolean initialized = false;
  protected T currentRecipe;
  //Recipe that was processed when the machine was unloaded, and we need to resume
  protected ResourceLocation futureRecipeID;
  protected Phase phase = Phase.NONE;
  private final boolean resetOnError;

  public MachineProcessor(E entity, boolean reset) {
    this.entity = entity;
    this.resetOnError = reset;
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
  public abstract MachineProcessor<T, E> init();

  public void tick () {
    if (!initialized) init();
    if (currentRecipe != null)
      switch (phase) {
        case NONE -> {
          entity.getProgress().ifPresent(component -> {
            component.setMaxProgress(currentRecipe.getTime());
            processStart();
            setPhase(Phase.STARTED);
          });
        }
        case STARTED -> {
          if (entity.getStatus().isError()) return;
          entity.getProgress().ifPresent(component -> {
            processTick();
            setPhase(Phase.TICKING);
          });
        }
        case TICKING -> {
          if (entity.getStatus().isError()) return;
          entity.getProgress().ifPresent(component -> {
            if (component.hasEnded()) {
              setPhase(Phase.END);
              return;
            }
            processTick();
            if (entity.getStatus().isError()) return;
            component.tick();
          });
        }
        case END -> {
          if (entity.getStatus().isError()) return;
          entity.getProgress().ifPresent(component -> {
            processEnd();
            component.resetProgress();
            component.setMaxProgress(0);
            entity.resetErrorMessage();
            setPhase(Phase.NONE);
//            searchForRecipe(entity.getComponentManager().get());
            entity.setIdle();
          });
        }
      }
    else entity.setIdle();
  }

  public void reset() {
    if (entity.getStatus().isError()) return;
    entity.getProgress().ifPresent(component -> {
      component.resetProgress();
      component.setMaxProgress(0);
      entity.resetErrorMessage();
      setPhase(Phase.NONE);
      entity.setIdle();
    });
  }

  @Override
  public void processTick() {
    currentRecipe.getTickRequirements().forEach(requirement -> {
      CraftingResult result = requirement.processTick();
      if (!result.isSuccess()) entity.setErrored(result.getMessage());
    });
  }

  @Override
  public void processStart() {
    currentRecipe.getStartRequirements().forEach(requirement -> {
      CraftingResult result = requirement.processStart();
      if (!result.isSuccess()) entity.setErrored(result.getMessage());
    });
  }

  @Override
  public void processEnd() {
    currentRecipe.getEndRequirements().forEach(requirement -> {
      CraftingResult result = requirement.processEnd();
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

  public int getProgress() {
    return entity.getProgress().map(ProgressComponent::getProgress).orElse(0);
  }

  public int getRecipeTime() {
    return entity.getProgress().map(ProgressComponent::getMaxProgress).orElse(0);
  }

  public String getProgressPercentage() {
    int progress = getProgress();
    int recipeTime = getRecipeTime();
    if (recipeTime == 0) return "";
    float progressPercent = (progress / (float) recipeTime) * 100;
    return Utils.formatWithPercent(progressPercent);
  }

  @Override
  public void setRecipe(T recipe) {
    this.currentRecipe = recipe == null ? null : recipe.copy();
    entity.setChanged();
  }

  public boolean shouldReset() {
    return resetOnError;
  }

  public void setPhase(Phase phase) {
    this.phase = phase;
    entity.setChanged();
  }

  @Override
  public String toString() {
    return asJson().toString();
  }

  public JsonObject asJson() {
    JsonObject json = new JsonObject();
    json.addProperty("initialized", initialized);
    if (currentRecipe != null) json.add("currentRecipe", currentRecipe.asJson());
    if (futureRecipeID != null) json.addProperty("futureRecipeID", futureRecipeID.toString());
    json.addProperty("phase", phase.name());
    json.addProperty("resetOnError", resetOnError);
    return json;
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
