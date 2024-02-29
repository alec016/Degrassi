package es.degrassi.forge.core.network.data;

import es.degrassi.forge.api.impl.core.network.Data;
import es.degrassi.forge.init.registration.DataRegistration;
import net.minecraft.network.FriendlyByteBuf;

public class StringData extends Data<String> {

    public StringData(short id, String value) {
        super(DataRegistration.STRING_DATA.get(), id, value);
    }

    public StringData(short id, FriendlyByteBuf buffer) {
        this(id, buffer.readUtf());
    }

    @Override
    public void writeData(FriendlyByteBuf buffer) {
        super.writeData(buffer);
        buffer.writeUtf(getValue());
    }
}
