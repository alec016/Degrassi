package es.degrassi.forge.core.common.machines.multiblock.parts.block.entity;

import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.tiers.MultiblockPartStorage;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

@Getter
@Setter
public class EnergyHatchEntity extends BaseMultiblockPartEntity<MultiblockPartStorage.Energy> {
  public EnergyHatchEntity(BlockPos pos, BlockState blockState, MultiblockPartStorage.Energy variant) {
    super(EntityRegistration.ENERGY_HATCH.get(), pos, blockState, variant);

    getComponentManager().addEnergy(variant.getCapacity(), "energy");
  }

  @Override
  public Component getName() {
    return Component.translatable(getBlockState().getBlock().getDescriptionId());
  }
}
