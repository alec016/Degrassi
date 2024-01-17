package es.degrassi.forge.init.gui.element;

import com.mojang.blaze3d.vertex.*;
import es.degrassi.forge.init.entity.type.IDegrassiEntity;
import es.degrassi.forge.init.gui.container.types.IContainer;
import es.degrassi.forge.init.gui.screen.*;
import es.degrassi.forge.util.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import org.jetbrains.annotations.*;

@SuppressWarnings("unused")
public interface IGuiElement {
  GuiElementType<? extends IGuiElement> getType();
  int getX();
  int getY();
  int getWidth();
  int getHeight();
  List<Component> getTooltips();

  default void handleClick(byte button, IDegrassiEntity entity, IContainer<?> menu, ServerPlayer player) {}

  default boolean showInJei() {
    return true;
  }

  Tag serializeNBT();
  void deserializeNBT(Tag tag);

  void draw(PoseStack poseStack, float partialTick, int mouseX, int mouseY);
  void renderLabels(Screen screen, @NotNull PoseStack poseStack, int mouseX, int mouseY);

  default void renderHover(PoseStack pPoseStack, int mouseX, int mouseY) {
    if(isMouseAboveArea(
      mouseX,
      mouseY,
      getX(),
      getY(),
      getWidth(),
      getHeight()
    )) {
      GuiComponent.fill(pPoseStack, getX(), getY(), getX() + getWidth(), getY() + getHeight(), -2130706433/*0x45FFFFFF*/);
    }
  }

  default boolean isMouseAboveArea(int mouseX, int mouseY, int x, int y, int width, int height) {
    return MouseUtil.isMouseOver(mouseX, mouseY, x, y, width, height);
  }
}
