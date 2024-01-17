package es.degrassi.forge.init.gui.screen.generators;

import com.mojang.blaze3d.systems.*;
import com.mojang.blaze3d.vertex.*;
import es.degrassi.common.*;
import es.degrassi.forge.init.entity.generators.*;
import es.degrassi.forge.init.gui.container.generators.*;
import es.degrassi.forge.init.gui.element.*;
import es.degrassi.forge.requirements.*;
import es.degrassi.forge.util.*;
import net.minecraft.client.renderer.*;
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
    this.imageWidth = TextureSizeHelper.getTextureWidth(BACKGROUND);
    this.imageHeight = TextureSizeHelper.getTextureHeight(BACKGROUND);
    this.leftPos = (this.width - this.imageWidth) / 2;
    this.topPos = (this.height - this.imageHeight) / 2;
  }

  @Override
  public void init() {
    super.init();
    assignEnergyElement(134, 19, ENERGY_FILLED, IRequirement.ModeIO.OUTPUT, true);
    assignProgressElement(88, 52, FILLED_ARROW, true, true);
  }

  @Override
  protected void renderBg(@NotNull PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, BACKGROUND);

    blit(pPoseStack, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);

    getMenu().getEntity().getElementManager().renderBg(
      pPoseStack, pPartialTick, pMouseX, pMouseY
    );
  }

  public ProgressGuiElement getComponent() {
    return progressComponent;
  }

  @Override
  public void setProgressElement(ProgressGuiElement progress) {
    this.progressComponent = progress;
  }

  @Override
  public ProgressGuiElement getProgressElement() {
    return progressComponent;
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
  public void setFluidElement(FluidGuiElement fluid) {
  }

  @Override
  public FluidGuiElement getFluidElement() {
    return null;
  }

  @Override
  public void setEfficiencyElement(EfficiencyGuiElement efficiency) {
  }

  @Override
  public EfficiencyGuiElement getEfficiencyElement() {
    return null;
  }
}
