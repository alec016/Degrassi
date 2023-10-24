package es.degrassi.forge.init.entity.furnace;

import es.degrassi.forge.init.registration.EntityRegister;
import es.degrassi.forge.integration.config.DegrassiConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class GoldFurnaceEntity extends FurnaceEntity {
  public GoldFurnaceEntity(BlockPos pos, BlockState state) {
    super(
      EntityRegister.GOLD_FURNACE.get(),
      pos,
      state,
      Component.translatable(
        "block.degrassi.gold_furnace"
      ),
      DegrassiConfig.gold_furnace_capacity.get(),
      DegrassiConfig.gold_furnace_transfer.get()
    );
  }
}
