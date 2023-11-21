package es.degrassi.forge.init.entity;

import es.degrassi.forge.init.entity.type.IDegrassiEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseEntity extends BlockEntity implements IDegrassiEntity {
  public BaseEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
    super(blockEntityType, blockPos, blockState);
  }
}
