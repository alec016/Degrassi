package es.degrassi.forge.integration.kubejs;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;
import es.degrassi.forge.integration.kubejs.events.DegrassiRecipesEvents;

@SuppressWarnings("unused")
public interface DegrassiEvents {
  EventGroup GROUP = EventGroup.of("DegrassiEvents");

//  EventHandler RECIPES = GROUP.server("recipes", () -> DegrassiRecipesEvents.class);

  static void register() {
    GROUP.register();
  }
}
