package es.degrassi.forge.core.elements;

import es.degrassi.forge.api.core.element.ElementType;
import es.degrassi.forge.api.core.element.IElementHandler;
import es.degrassi.forge.api.core.element.IElementManager;
import es.degrassi.forge.api.core.element.IGuiElement;
import es.degrassi.forge.init.tile.MachineTile;
import es.degrassi.forge.api.impl.core.elements.AbstractGuiElement;
import es.degrassi.forge.api.impl.util.TextureSizeHelper;
import es.degrassi.forge.init.registration.ElementRegistration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

public class ElementManager implements IElementManager {
  private final MachineTile tile;
  private Map<ElementType<?>, IGuiElement> elements;
  private List<IGuiElement> templates;

  public ElementManager(MachineTile tile) {
    this.tile = tile;
    this.templates = new LinkedList<>();
    updateManager();
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

  @SuppressWarnings("unchecked, rawtypes")
  public ElementManager updateManager() {
    Map<ElementType<?>, IGuiElement> elements = new LinkedHashMap<>();
    Map<ElementType<?>, List<IGuiElement>> handlers = new LinkedHashMap<>();
    templates.forEach(element -> {
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
    handlers.forEach((type, l) -> elements.put(type, type.getHandler(this, (List) Collections.unmodifiableList(templates))));

    StreamSupport.stream(ElementRegistration.ELEMENT_TYPE_REGISTRY.spliterator(), false)
      .filter(type ->
        type.isDefaultElement() &&
          elements.values().stream().noneMatch(element -> element.getType() == type)
      )
      .forEach(type -> elements.put(type, type.getDefaultElementBuilder().apply(this)));

    this.elements = elements;
    return this;
  }

  public ElementManager addEnergyElement(int x, int y, int priority, ResourceLocation texture, ResourceLocation textureFilled) {
    int width = TextureSizeHelper.getTextureWidth(texture);
    int height = TextureSizeHelper.getTextureHeight(texture);
    this.templates.add(new EnergyElement(
      new AbstractGuiElement.Properties(x, y, width, height, priority, texture, null, List.of()),
      texture,
      textureFilled
    ));
    return updateManager();
  }
}
