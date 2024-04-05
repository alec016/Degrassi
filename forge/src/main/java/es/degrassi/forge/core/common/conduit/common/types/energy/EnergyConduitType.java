package es.degrassi.forge.core.common.conduit.common.types.energy;

import es.degrassi.forge.Degrassi;
import es.degrassi.common.conduit.IClientConduitData;
import es.degrassi.common.conduit.IConduitMenuData;
import es.degrassi.common.conduit.IConduitType;
import es.degrassi.common.conduit.NodeIdentifier;
import es.degrassi.common.misc.RedstoneControl;
import es.degrassi.common.misc.Vector2i;
import es.degrassi.forge.core.common.conduit.common.init.EnderConduitTypes;
import es.degrassi.forge.core.common.conduit.common.tag.ConduitTags;
import es.degrassi.forge.core.common.conduit.common.types.SimpleConduitType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class EnergyConduitType extends SimpleConduitType<EnergyExtendedData> {
    public EnergyConduitType() {
        super(Degrassi.rl("block/conduit/energy"), new EnergyConduitTicker(), EnergyExtendedData::new,
            new IClientConduitData.Simple<>(EnderConduitTypes.ICON_TEXTURE, new Vector2i(0, 24)), IConduitMenuData.ENERGY);
    }

    @Override
    public IConduitType.ConduitConnectionData getDefaultConnection(Level level, BlockPos pos, Direction direction) {
        BlockEntity blockEntity = level.getBlockEntity(pos.relative(direction));
        if (blockEntity != null) {
            LazyOptional<IEnergyStorage> capability = blockEntity.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite());
            if (capability.isPresent()) {
                IEnergyStorage storage = capability.orElseThrow(() -> new RuntimeException("present capability was not found"));
                return new IConduitType.ConduitConnectionData(storage.canReceive(), storage.canExtract(), RedstoneControl.ALWAYS_ACTIVE);

            }
        }
        return super.getDefaultConnection(level, pos, direction);
    }

    @Override
    public <K> Optional<LazyOptional<K>> proxyCapability(Capability<K> cap, EnergyExtendedData extendedConduitData, Level level, BlockPos pos, @Nullable Direction direction, Optional<NodeIdentifier.IOState> state) {
        if (ForgeCapabilities.ENERGY == cap
            && state.map(NodeIdentifier.IOState::isExtract).orElse(true)
            && (direction == null || !level.getBlockState(pos.relative(direction)).is(ConduitTags.Blocks.ENERGY_CABLE))) {
                return Optional.of(extendedConduitData.getSelfCap().cast());

        }
        return Optional.empty();
    }

}
