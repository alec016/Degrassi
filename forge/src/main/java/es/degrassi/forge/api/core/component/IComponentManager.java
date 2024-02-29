package es.degrassi.forge.api.core.component;

import es.degrassi.forge.api.core.component.handler.IComponentHandler;
import es.degrassi.forge.api.core.network.ISyncableStuff;
import es.degrassi.forge.api.core.IManager;
import java.util.List;
import java.util.Optional;

public interface IComponentManager extends IManager<ComponentType<?>, IComponent> {
  List<ISerializableComponent> getSerializableComponents();
  List<ITickableComponent> getTickableComponents();
  List<ISyncableStuff> getSyncableComponents();
  List<IComparatorInputComponent> getComparatorInputComponents();
  List<IDumpComponent> getDumpComponents();
  <T extends IComponent> Optional<T> getComponent(ComponentType<T> type);
  <T extends IComponent> Optional<IComponentHandler<T>> getComponentHandler(ComponentType<T> type);
}
