package es.degrassi.forge.api.core.element;


import es.degrassi.forge.api.core.IManager;
import java.util.Optional;

public interface IElementManager extends IManager<ElementType<?>, IGuiElement> {
  <T extends IGuiElement> Optional<T> getElement(ElementType<T> type);
  <T extends IGuiElement> Optional<IElementHandler<T>> getElementHandler(ElementType<T> type);
}
