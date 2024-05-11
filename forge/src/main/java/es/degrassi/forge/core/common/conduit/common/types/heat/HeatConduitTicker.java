package es.degrassi.forge.core.common.conduit.common.types.heat;

import dev.gigaherz.graph3.Graph;
import dev.gigaherz.graph3.Mergeable;
import es.degrassi.common.conduit.IConduitType;
import es.degrassi.common.conduit.NodeIdentifier;
import es.degrassi.common.conduit.ticker.CapabilityAwareConduitTicker;
import es.degrassi.common.misc.ColorControl;
import es.degrassi.forge.api.core.capability.IHeatStorage;
import es.degrassi.forge.core.common.capability.DegrassiCaps;
import es.degrassi.forge.core.common.conduit.common.tag.ConduitTags;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import org.apache.commons.lang3.function.TriFunction;

public class HeatConduitTicker extends CapabilityAwareConduitTicker<IHeatStorage> {

    public HeatConduitTicker() {
    }

    @Override
    public void tickGraph(IConduitType<?> type, List<NodeIdentifier<?>> loadedNodes, ServerLevel level, Graph<Mergeable.Dummy> graph, TriFunction<ServerLevel, BlockPos, ColorControl, Boolean> isRedstoneActive) {
        super.tickGraph(type, loadedNodes, level, graph, isRedstoneActive);
        for (NodeIdentifier<?> node : loadedNodes) {
            HeatExtendedData heatExtendedData = node.getExtendedConduitData().castTo(HeatExtendedData.class);
            IHeatStorage heat = heatExtendedData.getSelfCap()
                .resolve()
                .orElseThrow();
            if (heat.getHeat() == 0) {
                heatExtendedData.setHeatCapacity(500);
                continue;
            }
            double previousStored = heat.getHeat();
            for (NodeIdentifier<?> otherNode : loadedNodes) {
               for (Direction dir: Direction.values()) {
                   if (otherNode.getIOState(dir).map(NodeIdentifier.IOState::isInsert).orElse(false)) {
                       BlockEntity be = level.getBlockEntity(otherNode.getPos().relative(dir));
                       if (be == null) {
                           continue;
                       }
                       Optional<IHeatStorage> capability = be.getCapability(DegrassiCaps.HEAT, dir.getOpposite()).resolve();
                       if (capability.isPresent()) {
                           IHeatStorage insert = capability.get();
                           extractHeat(heat, List.of(insert), 0, i -> {});
                       }
                   }
               }
            }
            if (heat.getHeat() == 0) {
                if (previousStored == heat.getHeatCapacity()) {
                    heatExtendedData.setHeatCapacity(Math.min(1_000_000_000, 2 * heatExtendedData.getHeatCapacity()));
                } else if (previousStored < heatExtendedData.getHeatCapacity() / 2) {
                    heatExtendedData.setHeatCapacity(Math.max(500, heatExtendedData.getHeatCapacity() / 2));
                }
            } else if (heat.getHeat() > 0) {
                heatExtendedData.setHeatCapacity(Math.max(500, heat.getHeat()));
            }
        }
    }
        @Override
    public void tickCapabilityGraph(IConduitType<?> type, List<CapabilityConnection> inserts, List<CapabilityConnection> extracts, ServerLevel level,
        Graph<Mergeable.Dummy> graph, TriFunction<ServerLevel, BlockPos, ColorControl, Boolean> isRedstoneActive) {


        for (CapabilityConnection extract : extracts) {
            IHeatStorage extractHandler = extract.cap;

            HeatExtendedData.HeatSidedData sidedExtractData = extract.data.castTo(HeatExtendedData.class).compute(extract.direction);
            extractHeat(extractHandler, inserts.stream().map(con -> con.cap).toList(), sidedExtractData.rotatingIndex, i -> sidedExtractData.rotatingIndex = i);
        }
    }

    private void extractHeat(IHeatStorage extractHandler, List<IHeatStorage> inserts, int startingIndex, Consumer<Integer> rotationIndexSetter) {

        double availableForExtraction = extractHandler.extract(Double.MAX_VALUE, true);
        if (availableForExtraction <= 0) {
            return;
        }

        if (inserts.size() <= startingIndex) {
            startingIndex = 0;
            rotationIndexSetter.accept(0);
        }

        for (int j = startingIndex; j < startingIndex + inserts.size(); j++) {
            int insertIndex = j % inserts.size();
            IHeatStorage insert = inserts.get(insertIndex);

            double inserted = insert.receive(availableForExtraction, false);
            extractHandler.extract(inserted, false);

            if (inserted == availableForExtraction) {
                rotationIndexSetter.accept(startingIndex + (insertIndex) + 1);
                return;
            }

            availableForExtraction -= inserted;
        }
    }

    @Override
    public Capability<IHeatStorage> getCapability() {
        return DegrassiCaps.HEAT;
    }

    @Override
    public boolean canConnectTo(Level level, BlockPos conduitPos, Direction direction) {
        return super.canConnectTo(level, conduitPos, direction) && !level.getBlockState(conduitPos.relative(direction)).is(ConduitTags.Blocks.HEAT_CABLE);
    }

    public int getTickRate() {
        return 1;
    }
}
