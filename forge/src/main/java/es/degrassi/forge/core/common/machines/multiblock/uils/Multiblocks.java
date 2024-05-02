package es.degrassi.forge.core.common.machines.multiblock.uils;

import es.degrassi.forge.core.common.machines.multiblock.IMultiblockController;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.core.BlockPos;

public abstract class Multiblocks {
  private static final Map<BlockPos, IMultiblockController<?, ?>> controllers = new LinkedHashMap<>();

  public static void addController(IMultiblockController<?, ?> controller, BlockPos pos) {
    if (controllers.containsKey(pos) || controllers.containsValue(controller)) return;
    controllers.put(pos, controller);
  }

  public static void removeController(BlockPos pos) {
    controllers.remove(pos);
  }

  public static void removeController(IMultiblockController<?, ?> controller) {
    AtomicReference<BlockPos> toRemove = new AtomicReference<>(null);
    controllers.forEach((pos, con) -> {
      if (con.equals(controller)) toRemove.set(pos);
    });
    if (toRemove.get() != null) controllers.remove(toRemove.get());
  }

  public static void setController(IMultiblockController<?, ?> controller, BlockPos pos) {
    controllers.put(pos, controller);
  }

  public static IMultiblockController<?, ?> getController(BlockPos pos) {
    return controllers.get(pos);
  }

  @SuppressWarnings("unchecked")
  public static <T extends IMultiblockController<?, ?>> List<T> getControllersForType(T type) {
    return controllers.values().stream().filter(controller -> type.getClass().isInstance(controller)).map(controller -> (T) controller).toList();
  }

  @SuppressWarnings("unchecked")
  public static <T extends IMultiblockController<?, ?>> Map<BlockPos, T> getControllersMapForType(T type) {
    Map<BlockPos, T> map = new LinkedHashMap<>();
    controllers.entrySet().stream().filter(entry -> type.getClass().isInstance(entry.getValue())).forEach(entry -> map.put(entry.getKey(), (T) entry.getValue()));
    return map;
  }

  public static Map<BlockPos, IMultiblockController<?, ?>> getAllControllers() {
    return controllers;
  }

  public static void clear() {
    controllers.clear();
  }
}
