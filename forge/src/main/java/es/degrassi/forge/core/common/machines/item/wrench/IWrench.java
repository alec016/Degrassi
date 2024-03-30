package es.degrassi.forge.core.common.machines.item.wrench;

import net.minecraft.world.item.ItemStack;

public interface IWrench {
  WrenchMode getWrenchMode(ItemStack stack);
}
