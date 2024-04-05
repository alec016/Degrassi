package es.degrassi.forge.core.common.conduit.common.network.slot;

import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public class ResourceLocationNetworkDataSlot extends NetworkDataSlot<ResourceLocation> {
    public ResourceLocationNetworkDataSlot(Supplier<ResourceLocation> getter, Consumer<ResourceLocation> setter) {
        super(getter, setter);
    }

    @Override
    public Tag serializeValueNBT(ResourceLocation value) {
        return StringTag.valueOf(value.toString());
    }

    @Override
    protected ResourceLocation valueFromNBT(Tag nbt) {
        if (nbt instanceof StringTag stringTag) {
            return new ResourceLocation(stringTag.getAsString());
        } else {
            throw new IllegalStateException("Invalid string tag was passed over the network.");
        }
    }

    @Override
    public void toBuffer(FriendlyByteBuf buf, ResourceLocation value) {
        buf.writeResourceLocation(value);
    }

    @Override
    public ResourceLocation valueFromBuffer(FriendlyByteBuf buf) {
        try {
            return buf.readResourceLocation();
        } catch (Exception e) {
            throw new IllegalStateException("Invalid resourceLocation buffer was passed over the network.");
        }
    }
}
