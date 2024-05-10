package es.degrassi.forge.core.common.machines.multiblock.parts.block.entity;

import es.degrassi.forge.core.common.component.ComponentIOMode;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.FluidInputTank;
import es.degrassi.forge.core.common.machines.multiblock.uils.handler.FluidSidedHandler;
import es.degrassi.forge.core.common.wrapper.DegrassiFluidHandler;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.tiers.MultiblockPartStorage;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class FluidOutputTankEntity extends BaseMultiblockPartEntity<MultiblockPartStorage.Fluid.Output> {
  public static final FluidOutputTankEntity DUMMY = new FluidOutputTankEntity(BlockPos.ZERO, BlockRegistration.FLUID_OUTPUT_TANK.get(MultiblockPartStorage.Fluid.Output.BASIC).defaultBlockState(), MultiblockPartStorage.Fluid.Output.BASIC) {
    @Override
    public boolean dummy() {
      return true;
    }
  };
  private final DegrassiFluidHandler fluid;
  private final Map<Direction, LazyOptional<FluidSidedHandler>> fluidWrapperHandlerMap;

  public FluidOutputTankEntity(BlockPos pos, BlockState blockState, MultiblockPartStorage.Fluid.Output variant) {
    super(EntityRegistration.FLUID_OUTPUT_TANK.get(), pos, blockState, variant);

    getComponentManager().addFluid(variant.getCapacity(), "fluid_output", ComponentIOMode.OUTPUT);

    fluid = getComponentManager().getFluidHandler();
    fluidWrapperHandlerMap = Map.of(
      Direction.UP, LazyOptional.of(() -> new FluidSidedHandler(
        getComponentManager(),
        fluid,
        getBlockState().getValue(FluidInputTank.FACING),
        null,
        Direction.UP
      )),
      Direction.DOWN, LazyOptional.of(() -> new FluidSidedHandler(
        getComponentManager(),
        fluid,
        getBlockState().getValue(FluidInputTank.FACING),
        null,
        Direction.DOWN
      )),
      Direction.NORTH, LazyOptional.of(() -> new FluidSidedHandler(
        getComponentManager(),
        fluid,
        getBlockState().getValue(FluidInputTank.FACING),
        null,
        Direction.NORTH
      )),
      Direction.SOUTH, LazyOptional.of(() -> new FluidSidedHandler(
        getComponentManager(),
        fluid,
        getBlockState().getValue(FluidInputTank.FACING),
        null,
        Direction.SOUTH
      )),
      Direction.EAST, LazyOptional.of(() -> new FluidSidedHandler(
        getComponentManager(),
        fluid,
        getBlockState().getValue(FluidInputTank.FACING),
        null,
        Direction.EAST
      )),
      Direction.WEST, LazyOptional.of(() -> new FluidSidedHandler(
        getComponentManager(),
        fluid,
        getBlockState().getValue(FluidInputTank.FACING),
        null,
        Direction.WEST
      ))
    );
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.FLUID_HANDLER) {
      if (side == null) return lazyFluidHandler.cast();
      if (fluidWrapperHandlerMap.containsKey(side)) {
        Direction localDir = this.getBlockState().getValue(FluidInputTank.FACING);
        if (side == localDir)
          return fluidWrapperHandlerMap.get(localDir).cast();
        return LazyOptional.empty();
      }
    }
    return super.getCapability(cap, side);
  }

  @Override
  public Component getName() {
    return Component.translatable(getBlockState().getBlock().getDescriptionId());
  }
}
