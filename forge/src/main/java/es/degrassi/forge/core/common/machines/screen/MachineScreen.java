package es.degrassi.forge.core.common.machines.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import es.degrassi.forge.api.utils.TextureSizeHelper;
import es.degrassi.forge.core.common.ElementManager;
import es.degrassi.forge.core.common.machines.container.MachineContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public abstract class MachineScreen<T extends MachineContainer<?>> extends AbstractContainerScreen<T> {
  protected final ResourceLocation background;
  protected final ElementManager manager;
  public MachineScreen(T menu, Inventory playerInventory, Component title, ResourceLocation background) {
    super(menu, playerInventory, title);
    this.background = background;
    this.manager = menu.getEntity().getElementManager();
    this.imageWidth = TextureSizeHelper.getTextureWidth(background);
    this.imageHeight = TextureSizeHelper.getTextureHeight(background);
    this.leftPos = (this.width - this.imageWidth) / 2;
    this.topPos = (this.height - this.imageHeight) / 2;
  }

  @Override
  protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
    renderBackground(guiGraphics);
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

    guiGraphics.blit(this.background, this.leftPos, this.topPos, 0F, 0F, imageWidth, imageHeight, imageWidth, imageHeight);

    guiGraphics.pose().pushPose();
    guiGraphics.pose().translate(leftPos, topPos, 0);

    manager.get().forEach(element -> element.renderWidget(guiGraphics, mouseX, mouseY, partialTick));

    guiGraphics.pose().popPose();
    renderTooltip(guiGraphics, mouseX, mouseY);
  }

  @Override
  protected void renderTooltip(@NotNull GuiGraphics guiGraphics, int x, int y) {
    guiGraphics.pose().pushPose();
    guiGraphics.pose().translate(leftPos, topPos, 0);
    manager.get().forEach(element -> element.renderTooltip(guiGraphics, x - this.leftPos, y - this.topPos));
    guiGraphics.pose().popPose();

    guiGraphics.pose().pushPose();
    guiGraphics.pose().translate(0, 0, -100);
    super.renderTooltip(guiGraphics, x, y);
    guiGraphics.pose().popPose();
  }

  @Override
  protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
    guiGraphics.drawString(font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
  }
}
