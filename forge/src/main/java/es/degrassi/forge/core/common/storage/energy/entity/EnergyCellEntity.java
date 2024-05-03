package es.degrassi.forge.core.common.storage.energy.entity;

import es.degrassi.forge.core.common.component.ComponentIOMode;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.common.storage.StorageEntity;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.tiers.Storage;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import es.degrassi.forge.core.common.storage.energy.block.EnergyCell;

@Getter
public class EnergyCellEntity extends StorageEntity<Storage.Energy> {
  final EnergyComponent energy;

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
      public int extractEnergy(int energy, boolean simulate) {
        return super.extractEnergy(energy, tier.isCreative() || simulate);
      }

      @Override
      public int receiveEnergy(int energy, boolean simulate) {
        return super.receiveEnergy(energy, tier.isCreative() || simulate);
      }

      @Override
      public void markDirty() {
        super.markDirty();
        float filledPercent = (float) getEnergyStored() / getMaxEnergyStored();
        BlockState state = getBlockState();
        if (filledPercent < 0.25) {
          state = state.setValue(EnergyCell.FILLED, 0);
        } else if (filledPercent < 0.5) {
          state = state.setValue(EnergyCell.FILLED, 25);
        } else if (filledPercent < 0.75) {
          state = state.setValue(EnergyCell.FILLED, 50);
        } else if (filledPercent < 1) {
          state = state.setValue(EnergyCell.FILLED, 75);
        } else {
          state = state.setValue(EnergyCell.FILLED, 100);
        }
        level.setBlockAndUpdate(worldPosition, state);
        requestModelDataUpdate();
        setChanged();
      }
    };

    getComponentManager().add(energy);
  }


  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap , @Nullable Direction side) {
    if (cap == ForgeCapabilities.ENERGY) {
      return lazyEnergyHandler.cast();
    }
    return super.getCapability(cap , side);
  }

  @Override
  public Component getName() {
    return getBlockState().getBlock().getName();
  }

  @Override
  public void load(@NotNull CompoundTag nbt) {
    super.load(nbt);
    if (tier.isCreative()) energy.setEnergy(Integer.MAX_VALUE);
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag nbt) {
    super.saveAdditional(nbt);
  }

  @Override
  public void onLoad() {
    super.onLoad();
    if (tier.isCreative()) energy.setEnergy(Integer.MAX_VALUE);
    lazyEnergyHandler = LazyOptional.of(() -> energy);
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    lazyEnergyHandler.invalidate();
  }
}
