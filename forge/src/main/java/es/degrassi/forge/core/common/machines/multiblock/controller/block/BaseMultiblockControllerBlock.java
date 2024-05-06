package es.degrassi.forge.core.common.machines.multiblock.controller.block;

import es.degrassi.common.utils.Utils;
import es.degrassi.forge.core.common.machines.block.MachineBlock;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.BaseMultiblockControllerEntity;
import es.degrassi.forge.core.init.EntityRegistration;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@SuppressWarnings("deprecation")
public abstract class BaseMultiblockControllerBlock extends MachineBlock {
  public static final BooleanProperty VALID = BooleanProperty.create("valid");

  public BaseMultiblockControllerBlock(Properties props) {
    super(props);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
    builder.add(VALID);
  }

  @Override
  public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
    return super.getStateForPlacement(pContext).setValue(VALID, false);
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    return super.use(state, level, pos, player, hand, hit);
  }

  @Override
  public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
    return Utils.createTickerHelper(
      type, EntityRegistration.MELTER.get(),
      level.isClientSide()
        ? MachineEntity::clientTick
        : BaseMultiblockControllerEntity::serverTick
    );
  }

  @Override
  public Facing getFacing() {
    return Facing.HORIZONTAL;
  }

  @Override
  public Process getProcess() {
    return Process.YES;
  }
}
