package es.degrassi.forge.api.impl.core.elements;

import es.degrassi.forge.api.core.element.IMachineScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class TexturedGuiElementWidget<T extends AbstractTexturedGuiElement> extends AbstractGuiElementWidget<T> {
  public TexturedGuiElementWidget(T element, IMachineScreen screen, Component title) {
    super(element, screen, title);
  }

  @Override
  public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
//    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    if(this.getElement().getTextureHovered() != null && this.isHoveredOrFocused())
//      RenderSystem.setShaderTexture(0, this.getElement().getTextureHovered());
      guiGraphics.blit(this.getElement().getTextureHovered(), this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);
    else
      guiGraphics.blit(this.getElement().getTexture(), this.getX(), this.getY(), 0, 0, this.width, this.height, this.width, this.height);
//      RenderSystem.setShaderTexture(0, this.getElement().getTexture());
//    GuiComponent.blit(poseStack, this.x, this.y, 0, 0, this.width, this.height, this.width, this.height);
  }
}
