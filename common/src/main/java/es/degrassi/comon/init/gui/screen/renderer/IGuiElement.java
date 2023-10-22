package es.degrassi.comon.init.gui.screen.renderer;

import es.degrassi.comon.init.entity.BaseEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.List;

public interface IGuiElement {
  GuiElementType<? extends IGuiElement> getType();
  int getX();
  int getY();
  int getWidth();
  int getHeight();
  List<Component> getTooltips();
  default void handleClick(byte button, BaseEntity entity, AbstractContainerMenu menu, ServerPlayer player) {}

  default boolean showInJei() {
    return true;
  }
}
