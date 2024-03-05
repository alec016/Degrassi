package es.degrassi.forge.api.core.common;

public interface IType {
  void markDirty();

  default void clientTick() {}
  default void serverTick() {}
}
