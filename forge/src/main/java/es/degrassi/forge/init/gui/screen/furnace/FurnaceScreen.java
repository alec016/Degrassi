package es.degrassi.forge.init.gui.screen.furnace;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.init.gui.container.furnace.FurnaceContainer;
import es.degrassi.forge.init.gui.screen.IScreen;
import es.degrassi.forge.init.gui.renderer.EnergyInfoArea;
import es.degrassi.forge.init.gui.renderer.ProgressComponent;
import es.degrassi.forge.util.TextureSizeHelper;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public abstract class FurnaceScreen extends AbstractContainerScreen<FurnaceContainer> implements IScreen {
  protected static final ResourceLocation BACKGROUND = new ResourceLocation(Degrassi.MODID,"textures/gui/furnace_gui.png");
  protected static final ResourceLocation ENERGY_FILLED = new ResourceLocation(Degrassi.MODID, "textures/gui/furnace_energy_storage_filled.png");
  protected EnergyInfoArea energyInfoArea;
  protected ProgressComponent progressComponent;

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

    energyInfoArea.draw(pPoseStack, this.leftPos + 7, this.topPos + 72, ENERGY_FILLED, false);
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
        energyInfoArea.getTooltips(),
        Optional.empty(),
        mouseX - x,
        mouseY - y
      );
    }
  }

  protected void assignEnergyInfoArea(int xOffset, int yOffset) {
    int x = this.leftPos;
    int y = this.topPos;
    this.energyInfoArea = new EnergyInfoArea(x + xOffset, y + yOffset, menu.getEntity().getEnergyStorage(), TextureSizeHelper.getTextureWidth(ENERGY_FILLED), TextureSizeHelper.getTextureHeight(ENERGY_FILLED));
  }

  protected void assignProgressComponent(int xOffset, int yOffset) {
    int x = this.leftPos;
    int y = this.topPos;
    this.progressComponent = new ProgressComponent(x + xOffset, y + yOffset, menu.getEntity().progressStorage, TextureSizeHelper.getTextureWidth(FILLED_ARROW), TextureSizeHelper.getTextureHeight(FILLED_ARROW));
  }

  @Override
  public IScreen getScreen() {
    return this;
  }
}
