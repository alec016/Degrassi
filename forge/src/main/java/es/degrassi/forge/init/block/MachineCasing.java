package es.degrassi.forge.init.block;

import es.degrassi.forge.init.registration.TagRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

import java.util.Objects;

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
