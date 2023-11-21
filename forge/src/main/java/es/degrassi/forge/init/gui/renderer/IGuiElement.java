package es.degrassi.forge.init.gui.renderer;

import es.degrassi.forge.init.entity.BaseEntity;
import es.degrassi.forge.init.entity.type.IDegrassiEntity;
import es.degrassi.forge.init.gui.container.types.IContainer;
import es.degrassi.forge.init.gui.screen.IScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

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
