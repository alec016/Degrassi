package es.degrassi.forge.core.common.machines.screen;

import es.degrassi.forge.core.common.machines.container.ChestContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class ChestScreen extends MachineScreen<ChestContainer> {
  public ChestScreen(ChestContainer menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title, menu.getEntity().getTier().getBackground());
  }

  @Override
  protected void init() {
    super.init();
    getMenu().getEntity().startOpen(player);
  }

  @Override
  public void onClose() {
    super.onClose();
    getMenu().getEntity().stopOpen(player);
  }
}
