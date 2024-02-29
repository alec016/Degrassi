package es.degrassi.forge.core.network.data;

import dev.architectury.fluid.FluidStack;
import es.degrassi.forge.api.impl.core.network.Data;
import es.degrassi.forge.init.registration.DataRegistration;
import net.minecraft.network.FriendlyByteBuf;

public class FluidStackData extends Data<FluidStack> {

    public FluidStackData(short id, FluidStack value) {
        super(DataRegistration.FLUIDSTACK_DATA.get(), id, value);
    }

    public FluidStackData(short id, FriendlyByteBuf buffer) {
        this(id, FluidStack.read(buffer));
    }

    @Override
    public void writeData(FriendlyByteBuf buffer) {
        super.writeData(buffer);
        getValue().write(buffer);
    }
}
