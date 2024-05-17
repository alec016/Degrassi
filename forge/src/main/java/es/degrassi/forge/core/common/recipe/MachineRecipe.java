package es.degrassi.forge.core.common.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import es.degrassi.common.utils.DegrassiLogger;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.common.component.ExperienceComponent;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.recipe.MelterRecipe;
import es.degrassi.forge.core.common.requirement.EnergyRequirement;
import es.degrassi.forge.core.common.requirement.ExperienceRequirement;
import es.degrassi.forge.core.common.requirement.FluidRequirement;
import es.degrassi.forge.core.common.requirement.ItemRequirement;
import es.degrassi.forge.core.common.wrapper.DegrassiFluidHandler;
import es.degrassi.forge.core.common.wrapper.DegrassiItemStackHandler;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

@Getter
public abstract class MachineRecipe<T extends MachineRecipe<T>> implements Recipe<Container> {
  private final List<IRequirement<? extends IComponent>> requirements;

  protected final List<IRequirement<? extends IComponent>> tickRequirements = new LinkedList<>();
  protected final List<IRequirement<? extends IComponent>> startRequirements = new LinkedList<>();
  protected final List<IRequirement<? extends IComponent>> endRequirements = new LinkedList<>();

  private int time;

  public MachineRecipe(int time, List<IRequirement<?>> requirements) {
    this.time = time;
    this.requirements = requirements;
  }

  @Override
  public boolean matches(@NotNull Container container, @NotNull Level level) {
    return false;
  }

