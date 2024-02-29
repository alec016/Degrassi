package es.degrassi.forge.api.core.component.variant;

import es.degrassi.forge.api.core.component.IComponent;

public interface ITickableComponentVariant<T extends IComponent> {

    void tick(T component);
}
