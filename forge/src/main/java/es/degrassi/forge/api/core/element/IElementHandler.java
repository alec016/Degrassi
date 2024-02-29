package es.degrassi.forge.api.core.element;

import java.util.List;
import java.util.Optional;

public interface IElementHandler<T extends IGuiElement> extends IGuiElement {
  List<T> getElements();
  Optional<T> getElementForID(String id);
}
