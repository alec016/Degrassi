package es.degrassi.forge.core.common.machines.screen;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.core.common.machines.container.SolarPanelContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class SolarPanelScreen extends MachineScreen<SolarPanelContainer> {
  public SolarPanelScreen(SolarPanelContainer menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title, new DegrassiLocation("textures/gui/panel_gui.png"));
  }
}
