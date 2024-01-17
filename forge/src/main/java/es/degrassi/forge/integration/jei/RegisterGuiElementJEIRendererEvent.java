package es.degrassi.forge.integration.jei;

import com.google.common.collect.ImmutableMap;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import es.degrassi.forge.init.gui.element.GuiElementType;
import es.degrassi.forge.init.gui.element.IGuiElement;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class RegisterGuiElementJEIRendererEvent {

  public static final Event<Register> EVENT = EventFactory.createLoop();

  private final Map<GuiElementType<?>, IJeiElementRenderer<?>> renderers = new HashMap<>();

  public <E extends IGuiElement> void register(GuiElementType<E> type, IJeiElementRenderer<E> renderer) {
    if(this.renderers.containsKey(type))
      throw new IllegalArgumentException("Jei renderer already registered for Gui Element: " + type.getId());
    this.renderers.put(type, renderer);
  }

  public Map<GuiElementType<?>, IJeiElementRenderer<?>> getRenderers() {
    return ImmutableMap.copyOf(this.renderers);
  }

  public interface Register {
    void registerRenderers(RegisterGuiElementJEIRendererEvent event);
  }
}
