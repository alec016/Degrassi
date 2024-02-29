package es.degrassi.forge.core.network.data;

import es.degrassi.forge.api.impl.core.network.Data;
import es.degrassi.forge.init.registration.DataRegistration;
import net.minecraft.network.FriendlyByteBuf;

public class DoubleData extends Data<Double> {

    public DoubleData(short id, double value) {
        super(DataRegistration.DOUBLE_DATA.get(), id, value);
    }

    public DoubleData(short id, FriendlyByteBuf buffer) {
        this(id, buffer.readDouble());
    }

    @Override
    public void writeData(FriendlyByteBuf buffer) {
        super.writeData(buffer);
        buffer.writeDouble(getValue());
    }
}
