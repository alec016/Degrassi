package es.degrassi.forge.core.elements;

import es.degrassi.forge.api.core.element.ElementType;
import es.degrassi.forge.api.core.element.IElementHandler;
import es.degrassi.forge.api.core.element.IElementManager;
import es.degrassi.forge.api.core.element.IGuiElement;
import es.degrassi.forge.api.core.machine.MachineTile;
import es.degrassi.forge.init.registration.ElementRegistration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

public class ElementManager implements IElementManager {
  private final MachineTile tile;
  private final Map<ElementType<?>, IGuiElement> elements;

  @SuppressWarnings("unchecked, rawtypes")
  public ElementManager(List<? extends IGuiElement> list, MachineTile tile) {
    this.tile = tile;
    Map<ElementType<?>, IGuiElement> elements = new LinkedHashMap<>();
    Map<ElementType<?>, List<IGuiElement>> handlers = new LinkedHashMap<>();
    list.forEach(element -> {
      if (handlers.get(element.getType()) != null) {
        handlers.compute(element.getType(), (type, l) -> l != null ? l : new ArrayList<>()).add(element);
      } else {
        if (elements.get(element.getType()) != null) {
          IGuiElement el = elements.remove(element.getType());
          List<IGuiElement> els = List.of(el, element);
          handlers.computeIfAbsent(element.getType(), type -> new ArrayList<>()).addAll(els);
        } else
          elements.put(element.getType(), element);
      }
    });
    handlers.forEach((type, l) -> elements.put(type, type.getHandler(this, (List) Collections.unmodifiableList(list))));

    StreamSupport.stream(ElementRegistration.ELEMENT_TYPE_REGISTRY.spliterator(), false)
      .filter(type ->
        type.isDefaultElement() &&
          elements.values().stream().noneMatch(element -> element.getType() == type)
      )
      .forEach(type -> elements.put(type, type.getDefaultElementBuilder().apply(this)));

    this.elements = elements;
  }

  @SuppressWarnings("unchecked")
  public <T extends IGuiElement> Optional<T> getElement(ElementType<T> type) {
    return Optional.ofNullable((T) this.elements.get(type));
  }

  @SuppressWarnings("unchecked")
  public <T extends IGuiElement> Optional<IElementHandler<T>> getElementHandler(ElementType<T> type) {
    return getElement(type).filter(element -> element instanceof IElementHandler).map(element -> (IElementHandler<T>) element);
  }

  public boolean hasElement(ElementType<?> type) {
    return this.elements.get(type) != null;
  }

  @Override
  public Map<ElementType<?>, IGuiElement> getMap() {
    return elements;
  }

  @Override
  public MachineTile getTile() {
    return tile;
  }

  @Override
  public Level getLevel() {
    return tile.getLevel();
  }

  @Override
  public MinecraftServer getServer() {
    return tile.getLevel().getServer();
  }

  @Override
  public void markDirty() {

  }
}
