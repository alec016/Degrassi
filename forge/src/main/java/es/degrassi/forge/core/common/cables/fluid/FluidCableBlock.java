package es.degrassi.forge.core.common.cables.fluid;

import es.degrassi.forge.EnvHandler;
import static es.degrassi.forge.api.utils.Utils.addCommas;
import es.degrassi.forge.core.common.cables.CableBlock;
import es.degrassi.forge.core.tiers.CableTier;
import es.degrassi.forge.lib.client.wiki.page.panel.InfoBox;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation, unused")
public class FluidCableBlock extends CableBlock<FluidCableBlock> implements IFluidItemProvider {
  public FluidCableBlock(Properties properties, CableTier tier) {
    super(properties, tier);
  }

  public FluidCableItem getBlockItem(Item.Properties properties) {
    return new FluidCableItem(this, properties);
  }

  public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block block, @NotNull BlockPos fromPos, boolean isMoving) {
    var newState = createCableState(level, pos);

    if (newState != state) {
      level.setBlock(pos, newState, Block.UPDATE_CLIENTS);
    }

    if (level.getBlockEntity(pos) instanceof FluidCableEntity cable) {
      var oldSides = EnumSet.copyOf(cable.sides);

      cable.sides.clear();
      for (Direction direction : Direction.values()) {
        if (canConnect(level, pos, direction)) {
          cable.sides.add(direction);
        }
      }

      if (!oldSides.equals(cable.sides)) {
        cable.sync();
      }
    }

    super.neighborChanged(state, level, pos, block, fromPos, isMoving);
  }

  public void onPlace(@NotNull BlockState state, Level world, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean isMoving) {
    BlockEntity tileEntity = world.getBlockEntity(pos);
    if (tileEntity instanceof FluidCableEntity cable) {
      cable.onAdded(world, state, oldState, isMoving);
      cable.sides.clear();
      for (Direction direction : Direction.values()) {
        if (canConnect(world, pos, direction)) {
          cable.sides.add(direction);
        }
      }
      cable.sync();
    }
    super.onPlace(state, world, pos, oldState, isMoving);
  }

  @Override
  public boolean canConnect(Level world, BlockPos pos, Direction direction) {
    BlockEntity tile = world.getBlockEntity(pos.relative(direction));
    return !(tile instanceof FluidCableEntity) && EnvHandler.INSTANCE.hasFluid(world, pos.relative(direction), direction.getOpposite());
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
    return EnvHandler.INSTANCE.createFluidCable(pos, state, tier);
  }

  @Override
  public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
    return (l, p, s, be) -> ((FluidCableEntity) be).tick();
  }

  @Override
  public InfoBox getInfoBox(ItemStack stack, InfoBox box) {
    box.set(Component.translatable("info.degrassi.max.io"),
      Component.translatable("info.degrassi.mb.per.tick", addCommas(tier.getFluidTransfer())));
    return box;
  }

  @Override
  public boolean isTransferable(ItemStack stack) {
    return false;
  }
}
