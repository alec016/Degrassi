package es.degrassi.forge.core.common.machines.entity;

import es.degrassi.forge.core.tiers.Furnace;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class FurnaceEntity extends MachineEntity {
  private Furnace tier;
  public FurnaceEntity(BlockPos pos, BlockState blockState, Furnace tier) {
    super(tier.getType().get(), pos, blockState);
    this.getComponentManager().addEnergy(tier.getCapacity(), "energy");
    this.tier = tier;
  }

  public Furnace getTier() {
    return tier;
  }

  @Override
  public Component getName() {
    return getTier().getName();
  }
}