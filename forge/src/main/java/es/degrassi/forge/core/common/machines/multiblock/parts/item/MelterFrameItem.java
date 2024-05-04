package es.degrassi.forge.core.common.machines.multiblock.parts.item;

import es.degrassi.forge.core.common.machines.multiblock.parts.block.MelterFrame;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class MelterFrameItem extends BlockItem {
  public MelterFrameItem(Block block, Properties properties) {
    super(block, properties);
  }

  @Override
  public MelterFrame getBlock() {
    return (MelterFrame) super.getBlock();
  }
}
