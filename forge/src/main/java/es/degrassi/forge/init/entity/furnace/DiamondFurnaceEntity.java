package es.degrassi.forge.init.entity.furnace;

import es.degrassi.forge.init.registration.EntityRegister;
import es.degrassi.forge.integration.config.DegrassiConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class DiamondFurnaceEntity extends FurnaceEntity {
  public DiamondFurnaceEntity(BlockPos pos, BlockState state) {
    super(
      EntityRegister.DIAMOND_FURNACE.get(),
      pos,
      state,
      Component.translatable(
        "block.degrassi.diamond_furnace"
      ),
      DegrassiConfig.diamond_furnace_capacity.get(),
      DegrassiConfig.diamond_furnace_transfer.get()
    );
  }
}
