package es.degrassi.forge.core.network.data;

import es.degrassi.forge.api.impl.core.components.config.RelativeSide;
import es.degrassi.forge.api.impl.core.components.config.SideConfig;
import es.degrassi.forge.api.impl.core.components.config.SideMode;
import es.degrassi.forge.api.impl.core.network.Data;
import es.degrassi.forge.init.registration.DataRegistration;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.network.FriendlyByteBuf;

public class SideConfigData extends Data<SideConfig> {

    public SideConfigData(Short id, SideConfig value) {
        super(DataRegistration.SIDE_CONFIG_DATA.get(), id, value);
    }

    public static SideConfigData readData(short id, FriendlyByteBuf buffer) {
        Map<RelativeSide, SideMode> map = new HashMap<>();
        for(RelativeSide side : RelativeSide.values())
            map.put(side, SideMode.values()[buffer.readByte()]);
        return new SideConfigData(id, new SideConfig(null, map, buffer.readBoolean(), buffer.readBoolean(), true));
    }

    @Override
    public void writeData(FriendlyByteBuf buffer) {
        super.writeData(buffer);
        for(RelativeSide side : RelativeSide.values())
            buffer.writeByte(getValue().getSideMode(side).ordinal());
        buffer.writeBoolean(getValue().isAutoInput());
        buffer.writeBoolean(getValue().isAutoOutput());
    }
}
