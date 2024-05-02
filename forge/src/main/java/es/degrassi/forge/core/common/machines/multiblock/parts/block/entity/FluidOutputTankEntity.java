package es.degrassi.forge.core.common.machines.multiblock.parts.block.entity;

import es.degrassi.forge.core.common.component.ComponentIOMode;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.tiers.MultiblockPartStorage;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

@Getter
@Setter
public class FluidOutputTankEntity extends BaseMultiblockPartEntity<MultiblockPartStorage.Fluid.Output> {
  public FluidOutputTankEntity(BlockPos pos, BlockState blockState, MultiblockPartStorage.Fluid.Output variant) {
    super(EntityRegistration.FLUID_OUTPUT_TANK.get(), pos, blockState, variant);

    getComponentManager().addFluid(variant.getCapacity(), "fluid_output", ComponentIOMode.OUTPUT);
  }

  @Override
  public Component getName() {
    return Component.translatable(getBlockState().getBlock().getDescriptionId());
  }
}
