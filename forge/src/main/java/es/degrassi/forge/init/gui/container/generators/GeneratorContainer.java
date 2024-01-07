package es.degrassi.forge.init.gui.container.generators;

import es.degrassi.forge.client.IClientHandler;
import es.degrassi.forge.init.entity.generators.GeneratorEntity;
import es.degrassi.forge.init.gui.container.BaseContainer;
import es.degrassi.forge.init.gui.container.types.IProgressContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class GeneratorContainer<T extends GeneratorEntity<?, ?, ?>> extends BaseContainer<T> implements IProgressContainer<T> {
  protected GeneratorContainer(@Nullable MenuType<?> menu, int i, int inventorySlotCount, T entity, Inventory inv) {
    super(menu, i, inventorySlotCount);
    checkContainerSize(inv, inventorySlotCount);
    this.entity = entity;
    this.level = inv.player.level;
    this.playerInv = inv;
    IClientHandler.addPlayerInventory(this, playerInv, 8, 98);
  }

  public T getEntity() {
    return this.entity;
  }
}
