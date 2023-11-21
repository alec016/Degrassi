package es.degrassi.forge.init.entity.type;

import net.minecraftforge.items.ItemStackHandler;

public interface IItemEntity extends IDegrassiEntity {
  void setHandler(ItemStackHandler itemStackHandler);
}
