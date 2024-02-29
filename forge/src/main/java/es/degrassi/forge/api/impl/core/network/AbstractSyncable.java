package es.degrassi.forge.api.impl.core.network;

import es.degrassi.forge.api.core.network.IData;
import es.degrassi.forge.api.core.network.ISyncable;

/**
 * Default implementation of ISyncable.
 */
public abstract class AbstractSyncable<D extends IData<?>, T> implements ISyncable<D, T> {

    public T lastKnownValue;

    /**
     * Ensure that the data is synced only if the hold value changed.
     * @return true if the data need to be synced.
     */
    @Override
    public boolean needSync() {
        T value = get();
        boolean needSync = !value.equals(this.lastKnownValue);
        this.lastKnownValue = value;
        return needSync;
    }
}
