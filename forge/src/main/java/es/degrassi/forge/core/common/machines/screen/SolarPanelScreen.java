package es.degrassi.forge.core.common.machines.screen;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.common.utils.Utils;
import es.degrassi.forge.core.common.component.BarComponent;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.common.machines.container.SolarPanelContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class SolarPanelScreen extends MachineScreen<SolarPanelContainer> {
  public SolarPanelScreen(SolarPanelContainer menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title, new DegrassiLocation("textures/gui/panel_gui.png"));
  }

  @Override
  protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
    super.renderLabels(guiGraphics, mouseX, mouseY);

    guiGraphics.pose().pushPose();
    guiGraphics.pose().scale(0.7F, 0.7F, 0.7F);
    renderEnergy(guiGraphics, mouseX, mouseY);
    renderGeneration(guiGraphics, mouseX, mouseY);
    guiGraphics.pose().popPose();
  }

  private void renderEnergy(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    menu.getEntity().getComponentManager().getComponentsByType("energy").stream().findFirst().map(comp -> (EnergyComponent) comp) .ifPresent(energy -> {
      guiGraphics.drawString(
        font,
        Component.translatable(
          "degrassi.gui.element.energy.stored",
          energy.getEnergyStored()
        ),
        95,
        35,
        defaultColor,
        false
      );
      guiGraphics.drawString(
        font,
        Component.translatable(
          "degrassi.gui.element.energy.capacity",
          energy.getMaxEnergyStored()
        ),
        95,
        55,
        defaultColor,
        false
      );
      guiGraphics.drawString(
        font,
        Component.translatable(
          "degrassi.gui.element.transfer",
          energy.getMaxOutput()
        ),
        95,
        95,
        defaultColor,
        false
      );
    });
  }

  private void renderGeneration(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    menu.getEntity().getComponentManager().getComponent("generation").map(comp -> (BarComponent) comp) .ifPresent(gen ->
      guiGraphics.drawString(
        font,
        Component.translatable(
          "degrassi.gui.element.generation",
          Utils.format(gen.getAmount()).replaceAll("\\.0", ""),
          Utils.format(gen.getCapacity()).replaceAll("\\.0", "")
        ),
        95,
        75,
        defaultColor,
        false
      )
    );
  }
}
