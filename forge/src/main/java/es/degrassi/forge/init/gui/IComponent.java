package es.degrassi.forge.init.gui;

@SuppressWarnings("unused")
public interface IComponent {
  default void init() {}

  default void onRemoved() {}
}
