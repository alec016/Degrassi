package es.degrassi.forge.core.common.machines.multiblock.parts.client.screen;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.core.common.machines.multiblock.parts.client.container.EnergyHatchContainer;
import es.degrassi.forge.core.common.machines.multiblock.parts.client.container.InputBusContainer;
import es.degrassi.forge.core.common.machines.screen.MachineScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class EnergyHatchScreen extends MachineScreen<EnergyHatchContainer> {

  public EnergyHatchScreen(EnergyHatchContainer menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title, new DegrassiLocation("textures/gui/multiblock/parts/energy_hatch_background.png"));
  }

  public static ResourceLocation getJeiBackground() {
    return new DegrassiLocation("textures/gui/jei/multiblock/parts/energy_hatch_background.png");
  }
}
