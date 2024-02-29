package es.degrassi.forge.core.network.data;

import es.degrassi.forge.api.impl.core.network.Data;
import es.degrassi.forge.init.registration.DataRegistration;
import net.minecraft.network.FriendlyByteBuf;

public class BooleanData extends Data<Boolean> {

    public BooleanData(short id, boolean value) {
        super(DataRegistration.BOOLEAN_DATA.get(), id, value);
    }

    public BooleanData(short id, FriendlyByteBuf buffer) {
        this(id, buffer.readBoolean());
    }

    @Override
    public void writeData(FriendlyByteBuf buffer) {
        super.writeData(buffer);
        buffer.writeBoolean(getValue());
    }
}
