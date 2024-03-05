package es.degrassi.forge.core.common.machines.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public abstract class MachineItem extends BlockItem {
  public MachineItem(Block block, Properties properties) {
    super(block, properties);
  }
}
