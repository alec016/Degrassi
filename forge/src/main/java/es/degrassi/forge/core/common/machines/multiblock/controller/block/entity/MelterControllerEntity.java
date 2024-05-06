package es.degrassi.forge.core.common.machines.multiblock.controller.block.entity;

import es.degrassi.forge.core.common.machines.multiblock.controller.block.BaseMultiblockControllerBlock;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.MelterController;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.recipe.MelterRecipe;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.recipe.processor.MelterProcessor;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.EnergyHatch;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.FluidOutputTank;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.InputBus;
import es.degrassi.forge.core.common.machines.multiblock.uils.StateMatcher;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.utils.EnumUtils;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import oshi.util.tuples.Pair;

public class MelterControllerEntity extends BaseMultiblockControllerEntity<MelterRecipe, MelterController, MelterControllerEntity> {
  public MelterControllerEntity(BlockPos blockPos, BlockState blockState, BaseMultiblockControllerBlock block) {
    super(EntityRegistration.MELTER.get(), blockPos, blockState, block);
    BlockPos pos = BlockPos.ZERO;
    addToPattern(
      pos.west(),
      new StateMatcher(BlockRegistration.MELTER_FRAME.get().defaultBlockState())
    ).addToPattern(
      pos.east(),
      new StateMatcher(BlockRegistration.MELTER_FRAME.get().defaultBlockState())
    ).addToPattern(
      pos.west().above(),
      new StateMatcher(BlockRegistration.MELTER_FRAME.get().defaultBlockState())
    ).addToPattern(
      pos.east().above(),
      new StateMatcher(BlockRegistration.MELTER_FRAME.get().defaultBlockState())
    ).addToPattern(
      pos.west().above().above(),
      new StateMatcher(BlockRegistration.MELTER_FRAME.get().defaultBlockState())
    ).addToPattern(
      pos.east().above().above(),
      new StateMatcher(BlockRegistration.MELTER_FRAME.get().defaultBlockState())
    ).addToPattern(
      pos.south(),
      new StateMatcher(BlockRegistration.MELTER_FRAME.get().defaultBlockState())
    ).addToPattern(
      pos.south().west(),
      new StateMatcher(BlockRegistration.MELTER_FRAME.get().defaultBlockState())
    ).addToPattern(
      pos.south().east(),
      new StateMatcher(BlockRegistration.MELTER_FRAME.get().defaultBlockState())
    ).addToPattern(
      pos.south().south(),
      new StateMatcher(
        BlockRegistration.FLUID_OUTPUT_TANK.getAll().stream().map(Block::defaultBlockState).toList(),
        new Pair<>(FluidOutputTank.FACING, List.of(EnumUtils.DIRECTION))
      )
    ).addToPattern(
      pos.south().south().west(),
      new StateMatcher(
        BlockRegistration.INPUT_BUS.getAll().stream().map(Block::defaultBlockState).toList(),
        new Pair<>(InputBus.FACING, List.of(EnumUtils.DIRECTION))
      )
    ).addToPattern(
      pos.south().south().east(),
      new StateMatcher(
        BlockRegistration.ENERGY_HATCH.getAll().stream().map(Block::defaultBlockState).toList(),
        new Pair<>(EnergyHatch.FACING, List.of(EnumUtils.DIRECTION))
      )
    ).addToPattern(
      pos.south().south().west().above(),
      new StateMatcher(BlockRegistration.MELTER_FRAME.get().defaultBlockState())
    ).addToPattern(
      pos.south().south().east().above(),
      new StateMatcher(BlockRegistration.MELTER_FRAME.get().defaultBlockState())
    ).addToPattern(
      pos.south().south().west().above().above(),
      new StateMatcher(BlockRegistration.MELTER_FRAME.get().defaultBlockState())
    ).addToPattern(
      pos.south().south().east().above().above(),
      new StateMatcher(BlockRegistration.MELTER_FRAME.get().defaultBlockState())
    );
    /*
     * layers: 3
     *
     * layer 0 -> {
       * "frame, controller, frame",
       * "frame, frame, frame",
       * "input_bus, fluid_output_tank, energy_hatch"
     * },
     * layer 1 -> {
       * "frame,air,frame",
       * "air,air,air",
       * "frame,air,frame
     * },
     * layer 2 -> {
       * "frame,,frame",
       * "air,air,air",
       * "frame,air,frame
     * }
     */
    init(blockState.getValue(BaseMultiblockControllerBlock.FACING));
    this.processor = new MelterProcessor(this, false);
  }
}
