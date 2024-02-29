package es.degrassi.forge.core.network.data;

import es.degrassi.forge.api.impl.core.network.Data;
import es.degrassi.forge.init.registration.DataRegistration;
import net.minecraft.network.FriendlyByteBuf;

public class IntegerData extends Data<Integer> {

    public IntegerData(short id, int value) {
        super(DataRegistration.INTEGER_DATA.get(), id, value);
    }

    public IntegerData(short id, FriendlyByteBuf buffer) {
        this(id, buffer.readInt());
    }

    @Override
    public void writeData(FriendlyByteBuf buffer) {
        super.writeData(buffer);
        buffer.writeInt(getValue());
    }
}
