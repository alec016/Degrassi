package es.degrassi.forge.core.network.syncable;

import es.degrassi.forge.api.impl.core.network.AbstractSyncable;
import es.degrassi.forge.core.network.data.ItemStackData;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.world.item.ItemStack;

public abstract class ItemStackSyncable extends AbstractSyncable<ItemStackData, ItemStack> {

    @Override
    public ItemStackData getData(short id) {
        return new ItemStackData(id, get());
    }

    @Override
    public boolean needSync() {
        ItemStack value = get();
        boolean needSync;
        if(this.lastKnownValue != null)
            needSync = !ItemStack.matches(value, this.lastKnownValue);
        else needSync = true;
        this.lastKnownValue = value.copy();
        return needSync;
    }

    public static ItemStackSyncable create(Supplier<ItemStack> supplier, Consumer<ItemStack> consumer) {
        return new ItemStackSyncable() {
            @Override
            public ItemStack get() {
                return supplier.get();
            }

            @Override
            public void set(ItemStack value) {
                consumer.accept(value);
            }
        };
    }
}
