package es.degrassi.forge.core.common.cables.fluid;

import net.minecraft.world.item.ItemStack;

public interface IFluidItemProvider {
  boolean isTransferable(ItemStack stack);
}
