package es.degrassi.forge.core.common.machines.multiblock.controller.block;

import es.degrassi.forge.EnvHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MelterController extends BaseMultiblockControllerBlock {
  public MelterController(Properties props) {
    super(props);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return EnvHandler.INSTANCE.createMelterControllerEntity(pos, state, this);
  }
}
