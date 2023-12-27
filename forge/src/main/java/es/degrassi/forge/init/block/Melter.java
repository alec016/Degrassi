package es.degrassi.forge.init.block;

import dev.architectury.registry.menu.MenuRegistry;
import es.degrassi.forge.init.entity.MelterEntity;
import es.degrassi.forge.init.gui.container.MelterContainer;
import es.degrassi.forge.init.registration.EntityRegister;
import es.degrassi.forge.network.EnergyPacket;
import es.degrassi.forge.network.FluidPacket;
import es.degrassi.forge.network.ProgressPacket;
import es.degrassi.forge.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Melter extends BaseBlock {
  public Melter() {
    super(
      BlockBehaviour.Properties
        .of(Material.STONE)
        .strength(6f)
        .requiresCorrectToolForDrops()
        .noOcclusion()
    );
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
    MelterEntity entity = new MelterEntity(pos, state);
    entity.delegate = this;
    return entity;
  }

  @SuppressWarnings("deprecation")
  @Override
  public @NotNull InteractionResult use(
    @NotNull BlockState state,
    @NotNull Level level,
    @NotNull BlockPos pos,
    @NotNull Player player,
    @NotNull InteractionHand hand,
    @NotNull BlockHitResult hit
  ) {
    BlockEntity tile = level.getBlockEntity(pos);
    if (tile instanceof MelterEntity entity) {
      if (!level.isClientSide()) {
        MenuRegistry.openExtendedMenu((ServerPlayer) player, new MenuProvider() {
          @Override
          public @NotNull Component getDisplayName() {
            return entity.getName();
          }

          @Override
          public AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player) {
            new EnergyPacket(entity.getEnergyStorage().getEnergyStored(), entity.getEnergyStorage().getMaxEnergyStored(), entity.getEnergyStorage().getMaxEnergyStored(), pos);
            new ProgressPacket(entity.getProgressStorage().getProgress(), entity.getProgressStorage().getMaxProgress(), pos);
            new FluidPacket(entity.getFluidStorage().getFluid(), pos);
            return new MelterContainer(id, inv, entity);
          }
        }, buf -> buf.writeBlockPos(pos));
        return InteractionResult.SUCCESS;
      }
      return InteractionResult.SUCCESS;
    }
    return super.use(state, level, pos, player, hand, hit);
  }

  @Override
  public float getFriction(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
    return this.getFriction();
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
    @NotNull Level level,
    @NotNull BlockState state,
    @NotNull BlockEntityType<T> type
  ) {
    return Utils.createTickerHelper(type, EntityRegister.MELTER.get(), MelterEntity::tick);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pNewState, boolean pIsMoving) {
    if (pState.getBlock() != pNewState.getBlock()) {
      BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
      if (blockEntity instanceof MelterEntity entity) {
        entity.drops();
      }
    }
    super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
  }


  private static final VoxelShape SHAPE = createVoxelShape();

  @SuppressWarnings("deprecation")
  @Override
  public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull CollisionContext collisionContext) {
    return SHAPE;
  }

  @SuppressWarnings("deprecation")
  @Override
  public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
    return RenderShape.MODEL;
  }

  private static @NotNull VoxelShape createVoxelShape() {
    VoxelShape shape = Shapes.empty();

    shape = Shapes.join(shape, Shapes.box(0, 0, 0, 1, 0.0625, 1), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.3125, 0.0625, 0.3125, 0.6875, 0.125, 0.6875), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.6875, 0.0625, 0, 1, 1, 0.3125), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0, 0.0625, 0.6875, 0.3125, 1, 1), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.6875, 0.0625, 0.6875, 1, 1, 1), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0, 0.0625, 0, 0.3125, 1, 0.3125), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.3125, 0.125, 0.375, 0.375, 0.1875, 0.625), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.375, 0.125, 0.625, 0.625, 0.1875, 0.6875), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.375, 0.9375, 0.625, 0.625, 1, 0.6875), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.625, 0.125, 0.625, 0.6875, 1, 0.6875), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.3125, 0.125, 0.625, 0.375, 1, 0.6875), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.3125, 0.125, 0.3125, 0.375, 1, 0.375), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.3125, 0.9375, 0.375, 0.375, 1, 0.625), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.3125, 0.125, 0.625, 0.375, 1, 0.6875), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.375, 0.125, 0.3125, 0.625, 0.1875, 0.375), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.375, 0.9375, 0.3125, 0.625, 1, 0.375), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.625, 0.125, 0.3125, 0.6875, 1, 0.375), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.3125, 0.125, 0.3125, 0.375, 1, 0.375), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.625, 0.125, 0.375, 0.6875, 0.1875, 0.625), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.625, 0.9375, 0.375, 0.6875, 1, 0.625), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.625, 0.125, 0.3125, 0.6875, 1, 0.375), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.625, 0.125, 0.625, 0.6875, 1, 0.6875), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.3125, 0.125, 0.3125, 0.6875, 1, 0.6875), BooleanOp.OR);

    return shape;
  }
}
