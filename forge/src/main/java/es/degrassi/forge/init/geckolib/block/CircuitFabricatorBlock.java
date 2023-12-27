package es.degrassi.forge.init.geckolib.block;

import es.degrassi.forge.init.block.BaseBlock;
import es.degrassi.forge.init.geckolib.entity.CircuitFabricatorEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class CircuitFabricatorBlock extends BaseBlock {
  public CircuitFabricatorBlock() {
    super(
      Properties.of(Material.METAL)
        .requiresCorrectToolForDrops()
        .destroyTime(60)
        .dynamicShape()
        .noOcclusion()
    );
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
    return new CircuitFabricatorEntity(pos, state);
  }

  @Override
  public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
    return RenderShape.ENTITYBLOCK_ANIMATED;
  }

  private static final VoxelShape SHAPE = createVoxelShape();

  @Override
  public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull CollisionContext collisionContext) {
    return SHAPE;
  }

  private static @NotNull VoxelShape createVoxelShape() {
    VoxelShape shape = Shapes.empty();
//    shape = Shapes.join(shape, Shapes.box(0, 0, 0, 1, 0.75, 1), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(-1, 0, 0.5, 2, 0.5, 1.5), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(-1, 0.5, 0.5, 2, 0.5625, 0.5625), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(-1, 0.5, 1.4375, 2, 0.5625, 1.5), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(-1, 0.5, 0.5625, -0.9375, 0.5625, 1.4375), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(1.9375, 0.5, 0.5625, 2, 0.5625, 1.4375), BooleanOp.OR);

    return shape;
  }
}
