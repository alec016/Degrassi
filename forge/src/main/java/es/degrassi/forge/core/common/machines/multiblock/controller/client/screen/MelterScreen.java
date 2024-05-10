package es.degrassi.forge.core.common.machines.multiblock.controller.client.screen;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.core.common.machines.multiblock.controller.client.container.MelterContainer;
import es.degrassi.forge.core.common.machines.screen.MachineScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MelterScreen extends MachineScreen<MelterContainer> {
  public MelterScreen(MelterContainer menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title, new DegrassiLocation("textures/gui/melter_gui.png"));
  }

  public static ResourceLocation getJeiBackground() {
    return new DegrassiLocation("textures/gui/jei/melter_gui.png");
  }
}
