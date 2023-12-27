package es.degrassi.forge.init.block.generators;

import es.degrassi.forge.init.block.BaseBlock;
import es.degrassi.forge.init.entity.generators.GeneratorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public abstract class GeneratorBlock extends BaseBlock {
  public GeneratorBlock(Properties arg) {
    super(arg);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pNewState, boolean pIsMoving) {
    if (pState.getBlock() != pNewState.getBlock()) {
      BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
      if (blockEntity instanceof GeneratorEntity<?, ?, ?> entity) {
        entity.drops();
      }
    }
    super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
  }
}
