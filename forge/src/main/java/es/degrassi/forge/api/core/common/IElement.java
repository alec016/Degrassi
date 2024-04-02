package es.degrassi.forge.api.core.common;

import es.degrassi.forge.core.common.ElementManager;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

public interface IElement<E extends IComponent> extends IType {
  String EMPTY_TEXTURE_KEY = "emptyTexture";
  String FILLED_TEXTURE_KEY = "filledTexture";
  ElementManager getManager();

  void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);
  void renderTooltip(GuiGraphics guiGraphics, int x, int y);

  void renderHighlight(@NotNull GuiGraphics guiGraphics, int x, int y);
  void markDirty();
  String getId();

  default ElementDirection getDirection() {
    return ElementDirection.RIGHT;
  }

  default Pair<Integer, Integer> getWidthHeight(int textureWidth, int textureHeight, float filledPercentage) {
    return switch (getDirection()) {
      case RIGHT, LEFT -> new Pair<>((int) (textureWidth * filledPercentage), textureHeight);
      case TOP, BOTTOM -> new Pair<>(textureWidth, (int) (textureHeight * filledPercentage));
    };
  }

  default Pair<Integer, Integer> getXYOffset(int textureWidth, int textureHeight, float filledPercentage) {
    return switch (getDirection()) {
      case RIGHT, BOTTOM -> new Pair<>(0, 0);
      case TOP -> new Pair<>(0, textureHeight - ((int) (textureHeight * filledPercentage)));
      case LEFT -> new Pair<>(textureWidth - ((int) (textureWidth * filledPercentage)), 0);
    };
  }

  default void clientTick() {}
  default void serverTick() {}

  void serialize(CompoundTag nbt);
  void deserialize(CompoundTag nbt);
}
