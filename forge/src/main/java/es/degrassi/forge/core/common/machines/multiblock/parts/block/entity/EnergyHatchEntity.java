package es.degrassi.forge.core.common.machines.multiblock.parts.block.entity;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.EnergyHatch;
import es.degrassi.forge.core.common.machines.multiblock.uils.handler.EnergySidedHandler;
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
public class EnergyHatchEntity extends BaseMultiblockPartEntity<MultiblockPartStorage.Energy> {
  public static final EnergyHatchEntity DUMMY = dummyEntity();

  private final EnergyComponent energy;
  private final Map<Direction, LazyOptional<EnergySidedHandler>> energyWrapperHandlerMap;
  public EnergyHatchEntity(BlockPos pos, BlockState blockState, MultiblockPartStorage.Energy variant) {
    super(EntityRegistration.ENERGY_HATCH.get(), pos, blockState, variant);

    componentManager.addEnergy(variant.getCapacity(), "energy");
    jeiComponentManager.addEnergy(variant.getCapacity(), "energy");
    energy = (EnergyComponent) componentManager.getComponent("energy").orElse(null);
    energyWrapperHandlerMap = EnergySidedHandler.DEFAULT_ALL_INSERT(componentManager, energy, getBlockState().getValue(EnergyHatch.FACING));

    elementManager
      .addEnergy(
        7,
        20,
        Component.literal("energy"),
        new DegrassiLocation("textures/gui/multiblock/parts/energy_hatch_empty.png"),
        new DegrassiLocation("textures/gui/multiblock/parts/energy_hatch_filled.png"),
        "energy"
      ).addPlayerInventory(
        7,
        97,
        Component.literal("player_inventory"),
        new DegrassiLocation("textures/gui/base_inventory.png")
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

  public EnergyHatchEntity copy(boolean dummy) {
    return dummy ? dummyEntity() : new EnergyHatchEntity(getBlockPos(), getBlockState(), getVariant());
  }

  public static EnergyHatchEntity dummyEntity() {
    return new EnergyHatchEntity(BlockPos.ZERO, BlockRegistration.ENERGY_HATCH.get(MultiblockPartStorage.Energy.BASIC).defaultBlockState(), MultiblockPartStorage.Energy.BASIC) {
      @Override
      public boolean dummy() {
        return true;
      }
    };
  }
}
