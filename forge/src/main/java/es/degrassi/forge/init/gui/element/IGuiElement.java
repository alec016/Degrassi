package es.degrassi.forge.init.gui.element;

import es.degrassi.forge.init.entity.type.IDegrassiEntity;
import es.degrassi.forge.init.gui.container.types.IContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;

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
}
