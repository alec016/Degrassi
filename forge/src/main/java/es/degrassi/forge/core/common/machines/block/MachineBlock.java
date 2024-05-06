package es.degrassi.forge.core.common.machines.block;

import es.degrassi.forge.core.common.machines.MachineStatus;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public abstract class MachineBlock extends Block implements EntityBlock {
  public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
  public static final EnumProperty<MachineStatus> STATUS = EnumProperty.create("status", MachineStatus.class);
  public MachineBlock(Properties properties) {
    super(properties);
  }

  @Nullable
  @Override
  public abstract <T extends BlockEntity> BlockEntityTicker<T> getTicker(
    @NotNull Level level,
    @NotNull BlockState state,
    @NotNull BlockEntityType<T> type
  );

  @Override
  public BlockState getStateForPlacement(@NotNull BlockPlaceContext pContext) {
    BlockState state = getFacing() != Facing.NONE ? this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite()) : defaultBlockState();
    return getProcess().isNone() ? state : state.setValue(STATUS, MachineStatus.IDLE);
  }

  @SuppressWarnings("deprecation")
  @Override
  public @NotNull BlockState rotate(@NotNull BlockState pState, @NotNull Rotation pRotation) {
    return getFacing() != Facing.NONE ? pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING))) : pState;
  }

  @SuppressWarnings("deprecation")
  @Override
  public @NotNull BlockState mirror(@NotNull BlockState pState, @NotNull Mirror pMirror) {
    return getFacing() != Facing.NONE ? pState.rotate(pMirror.getRotation(pState.getValue(FACING))) : pState;
  }

  protected void setDefaultState() {
    setStateProps(state -> state);
  }

  protected Facing getFacing() {
    return Facing.NONE;
  }
  protected Process getProcess() {
    return Process.NOP;
  }

  protected void setStateProps(BaseState baseState) {
    BlockState state = this.stateDefinition.any();
    if (!getFacing().equals(Facing.NONE)) {
      state = state.setValue(FACING, Direction.NORTH);
    }

    if (!getProcess().isNone())
      state = state.setValue(STATUS, MachineStatus.IDLE);

    registerDefaultState(baseState.get(state));
  }

  public Component getDisplayName(ItemStack stack) {
    return Component.translatable(asItem().getDescriptionId(stack));
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
    if (getFacing() != Facing.NONE)
      builder.add(FACING);

    if (!getProcess().isNone())
      builder.add(STATUS);
  }

  @FunctionalInterface
  protected interface BaseState {
    BlockState get(BlockState state);
  }

  @SuppressWarnings("unused")
  public enum Facing {
    HORIZONTAL,
    ALL,
    NONE
  }

  public enum Process {
    YES,
    NOP;

    public boolean isNone() {
      return this == NOP;
    }
  }
}
