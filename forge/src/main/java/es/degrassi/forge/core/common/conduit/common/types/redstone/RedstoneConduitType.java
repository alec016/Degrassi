package es.degrassi.forge.core.common.conduit.common.types.redstone;

import es.degrassi.forge.Degrassi;
import es.degrassi.common.conduit.IConduitMenuData;
import es.degrassi.common.misc.Vector2i;
import es.degrassi.forge.core.common.conduit.common.init.EnderConduitTypes;
import es.degrassi.forge.core.common.conduit.common.types.SimpleConduitType;
import net.minecraft.resources.ResourceLocation;

public class RedstoneConduitType extends SimpleConduitType<RedstoneExtendedData> {

    private static final ResourceLocation ACTIVE = Degrassi.rl("block/conduit/redstone_active");
    private static final ResourceLocation INACTIVE = Degrassi.rl("block/conduit/redstone");

    public RedstoneConduitType() {
        super(INACTIVE, new RedstoneConduitTicker(), RedstoneExtendedData::new, EnderConduitTypes.ICON_TEXTURE, Vector2i.ZERO, IConduitMenuData.REDSTONE);
    }

    @Override
    public ResourceLocation getTexture(RedstoneExtendedData extendedData) {
        return extendedData.isActive() ? ACTIVE : INACTIVE;
    }
}
