package es.degrassi.forge.api.core.component;

/**
 * Used to define a custom comparator behaviour for this component.
 */
public interface IComparatorInputComponent extends IComponent {

    /**
     * @return The redstone signal power (between 0 and 15 included) that the redstone comparator will emit.
     */
    int getComparatorInput();
}
