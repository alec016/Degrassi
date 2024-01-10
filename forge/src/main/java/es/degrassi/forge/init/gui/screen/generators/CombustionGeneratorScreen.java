package es.degrassi.forge.init.gui.screen.generators;

import com.mojang.blaze3d.systems.*;
import com.mojang.blaze3d.vertex.*;
import es.degrassi.common.*;
import es.degrassi.forge.init.entity.generators.*;
import es.degrassi.forge.init.gui.container.generators.*;
import es.degrassi.forge.init.gui.renderer.*;
import es.degrassi.forge.util.*;
import java.util.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.*;
import net.minecraft.world.entity.player.*;
import org.jetbrains.annotations.*;

public class CombustionGeneratorScreen extends GeneratorScreen<CombustionGeneratorEntity> {
  protected static final ResourceLocation BACKGROUND = new DegrassiLocation("textures/gui/combustion_generator_gui.png");
  protected static final ResourceLocation ENERGY_FILLED = new DegrassiLocation("textures/gui/combustion_generator_energy_filled.png");
  public static final ResourceLocation FILLED_ARROW = new DegrassiLocation("textures/gui/combustion_generator_progress_filled.png");

  public CombustionGeneratorScreen(GeneratorContainer<CombustionGeneratorEntity> arg, Inventory arg2, Component arg3) {
    super(arg, arg2, arg3);
  }

  @Override
  public void init() {
    super.init();
    assignEnergyInfoArea(134, 19);
    assignProgressComponent(88, 52, FILLED_ARROW);
  }

  @Override
  protected void renderBg(@NotNull PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, BACKGROUND);
    this.imageWidth = TextureSizeHelper.getTextureWidth(BACKGROUND);
    this.imageHeight = TextureSizeHelper.getTextureHeight(BACKGROUND);
    this.leftPos = (this.width - this.imageWidth) / 2;
    this.topPos = (this.height - this.imageHeight) / 2;

    blit(pPoseStack, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);

    energyInfoArea.draw(pPoseStack, this.leftPos + 134, this.topPos + 19, ENERGY_FILLED, true);
    renderHover(pPoseStack, this.leftPos, this.topPos, 134, 19, pMouseX, pMouseY, TextureSizeHelper.getTextureWidth(ENERGY_FILLED), TextureSizeHelper.getTextureHeight(ENERGY_FILLED));

    if(menu.isCrafting()) {
      progressComponent.draw(pPoseStack, this.leftPos + 88, this.topPos + 52, FILLED_ARROW, true, true);
    }
  }

  @Override
  protected void renderProgressAreaTooltips(PoseStack poseStack, int mouseX, int mouseY, int x, int y) {
    if(
      isMouseAboveArea(
        mouseX,
        mouseY,
        x,
        y,
        88,
        52,
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
        134,
        19,
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
  protected void renderLabels(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
    int x = this.leftPos;
    int y = this.topPos;

    this.font.draw(poseStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY, 4210752);

    renderEnergyAreaTooltips(poseStack, mouseX, mouseY, x, y);
    renderProgressAreaTooltips(poseStack, mouseX, mouseY, x, y);
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
