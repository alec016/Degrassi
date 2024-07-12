package es.degrassi.forge.api.core.common;

import com.google.gson.JsonObject;
import es.degrassi.forge.core.common.ElementManager;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

public interface IElement<E extends IComponent> extends IType {
  String EMPTY_TEXTURE_KEY = "emptyTexture";
  String FILLED_TEXTURE_KEY = "filledTexture";
  String HOVERED_TEXTURE_KEY = "hoveredTexture";
  String TEXTURE_KEY = "texture";
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

  int getX();
  int getY();

  void serialize(CompoundTag nbt);
  default CompoundTag serialize() {
    return new CompoundTag();
  }
  void deserialize(CompoundTag nbt);

  void renderInJei(GuiGraphics guiGraphics, IRequirement<?> requirement, MachineRecipe<?> recipe, double mouseX, double mouseY, IComponent component);

  boolean isJei();

  void setRequirement(IRequirement<E> requirement);

  IElement<E> copy(ElementManager manager);

  default void asJson(JsonObject json) {
    json.addProperty("x", getX());
    json.addProperty("y", getY());
    json.addProperty("jei", isJei());
    json.addProperty("id", getId());
    json.addProperty("direction", getDirection().toString());
  }
}
