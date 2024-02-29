package es.degrassi.forge.api.core;

import es.degrassi.forge.init.tile.MachineTile;
import java.util.Map;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

public interface IManager<T, E> {
  Map<T, E> getMap();
  MachineTile getTile();
  Level getLevel();
  MinecraftServer getServer();
  void markDirty();
}
