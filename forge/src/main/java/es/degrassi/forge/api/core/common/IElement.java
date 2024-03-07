package es.degrassi.forge.api.core.common;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

public interface IElement<E extends IComponent> extends IType {
  String EMPTY_TEXTURE_KEY = "emptyTexture";
  String FILLED_TEXTURE_KEY = "filledTexture";
  IManager<IElement<?>> getManager();

  void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);
  void renderTooltip(GuiGraphics guiGraphics, int x, int y);

  void renderHighlight(@NotNull GuiGraphics guiGraphics, int x, int y);
  void markDirty();
  String getId();

  default void clientTick() {}
  default void serverTick() {}

  void serialize(CompoundTag nbt);
  void deserialize(CompoundTag nbt);
}
