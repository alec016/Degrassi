package es.degrassi.forge.core.network.data;

import es.degrassi.forge.api.impl.core.network.Data;
import es.degrassi.forge.init.registration.DataRegistration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public class ItemStackData extends Data<ItemStack> {
    public ItemStackData(short id, ItemStack value) {
        super(DataRegistration.ITEMSTACK_DATA.get(), id, value);
    }

    public ItemStackData(short id, FriendlyByteBuf buffer) {
        this(id, buffer.readItem());
    }

    @Override
    public void writeData(FriendlyByteBuf buffer) {
        super.writeData(buffer);
        buffer.writeItem(getValue());
    }
}
