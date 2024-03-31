package es.degrassi.forge.core.common.cables.energy;

import com.google.common.collect.Iterables;
import com.google.common.primitives.Ints;
import es.degrassi.forge.core.common.cables.CableEntity;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.tiers.CableTier;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnergyCableEntity extends CableEntity<EnergyCableNet, EnergySideConfig> {

  public EnergyCableEntity(BlockPos pos, BlockState state, CableTier tier) {
    super(EntityRegistration.ENERGY_CABLE.get(), pos, state, tier);
    sideConfig = new EnergySideConfig(this);
    getComponentManager().addEnergy(0, tier.getEnergyTransfer(), "energy");
  }

  public void clearRemoved() {
    super.clearRemoved();
    EnergyCableNet.addCable(this);
  }

  public void setRemoved() {
    super.setRemoved();
    EnergyCableNet.removeCable(this);
  }

  protected Iterable<EnergyCableEntity> getCables() {
    if (net == null) {
      EnergyCableNet.calculateNetwork(this);
    }
    startIndex %= net.cableList.size();
    return Iterables.concat(net.cableList.subList(startIndex, net.cableList.size()), net.cableList.subList(0, startIndex));
  }

  public void readSync(CompoundTag nbt) {
    super.readSync(nbt);
    readSides(nbt);
  }

  public CompoundTag writeSync(CompoundTag nbt) {
    writeSides(nbt);
    return super.writeSync(nbt);
  }

  @Override
  public Component getName() {
    return Component.translatable("block.degrassi." + tier.getName() + "_energy_cable");
  }

  public boolean isEnergyPresent(Direction direction) {
    return true;
  }

  public EnergySideConfig getSideConfig() {
    return this.sideConfig;
  }

  public void onAdded(Level world, BlockState state, BlockState oldState, boolean isMoving) {
    if (state.getBlock() != oldState.getBlock()) {
      getSideConfig().init();
    }
  }

  @Override
  protected void serverTick(Level world) {
    super.serverTick(world);
    for (Direction direction : Direction.values()) {
      BlockEntity te = world.getBlockEntity(worldPosition.relative(direction));
      if (te == null || te instanceof CableEntity<?,?>) continue;
      te.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).ifPresent(energyHandler -> {
        if (getSideConfig().getType(direction).canExtract()) {
          energyHandler.extractEnergy(tier.getEnergyTransfer(), false);
          receiveEnergy(tier.getEnergyTransfer(), false, direction);
        }
      });
    }
  }

  protected void onFirstTick(Level world) {
    super.onFirstTick(world);
    getComponentManager().getComponent("energy").map(comp -> (EnergyComponent) comp).ifPresent(
      energy -> energy
        .setCapacity(0)
        .setTransfer(tier.getEnergyTransfer())
    );
    getSideConfig().init();
    sync();
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ENERGY) {
      if (isEnergyPresent(side)) {
        return LazyOptional.of(() -> new IEnergyStorage() {
          @Override
          public int extractEnergy(int i, boolean bl) {
            if (!canExtractEnergy(side)) return 0;
            return (int) EnergyCableEntity.this.extractEnergy(i, bl, side);
          }

          @Override
          public int getEnergyStored() {
            AtomicInteger toReturn = new AtomicInteger(0);
            getComponentManager().getComponent("energy").map(comp -> (EnergyComponent) comp).ifPresent(comp -> toReturn.set(comp.getEnergyStored()));
            return toReturn.get();
          }

          @Override
          public int getMaxEnergyStored() {
            AtomicInteger toReturn = new AtomicInteger(0);
            getComponentManager().getComponent("energy").map(comp -> (EnergyComponent) comp).ifPresent(comp -> toReturn.set(comp.getMaxEnergyStored()));
            return toReturn.get();
          }

          @Override
          public int receiveEnergy(int i, boolean bl) {
            if (!canReceiveEnergy(side)) return 0;
            return (int) EnergyCableEntity.this.receiveEnergy(i, bl, side);
          }

          @Override
          public boolean canReceive() {
            return canReceiveEnergy(side);
          }

          @Override
          public boolean canExtract() {
            return canExtractEnergy(side);
          }
        }).cast();
      }
    }
    return LazyOptional.empty();
  }

  public long extractEnergy(long maxExtract, boolean simulate, @Nullable Direction side) {
    if (!canExtractEnergy(side))
      return 0;
    final EnergyComponent energy = (EnergyComponent) getComponentManager().getComponent("energy").orElse(null);
    if (energy == null) return 0;
    long extracted = Math.min(energy.getEnergyStored(), Math.min(energy.getMaxOutput(), maxExtract));
    if (!simulate && extracted > 0) {
      energy.extractEnergy((int) extracted, simulate);
      sync(10);
    }
    return extracted;
  }

  public long receiveEnergy(long maxReceive, boolean simulate, @Nullable Direction direction) {
    if (this.level == null || isRemote() || direction == null || !checkRedstone() || !canReceiveEnergy(direction))
      return 0;
    long received = 0;
    var cables = getCables();

    var insertionGuard = this.netInsertionGuard;
    if (insertionGuard.isTrue())
      return 0;
    insertionGuard.setTrue();

    try {
      if (!simulate) {
        startIndex++; // round robin!
      }

      for (var cable : cables) {
        long amount = maxReceive - received;
        if (amount <= 0)
          break;
        if (!cable.sides.isEmpty() && cable.isActive()) {
          received += cable.pushEnergy(amount, simulate, direction, this);
        }
      }

      return received;
    } finally {
      insertionGuard.setFalse();
    }
  }

  private long pushEnergy(long maxReceive, boolean simulate, @Nullable Direction direction, EnergyCableEntity cable) {
    if (!(getLevel() instanceof ServerLevel serverLevel))
      throw new RuntimeException("Expected server level");

    long received = 0;
    for (int i = 0; i < 6; ++i) {
      // Shift by tick count to ensure that it distributes evenly on average
      Direction side = Direction.from3DDataValue((i + serverLevel.getServer().getTickCount()) % 6);
      if (!this.sides.contains(side))
        continue;

      long amount = Math.min(maxReceive - received, tier.getEnergyTransfer());
      if (amount <= 0)
        break;
      if (cable.equals(this) && side.equals(direction) || !canExtractEnergy(side))
        continue;
      BlockPos pos = this.worldPosition.relative(side);
      if (direction != null && cable.getBlockPos().relative(direction).equals(pos))
        continue;
      received += receive(getLevel(), pos, side.getOpposite(), amount, simulate);
    }
    return received;
  }

  private long receive(Level level, BlockPos pos, Direction side, long amount, boolean simulate) {
    var tile = level.getBlockEntity(pos);
    var energy = tile != null ? tile.getCapability(ForgeCapabilities.ENERGY, side).orElse(null) : null;
    return energy != null ? energy.receiveEnergy(Ints.saturatedCast(amount), simulate) : 0;
  }

  public boolean canExtractEnergy(@Nullable Direction side) {
    return side == null || isEnergyPresent(side) && this.sideConfig.getType(side).canExtract();
  }

  public boolean canReceiveEnergy(@Nullable Direction side) {
    return side == null || isEnergyPresent(side) && this.sideConfig.getType(side).canReceive();
  }
}
