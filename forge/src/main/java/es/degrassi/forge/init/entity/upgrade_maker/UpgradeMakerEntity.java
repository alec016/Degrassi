package es.degrassi.forge.init.entity.upgrade_maker;

import es.degrassi.forge.init.entity.BaseEntity;
import es.degrassi.forge.init.registration.EntityRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class UpgradeMakerEntity extends BaseEntity {
  public UpgradeMakerEntity(BlockPos blockPos, BlockState blockState) {
    super(EntityRegister.UPGRADE_MAKER.get(), blockPos, blockState);
  }
}
