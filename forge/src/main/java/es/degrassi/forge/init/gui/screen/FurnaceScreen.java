package es.degrassi.forge.init.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.init.gui.container.FurnaceContainer;
import es.degrassi.forge.init.gui.element.EfficiencyGuiElement;
import es.degrassi.forge.init.gui.element.EnergyGuiElement;
import es.degrassi.forge.init.gui.element.FluidGuiElement;
import es.degrassi.forge.init.gui.element.ProgressGuiElement;
import es.degrassi.forge.requirements.*;
import es.degrassi.forge.util.TextureSizeHelper;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class FurnaceScreen extends AbstractContainerScreen<FurnaceContainer> implements IScreen {
  public static final ResourceLocation BACKGROUND = new DegrassiLocation("textures/gui/furnace_gui.png");
  public static final ResourceLocation ENERGY_FILLED = new DegrassiLocation("textures/gui/furnace_energy_storage_filled.png");
  public static final ResourceLocation FILLED_ARROW = new DegrassiLocation("textures/gui/furnace_progress_filled.png");
  protected EnergyGuiElement energyComponent;
  protected ProgressGuiElement progressComponent;

  public FurnaceScreen(FurnaceContainer abstractContainerMenu, Inventory inventory, Component component) {
    super(abstractContainerMenu, inventory, component);
    this.imageWidth = TextureSizeHelper.getTextureWidth(BACKGROUND);
    this.imageHeight = TextureSizeHelper.getTextureHeight(BACKGROUND);
    this.leftPos = (this.width - this.imageWidth) / 2;
    this.topPos = (this.height - this.imageHeight) / 2;
  }

  @Override
  public void init() {
    super.init();
    getMenu().getEntity().getElementManager().getElements().clear();
    assignEnergyElement(7, 72, ENERGY_FILLED, IRequirement.ModeIO.INPUT, false);
    assignProgressElement(66, 33, FILLED_ARROW, false, false);
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

  @Override
  public void render(@NotNull PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
    renderBackground(pPoseStack);
    super.render(pPoseStack, mouseX, mouseY, delta);
    renderTooltip(pPoseStack, mouseX, mouseY);
  }

  @Override
  protected void renderLabels(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
    this.font.draw(poseStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY, 4210752);

    getMenu().getEntity().getElementManager().renderLabels(
      this, poseStack, mouseX, mouseY
    );
  }

  @Override
  public IScreen getScreen() {
    return this;
  }

  public ProgressGuiElement getComponent() {
    return progressComponent;
  }

  @Override
  public void drawTooltips(PoseStack poseStack, List<Component> tooltips, int mouseX, int mouseY) {
    tooltips.forEach(tooltip -> {
      renderTooltip(poseStack, mouseX, mouseY);
    });
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
  public void setFluidElement(FluidGuiElement fluid) {}

  @Override
  public FluidGuiElement getFluidElement() {
    return null;
  }

  @Override
  public void setEfficiencyElement(EfficiencyGuiElement efficiency) {}

  @Override
  public EfficiencyGuiElement getEfficiencyElement() {
    return null;
  }
}
