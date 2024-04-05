package es.degrassi.forge.core.common.conduit.common.blockentity.connection;

public sealed interface IConnectionState permits StaticConnectionStates, DynamicConnectionState {

    boolean isConnection();
}
