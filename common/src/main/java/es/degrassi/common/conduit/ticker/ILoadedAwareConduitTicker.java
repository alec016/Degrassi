package es.degrassi.common.conduit.ticker;

import dev.gigaherz.graph3.Graph;
import dev.gigaherz.graph3.GraphObject;
import dev.gigaherz.graph3.Mergeable;
import es.degrassi.common.conduit.IConduitType;
import es.degrassi.common.conduit.NodeIdentifier;
import es.degrassi.common.misc.ColorControl;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.function.TriFunction;

public interface ILoadedAwareConduitTicker extends IConduitTicker {

  @Override
  default void tickGraph(IConduitType<?> type, Graph<Mergeable.Dummy> graph, ServerLevel level, TriFunction<ServerLevel, BlockPos, ColorControl, Boolean> isRedstoneActive) {
    List<NodeIdentifier<?>> nodeIdentifiers = new ArrayList<>();
    for (GraphObject<Mergeable.Dummy> object : graph.getObjects()) {
      if (object instanceof NodeIdentifier<?> node && isLoaded(level, node.getPos())) {
        nodeIdentifiers.add(node);
      }
    }
    tickGraph(type, nodeIdentifiers, level, graph, isRedstoneActive);
  }

  void tickGraph(IConduitType<?> type, List<NodeIdentifier<?>> loadedNodes, ServerLevel level, Graph<Mergeable.Dummy> graph, TriFunction<ServerLevel, BlockPos, ColorControl, Boolean> isRedstoneActive);

  default boolean isLoaded(Level level, BlockPos pos) {
    return level.isLoaded(pos) && level.shouldTickBlocksAt(pos);
  }
}
