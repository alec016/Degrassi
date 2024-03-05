package es.degrassi.forge.api.core.common;

public interface IComponent<C> extends IType {

  IManager<IComponent<?>> getManager();
}
