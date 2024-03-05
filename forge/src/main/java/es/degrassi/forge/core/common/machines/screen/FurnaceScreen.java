package es.degrassi.forge.core.common.machines.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.api.utils.TextureSizeHelper;
import es.degrassi.forge.core.common.ElementManager;
import es.degrassi.forge.core.common.machines.container.FurnaceContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class FurnaceScreen extends MachineScreen<FurnaceContainer> {
  public static final ResourceLocation BACKGROUND = new DegrassiLocation("textures/gui/furnace_gui.png");
  private final ElementManager manager;
  public FurnaceScreen(FurnaceContainer menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
    this.manager = menu.getEntity().getElementManager();
    this.imageWidth = TextureSizeHelper.getTextureWidth(BACKGROUND);
    this.imageHeight = TextureSizeHelper.getTextureHeight(BACKGROUND);
    this.leftPos = (this.width - this.imageWidth) / 2;
    this.topPos = (this.height - this.imageHeight) / 2;
  }

  @Override
  protected void init() {
    super.init();
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
    super.renderBg(guiGraphics, partialTick, mouseX, mouseY);
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.setShaderTexture(0, BACKGROUND);

    guiGraphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);

    manager.get().forEach(element -> element.render(guiGraphics, partialTick, mouseX, mouseY));
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    renderBackground(guiGraphics);
    super.render(guiGraphics, mouseX, mouseY, partialTick);
    renderTooltip(guiGraphics, mouseX, mouseY);
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    guiGraphics.drawString(font, this.title, this.titleLabelX, this.titleLabelY, 4210752);
  }
}
