package es.degrassi.forge.core.common.processor;

import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IProcessor;
import es.degrassi.forge.api.utils.DegrassiLogger;
import es.degrassi.forge.core.common.component.ProgressComponent;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

public class MachineProcessor<T extends MachineRecipe> implements IProcessor<T> {
  private List<T> recipes;
  private final MachineEntity<T> entity;
  private boolean initialized = false;
  private MachineRecipe currentRecipe;
  //Recipe that was processed when the machine was unloaded, and we need to resume
  private ResourceLocation futureRecipeID;
  private final RecipeType<T> recipeType;
  private Phase phase = Phase.NONE;
  private final boolean resetOnError;
  public MachineProcessor(MachineEntity<T> entity, RecipeType<T> recipeType, boolean reset) {
    this.entity = entity;
    this.recipeType = recipeType;
    this.resetOnError = reset;
  }
  public MachineProcessor(MachineEntity<T> entity, RecipeType<T> recipeType) {
    this(entity, recipeType, false);
  }

  public MachineEntity<T> getEntity() {
    return entity;
  }

  public final void init() {
    initialized = true;
    recipes = Objects.requireNonNull(entity.getLevel()).getRecipeManager().getAllRecipesFor(recipeType);
    if(this.futureRecipeID != null && this.entity.getLevel() != null) {
      this.entity.getLevel().getRecipeManager()
        .byKey(this.futureRecipeID)
        .filter(recipe -> recipe instanceof MachineRecipe)
        .map(recipe -> (MachineRecipe)recipe)
        .ifPresent(this::setRecipe);
      this.futureRecipeID = null;
    }
  }

  @Override
  public List<T> getRecipes() {
    return recipes;
  }

  public void tick (List<? extends IComponent> components) {
    if (!initialized) init();
    if (currentRecipe != null)
      switch (phase) {
        case NONE -> {
          entity.getComponentManager().getComponent(ProgressComponent.id).map(component -> (ProgressComponent) component)
            .ifPresent(component -> component.setMaxProgress(currentRecipe.getTime()));
          processStart(components);
          phase = Phase.STARTED;
        }
        case STARTED -> {
          processTick(components);
          phase = Phase.TICKING;
        }
        case TICKING ->
          entity.getComponentManager().getComponent(ProgressComponent.id).map(component -> (ProgressComponent) component).ifPresent(component -> {
            if (component.hasEnded()) {
              phase = Phase.END;
              return;
            }
            component.tick();
            processTick(components);
          });
        case END -> {
          processEnd(components);
          entity.getComponentManager().getComponent(ProgressComponent.id).map(component -> (ProgressComponent) component).ifPresent(component -> {
            component.resetProgress();
            component.setMaxProgress(0);
          });
          phase = Phase.NONE;
        }
      }
    else entity.setStatus(MachineEntity.MachineStatus.IDLE);
  }

  @Override
  public void processTick(List<? extends IComponent> components) {
    components.forEach(component -> currentRecipe.getRequirements().forEach(requirement -> {
      if (requirement.componentMatches(component)) {
        if (requirement.matches(component)) {
          requirement.processTick(component);
        } else {
          entity.setStatus(MachineEntity.MachineStatus.ERROR);
        }
      }
    }));
  }

  @Override
  public void processStart(List<? extends IComponent> components) {
    components.forEach(component -> currentRecipe.getRequirements().forEach(requirement -> {
      if (requirement.componentMatches(component)) {
        if (requirement.matches(component)) {
          requirement.processStart(component);
        } else {
          entity.setStatus(MachineEntity.MachineStatus.ERROR);
          setRecipe(null);
        }
      }
    }));
  }

  @Override
  public void processEnd(List<? extends IComponent> components) {
    components.forEach(component -> currentRecipe.getRequirements().forEach(requirement -> {
      if (requirement.componentMatches(component)) {
        if (requirement.matches(component)) {
          requirement.processEnd(component);
        } else {
          entity.setStatus(MachineEntity.MachineStatus.ERROR);
        }
      }
    }));
    setRecipe(null);
  }

  @Override
  public MachineRecipe getOldRecipe() {
    return currentRecipe;
  }

  @Override
  public void setRecipe(MachineRecipe recipe) {
    this.currentRecipe = recipe;
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

  public void searchForRecipe(List<? extends IComponent> components) {
    if (!initialized) init();
    AtomicReference<MachineRecipe> r = new AtomicReference<>(null);
    recipes.forEach(recipe -> {
      if (r.get() != null) return;
      AtomicInteger count = new AtomicInteger(0);
      recipe.getRequirements().forEach(requirement -> components.forEach(component -> {
        if (component.getId().equals(requirement.getId())) {
          if (requirement.matches(component)) count.getAndIncrement();
        }
      }));
      if (count.get() == recipe.getRequirements().size()) r.set(recipe);
    });
    if (r.get() != null) {
      setRecipe(r.get());
      entity.setStatus(MachineEntity.MachineStatus.RUNNING);
    }
  }

  public boolean shouldReset() {
    return resetOnError;
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
