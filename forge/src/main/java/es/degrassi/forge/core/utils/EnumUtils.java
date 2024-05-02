package es.degrassi.forge.core.utils;

import es.degrassi.forge.core.common.machines.MachineStatus;
import net.minecraft.core.Direction;

public abstract class EnumUtils {
  public static final Boolean[] BOOLEAN = new Boolean[]{ true, false };
  public static final Direction[] HORIZONTAL_DIRECTION = new Direction[] { Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH };
  public static final Direction[] VERTICAL_DIRECTION = new Direction[] { Direction.UP, Direction.DOWN };
  public static final Direction[] DIRECTION = Direction.values();
  public static final MachineStatus[] MACHINE_STATUS = MachineStatus.values();
}
