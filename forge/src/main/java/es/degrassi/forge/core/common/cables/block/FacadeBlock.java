package es.degrassi.forge.core.common.cables.block;

import es.degrassi.forge.core.common.cables.CableType;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.tiers.CableTier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class FacadeBlock extends CableBlock implements EntityBlock {
  public FacadeBlock() {
    super(CableTier.EMPTY);
    setCableType(CableType.FACADE);
  }

  @Override
  public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new FacadeBlockEntity(pos, state);
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
    if (world.getBlockEntity(pos) instanceof FacadeBlockEntity facade) {
      BlockState mimicBlock = facade.getMimicBlock();
      if (mimicBlock != null) {
        return mimicBlock.getShape(world, pos, context);
      }
    }
    return super.getShape(state, world, pos, context);
  }

  @Override
  public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity te, ItemStack tool) {
    ItemStack item = new ItemStack(BlockRegistration.CABLE_FACADE.get());
    BlockState mimicBlock;
    if (te instanceof FacadeBlockEntity) {
      mimicBlock = ((FacadeBlockEntity) te).getMimicBlock();
    } else {
      mimicBlock = Blocks.COBBLESTONE.defaultBlockState();
    }
    FacadeBlockItem.setMimicBlock(item, mimicBlock);
    popResource(level, pos, item);
  }

  @Override
  public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
    BlockState defaultState = level.getBlockState(pos);
    CableType type = defaultState.getValue(CableBlock.TYPE);
    CableTier tier = defaultState.getValue(CableBlock.TIER);
    switch (type) {
      case ENERGY -> {
        switch (tier) {
          case BASIC -> defaultState = BlockRegistration.BASIC_ENERGY_CABLE.get().defaultBlockState();
          case ADVANCE -> defaultState = BlockRegistration.ADVANCE_ENERGY_CABLE.get().defaultBlockState();
          case EXTREME -> defaultState = BlockRegistration.EXTREME_ENERGY_CABLE.get().defaultBlockState();
        }
      }
    }
    BlockState newState = CableBlock.calculateState(level, pos, defaultState);
    return ((LevelAccessor) level).setBlock(pos, newState, ((LevelAccessor) level).isClientSide()
      ? Block.UPDATE_ALL + Block.UPDATE_IMMEDIATE
      : Block.UPDATE_ALL);
  }
}
