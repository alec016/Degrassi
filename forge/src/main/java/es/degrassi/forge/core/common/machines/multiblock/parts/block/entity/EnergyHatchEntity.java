package es.degrassi.forge.core.common.machines.multiblock.parts.block.entity;

import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.EnergyHatch;
import es.degrassi.forge.core.common.machines.multiblock.uils.handler.EnergySidedHandler;
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
public class EnergyHatchEntity extends BaseMultiblockPartEntity<MultiblockPartStorage.Energy> {
  private final EnergyComponent energy;
  private final Map<Direction, LazyOptional<EnergySidedHandler>> energyWrapperHandlerMap;
  public EnergyHatchEntity(BlockPos pos, BlockState blockState, MultiblockPartStorage.Energy variant) {
    super(EntityRegistration.ENERGY_HATCH.get(), pos, blockState, variant);

    getComponentManager().addEnergy(variant.getCapacity(), "energy");
    energy = (EnergyComponent) getComponentManager().getComponent("energy").orElse(null);
    energyWrapperHandlerMap = Map.of(
      Direction.UP, LazyOptional.of(() -> new EnergySidedHandler(
        getComponentManager(),
        energy,
        null,
        getBlockState().getValue(EnergyHatch.FACING),
        Direction.UP
      )),
      Direction.DOWN, LazyOptional.of(() -> new EnergySidedHandler(
        getComponentManager(),
        energy,
        null,
        getBlockState().getValue(EnergyHatch.FACING),
        Direction.DOWN
      )),
      Direction.NORTH, LazyOptional.of(() -> new EnergySidedHandler(
        getComponentManager(),
        energy,
        null,
        getBlockState().getValue(EnergyHatch.FACING),
        Direction.NORTH
      )),
      Direction.SOUTH, LazyOptional.of(() -> new EnergySidedHandler(
        getComponentManager(),
        energy,
        null,
        getBlockState().getValue(EnergyHatch.FACING),
        Direction.SOUTH
      )),
      Direction.EAST, LazyOptional.of(() -> new EnergySidedHandler(
        getComponentManager(),
        energy,
        null,
        getBlockState().getValue(EnergyHatch.FACING),
        Direction.EAST
      )),
      Direction.WEST, LazyOptional.of(() -> new EnergySidedHandler(
        getComponentManager(),
        energy,
        null,
        getBlockState().getValue(EnergyHatch.FACING),
        Direction.WEST
      ))
    );
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ENERGY) {
      if (side == null) return lazyEnergyHandler.cast();
      if (energyWrapperHandlerMap.containsKey(side)) {
        Direction localDir = this.getBlockState().getValue(EnergyHatch.FACING);
        if (side == localDir)
          return energyWrapperHandlerMap.get(localDir).cast();
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
