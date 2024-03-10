package es.degrassi.forge.core.common.recipe;

import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IRequirement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public abstract class MachineRecipe implements Recipe<Container> {
  private final List<IRequirement<?>> requirements;
  protected final Map<IRequirement<?>, IComponent> tickRequirements = new LinkedHashMap<>();
  protected final Map<IRequirement<?>, IComponent> startRequirements = new LinkedHashMap<>();
  protected final Map<IRequirement<?>, IComponent> endRequirements = new LinkedHashMap<>();
  private final int time;
  public MachineRecipe(int time, List<IRequirement<?>> requirements) {
    this.time = time;
    this.requirements = requirements;
  }

  @Override
  public boolean matches(@NotNull Container container, @NotNull Level level) {
    return false;
  }

  public int getTime() {
    return time;
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

  public List<IRequirement<?>> getRequirements() {
    return requirements;
  }

  public Map<IRequirement<?>, IComponent> getEndRequirements() {
    return endRequirements;
  }

  public Map<IRequirement<?>, IComponent> getStartRequirements() {
    return startRequirements;
  }

  public Map<IRequirement<?>, IComponent> getTickRequirements() {
    return tickRequirements;
  }

  public abstract boolean matches(List<? extends IComponent> components);

  public abstract MachineRecipe copy();

  @Override
  public String toString() {
    return "{" +
      "time=" + time +
      ", requirements=" + requirements +
      '}';
  }
}
