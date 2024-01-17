package es.degrassi.forge.init.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.init.gui.container.UpgradeMakerContainer;
import es.degrassi.forge.init.gui.element.EfficiencyGuiElement;
import es.degrassi.forge.init.gui.element.EnergyGuiElement;
import es.degrassi.forge.init.gui.element.FluidGuiElement;
import es.degrassi.forge.init.gui.element.ProgressGuiElement;
import es.degrassi.forge.network.GuiElementClickPacket;
import es.degrassi.forge.requirements.*;
import es.degrassi.forge.util.TextureSizeHelper;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UpgradeMakerScreen extends AbstractContainerScreen<UpgradeMakerContainer> implements IScreen {
  public static final ResourceLocation ENERGY_FILLED = new DegrassiLocation("textures/gui/upgrade_maker_energy_filled.png");
  public static final ResourceLocation TEXTURE = new DegrassiLocation("textures/gui/upgrade_maker_gui.png");
  public static final ResourceLocation FILLED_ARROW = new DegrassiLocation("textures/gui/upgrade_maker_progress_filled.png");

  private EnergyGuiElement energyComponent;
  private ProgressGuiElement progressComponent;
  private FluidGuiElement fluidComponent;
  public UpgradeMakerScreen(UpgradeMakerContainer container, Inventory inv, Component name) {
    super(container, inv, name);
    this.imageWidth = TextureSizeHelper.getTextureWidth(TEXTURE);
    this.imageHeight = TextureSizeHelper.getTextureHeight(TEXTURE);
    this.leftPos = (this.width - this.imageWidth) / 2;
    this.topPos = (this.height - this.imageHeight) / 2;
  }

  @Override
  protected void init() {
    super.init();
    getMenu().getEntity().getElementManager().getElements().clear();
    assignEnergyElement(8, 23, ENERGY_FILLED, IRequirement.ModeIO.INPUT, true);
    assignProgressElement(77, 49, FILLED_ARROW, false, false);
    assignFluidElement(26, 23, 16, 70);
  }

  @Override
  public IScreen getScreen() {
    return this;
  }

  @Override
  public void drawTooltips(PoseStack poseStack, @NotNull List<Component> tooltips, int mouseX, int mouseY) {
    tooltips.forEach(tooltip -> renderTooltip(poseStack, mouseX, mouseY));
  }

  @Override
  public int getX() {
    return this.leftPos;
  }

  @Override
  public int getY() {
    return this.topPos;
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
    this.fluidComponent = fluid;
  }

  @Override
  public FluidGuiElement getFluidElement() {
    return fluidComponent;
  }

  @Override
  public void setEfficiencyElement(EfficiencyGuiElement efficiency) {}

  @Override
  public EfficiencyGuiElement getEfficiencyElement() {
    return null;
  }

  @Override
  public void renderBg(@NotNull PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, TEXTURE);

    blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

    getMenu().getEntity().getElementManager().renderBg(
      poseStack, partialTick, mouseX, mouseY
    );
  }

  @Override
  public void render(@NotNull PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
    renderBackground(pPoseStack);
    super.render(pPoseStack, mouseX, mouseY, delta);
    renderTooltip(pPoseStack, mouseX, mouseY);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (progressComponent.mouseClicked(mouseX, mouseY, button)) {
      new GuiElementClickPacket(progressComponent, (byte) button).sendToServer();

      return true;
    }
    return super.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  protected void renderLabels(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
    this.font.draw(poseStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY, 4210752);

    getMenu().getEntity().getElementManager().renderLabels(
      this, poseStack, mouseX, mouseY
    );
  }

  public ProgressGuiElement getComponent() {
    return progressComponent;
  }
}
