package es.degrassi.forge.core.common.conduit.common.types.heat;

import es.degrassi.common.conduit.IClientConduitData;
import es.degrassi.common.conduit.IConduitMenuData;
import es.degrassi.common.conduit.NodeIdentifier;
import es.degrassi.common.misc.RedstoneControl;
import es.degrassi.common.misc.Vector2i;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.api.core.capability.IHeatStorage;
import es.degrassi.forge.core.common.capability.DegrassiCaps;
import es.degrassi.forge.core.common.conduit.common.init.EnderConduitTypes;
import es.degrassi.forge.core.common.conduit.common.tag.ConduitTags;
import es.degrassi.forge.core.common.conduit.common.types.SimpleConduitType;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

public class HeatConduitType extends SimpleConduitType<HeatExtendedData> {
    public HeatConduitType() {
        super(Degrassi.rl("block/conduit/heat"), new HeatConduitTicker(), HeatExtendedData::new,
            new IClientConduitData.Simple<>(EnderConduitTypes.ICON_TEXTURE, new Vector2i(0, 72)), IConduitMenuData.HEAT);
    }

    @Override
    public ConduitConnectionData getDefaultConnection(Level level, BlockPos pos, Direction direction) {
        BlockEntity blockEntity = level.getBlockEntity(pos.relative(direction));
        if (blockEntity != null) {
            LazyOptional<IHeatStorage> capability = blockEntity.getCapability(DegrassiCaps.HEAT, direction.getOpposite());
            if (capability.isPresent()) {
                IHeatStorage storage = capability.orElseThrow(() -> new RuntimeException("present capability was not found"));
                return new ConduitConnectionData(storage.canInsert(), storage.canExtract(), RedstoneControl.ALWAYS_ACTIVE);

            }
        }
        return super.getDefaultConnection(level, pos, direction);
    }

    @Override
    public <K> Optional<LazyOptional<K>> proxyCapability(Capability<K> cap, HeatExtendedData extendedConduitData, Level level, BlockPos pos, @Nullable Direction direction, Optional<NodeIdentifier.IOState> state) {
        if (DegrassiCaps.HEAT == cap
            && state.map(NodeIdentifier.IOState::isExtract).orElse(true)
            && (direction == null || !level.getBlockState(pos.relative(direction)).is(ConduitTags.Blocks.HEAT_CABLE))) {
                return Optional.of(extendedConduitData.getSelfCap().cast());

        }
        return Optional.empty();
    }

}
