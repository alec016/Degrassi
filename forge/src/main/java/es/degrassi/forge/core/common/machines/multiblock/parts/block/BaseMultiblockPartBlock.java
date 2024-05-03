package es.degrassi.forge.core.common.machines.multiblock.parts.block;

import es.degrassi.common.registry.IBlock;
import es.degrassi.common.registry.IVariant;
import es.degrassi.forge.core.common.machines.MachineStatus;
import es.degrassi.forge.core.common.machines.block.MachineBlock;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public abstract class BaseMultiblockPartBlock<
  V extends Enum<V> & IVariant<V>,
  B extends Block & IBlock<V, B>
> extends MachineBlock implements IBlock<V, B> {
  public static final DirectionProperty FACING = BlockStateProperties.FACING;
  private final V variant;

  public BaseMultiblockPartBlock(Properties properties, V variant) {
    super(properties);
    this.variant = variant;
  }

  @Override
  public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
    return null;
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
    builder.add(FACING).add(STATUS);
  }

  @Override
  public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
    return this.defaultBlockState().setValue(STATUS, MachineStatus.IDLE).setValue(FACING, pContext.getNearestLookingDirection().getOpposite());
  }

  @Override
  public @NotNull BlockState rotate(@NotNull BlockState pState, @NotNull Rotation pRotation) {
    return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
  }

  @SuppressWarnings("deprecation")
  @Override
  public @NotNull BlockState mirror(@NotNull BlockState pState, @NotNull Mirror pMirror) {
    return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
  }
}
