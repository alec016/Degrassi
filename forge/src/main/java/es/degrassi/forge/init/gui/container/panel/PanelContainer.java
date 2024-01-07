package es.degrassi.forge.init.gui.container.panel;

import es.degrassi.forge.client.IClientHandler;
import es.degrassi.forge.init.entity.panel.PanelEntity;
import es.degrassi.forge.init.gui.container.BaseContainer;
import es.degrassi.forge.init.gui.container.types.IContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class PanelContainer<T extends PanelEntity> extends BaseContainer<T> implements IContainer<T> {

  // THIS YOU HAVE TO DEFINE!
  private static final int TE_INVENTORY_SLOT_COUNT = 4;  // must be the number of slots you have!

  protected PanelContainer(@Nullable MenuType<?> menuType, int id, T entity, Inventory inv) {
    super(menuType, id, TE_INVENTORY_SLOT_COUNT);
    checkContainerSize(inv, 4);
    this.entity = entity;
    this.level = inv.player.level;
    this.playerInv = inv;

    IClientHandler.addPlayerInventory(this, playerInv, 8, 98);
  }

  public T getEntity() {
    return this.entity;
  }
}
