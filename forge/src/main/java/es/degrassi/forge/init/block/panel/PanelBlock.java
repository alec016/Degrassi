package es.degrassi.forge.init.block.panel;

import es.degrassi.forge.init.block.BaseBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.BooleanOp;
import org.jetbrains.annotations.NotNull;

public abstract class PanelBlock extends BaseBlock {

  public PanelBlock() {
    super(
      BlockBehaviour.Properties
        .of(Material.STONE)
        .strength(6f)
        .requiresCorrectToolForDrops()
        .noOcclusion()
    );
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

    shape = Shapes.join(shape, Shapes.box(0, 0, 0, 0.0625, 0.375, 1), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.9375, 0, 0, 1, 0.375, 1), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0, 0.9375, 0.375, 0.0625), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.9375, 0.9375, 0.375, 1), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.0625, 0, 0.0625, 0.9375, 0.0625, 0.9375), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.box(0.0625, 0.28125, 0.0625, 0.9375, 0.34375, 0.9375), BooleanOp.OR);

    return shape;
  }
}
