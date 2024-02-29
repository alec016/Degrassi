package es.degrassi.forge.core.network.syncable;

import es.degrassi.forge.api.impl.core.network.AbstractSyncable;
import es.degrassi.forge.core.network.data.NbtData;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;

public abstract class NbtSyncable extends AbstractSyncable<NbtData, CompoundTag> {

    @Override
    public NbtData getData(short id) {
        return new NbtData(id, get());
    }

    @Override
    public boolean needSync() {
        CompoundTag value = get();
        boolean needSync;
        if(this.lastKnownValue != null)
            needSync = !value.equals(this.lastKnownValue);
        else needSync = true;
        this.lastKnownValue = value.copy();
        return needSync;
    }

    public static NbtSyncable create(Supplier<CompoundTag> supplier, Consumer<CompoundTag> consumer) {
        return new NbtSyncable() {
            @Override
            public CompoundTag get() {
                return supplier.get();
            }

            @Override
            public void set(CompoundTag value) {
                consumer.accept(value);
            }
        };
    }
}
