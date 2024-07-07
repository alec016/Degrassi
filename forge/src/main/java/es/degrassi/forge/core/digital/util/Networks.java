package es.degrassi.forge.core.digital.util;

import es.degrassi.common.utils.DegrassiLogger;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.digital.network.Network;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class Networks extends SavedData {
  private static final String KEY_NETWORKS = "Networks";
  private static final String KEY_NETWORK_CONTROLLER = "NetworkController";
  private static final String KEY_NETWORK_CONNECTIONS = "NetworkConnections";
  private static final String KEY_NETWORK_FREQUENCY = "NetworkFrequency";

  private final List<Network> NETWORKS = new LinkedList<>();

  private Networks() {}
  private Networks(Level level, CompoundTag nbt) {
    /* NBT layout
    data
    ┖ Networks (list)
      ┠ NetworkController
      ┠ NetworkFrequency
      ┖ NetworkConnections (list)
     */
    ListTag networksTag = nbt.getList(KEY_NETWORKS, Tag.TAG_COMPOUND);
    for (Tag tag : networksTag) {
      CompoundTag networkTag = (CompoundTag) tag;
      ListTag connectionsTag = networkTag.getList(KEY_NETWORK_CONNECTIONS, Tag.TAG_LONG);
      long controllerLongPos = networkTag.getLong(KEY_NETWORK_CONTROLLER);
      String frequency = networkTag.getString(KEY_NETWORK_FREQUENCY);
      Network network = new Network(frequency);
      network.setController(BlockPos.of(controllerLongPos), (ServerLevel) level);
      for (Tag t : connectionsTag) {
        network.addConnection(BlockPos.of(((LongTag) t).getAsLong()));
      }
      NETWORKS.add(network);
    }

    DegrassiLogger.INSTANCE.info(NETWORKS);
  }

  public static Networks get(ServerLevel level) {
    return level.getDataStorage().computeIfAbsent(nbt -> new Networks(level, nbt), Networks::new, "degrassi_networks");
  }

  public Network getOrCreateNetwork(String frequency, Level level) {
    for (Network net : NETWORKS)
      if (net.getFrequency().equals(frequency)) return net;
    if (createNetwork(frequency, level))
      return getNetwork(frequency, level);
    return null;
  }

  public Network getNetwork(String frequency, Level level) {
    for (Network net : NETWORKS)
      if (net.getFrequency().equals(frequency)) return net;
    return null;
  }

  public boolean addNetwork(Network net, Level level) {
    if (NETWORKS.stream().anyMatch(network -> network.getFrequency().equals(net.getFrequency()))) return false;
    NETWORKS.add(net);
    return true;
  }

  public boolean removeNetwork(String frequency, Level level) {
    frequency = frequency.replaceAll(" ", "_");
    String finalFrequency = frequency;
    if (NETWORKS.stream().noneMatch(network -> network.getFrequency().equals(finalFrequency))) return false;
    final Network[] toRemove = { null };

    NETWORKS.forEach(net -> {
      if (toRemove[0] != null) return;
      if (net.getFrequency().equals(finalFrequency))
        toRemove[0] = net;
    });

    if (toRemove[0] == null) return false;

    NETWORKS.remove(toRemove[0]);
    return true;
  }

  public boolean createNetwork(String frequency, Level level) {
    frequency = frequency.replaceAll(" ", "_");
    String finalFrequency = frequency;
    if (NETWORKS.stream().anyMatch(network -> network.getFrequency().equals(finalFrequency))) return false;

    Network created = new Network(finalFrequency);
    NETWORKS.add(created);
    return true;
  }

  @Override
  public void save(File file) {
    if (isDirty()) {
      File tempFile = file.toPath().getParent().resolve(file.getName() + ".tmp").toFile();
      super.save(tempFile);
      if (file.exists() && !file.delete()) {
        Degrassi.LOGGER.error("Filed to delete {}", file.getName());
      }
      if (!tempFile.renameTo(file)) {
        Degrassi.LOGGER.error("Failed to rename {}", tempFile.getName());
      }
    }
  }

  @Override
  public boolean isDirty() {
    return true;
  }

  @Override
  public CompoundTag save(CompoundTag nbt) {
    ListTag networksTag = new ListTag();
    for (Network network : NETWORKS) {
      CompoundTag networkTag = new CompoundTag();
      if (!network.getConnections().isEmpty()) {
        List<BlockPos> connections = network.getConnections();
        ListTag connectionsTag = new ListTag();
        for (BlockPos pos :  connections) {
          long longPos = pos.asLong();
          connectionsTag.add(LongTag.valueOf(longPos));
        }
        networkTag.put(KEY_NETWORK_CONNECTIONS, connectionsTag);
      }
      networkTag.putLong(KEY_NETWORK_CONTROLLER, network.getControllerPos().asLong());
      networkTag.putString(KEY_NETWORK_FREQUENCY, network.getFrequency());
      networksTag.add(networkTag);
    }
    nbt.put(KEY_NETWORKS, networksTag);
    DegrassiLogger.INSTANCE.info("NetworksSavedData: {}", nbt);
    return nbt;
  }

  @SubscribeEvent
  public static void onLevelTick(TickEvent.LevelTickEvent event) {
    if (event.phase == TickEvent.Phase.START) {
      return;
    }

    if (event.level instanceof ServerLevel serverLevel) {
      get(serverLevel).tick(serverLevel);
    }
  }

  private void tick(ServerLevel level) {
    setDirty();
    for (Network network : NETWORKS) {
      network.passiveDrain(level);
    }
  }
}
