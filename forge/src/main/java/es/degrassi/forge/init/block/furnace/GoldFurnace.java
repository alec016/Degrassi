package es.degrassi.forge.init.block.furnace;

import es.degrassi.comon.init.block.furnace.FurnaceBlock;
import es.degrassi.forge.init.entity.furnace.GoldFurnaceEntity;
import es.degrassi.forge.init.entity.furnace.IronFurnaceEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class GoldFurnace extends FurnaceBlock {
  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new GoldFurnaceEntity(pos, state);
  }
}
