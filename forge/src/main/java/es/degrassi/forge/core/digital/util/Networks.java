package es.degrassi.forge.core.digital.util;

import es.degrassi.common.utils.DegrassiLogger;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.digital.block.entity.DigitalControllerEntity;
import es.degrassi.forge.core.digital.client.container.DigitalControllerContainer;
import es.degrassi.forge.core.digital.network.Network;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Getter;
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

@Getter
@Mod.EventBusSubscriber
public class Networks extends SavedData {
  private static final String KEY_NETWORKS = "Networks";
  private static final String KEY_NETWORK_CONTROLLER = "NetworkController";
  private static final String KEY_NETWORK_LEVEL = "NetworkLevel";
  private static final String KEY_NETWORK_CONNECTIONS = "NetworkConnections";
  private static final String KEY_NETWORK_FREQUENCY = "NetworkFrequency";

  public final List<Network> NETWORKS = new LinkedList<>();

  private Networks() {}
  private Networks(Level level, CompoundTag nbt) {
    if (!level.dimension().location().toString().equals(level.getServer().overworld().dimension().location().toString())) return;
    /* NBT layout
    data
    ┖ Networks (list)
      ┠ NetworkController
      ┠ NetworkFrequency
      ┠ NetworkLevel
      ┖ NetworkConnections (list)
     */
    ListTag networksTag = nbt.getList(KEY_NETWORKS, Tag.TAG_COMPOUND);
    for (Tag tag : networksTag) {
      CompoundTag networkTag = (CompoundTag) tag;
      ListTag connectionsTag = networkTag.getList(KEY_NETWORK_CONNECTIONS, Tag.TAG_LONG);
      long controllerLongPos = networkTag.getLong(KEY_NETWORK_CONTROLLER);
      String frequency = networkTag.getString(KEY_NETWORK_FREQUENCY);
      String networkLevelId = networkTag.getString(KEY_NETWORK_LEVEL);
      AtomicBoolean added = new AtomicBoolean(false);
      if (added.get()) return;
      Network network = new Network(frequency);
      network.setController(BlockPos.of(controllerLongPos));
      for (Tag t : connectionsTag) {
        network.addConnection(BlockPos.of(((LongTag) t).getAsLong()));
      }
      NETWORKS.add(network);
      BlockPos controllerPos = network.getControllerPos();
      if (controllerPos != null && level.getBlockEntity(controllerPos) instanceof DigitalControllerEntity entity) {
        entity.setCurrentNetwork(network);
      }
      added.set(true);
    }

    if (NETWORKS.isEmpty()) {
      createNetwork("testing", level.getServer().overworld());
      createNetwork("testing 2", level.getServer().overworld());
      createNetwork("testing 3", level.getServer().overworld());
    }

    DegrassiLogger.INSTANCE.info("Networks in level: {} {}", NETWORKS, level.dimension().location());

    DigitalControllerContainer.networks = NETWORKS;
  }

  public static Networks get(ServerLevel level) {
    return level.getServer().overworld().getDataStorage().computeIfAbsent(nbt -> new Networks(level.getServer().overworld(), nbt), Networks::new, "degrassi_networks");
  }

  public Network getOrCreateNetwork(String frequency, ServerLevel level) {
    level = level.getServer().overworld();
    for (Network net : get(level).NETWORKS)
      if (net.getFrequency().equals(frequency)) return net;
    if (createNetwork(frequency, level))
      return getNetwork(frequency, level);
    return null;
  }

  public static Network getNetwork(String frequency, ServerLevel level) {
    level = level.getServer().overworld();
    for (Network net : get(level).NETWORKS)
      if (net.getFrequency().equals(frequency)) return net;
    return null;
  }

  public static void addNetwork(Network net, ServerLevel level) {
    level = level.getServer().overworld();
    if (get(level).NETWORKS.stream().anyMatch(network -> network.getFrequency().equals(net.getFrequency()))) return;
    get(level).NETWORKS.add(net);
  }

  public static void removeNetwork(String frequency, ServerLevel level) {
    level = level.getServer().overworld();
    frequency = frequency.replaceAll(" ", "_");
    String finalFrequency = frequency;
    if (get(level).NETWORKS.stream().noneMatch(network -> network.getFrequency().equals(finalFrequency))) return;
    final Network[] toRemove = { null };

    get(level).NETWORKS.forEach(net -> {
      if (toRemove[0] != null) return;
      if (net.getFrequency().equals(finalFrequency))
        toRemove[0] = net;
    });

    if (toRemove[0] == null) return;

    get(level).NETWORKS.remove(toRemove[0]);
  }

  public boolean createNetwork(String frequency, ServerLevel level) {
    String finalFrequency = frequency.replaceAll(" ", "_");
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
          if (pos == null) continue;
          long longPos = pos.asLong();
          connectionsTag.add(LongTag.valueOf(longPos));
        }
        networkTag.put(KEY_NETWORK_CONNECTIONS, connectionsTag);
      }
      BlockPos controllerPos = network.getControllerPos();
      if (controllerPos != null) networkTag.putLong(KEY_NETWORK_CONTROLLER, controllerPos.asLong());
      networkTag.putString(KEY_NETWORK_FREQUENCY, network.getFrequency());
      networksTag.add(networkTag);
    }
    nbt.put(KEY_NETWORKS, networksTag);
    return nbt;
  }

  @SubscribeEvent
  public static void onLevelTick(TickEvent.LevelTickEvent event) {
    if (event.phase == TickEvent.Phase.START) {
      return;
    }

    if (event.level instanceof ServerLevel serverLevel) {
      if (serverLevel.dimension().location().toString().equals(serverLevel.getServer().overworld().dimension().location().toString()))
        tick(serverLevel);
    }
  }

  private static void tick(ServerLevel level) {
    DigitalControllerContainer.networks = get(level).NETWORKS;
    for (Network network : get(level).NETWORKS) {
      // TODO: remove unreachable controllers or duplicated controllers(keep one connection), on change frequency, remove any other frequency connection
      network.passiveDrain(level);
    }
  }
}
