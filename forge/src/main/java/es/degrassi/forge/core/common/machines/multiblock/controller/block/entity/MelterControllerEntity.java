package es.degrassi.forge.core.common.machines.multiblock.controller.block.entity;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.BaseMultiblockControllerBlock;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.MelterController;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.recipe.MelterRecipe;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.recipe.processor.MelterProcessor;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.EnergyHatch;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.FluidOutputTank;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.InputBus;
import es.degrassi.forge.core.common.machines.multiblock.uils.StateMatcher;
import es.degrassi.forge.core.data.DegrassiTags;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.utils.EnumUtils;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import oshi.util.tuples.Pair;

public class MelterControllerEntity extends BaseMultiblockControllerEntity<MelterRecipe, MelterController, MelterControllerEntity> {
  public static final MelterControllerEntity DUMMY = dummyController();

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
    )
      .addToPattern(
      pos.south().south(),
      new StateMatcher(DegrassiTags.Blocks.MBPARTS.get())
    ).addToPattern(
      pos.south().south().west(),
      new StateMatcher(DegrassiTags.Blocks.MBPARTS.get())
    ).addToPattern(
      pos.south().south().east(),
      new StateMatcher(DegrassiTags.Blocks.MBPARTS.get())
    )
    .addToPattern(
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

    elementManager
      .addPlayerInventory(
        7,
        97,
        Component.literal("player_inventory"),
        new DegrassiLocation("textures/gui/base_inventory.png")
      );

    jeiComponentManager
      .addEnergy(0, "energy")
      .addItem("input")
      .addFluid(0, "output");

    jeiElementManager
      .addEnergy(
        10, 10,
        Component.literal("energy"),
        new DegrassiLocation("textures/gui/jei/melter_energy_empty.png"),
        new DegrassiLocation("textures/gui/jei/melter_energy_filled.png"),
        "energy"
      ).addItem(
        35, 33,
        Component.literal("item_input"),
        new DegrassiLocation("textures/gui/base_slot.png"),
        "input"
      ).addFluid(
        115, 6,
        Component.literal("fluid_output"),
        new DegrassiLocation("textures/gui/jei/melter_fluid.png"),
        "output"
      ).addProgress(
        62, 33,
        Component.literal("progress"),
        new DegrassiLocation("textures/gui/jei/melter_progress_empty.png"),
        new DegrassiLocation("textures/gui/jei/melter_progress_filled.png")
      );

    this.processor = new MelterProcessor(this, false);
    init();
  }

  public MelterControllerEntity copy(boolean dummy) {
    return dummy ? dummyController() : new MelterControllerEntity(getBlockPos(), getBlockState(), getBlock());
  }

  public static MelterControllerEntity dummyController() {
    return new MelterControllerEntity(BlockPos.ZERO, BlockRegistration.MELTER_CONTROLLER.get().defaultBlockState(), BlockRegistration.MELTER_CONTROLLER.get()) {
      @Override
      public boolean dummy() {
        return true;
      }
    };
  }
}
