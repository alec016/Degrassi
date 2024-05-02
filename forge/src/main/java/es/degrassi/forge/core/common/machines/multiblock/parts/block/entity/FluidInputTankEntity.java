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
public class FluidInputTankEntity extends BaseMultiblockPartEntity<MultiblockPartStorage.Fluid.Input> {
  public FluidInputTankEntity(BlockPos pos, BlockState blockState, MultiblockPartStorage.Fluid.Input variant) {
    super(EntityRegistration.FLUID_INPUT_TANK.get(), pos, blockState, variant);

    getComponentManager().addFluid(variant.getCapacity(), "fluid_input", ComponentIOMode.INPUT);
  }

  @Override
  public Component getName() {
    return Component.translatable(getBlockState().getBlock().getDescriptionId());
  }
}
