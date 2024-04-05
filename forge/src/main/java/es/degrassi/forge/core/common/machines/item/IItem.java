package es.degrassi.forge.core.common.machines.item;

import es.degrassi.common.registry.ItemModelType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;

public interface IItem {
  default ItemModelType getItemModelType() {
    return this instanceof DiggerItem ? ItemModelType.HANDHELD : ItemModelType.GENERATED;
  }

  default void oneTimeInfo(Player player, ItemStack stack, Component component) {}
}
