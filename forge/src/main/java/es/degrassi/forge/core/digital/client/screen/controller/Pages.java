package es.degrassi.forge.core.digital.client.screen.controller;

import java.util.Locale;
import org.jetbrains.annotations.NotNull;

public enum Pages {
  CONTROLLER,
  FREQUENCY_LIST,
  CREATE,
  EDIT;

  public @NotNull String getId() {
    return name().toLowerCase(Locale.ROOT);
  }

  public static @NotNull String getTitle(String page) {
    return switch (valueOf(page.toUpperCase(Locale.ROOT))) {
      case CONTROLLER -> "Controller";
      case FREQUENCY_LIST -> "Select frequency";
      case CREATE -> "Create frequency";
      case EDIT -> "Edit frequency";
    };
  }
}
