package es.degrassi.forge.core.common.machines.multiblock;

import es.degrassi.forge.core.common.machines.multiblock.controller.block.BaseMultiblockControllerBlock;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.BaseMultiblockControllerEntity;
import es.degrassi.forge.core.common.machines.multiblock.uils.StateMatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;

public interface IMultiblockController<
  B extends BaseMultiblockControllerBlock,
  E extends BaseMultiblockControllerEntity<?, ?, ?>
> {

  IMultiblockController<B, E> addToPattern(BlockPos pos, StateMatcher block);

  /**
   * @param pos {@link BlockPos} to validate
   * @param block {@link Block} to validate
   * @return True if the {@link BlockPos} is a validate position and if in that position the {@link Block} matches with the defined in {@link BaseMultiblockControllerEntity#getPattern()} and if matches in {@link BaseMultiblockControllerEntity#getLevel()}
   */
  boolean validate(BlockPos pos, StateMatcher block);

  /**
   * Validation method
   */
  void validate();
}
