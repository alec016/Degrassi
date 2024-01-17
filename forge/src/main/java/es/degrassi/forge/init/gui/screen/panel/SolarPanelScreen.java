package es.degrassi.forge.init.gui.screen.panel;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.init.gui.container.panel.PanelContainer;
import es.degrassi.forge.init.gui.element.EnergyGuiElement;
import es.degrassi.forge.init.gui.element.FluidGuiElement;
import es.degrassi.forge.init.gui.element.ProgressGuiElement;
import es.degrassi.forge.init.gui.element.EfficiencyGuiElement;
import es.degrassi.forge.init.gui.component.EnergyComponent;
import es.degrassi.forge.util.TextureSizeHelper;
import es.degrassi.forge.util.Utils;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class SolarPanelScreen extends PanelScreen {
  private static final ResourceLocation EFFICIENCY_FILLED = new DegrassiLocation("textures/gui/panel_efficiency_filled.png");
  protected EfficiencyGuiElement efficiencyComponent;
  public SolarPanelScreen(PanelContainer<?> container, Inventory inventory, Component name) {
    super(container, inventory, name);
  }

  @Override
  public void init() {
    super.init();
    assignEfficiencyElement(43, 20, EFFICIENCY_FILLED, true);
  }

  @Override
  protected void renderLabels(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
    EnergyComponent energy = menu.getEntity().getEnergyStorage();

    this.font.draw(poseStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY, 4210752);

    getMenu().getEntity().getElementManager().renderLabels(
      this, poseStack, mouseX, mouseY
    );

    poseStack.pushPose();
    poseStack.scale(0.7F, 0.7F, 0.7F);
    this.font.draw(
      poseStack,
      Component.translatable(
        "degrassi.gui.element.energy.stored",
        Utils.format(energy.getEnergyStored()),
        Component.translatable("unit.energy.forge"),
        Utils.format(energy.getMaxEnergyStored()),
        Component.translatable("unit.energy.forge")
      ),
      95,
      35,
      4210752
    );
    this.font.draw(
      poseStack,
      Component.translatable(
        "degrassi.gui.element.energy.generation",
        Utils.format(menu.getEntity().getCurrentGeneration()),
        Component.translatable("unit.energy.forge")
      ),
      95,
      55,
      4210752
    );
    this.font.draw(
      poseStack,
      Component.translatable(
        "degrassi.gui.element.transfer",
        Utils.format(menu.getEntity().getEnergyStorage().getMaxExtract()),
        Component.translatable("unit.energy.forge")
      ),
      95,
      75,
      4210752
    );
    this.font.draw(
      poseStack,
      efficiencyComponent.getTooltips().get(0),
      95,
      95,
      4210752
    );
    poseStack.popPose();
  }

  @Override
  public void setProgressElement(ProgressGuiElement progress) {
  }

  @Override
  public ProgressGuiElement getProgressElement() {
    return null;
  }

  @Override
  public void setEnergyElement(EnergyGuiElement energy) {
    this.energyComponent = energy;
  }

  @Override
  public EnergyGuiElement getEnergyElement() {
    return energyComponent;
  }

  @Override
  public void setFluidElement(FluidGuiElement fluid) {}

  @Override
  public FluidGuiElement getFluidElement() {
    return null;
  }

  @Override
  public void setEfficiencyElement(EfficiencyGuiElement efficiency) {
    this.efficiencyComponent = efficiency;
  }

  @Override
  public EfficiencyGuiElement getEfficiencyElement() {
    return efficiencyComponent;
  }
}
