package es.degrassi.forge.core.common.machines.multiblock.controller.client.screen;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.api.core.common.RequirementMode;
import es.degrassi.forge.core.common.machines.multiblock.controller.client.container.MelterContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MelterScreen extends MultiblockControllerScreen<MelterContainer> {
  public MelterScreen(MelterContainer menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title, new DegrassiLocation("textures/gui/base_background.png"));
  }

  public static ResourceLocation getJeiBackground() {
    return new DegrassiLocation("textures/gui/jei/melter_gui.png");
  }
}
