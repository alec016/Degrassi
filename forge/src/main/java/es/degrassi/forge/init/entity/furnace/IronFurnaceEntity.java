package es.degrassi.forge.init.entity.furnace;

import es.degrassi.forge.init.registration.EntityRegister;
import es.degrassi.forge.integration.config.DegrassiConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class IronFurnaceEntity extends FurnaceEntity {
  public IronFurnaceEntity(BlockPos pos, BlockState state) {
    super(
      EntityRegister.IRON_FURNACE.get(),
      pos,
      state,
      Component.translatable(
        "block.degrassi.iron_furnace"
      ),
      DegrassiConfig.iron_furnace_capacity.get(),
      DegrassiConfig.iron_furnace_transfer.get()
    );
  }
}
