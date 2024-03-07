package es.degrassi.forge.core.common.machines.screen;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.core.common.machines.container.FurnaceContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class FurnaceScreen extends MachineScreen<FurnaceContainer> {
  public FurnaceScreen(FurnaceContainer menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title, new DegrassiLocation("textures/gui/furnace_gui.png"));
  }
}
