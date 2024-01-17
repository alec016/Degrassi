package es.degrassi.forge.api.gui;

import es.degrassi.forge.init.gui.element.IGuiElement;
import java.util.List;

public interface IElementManager extends IManager {
  List<IGuiElement> getElements();

  void addElement(IGuiElement element);
}
