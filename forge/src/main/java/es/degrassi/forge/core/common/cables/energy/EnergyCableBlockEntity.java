package es.degrassi.forge.core.common.cables.energy;

import es.degrassi.forge.core.common.cables.CableType;
import es.degrassi.forge.core.common.cables.block.CableBlockEntity;
import es.degrassi.forge.core.tiers.CableTier;
import es.degrassi.forge.core.tiers.EnergyCableTier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class EnergyCableBlockEntity extends CableBlockEntity {
  public EnergyCableBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState, CableTier tier) {
    super(type, pos, blockState, tier, CableType.ENERGY);
  }

  public EnergyCableBlockEntity(BlockPos pos, BlockState blockState, CableTier tier) {
    this(tier.energy().getType(), pos, blockState, tier);
  }
}
