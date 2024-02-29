package es.degrassi.forge.api.core.element;

import es.degrassi.forge.init.tile.MachineTile;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public interface IGuiElement {
//  NamedCodec<IGuiElement> CODEC = RegistrarCodec.GUI_ELEMENT.dispatch(
//    IGuiElement::getType,
//    ElementType::getCodec,
//    "Gui Element"
//  );

  /**
   * @return A registered GuiElementType corresponding to this IGuiElement.
   */
  ElementType<? extends IGuiElement> getType();

  /**
   * @return The X pos of the element in pixel (horizontal, 0 is the left of the machine gui)
   */
  int getX();

  /**
   * @return The Y pos of the element in pixel (vertical, 0 is the top of the machine gui)
   */
  int getY();

  /**
   * @return The width in pixel of the element, used to scale the textures and calculate if the mouse cursor is hovering the element.
   */
  int getWidth();

  /**
   * @return The height in pixel of the element, used to scale the textures and calculate if the mouse cursor is hovering the element.
   */
  int getHeight();

  /**
   * @return The priority for rendering the element. If several elements overlap, the elements with higher priority are rendered on top of those with lower priority.
   */
  int getPriority();

  /**
   * A list of components that will be displayed as tooltips when the element is hovered in the machine gui.
   * The element title is returned by default if not override.
   * @return The tooltips of the gui element.
   */
  List<Component> getTooltips();

  /**
   * Called server-side when player click on a gui element.
   * @param button The mouse button that was clicked.
   *               0 : left
   *               1 : right
   *               2 : middle
   * @param tile The machine the player is currently using.
   */
  default void handleClick(byte button, MachineTile tile, AbstractContainerMenu container, ServerPlayer player) {}

  default boolean showInJei() {
    return true;
  }
}
