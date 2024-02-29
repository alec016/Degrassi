package es.degrassi.forge.core.network.data;

import es.degrassi.forge.api.impl.core.network.Data;
import es.degrassi.forge.init.registration.DataRegistration;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class NbtData extends Data<CompoundTag> {

    public NbtData(short id, CompoundTag value) {
        super(DataRegistration.NBT_DATA.get(), id, value);
    }

    public NbtData(short id, FriendlyByteBuf buffer) {
        this(id, buffer.readNbt());
    }

    @Override
    public void writeData(FriendlyByteBuf buffer) {
        super.writeData(buffer);
        buffer.writeNbt(getValue());
    }
}
