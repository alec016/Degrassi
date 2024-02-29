package es.degrassi.forge.api.core.component;

/**
 * Used to do some logic each tick of the MachineTile.
 */
public interface ITickableComponent extends IComponent {

    /**
     * Called each tick on the logical server for each component that implements this interface, for each MachineTile loaded in the world.
     */
    default void serverTick() {}

    /**
     * Called each tick on the logical client for each component that implements this interface, for each MachineTile loaded in the world.
     */
    default void clientTick() {}
}
