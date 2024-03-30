package es.degrassi.forge.core.common.cables.energy;

import es.degrassi.forge.EnvHandler;
import static es.degrassi.forge.api.utils.Utils.addCommas;
import es.degrassi.forge.core.common.cables.CableBlock;
import es.degrassi.forge.core.tiers.CableTier;
import es.degrassi.forge.lib.client.wiki.page.panel.InfoBox;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation, unused")
public class EnergyCableBlock extends CableBlock<EnergyCableBlock> implements IEnergyItemProvider {
  public EnergyCableBlock(Properties properties, CableTier tier) {
    super(properties, tier);
  }

  public EnergyCableItem getBlockItem(Item.Properties properties) {
    return new EnergyCableItem(this, properties);
  }

  public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block block, @NotNull BlockPos fromPos, boolean isMoving) {
    var newState = createCableState(level, pos);

    if (newState != state) {
      level.setBlock(pos, newState, Block.UPDATE_CLIENTS);
    }

    if (level.getBlockEntity(pos) instanceof EnergyCableEntity cable) {
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
    if (tileEntity instanceof EnergyCableEntity cable) {
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

  public boolean canConnect(Level world, BlockPos pos, Direction direction) {
    BlockEntity tile = world.getBlockEntity(pos.relative(direction));
    return !(tile instanceof EnergyCableEntity) && EnvHandler.INSTANCE.hasEnergy(world, pos.relative(direction), direction.getOpposite());
  }

  @Override
  public void onRemove(@NotNull BlockState state, Level world, @NotNull BlockPos pos, @NotNull BlockState newState, boolean isMoving) {
    BlockEntity tile = world.getBlockEntity(pos);
    if (tile instanceof EnergyCableEntity) {
      ((EnergyCableEntity) tile).onRemoved(world, state, newState, isMoving);
    }
    super.onRemove(state, world, pos, newState, isMoving);
  }

  @Override
  public void setPlacedBy(Level world, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
    BlockEntity tile = world.getBlockEntity(pos);
    if (tile instanceof EnergyCableEntity) {
      ((EnergyCableEntity) tile).onPlaced(world, state, placer, stack);
    }
  }

  @Override
  public void playerDestroy(@NotNull Level world, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable BlockEntity te, @NotNull ItemStack stack) {
    if (te instanceof EnergyCableEntity tile) {
      ItemStack stack1 = new ItemStack(this);
      popResource(world, pos, stack1);
      player.awardStat(Stats.BLOCK_MINED.get(this));
      player.causeFoodExhaustion(0.005F);
    } else {
      super.playerDestroy(world, player, pos, state, te, stack);
    }
  }

  @Override
  public @NotNull BlockState updateShape(BlockState state, @NotNull Direction facing, @NotNull BlockState facingState, @NotNull LevelAccessor world, @NotNull BlockPos currentPos,
                                         @NotNull BlockPos facingPos) {
    if (state.getValue(WATERLOGGED))
      world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
    if (!state.canSurvive(world, currentPos)) {
      BlockEntity tileEntity = world.getBlockEntity(currentPos);
      if (!world.isClientSide() && tileEntity instanceof EnergyCableEntity tile) {
        ItemStack stack = new ItemStack(this);
        popResource((Level) world, currentPos, stack);
        world.destroyBlock(currentPos, false);
      }
    }
    return super.updateShape(state, facing, facingState, world, currentPos, facingPos);
  }

  public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader world, @NotNull BlockPos pos) {
    if (checkValidEnergySide()) {
      Direction side = state.getValue(BlockStateProperties.FACING);
      BlockPos pos1 = pos.relative(side);
      return world.getBlockState(pos1).getBlock() instanceof EnergyCableBlock ||
        world instanceof Level level && EnvHandler.INSTANCE.hasEnergy(level, pos1, side.getOpposite());
    }
    return super.canSurvive(state, world, pos);
  }

  protected boolean checkValidEnergySide() {
    return false;
  }

  public boolean isChargeable(ItemStack stack) {
    return false;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
    return EnvHandler.INSTANCE.createEnergyCable(pos, state, tier);
  }

  @Override
  public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
    return (l, p, s, be) -> ((EnergyCableEntity) be).tick();
  }

  @Override
  public InfoBox getInfoBox(ItemStack stack, InfoBox box) {
    box.set(Component.translatable("info.degrassi.max.io"),
      Component.translatable("info.degrassi.fe.per.tick", addCommas(tier.getEnergyTransfer())));
    return box;
  }
}
