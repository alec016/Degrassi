package es.degrassi.forge.core.common.cables.energy;

import net.minecraft.world.item.ItemStack;

public interface IEnergyItemProvider {
  boolean isChargeable(ItemStack stack);
}
