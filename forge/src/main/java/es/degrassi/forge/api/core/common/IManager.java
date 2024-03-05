package es.degrassi.forge.api.core.common;

import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import java.util.List;

public interface IManager<T extends IType> {
  List<T> get();
  void add(T value);

  MachineEntity getEntity();

  void clientTick();
  void serverTick();

  default void markDirty() {
    get().forEach(IType::markDirty);
  }
}
