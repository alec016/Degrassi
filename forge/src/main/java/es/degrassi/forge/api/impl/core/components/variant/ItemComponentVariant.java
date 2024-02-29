package es.degrassi.forge.api.impl.core.components.variant;

import es.degrassi.forge.api.core.component.IComponentManager;
import es.degrassi.forge.api.core.component.variant.IComponentVariant;
import net.minecraft.world.item.ItemStack;

public abstract class ItemComponentVariant implements IComponentVariant {

  public abstract boolean canAccept(IComponentManager manager, ItemStack stack);

  public boolean canOutput(IComponentManager manager) {
    return true;
  }

  public boolean shouldDrop(IComponentManager manager) {
    return true;
  }
}
