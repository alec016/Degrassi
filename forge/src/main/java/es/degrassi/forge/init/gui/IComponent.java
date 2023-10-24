package es.degrassi.forge.init.gui;

public interface IComponent {
  default void init() {}

  default void onRemoved() {}
}
