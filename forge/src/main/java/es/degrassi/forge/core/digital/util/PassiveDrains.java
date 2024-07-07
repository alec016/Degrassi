package es.degrassi.forge.core.digital.util;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public abstract class PassiveDrains {
  public static final int CONTROLLER = 10;
  public static final int IMPORTER = 1;
  public static final int EXPORTER = 1;
  public static final int CRAFTING_PANEL = 5;
  public static final int STORAGE_PANEL = 5;
  public static final int NASS = 10;
  public static final int SSD = 1;

  public static int getTotalFromConnections(List<BlockPos> connections, Level level) {
    if (connections.isEmpty()) return 0;
    int total = 0;
    for (BlockPos connection : connections) {
      BlockState state = level.getBlockState(connection);
//      if (state.getBlock() instanceof Importer) total += IMPORTER
//      else if (state.getBlock() instanceof Exporter) total += EXPORTER
//      else if (state.getBlock() instanceof CraftingPanel) total += CRAFTING_PANEL
//      else if (state.getBlock() instanceof StoragePanel) total += STORAGE_PANEL
//      else if (state.getBlock() instanceof Nass) {
//        total += NASS;
//        NassEntity nass = (NassEntity) level.getBlockEntity(connection);
//        total += nass.getSSDs().size() * SSD;
//      }
    }
    return total;
  }
}
