package es.degrassi.forge.init.gui.container;

import es.degrassi.forge.init.entity.type.IDegrassiEntity;
import es.degrassi.forge.init.gui.container.types.IContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public abstract class BaseContainer<T extends IDegrassiEntity> extends AbstractContainerMenu implements IContainer<T> {
  protected T entity;
  protected Level level;
  protected Inventory playerInv;
  protected ContainerData data;
  protected BaseContainer(@Nullable MenuType<?> menu, int i) {
    super(menu, i);
  }

  public Inventory getPlayerInv() {
    return playerInv;
  }
}
