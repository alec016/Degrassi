package es.degrassi.forge.init.gui.screen.panel;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.init.gui.container.panel.PanelContainer;
import es.degrassi.forge.init.gui.renderer.EnergyInfoArea;
import es.degrassi.forge.init.gui.renderer.FluidTankRenderer;
import es.degrassi.forge.init.gui.renderer.ProgressComponent;
import es.degrassi.forge.init.gui.renderer.EfficiencyInfoArea;
import es.degrassi.forge.util.storage.AbstractEnergyStorage;
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
  protected EfficiencyInfoArea efficiencyInfoArea;
  public SolarPanelScreen(PanelContainer<?> container, Inventory inventory, Component name) {
    super(container, inventory, name);
  }

  @Override
  public void init() {
    super.init();
    assignEfficiencyInfoArea(43, 20);
  }

  @Override
  protected void renderBg(@NotNull PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, BACKGROUND);
    this.imageWidth = TextureSizeHelper.getTextureWidth(BACKGROUND);
    this.imageHeight = TextureSizeHelper.getTextureHeight(BACKGROUND);
    this.leftPos = (this.width - this.imageWidth) / 2;
    this.topPos = (this.height - this.imageHeight) / 2;

    blit(pPoseStack, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);

    energyInfoArea.draw(pPoseStack, this.leftPos + 25, this.topPos + 20, ENERGY_FILLED);
    renderHover(pPoseStack, this.leftPos, this.topPos, 25, 20, pMouseX, pMouseY, TextureSizeHelper.getTextureWidth(ENERGY_FILLED), TextureSizeHelper.getTextureHeight(ENERGY_FILLED));
    efficiencyInfoArea.draw(pPoseStack, this.leftPos + 43, this.topPos + 20, EFFICIENCY_FILLED);
    renderHover(pPoseStack, this.leftPos, this.topPos, 43, 20, pMouseX, pMouseY, TextureSizeHelper.getTextureWidth(EFFICIENCY_FILLED), TextureSizeHelper.getTextureHeight(EFFICIENCY_FILLED));
  }

  @Override
  protected void renderLabels(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
    int x = this.leftPos;
    int y = this.topPos;
    AbstractEnergyStorage energy = menu.getEntity().getEnergyStorage();

    this.font.draw(poseStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY, 4210752);

    renderEnergyAreaTooltips(poseStack, mouseX, mouseY, x, y);
    renderEfficiencyAreaTooltips(poseStack, mouseX, mouseY, x, y);
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
      efficiencyInfoArea.getTooltips().get(0),
      95,
      95,
      4210752
    );
    poseStack.popPose();
  }



  private void renderEfficiencyAreaTooltips(PoseStack poseStack, int mouseX, int mouseY, int x, int y) {
    if (
      isMouseAboveArea(
        mouseX,
        mouseY,
        x,
        y,
        43,
        20,
        TextureSizeHelper.getTextureWidth(EFFICIENCY_FILLED),
        TextureSizeHelper.getTextureHeight(EFFICIENCY_FILLED)
      )
    ) {
      renderTooltip(
        poseStack,
        efficiencyInfoArea.getTooltips(),
        Optional.empty(),
        mouseX - x,
        mouseY - y
      );
    }
  }

  @Override
  public void setProgressComponent(ProgressComponent progress) {
  }

  @Override
  public void setEnergyComponent(EnergyInfoArea energy) {
    this.energyInfoArea = energy;
  }

  @Override
  public void setFluidComponent(FluidTankRenderer fluid) {}

  @Override
  public void setEfficiencyComponent(EfficiencyInfoArea efficiency) {
    this.efficiencyInfoArea = efficiency;
  }
}
