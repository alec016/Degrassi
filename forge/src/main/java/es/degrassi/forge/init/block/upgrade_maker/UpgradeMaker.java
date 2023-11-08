package es.degrassi.forge.init.block.upgrade_maker;

import es.degrassi.forge.init.block.BaseBlock;
import es.degrassi.forge.init.entity.upgrade_maker.UpgradeMakerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class UpgradeMaker extends BaseBlock {
  public UpgradeMaker() {
    super(
      Properties
        .of(Material.STONE)
        .strength(6f)
        .requiresCorrectToolForDrops()
        .noOcclusion()
    );
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
    return new UpgradeMakerEntity(pos, state);
  }
}
