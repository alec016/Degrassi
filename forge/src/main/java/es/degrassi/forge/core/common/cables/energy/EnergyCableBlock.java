package es.degrassi.forge.core.common.cables.energy;

import es.degrassi.forge.core.common.cables.CableType;
import es.degrassi.forge.core.common.cables.block.CableBlock;
import es.degrassi.forge.core.common.cables.block.CableBlockEntity;
import es.degrassi.forge.core.tiers.CableTier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class EnergyCableBlock extends CableBlock {
  public EnergyCableBlock(CableTier tier) {
    super(tier);
    setCableType(CableType.ENERGY);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new EnergyCableBlockEntity(pos, state, tier);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
    if (level.isClientSide()) return null;
    return (lvl, pos, st, be) -> {
      if (be instanceof CableBlockEntity cable) {
        cable.serverTick();
      }
    };
  }
}
