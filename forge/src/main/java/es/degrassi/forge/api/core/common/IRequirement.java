package es.degrassi.forge.api.core.common;

public interface IRequirement<R> extends IType {
  IManager<IRequirement<?>> getManager();
}
