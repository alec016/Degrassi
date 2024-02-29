package es.degrassi.forge.api.core.component;

import es.degrassi.forge.api.core.component.variant.IComponentVariant;

public interface IVariableComponent<T extends IComponentVariant> {

    T getVariant();
}
