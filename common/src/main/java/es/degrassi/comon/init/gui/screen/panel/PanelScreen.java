package es.degrassi.comon.init.gui.screen.panel;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.Degrassi;
import es.degrassi.comon.init.gui.container.panel.PanelContainer;
import es.degrassi.comon.init.gui.screen.IScreen;
import es.degrassi.comon.init.gui.screen.renderer.EnergyInfoArea;
import es.degrassi.comon.util.storage.AbstractEnergyStorage;
import es.degrassi.comon.util.TextureSizeHelper;
import es.degrassi.comon.util.Utils;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@SuppressWarnings("unused")
public abstract class PanelScreen extends AbstractContainerScreen<PanelContainer> implements IScreen {
  protected static final ResourceLocation BACKGROUND = new ResourceLocation(Degrassi.MODID,"textures/gui/panel_gui.png");
  protected static final ResourceLocation ENERGY_FILLED = new ResourceLocation(Degrassi.MODID, "textures/gui/panel_energy_storage_filled.png");

  protected EnergyInfoArea energyInfoArea;

  public PanelScreen(PanelContainer container, Inventory inventory, Component name) {
    super(container, inventory, name);
  }

  @Override
  public void init() {
    super.init();
    assignEnergyInfoArea(25, 20);
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

    // IClientHandler.renderInventory(pPoseStack, 8, 85, x, y, BASE_INV, 162, 76);
    // IClientHandler.renderProgressArrow(pPoseStack, x, y, false, 24, 16, EMPTY_ARROW, FILLED_ARROW, this.menu);
    // IClientHandler.renderEnergyStorage(pPoseStack, this.leftPos + 156, this.topPos + 19, ENERGY_EMPTY);
    energyInfoArea.draw(pPoseStack, this.leftPos + 25, this.topPos + 20, ENERGY_FILLED, true);
    renderHover(pPoseStack, this.leftPos, this.topPos, 25, 20, pMouseX, pMouseY, TextureSizeHelper.getTextureWidth(ENERGY_FILLED), TextureSizeHelper.getTextureHeight(ENERGY_FILLED));
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
    AbstractEnergyStorage energy = menu.getEntity().getEnergyStorage();

    this.font.draw(poseStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY, 4210752);

    renderEnergyAreaTooltips(poseStack, mouseX, mouseY, x, y);
    PoseStack.Pose prev = poseStack.last();
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
    // poseStack.scale(1F, 1F, 1F);
  }

  void renderEnergyAreaTooltips(PoseStack poseStack, int mouseX, int mouseY, int x, int y) {
    if (
      isMouseAboveArea(
        mouseX,
        mouseY,
        x,
        y,
        25,
        20,
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
    this.energyInfoArea = new EnergyInfoArea(x + xOffset, y + yOffset, menu.getEntity().getEnergyStorage(), 18, 72);
  }

  @Override
  public IScreen getScreen() {
    return this;
  }
}
