package es.degrassi.forge.init.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.init.gui.container.UpgradeMakerContainer;
import es.degrassi.forge.init.gui.renderer.EnergyInfoArea;
import es.degrassi.forge.init.gui.renderer.FluidTankRenderer;
import es.degrassi.forge.init.gui.renderer.ProgressComponent;
import es.degrassi.forge.util.TextureSizeHelper;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class UpgradeMakerScreen extends AbstractContainerScreen<UpgradeMakerContainer> implements IScreen {
  protected static final ResourceLocation ENERGY_FILLED = new DegrassiLocation("textures/gui/upgrade_maker_energy_filled.png");
  private static final ResourceLocation TEXTURE = new DegrassiLocation("textures/gui/upgrade_maker_gui.png");
  private static final ResourceLocation FILLED_ARROW = new DegrassiLocation("textures/gui/upgrade_maker_progress_filled.png");

  private EnergyInfoArea energyInfoArea;
  private ProgressComponent progressComponent;
  public UpgradeMakerScreen(UpgradeMakerContainer container, Inventory inv, Component name) {
    super(container, inv, name);
  }

  @Override
  protected void init() {
    super.init();
    assignEnergyInfoArea(7, 22);
    assignProgressComponent(77, 49);
    // assignFluidRenderer(16, 70);
  }

  @Override
  public IScreen getScreen() {
    return this;
  }

  @Override
  public void renderBg(@NotNull PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, TEXTURE);
    this.imageWidth = TextureSizeHelper.getTextureWidth(TEXTURE);
    this.imageHeight = TextureSizeHelper.getTextureHeight(TEXTURE);
    this.leftPos = (this.width - this.imageWidth) / 2;
    this.topPos = (this.height - this.imageHeight) / 2;

    blit(poseStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);

    this.energyInfoArea.draw(poseStack, this.leftPos + 8, this.topPos + 23, ENERGY_FILLED, true);
    renderHover(poseStack, this.leftPos, this.topPos, 8, 23, mouseX, mouseY, TextureSizeHelper.getTextureWidth(ENERGY_FILLED), TextureSizeHelper.getTextureHeight(ENERGY_FILLED));
    if(this.menu.isCrafting()) {
      this.progressComponent.draw(poseStack, this.leftPos + 77, this.topPos + 49, FILLED_ARROW, false);
    }
    FluidTankRenderer.renderFluid(poseStack, this.leftPos + 26, this.topPos + 23, 16, 70, this.menu.getEntity().getFluidStorage().getFluid(), this.menu.getEntity().getFluidStorage().getCapacity());
    renderHover(poseStack, this.leftPos, this.topPos, 26, 23, mouseX, mouseY, 16, 70);
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

    renderEnergyAreaTooltips(poseStack, mouseX, mouseY, this.leftPos, this.topPos);
    renderProgressAreaTooltips(poseStack, mouseX, mouseY, this.leftPos, this.topPos);
    renderFluidAreaTooltips(poseStack, mouseX, mouseY, this.leftPos, this.topPos);
  }

  private void renderEnergyAreaTooltips(PoseStack poseStack, int mouseX, int mouseY, int x, int y) {
    if (
      isMouseAboveArea(
        mouseX,
        mouseY,
        x,
        y,
        7,
        22,
        TextureSizeHelper.getTextureWidth(ENERGY_FILLED),
        TextureSizeHelper.getTextureHeight(ENERGY_FILLED)
      )
    ) {
      renderTooltip(
        poseStack,
        this.energyInfoArea.getTooltips(),
        Optional.empty(),
        mouseX - x,
        mouseY - y
      );
    }
  }

  protected void renderProgressAreaTooltips(PoseStack poseStack, int mouseX, int mouseY, int x, int y) {
    if(
      isMouseAboveArea(
        mouseX,
        mouseY,
        x,
        y,
        77,
        49,
        TextureSizeHelper.getTextureWidth(FILLED_ARROW),
        TextureSizeHelper.getTextureHeight(FILLED_ARROW)
      )
    ) {
      renderTooltip(
        poseStack,
        this.progressComponent.getTooltips(),
        Optional.empty(),
        mouseX - x,
        mouseY - y
      );
    }
  }

  private void renderFluidAreaTooltips(PoseStack poseStack, int mouseX, int mouseY, int x, int y) {
    if (isMouseAboveArea(mouseX, mouseY, x, y, 26, 23, 16, 70)) {
      renderTooltip(
        poseStack,
        FluidTankRenderer.getTooltip(this.menu.getEntity().getFluidStorage().getFluid(), FluidTankRenderer.TooltipMode.SHOW_AMOUNT_AND_CAPACITY, this.menu.getEntity().getFluidStorage().getCapacity()),
        Optional.empty(),
        mouseX - x,
        mouseY - y
      );
    }
  }

  protected void assignEnergyInfoArea(int xOffset, int yOffset) {
    this.energyInfoArea = new EnergyInfoArea(this.leftPos + xOffset, this.topPos + yOffset, this.menu.getEntity().getEnergyStorage());
  }

  protected void assignProgressComponent(int xOffset, int yOffset) {
    this.progressComponent = new ProgressComponent(this.leftPos + xOffset, this.topPos + yOffset, this.menu.getEntity().getProgressStorage(), TextureSizeHelper.getTextureWidth(FILLED_ARROW), TextureSizeHelper.getTextureHeight(FILLED_ARROW));
  }
}
