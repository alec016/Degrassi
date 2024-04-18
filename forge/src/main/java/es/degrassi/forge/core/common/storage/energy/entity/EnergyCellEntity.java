package es.degrassi.forge.core.common.storage.energy.entity;

import es.degrassi.forge.core.common.component.ComponentIOMode;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.common.storage.StorageEntity;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.tiers.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnergyCellEntity extends StorageEntity<Storage.Energy> {
  EnergyComponent energy;

  public EnergyCellEntity(BlockPos pos, BlockState blockState, Storage.Energy tier) {
    super(EntityRegistration.ENERGY_CELL.get(), pos, blockState, tier);

    energy = new EnergyComponent(
      getComponentManager(),
      tier.getCapacity(),
      tier.getTransfer(),
      tier.getTransfer(),
      this,
      "energy",
      ComponentIOMode.BOTH
    ) {
      @Override
      public int getEnergyStored() {
        return tier.isCreative() ? Integer.MAX_VALUE : super.getEnergyStored();
      }

      @Override
      public int extractEnergy(int energy, boolean simulate) {
        return super.extractEnergy(energy, true);
      }

      @Override
      public int receiveEnergy(int energy, boolean simulate) {
        if (tier.isCreative()) return energy;
        return super.receiveEnergy(energy, simulate);
      }
    };

    getComponentManager().add(energy);
  }


  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap , @Nullable Direction side) {
    if (cap == ForgeCapabilities.ENERGY) {
      return LazyOptional.of(() -> energy).cast();
    }
    return super.getCapability(cap , side);
  }

  @Override
  public Component getName() {
    return null;
  }
}
