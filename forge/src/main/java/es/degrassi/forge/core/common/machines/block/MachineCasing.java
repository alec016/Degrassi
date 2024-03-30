package es.degrassi.forge.core.common.machines.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;

public class MachineCasing extends Block {
  public MachineCasing() {
    super(
      Properties
        .copy(Blocks.GLASS)
        .sound(SoundType.GLASS)
        .strength(6f)
        .requiresCorrectToolForDrops()
        .noOcclusion()
    );
  }
}
