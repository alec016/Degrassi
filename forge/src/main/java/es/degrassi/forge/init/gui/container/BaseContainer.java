package es.degrassi.forge.init.gui.container;

import es.degrassi.forge.init.entity.BaseEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;

public abstract class BaseContainer<T extends BaseEntity> extends AbstractContainerMenu implements IContainer<T> {
  protected BaseContainer(@Nullable MenuType<?> menu, int i) {
    super(menu, i);
  }
}
