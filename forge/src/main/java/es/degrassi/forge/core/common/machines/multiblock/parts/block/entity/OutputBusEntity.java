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
public class OutputBusEntity extends BaseMultiblockPartEntity<MultiblockPartStorage.Item.Output> {
  public OutputBusEntity(BlockPos pos, BlockState blockState, MultiblockPartStorage.Item.Output variant) {
    super(EntityRegistration.OUTPUT_BUS.get(), pos, blockState, variant);

    for (int i = 0; i < variant.getRows(); i++)
      for (int j = 0; j < variant.getCols(); j++) {
        getComponentManager().addItem("output_bus_" + i + "_" + j, ComponentIOMode.OUTPUT);
      }
  }

  @Override
  public Component getName() {
    return Component.translatable(getBlockState().getBlock().getDescriptionId());
  }
}
