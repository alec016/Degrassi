package es.degrassi.forge.core.common.machines.multiblock;

import es.degrassi.forge.core.common.machines.multiblock.controller.block.BaseMultiblockControllerBlock;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.BaseMultiblockControllerEntity;
import es.degrassi.forge.core.common.machines.multiblock.uils.StateMatcher;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;

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

  /**
   * initialize the pattern in a determined direction
   */
  void init();

  /**
   * @param blocks the pattern without rotation
   * @param rotation the degrees rotation to do
   * @return the pattern rotated
   */
  Map<BlockPos, StateMatcher> rotate(Map<BlockPos, StateMatcher> blocks, Rotation rotation);
}
