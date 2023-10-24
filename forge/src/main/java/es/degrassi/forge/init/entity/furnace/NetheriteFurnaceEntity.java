package es.degrassi.forge.init.entity.furnace;

import es.degrassi.forge.init.registration.EntityRegister;
import es.degrassi.forge.integration.config.DegrassiConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class NetheriteFurnaceEntity extends FurnaceEntity {
  public NetheriteFurnaceEntity(BlockPos pos, BlockState state) {
    super(
      EntityRegister.NETHERITE_FURNACE.get(),
      pos,
      state,
      Component.translatable(
        "block.degrassi.netherite_furnace"
      ),
      DegrassiConfig.netherite_furnace_capacity.get(),
      DegrassiConfig.netherite_furnace_transfer.get()
    );
  }
}
