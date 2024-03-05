package es.degrassi.forge.core.common.machines.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;

public abstract class MachineBlock extends Block implements EntityBlock {
  public MachineBlock(Properties properties) {
    super(properties);
  }
}
