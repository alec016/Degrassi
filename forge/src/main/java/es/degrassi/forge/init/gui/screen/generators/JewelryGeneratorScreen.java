package es.degrassi.forge.init.gui.screen.generators;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.forge.init.entity.generators.JewelryGeneratorEntity;
import es.degrassi.forge.init.gui.container.generators.GeneratorContainer;
import es.degrassi.forge.init.gui.renderer.EfficiencyInfoArea;
import es.degrassi.forge.init.gui.renderer.EnergyInfoArea;
import es.degrassi.forge.init.gui.renderer.FluidTankRenderer;
import es.degrassi.forge.init.gui.renderer.ProgressComponent;
import es.degrassi.forge.util.TextureSizeHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import java.util.Optional;

public class JewelryGeneratorScreen extends GeneratorScreen<JewelryGeneratorEntity> {
  public JewelryGeneratorScreen(GeneratorContainer<JewelryGeneratorEntity> arg, Inventory arg2, Component arg3) {
    super(arg, arg2, arg3);
  }

  protected void renderProgressAreaTooltips(PoseStack poseStack, int mouseX, int mouseY, int x, int y) {
    if(
      isMouseAboveArea(
        mouseX,
        mouseY,
        x,
        y,
        66,
        33,
        TextureSizeHelper.getTextureWidth(FILLED_ARROW),
        TextureSizeHelper.getTextureHeight(FILLED_ARROW)
      )
    ) {
      renderTooltip(
        poseStack,
        progressComponent.getTooltips(),
        Optional.empty(),
        mouseX - x,
        mouseY - y
      );
    }
  }


  public ProgressComponent getComponent() {
    return progressComponent;
  }

  protected void renderEnergyAreaTooltips(PoseStack poseStack, int mouseX, int mouseY, int x, int y) {
    if (
      isMouseAboveArea(
        mouseX,
        mouseY,
        x,
        y,
        7,
        72,
        TextureSizeHelper.getTextureWidth(ENERGY_FILLED),
        TextureSizeHelper.getTextureHeight(ENERGY_FILLED)
      )
    ) {
      renderTooltip(
        poseStack,
        energyInfoArea.getTooltips(),
        Optional.empty(),
        mouseX - x,
        mouseY - y
      );
    }
  }


  @Override
  public void setProgressComponent(ProgressComponent progress) {
    this.progressComponent = progress;
  }

  @Override
  public void setEnergyComponent(EnergyInfoArea energy) {
    this.energyInfoArea = energy;
  }

  @Override
  public void setFluidComponent(FluidTankRenderer fluid) {

  }

  @Override
  public void setEfficiencyComponent(EfficiencyInfoArea efficiency) {
  }
}
