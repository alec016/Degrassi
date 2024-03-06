package es.degrassi.forge.api.core.common;

public interface IComponent extends IType {

  IManager<IComponent> getManager();
}
