package es.degrassi.forge.core.common.recipe;

import es.degrassi.forge.api.core.common.IRequirement;
import java.util.List;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

public abstract class MachineRecipe implements Recipe<Container> {
  private final List<IRequirement<?>> requirements;
  private final int time;
  public MachineRecipe(int time, List<IRequirement<?>> requirements) {
    this.time = time;
    this.requirements = requirements;
  }

  @Override
  public boolean matches(Container container, Level level) {
    return false;
  }

  public int getTime() {
    return time;
  }

  @Override
  public ItemStack assemble(Container container, RegistryAccess registryAccess) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return true;
  }

  @Override
  public ItemStack getResultItem(RegistryAccess registryAccess) {
    return ItemStack.EMPTY;
  }

  public List<IRequirement<?>> getRequirements() {
    return requirements;
  }
}
