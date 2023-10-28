package es.degrassi.forge.init.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.util.MouseUtil;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused")
public interface IScreen {
  ResourceLocation BASE_BACKGROUND = new DegrassiLocation("textures/gui/base_background.png");
  ResourceLocation FILLED_ARROW = new DegrassiLocation("textures/gui/furnace_progress_filled.png");

  IScreen getScreen();

  default void renderHover(PoseStack pPoseStack, int x, int y, int xOffset, int yOffset, int mouseX, int mouseY, int width, int height) {
    if(isMouseAboveArea(
      mouseX,
      mouseY,
      x,
      y,
      xOffset,
      yOffset,
      width,
      height
    )) {
      GuiComponent.fill(pPoseStack, x + xOffset, y + yOffset, x + xOffset + width, y + yOffset + height, -2130706433/*0x45FFFFFF*/);
    }
  }

  default boolean isMouseAboveArea(int mouseX, int mouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
    return MouseUtil.isMouseOver(mouseX, mouseY, x + offsetX, y + offsetY, width, height);
  }
}
