package es.degrassi.forge.core.common.machines.multiblock;

import es.degrassi.forge.core.common.machines.multiblock.controller.block.BaseMultiblockControllerBlock;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.BaseMultiblockControllerEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface IMultiblockPart {
  BaseMultiblockControllerEntity<?, ?, ?> getControllerEntity();
  void setControllerEntity(BaseMultiblockControllerEntity<?, ?, ?> entity);
  BaseMultiblockControllerBlock getControllerBlock();
  void setControllerBlock(BaseMultiblockControllerBlock block);

  /**
   * @return {@link Level} of the {@link #getControllerEntity()}
   */
  Level getControllerLevel();

  /**
   * @return {@link BlockPos} of the {@link #getControllerEntity()}
   */
  BlockPos getControllerPos();
}
