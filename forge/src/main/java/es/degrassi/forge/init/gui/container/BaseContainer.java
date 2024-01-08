package es.degrassi.forge.init.gui.container;

import es.degrassi.forge.init.entity.type.IDegrassiEntity;
import es.degrassi.forge.init.gui.container.types.IContainer;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.*;

public abstract class BaseContainer<T extends IDegrassiEntity> extends AbstractContainerMenu implements IContainer<T> {
  protected T entity;
  protected Level level;
  protected Inventory playerInv;
  protected ContainerData data;  // THIS YOU HAVE TO DEFINE!
  protected BaseContainer(@Nullable MenuType<?> menu, int i) {
    super(menu, i);
  }

  public Inventory getPlayerInv() {
    return playerInv;
  }
}
