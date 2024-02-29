package es.degrassi.forge.core.network.data;

import es.degrassi.forge.api.impl.core.network.Data;
import es.degrassi.forge.init.registration.DataRegistration;
import net.minecraft.network.FriendlyByteBuf;

public class LongData extends Data<Long> {

    public LongData(short id, long value) {
        super(DataRegistration.LONG_DATA.get(), id, value);
    }

    public LongData(short id, FriendlyByteBuf buffer) {
        this(id, buffer.readLong());
    }

    @Override
    public void writeData(FriendlyByteBuf buffer) {
        super.writeData(buffer);
        buffer.writeLong(getValue());
    }
}
