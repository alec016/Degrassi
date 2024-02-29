package es.degrassi.forge.init.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class MachineBlock extends Block implements EntityBlock {
  static final StateArgumentPredicate<EntityType<?>> spawnPredicate = ((state, level, pos, type) -> state.isFaceSturdy(level, pos, Direction.UP) && state.getBlock() instanceof MachineBlock machineBlock && machineBlock.getLightEmission(state, level, pos) < 14);
  private final String renderType;

  public static Properties makeProperties(boolean occlusion) {
    Properties props = Properties.of().requiresCorrectToolForDrops().strength(3.5F).dynamicShape().isValidSpawn(spawnPredicate);
    return occlusion ? props : props.noOcclusion();
  }

  public MachineBlock(String renderType, boolean occlusion) {
    super(makeProperties(occlusion));
    this.renderType = renderType;
  }

  public MachineBlock() {
    this("translucent", false);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return null;
  }
}
