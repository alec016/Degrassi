package es.degrassi.common.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("rawtypes")
public interface IBlock<V extends IVariant, B extends Block & IBlock<V, B>> extends IVariantEntry<V, B>, EntityBlock {

  default BlockItem getBlockItem(Item.Properties properties) {
    return new BlockItem((Block) this, properties);
  }

  @Nullable
  @Override
  default BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return null;
  }

  @Nullable
  @Override
  default <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
    return null;
  }
}
