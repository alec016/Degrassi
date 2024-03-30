package es.degrassi.forge.core.common.cables.energy;

import es.degrassi.forge.core.common.cables.CableNet;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class EnergyCableNet extends CableNet<EnergyCableEntity> {
  protected static final Map<Level, Map<BlockPos, EnergyCableEntity>> loadedCables = new WeakHashMap<>();

  static void addCable(EnergyCableEntity cable) {
    var previousCable = loadedCables.computeIfAbsent(cable.getLevel(), l -> new HashMap<>()).put(cable.getBlockPos().immutable(), cable);
    if (previousCable != null)
      throw new RuntimeException("Cable added to position %s, but there was already one there?".formatted(cable.getBlockPos()));

    updateAdjacentCables(cable);
  }

  static void removeCable(EnergyCableEntity cable) {
    var levelMap = loadedCables.get(cable.getLevel());
    if (levelMap.remove(cable.getBlockPos()) != cable)
      throw new RuntimeException("Removed wrong cable from position %s".formatted(cable.getBlockPos()));
    if (levelMap.isEmpty())
      loadedCables.remove(cable.getLevel());

    updateAdjacentCables(cable);
  }

  static void updateAdjacentCables(EnergyCableEntity cable) {
    var levelMap = loadedCables.get(cable.getLevel());
    if (levelMap == null) return;

    for (var direction : Direction.values()) {
      var adjPos = cable.getBlockPos().relative(direction);
      var adjCable = levelMap.get(adjPos);
      if (adjCable != null && adjCable.net != null)
        adjCable.net.cableList.forEach(c -> c.net = null);
    }
  }

  static void calculateNetwork(EnergyCableEntity cable) {
    var levelMap = Objects.requireNonNull(loadedCables.get(cable.getLevel()), "No level map");

    var cables = new LinkedHashSet<EnergyCableEntity>();
    var queue = new ArrayDeque<EnergyCableEntity>();
    cables.add(cable);
    queue.add(cable);
    while (!queue.isEmpty()) {
      var cur = queue.pop();

      for (var direction : Direction.values()) {
        var adjPos = cur.getBlockPos().relative(direction);
        var adjCable = levelMap.get(adjPos);

        if (adjCable != null && cables.add(adjCable))
          queue.add(adjCable);
      }

      var insertionGuard = new MutableBoolean();
      var net = new EnergyCableNet(new ArrayList<>(cables));
      for (var tile : net.cableList) {
        tile.net = net;
        tile.netInsertionGuard = insertionGuard;
      }
    }
  }

  EnergyCableNet(List<EnergyCableEntity> cableList) {
    super(cableList);
  }
}