  @Override
  public @NotNull ItemStack assemble(@NotNull Container container, @NotNull RegistryAccess registryAccess) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return true;
  }

  @Override
  public @NotNull ItemStack getResultItem(@NotNull RegistryAccess registryAccess) {
    return ItemStack.EMPTY;
  }

  public boolean matches(List<? extends IComponent> components) {
    AtomicInteger count = new AtomicInteger(0);
    getRequirements().forEach(req -> {
      if (req instanceof ItemRequirement requirement) {
        AtomicBoolean match = new AtomicBoolean(false);
        components.stream().filter(component -> component instanceof DegrassiItemStackHandler).map(component -> (DegrassiItemStackHandler) component).forEach(comp -> {
          if (match.get()) return;
          comp.getComponents().forEach(component -> {
            if (match.get()) return;
            if (requirement.getId().isEmpty()) {
              if (requirement.matches(component, getTime())) {
                count.getAndIncrement();
                requirement.setComponent(component);
                match.set(true);
              }
            } else if (component.getId().equals(requirement.getId())) {
              if (requirement.matches(component, getTime())) {
                count.getAndIncrement();
                requirement.setComponent(component);
                match.set(true);
              }
            }
          });
        });
      } else if (req instanceof FluidRequirement requirement) {
        AtomicBoolean match = new AtomicBoolean(false);
        components.stream().filter(component -> component instanceof DegrassiFluidHandler).map(component -> (DegrassiFluidHandler) component).forEach(comp -> {
          if (match.get()) return;
          comp.getComponents().forEach(component -> {
            if (match.get()) return;
            if (requirement.getId().isEmpty()) {
              if (requirement.matches(component, getTime())) {
                count.getAndIncrement();
                requirement.setComponent(component);
                match.set(true);
              }
            } else if (component.getId().equals(requirement.getId())) {
              if (requirement.matches(component, getTime())) {
                count.getAndIncrement();
                requirement.setComponent(component);
                match.set(true);
              }
            }
          });
        });
      } else if (req instanceof EnergyRequirement requirement) {
        AtomicBoolean match = new AtomicBoolean(false);
        components.stream().filter(component -> component instanceof EnergyComponent).map(component -> (EnergyComponent) component).forEach(component -> {
          if (match.get()) return;
          if (requirement.getId().isEmpty()) {
            if (requirement.matches(component, getTime())) {
              count.getAndIncrement();
              requirement.setComponent(component);
              match.set(true);
            }
          } else if (component.getId().equals(requirement.getId())) {
            if (requirement.matches(component, getTime())) {
              count.getAndIncrement();
              requirement.setComponent(component);
              match.set(true);
            }
          }
        });
      } else if (req instanceof ExperienceRequirement requirement) {
        AtomicBoolean match = new AtomicBoolean(false);
        components.stream().filter(component -> component instanceof ExperienceComponent).map(component -> (ExperienceComponent) component).forEach(component -> {
          if (match.get()) return;
          if (requirement.getId().isEmpty()) {
            if (requirement.matches(component, getTime())) {
              count.getAndIncrement();
              requirement.setComponent(component);
              match.set(true);
            }
          } else if (component.getId().equals(requirement.getId())) {
            if (requirement.matches(component, getTime())) {
              count.getAndIncrement();
              requirement.setComponent(component);
              match.set(true);
            }
          }
        });
      }
    });
    tickRequirements.clear();
    endRequirements.clear();
    startRequirements.clear();
    if (count.get() == getRequirements().size()) {
      getRequirements().forEach(requirement -> {
        if (requirement.getMode().isPerTick()) tickRequirements.add(requirement);
        else {
          if (requirement.getMode().isInput()) startRequirements.add(requirement);
          else if (requirement.getMode().isOutput()) endRequirements.add(requirement);
        }
      });
      return true;
    }
    return false;
  }

  public static <R extends MachineRecipe<R>> void separateRequirements(R recipe, List<? extends IComponent> components) {
    AtomicInteger count = new AtomicInteger(0);
    recipe.getRequirements().forEach(req -> {
      if (req instanceof ItemRequirement requirement) {
        AtomicBoolean match = new AtomicBoolean(false);
        components.stream().filter(component -> component instanceof DegrassiItemStackHandler).map(component -> (DegrassiItemStackHandler) component).forEach(comp -> {
          if (match.get()) return;
          comp.getComponents().forEach(component -> {
            if (match.get()) return;
            component.fill(requirement);
            if (requirement.getId().isEmpty()) {
              if (requirement.matches(component, recipe.getTime())) {
                count.getAndIncrement();
                requirement.setComponent(component);
                match.set(true);
              }
            } else if (component.getId().equals(requirement.getId())) {
              if (requirement.matches(component, recipe.getTime())) {
                count.getAndIncrement();
                requirement.setComponent(component);
                match.set(true);
              }
            }
          });
        });
      } else if (req instanceof FluidRequirement requirement) {
        AtomicBoolean match = new AtomicBoolean(false);
        components.stream().filter(component -> component instanceof DegrassiFluidHandler).map(component -> (DegrassiFluidHandler) component).forEach(comp -> {
          if (match.get()) return;
          comp.getComponents().forEach(component -> {
            if (match.get()) return;
            component.fill(requirement);
            if (requirement.getId().isEmpty()) {
              if (requirement.matches(component, recipe.getTime())) {
                count.getAndIncrement();
                requirement.setComponent(component);
                match.set(true);
              }
            } else if (component.getId().equals(requirement.getId())) {
              if (requirement.matches(component, recipe.getTime())) {
                count.getAndIncrement();
                requirement.setComponent(component);
                match.set(true);
              }
            }
          });
        });
      } else if (req instanceof EnergyRequirement requirement) {
        AtomicBoolean match = new AtomicBoolean(false);
        components.stream().filter(component -> component instanceof EnergyComponent).map(component -> (EnergyComponent) component).forEach(component -> {
          if (match.get()) return;
          component.fill(requirement, recipe.getTime());
          if (recipe instanceof MelterRecipe r) {
            DegrassiLogger.INSTANCE.info("=========================================================================================");
            DegrassiLogger.INSTANCE.info("recipe: {}", recipe);
            DegrassiLogger.INSTANCE.info("component: {}", component);
          }
          if (requirement.getId().isEmpty()) {
            if (recipe instanceof MelterRecipe r) {
              DegrassiLogger.INSTANCE.info("empty requirement id");
            }
            if (requirement.matches(component, recipe.getTime())) {
              if (recipe instanceof MelterRecipe r) {
                DegrassiLogger.INSTANCE.info("component matches");
              }
              count.getAndIncrement();
              requirement.setComponent(component);
              match.set(true);
            }
          } else if (component.getId().equals(requirement.getId())) {
            if (recipe instanceof MelterRecipe r) {
              DegrassiLogger.INSTANCE.info("requirement id: {}", requirement.getId());
            }
            if (requirement.matches(component, recipe.getTime())) {
              if (recipe instanceof MelterRecipe r) {
                DegrassiLogger.INSTANCE.info("component matches");
              }
              count.getAndIncrement();
              requirement.setComponent(component);
              match.set(true);
            }
          }
        });
      } else if (req instanceof ExperienceRequirement requirement) {
        AtomicBoolean match = new AtomicBoolean(false);
        components.stream().filter(component -> component instanceof ExperienceComponent).map(component -> (ExperienceComponent) component).forEach(component -> {
          if (match.get()) return;
          component.fill(requirement);
          if (requirement.getId().isEmpty()) {
            if (requirement.matches(component, recipe.getTime())) {
              count.getAndIncrement();
              requirement.setComponent(component);
              match.set(true);
            }
          } else if (component.getId().equals(requirement.getId())) {
            if (requirement.matches(component, recipe.getTime())) {
              count.getAndIncrement();
              requirement.setComponent(component);
              match.set(true);
            }
          }
        });
      }
    });
    recipe.tickRequirements.clear();
    recipe.endRequirements.clear();
    recipe.startRequirements.clear();
    if (count.get() == recipe.getRequirements().size()) {
      recipe.getRequirements().forEach(requirement -> {
        if (requirement.getMode().isPerTick()) recipe.tickRequirements.add(requirement);
        else {
          if (requirement.getMode().isInput()) recipe.startRequirements.add(requirement);
          else if (requirement.getMode().isOutput()) recipe.endRequirements.add(requirement);
        }
      });
    }
  }

  public abstract T copy();

  @Override
  public String toString() {
    return asJson().toString();
  }

  public JsonObject asJson() {
    JsonObject json = new JsonObject();
    json.addProperty("type", getType().toString());
    json.addProperty("id", getId().toString());
    json.addProperty("time", time);
    JsonArray requirements = new JsonArray();
    this.requirements.forEach(requirement -> requirements.add(requirement.asJson()));
    json.add("requirements", requirements);
    return json;
  }

  @SuppressWarnings("unchecked")
  public T setTime(int time) {
    this.time = time;
    return (T) this;
  }
}
