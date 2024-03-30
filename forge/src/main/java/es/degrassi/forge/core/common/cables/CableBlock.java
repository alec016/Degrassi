package es.degrassi.forge.core.common.cables;

import es.degrassi.forge.core.common.cables.energy.EnergyCableEntity;
import es.degrassi.forge.core.common.cables.fluid.FluidCableEntity;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.common.component.FluidComponent;
import es.degrassi.forge.core.common.machines.block.MachineBlock;
import es.degrassi.forge.core.tiers.CableTier;
import es.degrassi.common.registry.IVariantEntry;
import es.degrassi.forge.lib.client.wiki.page.panel.InfoBox;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.LIT;
import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class CableBlock<T extends CableBlock<T>> extends MachineBlock implements SimpleWaterloggedBlock, IVariantEntry<CableTier, T>, InfoBox.IInfoBoxHolder, IBlock<CableTier, T> {
  public static final BooleanProperty NORTH = PipeBlock.NORTH;
  public static final BooleanProperty EAST = PipeBlock.EAST;
  public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
  public static final BooleanProperty WEST = PipeBlock.WEST;
  public static final BooleanProperty UP = PipeBlock.UP;
  public static final BooleanProperty DOWN = PipeBlock.DOWN;
  private static final VoxelShape CABLE = box(6.25, 6.25, 6.25, 9.75, 9.75, 9.75);
  private static final VoxelShape[] MULTIPART = new VoxelShape[] { box(6.5, 6.5, 0, 9.5, 9.5, 7), box(9.5, 6.5, 6.5, 16, 9.5, 9.5),
    box(6.5, 6.5, 9.5, 9.5, 9.5, 16), box(0, 6.5, 6.5, 6.5, 9.5, 9.5), box(6.5, 9.5, 6.5, 9.5, 16, 9.5), box(6.5, 0, 6.5, 9.5, 7, 9.5) };

  protected final CableTier tier;
  protected final Map<Direction, VoxelShape> shapes = new HashMap<>();
  
  public CableBlock(Properties properties, CableTier tier) {
    super(properties);
    this.tier = tier;
    this.shapes.put(Direction.UP, Shapes.block());
    this.shapes.put(Direction.DOWN, Shapes.block());
    this.shapes.put(Direction.NORTH, Shapes.block());
    this.shapes.put(Direction.SOUTH, Shapes.block());
    this.shapes.put(Direction.EAST, Shapes.block());
    this.shapes.put(Direction.WEST, Shapes.block());
  }

  public static @NotNull VoxelShape box(double x1, double y1, double z1, double x2, double y2, double z2) {
    return Block.box(Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2), Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2));
  }

  @Override
  public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter blockGetter, @NotNull BlockPos pos, @NotNull CollisionContext context) {
    VoxelShape voxelShape = CABLE;
    if (blockGetter instanceof Level world) {
      if (state.getValue(NORTH) || canConnect(world, pos, Direction.NORTH))
        voxelShape = Shapes.or(voxelShape, MULTIPART[0]);
      if (state.getValue(EAST) || canConnect(world, pos, Direction.EAST))
        voxelShape = Shapes.or(voxelShape, MULTIPART[1]);
      if (state.getValue(SOUTH) || canConnect(world, pos, Direction.SOUTH))
        voxelShape = Shapes.or(voxelShape, MULTIPART[2]);
      if (state.getValue(WEST) || canConnect(world, pos, Direction.WEST))
        voxelShape = Shapes.or(voxelShape, MULTIPART[3]);
      if (state.getValue(UP) || canConnect(world, pos, Direction.UP))
        voxelShape = Shapes.or(voxelShape, MULTIPART[4]);
      if (state.getValue(DOWN) || canConnect(world, pos, Direction.DOWN))
        voxelShape = Shapes.or(voxelShape, MULTIPART[5]);
    }
    return voxelShape;
  }

  public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
    return createCableState(context.getLevel(), context.getClickedPos());
  }

  protected BlockState createCableState(Level world, BlockPos pos) {
    final BlockState state = defaultBlockState();
    boolean[] north = canAttach(state, world, pos, Direction.NORTH);
    boolean[] south = canAttach(state, world, pos, Direction.SOUTH);
    boolean[] west = canAttach(state, world, pos, Direction.WEST);
    boolean[] east = canAttach(state, world, pos, Direction.EAST);
    boolean[] up = canAttach(state, world, pos, Direction.UP);
    boolean[] down = canAttach(state, world, pos, Direction.DOWN);
    FluidState fluidState = world.getFluidState(pos);
    return state.setValue(NORTH, north[0] && !north[1]).setValue(SOUTH, south[0] && !south[1]).setValue(WEST, west[0] && !west[1])
      .setValue(EAST, east[0] && !east[1]).setValue(UP, up[0] && !up[1]).setValue(DOWN, down[0] && !down[1])
      .setValue(BlockStateProperties.WATERLOGGED, fluidState.getType() == Fluids.WATER);
  }

  public boolean[] canAttach(BlockState state, Level world, BlockPos pos, Direction direction) {
    return new boolean[] { world.getBlockState(pos.relative(direction)).getBlock() == this || canConnect(world, pos, direction),
      canConnect(world, pos, direction) };
  }

  public Component getDisplayName(ItemStack stack) {
    return Component.translatable(asItem().getDescriptionId(stack));
  }

  public CableTier getTier() {
    return getVariant();
  }

  @Override
  public boolean useShapeForLightOcclusion(BlockState state) {
    return !state.canOcclude();
  }

  protected void setDefaultState() {
    setStateProps(state -> state);
  }

  protected void setStateProps(BaseState baseState) {
    BlockState state = this.stateDefinition.any();
    state = state.setValue(WATERLOGGED, false);
    if (!getFacing().equals(Facing.NONE)) {
      state = state.setValue(FACING, Direction.NORTH);
    }
    if (hasLitProp()) {
      state = state.setValue(LIT, false);
    }
    registerDefaultState(baseState.get(state));
  }

  protected boolean isPlacerFacing() {
    return false;
  }

  protected boolean hasLitProp() {
    return false;
  }

  @Override
  public boolean propagatesSkylightDown(@NotNull BlockState state, @NotNull BlockGetter reader, @NotNull BlockPos pos) {
    return getFluidState(state).isEmpty() || super.propagatesSkylightDown(state, reader, pos);
  }

  @Nullable
  private BlockState facing(BlockPlaceContext context, boolean b) {
    BlockState blockstate = this.defaultBlockState();
    for (Direction direction : context.getNearestLookingDirections()) {
      if (b || direction.getAxis().isHorizontal()) {
        blockstate = blockstate.setValue(FACING, b ? direction : direction.getOpposite());
        if (blockstate.canSurvive(context.getLevel(), context.getClickedPos())) {
          return blockstate;
        }
      }
    }
    return null;
  }

  @Override
  public @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rot) {
    if (getFacing().equals(Facing.ALL) || getFacing().equals(Facing.HORIZONTAL)) {
      return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }
    return super.rotate(state, rot);
  }

  @Override
  public @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
    if (getFacing().equals(Facing.ALL) || getFacing().equals(Facing.HORIZONTAL)) {
      return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
    return super.mirror(state, mirror);
  }

  @Override
  public @NotNull FluidState getFluidState(BlockState state) {
    return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
  }

  @Override
  public boolean triggerEvent(@NotNull BlockState state, Level world, @NotNull BlockPos pos, int id, int param) {
    BlockEntity tileEntity = world.getBlockEntity(pos);
    return tileEntity != null && tileEntity.triggerEvent(id, param);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
    builder.add(NORTH, EAST, SOUTH, WEST, UP, DOWN);
    if (getFacing().equals(Facing.ALL) || getFacing().equals(Facing.HORIZONTAL))
      builder.add(FACING);
    builder.add(WATERLOGGED);
    if (hasLitProp())
      builder.add(LIT);
  }

  public boolean hasAnalogOutputSignal(@NotNull BlockState state) {
    return true;
  }

  public int getAnalogOutputSignal(@NotNull BlockState state, Level world, @NotNull BlockPos pos) {
    BlockEntity tile = world.getBlockEntity(pos);
    if (tile instanceof EnergyCableEntity entity) {
      return entity.getComponentManager().getComponent("energy").map(comp -> (EnergyComponent) comp).map(EnergyComponent::toComparatorPower).orElse(0);
    } else if (tile instanceof FluidCableEntity entity) {
      return entity.getComponentManager().getComponent("fluid").map(comp -> (FluidComponent) comp).map(FluidComponent::toComparatorPower).orElse(0);
    }
    return super.getAnalogOutputSignal(state, world, pos);
  }

  public Transfer getTransferType() {
    return Transfer.ALL;
  }

  @Override
  public CableTier getVariant() {
    return tier;
  }

  public static Optional<Direction> getHitSide(Vec3 hit, BlockPos pos) {
    double x = hit.x - pos.getX();
    double y = hit.y - pos.getY();
    double z = hit.z - pos.getZ();
    if (x > 0.0D && x < 0.4D)
      return Optional.of(Direction.WEST);
    else if (x > 0.6D && x < 1.0D)
      return Optional.of(Direction.EAST);
    else if (z > 0.0D && z < 0.4D)
      return Optional.of(Direction.NORTH);
    else if (z > 0.6D && z < 1.0D)
      return Optional.of(Direction.SOUTH);
    else if (y > 0.6D && y < 1.0D)
      return Optional.of(Direction.UP);
    else if (y > 0.0D && y < 0.4D)
      return Optional.of(Direction.DOWN);
    return Optional.empty();
  }

  public abstract boolean canConnect(Level world, BlockPos pos, Direction direction);
}
