package es.degrassi.forge.api.impl.core.elements;

import es.degrassi.forge.api.core.element.ElementType;
import es.degrassi.forge.api.core.element.IGuiElement;
import es.degrassi.forge.api.core.element.IGuiElementWidgetSupplier;
import es.degrassi.forge.api.core.element.RegisterGuiElementWidgetSupplierEvent;
import java.util.Map;

public class GuiElementWidgetSupplierRegistry {

  private static Map<ElementType<?>, IGuiElementWidgetSupplier<?>> widgetSuppliers;

  public static void init() {
    RegisterGuiElementWidgetSupplierEvent event = new RegisterGuiElementWidgetSupplierEvent();
    RegisterGuiElementWidgetSupplierEvent.EVENT.invoker().registerWidgetSuppliers(event);
    widgetSuppliers = event.getWidgetSuppliers();
  }

  public static <E extends IGuiElement> boolean hasWidgetSupplier(ElementType<E> type) {
    return widgetSuppliers.containsKey(type);
  }

  @SuppressWarnings("unchecked")
  public static <E extends IGuiElement> IGuiElementWidgetSupplier<E> getWidgetSupplier(ElementType<E> type) {
    return (IGuiElementWidgetSupplier<E>) widgetSuppliers.get(type);
  }
}
