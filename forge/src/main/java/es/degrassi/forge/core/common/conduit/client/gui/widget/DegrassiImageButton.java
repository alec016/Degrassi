package es.degrassi.forge.core.common.conduit.client.gui.widget;

import es.degrassi.common.misc.Vector2i;
import es.degrassi.forge.core.common.conduit.client.gui.DegrassiScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class DegrassiImageButton extends ImageButton {

  private final DegrassiScreen<?> addedOn;

  public DegrassiImageButton(DegrassiScreen<?> addedOn, int x, int y, int width, int height, int xTexStart, int yTexStart, ResourceLocation resourceLocation,
                        OnPress onPress) {
    super(x, y, width, height, xTexStart, yTexStart, resourceLocation, onPress);
    this.addedOn = addedOn;
  }

  public DegrassiImageButton(DegrassiScreen<?> addedOn, int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffTex,
                        ResourceLocation resourceLocation, OnPress onPress) {
    super(x, y, width, height, xTexStart, yTexStart, yDiffTex, resourceLocation, onPress);
    this.addedOn = addedOn;
  }

  public DegrassiImageButton(DegrassiScreen<?> addedOn, int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffTex,
                        ResourceLocation resourceLocation, int textureWidth, int textureHeight, OnPress onPress) {
    super(x, y, width, height, xTexStart, yTexStart, yDiffTex, resourceLocation, textureWidth, textureHeight, onPress);
    this.addedOn = addedOn;
  }

  public DegrassiImageButton(DegrassiScreen<?> addedOn, int x, int y, int width, int height, int xTexStart, int yTexStart, int yDiffTex,
                        ResourceLocation resourceLocation, int textureWidth, int textureHeight, OnPress onPress, Component message) {
    super(x, y, width, height, xTexStart, yTexStart, yDiffTex, resourceLocation, textureWidth, textureHeight, onPress, message);
    this.addedOn = addedOn;
  }

  @Override
  public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    addedOn.renderSimpleArea(guiGraphics, new Vector2i(getX(), getY()), new Vector2i(getX() + getWidth(), getY() + getHeight()));
    super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
  }
}
