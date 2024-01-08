package es.degrassi.forge.init.block.generators;

import dev.architectury.registry.menu.*;
import es.degrassi.forge.init.entity.generators.*;
import es.degrassi.forge.init.gui.container.generators.*;
import es.degrassi.forge.init.registration.*;
import es.degrassi.forge.network.*;
import es.degrassi.forge.util.*;
import net.minecraft.core.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.*;
import org.jetbrains.annotations.*;

public class CombustionGenerator extends GeneratorBlock {
  public CombustionGenerator() {
    super(
      Properties
        .of(Material.STONE)
        .sound(SoundType.METAL)
        .requiresCorrectToolForDrops()
        .strength(6f)
        .noOcclusion()
    );
  }

  private static final VoxelShape SHAPE = Shapes.box(0, 0, 0, 1, 1, 1);

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

  @Nullable
  @Override
  public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
    return new CombustionGeneratorEntity(pos, state, this);
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
    if (tile instanceof CombustionGeneratorEntity entity) {
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
            return new CombustionGeneratorContainer(id, 3, inv, entity, entity.data);
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
    return Utils.createTickerHelper(type, EntityRegister.COMBUSTION_GENERATOR.get(), CombustionGeneratorEntity::tick);
  }

  @Override
  public void onRemove(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pNewState, boolean pIsMoving) {
    if (pState.getBlock() != pNewState.getBlock()) {
      BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
      if (blockEntity instanceof CombustionGeneratorEntity entity) {
        entity.drops();
      }
    }
    super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
  }
}
