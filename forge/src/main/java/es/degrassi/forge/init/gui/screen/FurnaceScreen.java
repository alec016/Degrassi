package es.degrassi.forge.init.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.init.gui.container.FurnaceContainer;
import es.degrassi.forge.init.gui.element.EfficiencyGuiElement;
import es.degrassi.forge.init.gui.element.EnergyGuiElement;
import es.degrassi.forge.init.gui.element.FluidGuiElement;
import es.degrassi.forge.init.gui.element.ProgressGuiElement;
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
  }

  @Override
  public void init() {
    super.init();
    assignEnergyInfoArea(7, 72);
    assignProgressComponent(66, 33);
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

    energyComponent.draw(pPoseStack, this.leftPos + 7, this.topPos + 72, ENERGY_FILLED, false);
    renderHover(pPoseStack, this.leftPos, this.topPos, 7, 72, pMouseX, pMouseY, TextureSizeHelper.getTextureWidth(ENERGY_FILLED), TextureSizeHelper.getTextureHeight(ENERGY_FILLED));

    if(menu.isCrafting()) {
      progressComponent.draw(pPoseStack, this.leftPos + 66, this.topPos + 33, FILLED_ARROW, false);
    }
  }

  @Override
  public void render(@NotNull PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
    renderBackground(pPoseStack);
    super.render(pPoseStack, mouseX, mouseY, delta);
    renderTooltip(pPoseStack, mouseX, mouseY);
  }

  @Override
  protected void renderLabels(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
    int x = this.leftPos;
    int y = this.topPos;
    this.font.draw(poseStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY, 4210752);

    renderEnergyAreaTooltips(poseStack, mouseX, mouseY, x, y);
    renderProgressAreaTooltips(poseStack, mouseX, mouseY, x, y);
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
        energyComponent.getTooltips(),
        Optional.empty(),
        mouseX - x,
        mouseY - y
      );
    }
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
    return this.topPos;
  }

  @Override
  public int getY() {
    return this.leftPos;
  }

  @Override
  public void setProgressComponent(ProgressGuiElement progress) {
    this.progressComponent = progress;
  }

  @Override
  public void setEnergyComponent(EnergyGuiElement energy) {
    this.energyComponent = energy;
  }

  @Override
  public void setFluidComponent(FluidGuiElement fluid) {}

  @Override
  public void setEfficiencyComponent(EfficiencyGuiElement efficiency) {}
}
