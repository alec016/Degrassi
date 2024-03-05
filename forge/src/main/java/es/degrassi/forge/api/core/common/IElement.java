package es.degrassi.forge.api.core.common;

import net.minecraft.client.gui.GuiGraphics;

public interface IElement<E extends IComponent<?>> extends IType {
  IManager<IElement<?>> getManager();

  void render(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY);
}
