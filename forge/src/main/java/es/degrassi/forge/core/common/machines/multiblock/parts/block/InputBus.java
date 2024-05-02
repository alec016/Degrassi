package es.degrassi.forge.core.common.machines.multiblock.parts.block;

import es.degrassi.forge.EnvHandler;
import es.degrassi.forge.core.common.machines.multiblock.parts.item.InputBusItem;
import es.degrassi.forge.core.tiers.MultiblockPartStorage;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@Getter
public class InputBus extends BaseMultiblockPartBlock<MultiblockPartStorage.Item.Input, InputBus> {
  public InputBus(Properties properties, MultiblockPartStorage.Item.Input variant) {
    super(properties, variant);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return EnvHandler.INSTANCE.createInputBus(pos, state, getVariant());
  }

  public BlockItem getBlockItem(Item.Properties properties) {
    return new InputBusItem(this, new Item.Properties());
  }
}
