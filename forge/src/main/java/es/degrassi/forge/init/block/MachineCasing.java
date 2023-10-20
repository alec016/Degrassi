package es.degrassi.forge.init.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class MachineCasing extends Block {
  public MachineCasing() {
    super(
      Properties
        .of(Material.GLASS)
        .sound(SoundType.GLASS)
        .strength(6f)
        .requiresCorrectToolForDrops()
        .noOcclusion()
    );
  }
}
