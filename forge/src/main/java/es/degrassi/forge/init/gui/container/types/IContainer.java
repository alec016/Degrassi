package es.degrassi.forge.init.gui.container.types;

import es.degrassi.forge.init.entity.BaseEntity;
import net.minecraft.network.chat.Component;

public interface IContainer<T extends BaseEntity> {
  int HOTBAR_SLOT_COUNT = 9;
  int PLAYER_INVENTORY_ROW_COUNT = 3;
  int PLAYER_INVENTORY_COLUMN_COUNT = 9;
  int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
  int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
  int VANILLA_FIRST_SLOT_INDEX = 0;
  int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

  T getEntity();
}
