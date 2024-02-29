package es.degrassi.forge.api.core.component;

import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.impl.codec.RegistrarCodec;

public interface IComponentTemplate<T extends IComponent> {
  NamedCodec<IComponentTemplate<? extends IComponent>> CODEC = RegistrarCodec.COMPONENT.dispatch(
    IComponentTemplate::getType,
    ComponentType::getCodec,
    "Component"
  );
  ComponentType<T> getType();
  String getId();
  boolean canAccept(Object ingredient, boolean isInput, IComponentManager manager);
  T build(IComponentManager manager);
}
