package es.degrassi.forge.core.common.storage.fluid.entity;

import es.degrassi.common.utils.LerpedFloat;
import es.degrassi.forge.core.common.component.ComponentIOMode;
import es.degrassi.forge.core.common.component.FluidComponent;
import es.degrassi.forge.core.common.storage.StorageEntity;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.network.component.FluidPacket;
import es.degrassi.forge.core.tiers.Storage;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class FluidTankEntity extends StorageEntity<Storage.Fluid> {
  final FluidComponent fluid = new FluidComponent(
    getComponentManager(),
    "fluid",
    false,
    tier.getCapacity(),
    this,
    ComponentIOMode.BOTH
  ) {
    @Override
    public int getFluidAmount() {
      return tier.isCreative() && !fluid.isEmpty() ? Integer.MAX_VALUE : super.getFluidAmount();
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
      if (tier.isCreative()) return resource.getAmount();
      return super.fill(resource, action);
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
      return super.drain(resource, action);
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
      return tier.isCreative() ? super.drain(maxDrain, FluidAction.SIMULATE) : super.drain(maxDrain, action);
    }

    @Override
    public void markDirty() {
      setChanged();
      updateFluidLevel();
      if(getLevel() != null && !getLevel().isClientSide()) {
        new FluidPacket(this.fluid, this.capacity, getId(), getBlockPos())
          .sendToChunkListeners(getLevel().getChunkAt(getBlockPos()));
        if (getLevel() instanceof ServerLevel server) {
          server.getChunkSource().blockChanged(getBlockPos());
        }
      }
    }
  };

  LerpedFloat fluidLevel;

  public FluidTankEntity(BlockPos pos, BlockState blockState, Storage.Fluid tier) {
    super(EntityRegistration.FLUID_TANK.get(), pos, blockState, tier);
    getComponentManager().add(fluid);
  }

  @Override
  public Component getName() {
    return getBlockState().getBlock().getName();
  }

  public void updateFluidLevel() {
    this.fluidLevel = LerpedFloat.linear().startWithValue(this.fluid.getFillState());
    this.fluidLevel.chase(this.fluid.getFillState(), 0.5, LerpedFloat.Chaser.EXP);
  }

  public void setFluid(FluidStack fluid, int capacity) {
    if (tier.isCreative()) {
      this.fluid.setFluid(new FluidStack(fluid, Integer.MAX_VALUE));
      this.fluid.setCapacity(Integer.MAX_VALUE);
    } else {
      this.fluid.setFluid(fluid.copy());
      this.fluid.setCapacity(capacity);
    }
    getComponentManager().markDirty();
  }

  public boolean addFluid(Fluid fluid) {
    if (tier.isCreative()) {
      this.fluid.setFluid(new FluidStack(fluid, Integer.MAX_VALUE));
      getComponentManager().markDirty();
      return true;
    } else {
      int insert = this.fluid.fill(new FluidStack(fluid, 1000), IFluidHandler.FluidAction.SIMULATE);
      if (insert >= 1000) {
        this.fluid.fill(new FluidStack(fluid, insert), IFluidHandler.FluidAction.EXECUTE);
        getComponentManager().markDirty();
        return true;
      }
    }
    return false;
  }

  public Fluid removeFluid() {
    FluidStack f = this.fluid.drain(1000, IFluidHandler.FluidAction.SIMULATE);
    if (f.getAmount() == 1000 && !f.getFluid().isSame(Fluids.EMPTY)) {
      if (!tier.isCreative()) this.fluid.drain(1000, IFluidHandler.FluidAction.EXECUTE);
      getComponentManager().markDirty();
      return f.getFluid();
    }
    return Fluids.EMPTY;
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.FLUID_HANDLER) {
      return lazyFluidHandler.cast();
    }
    return super.getCapability(cap, side);
  }

  public FluidStack getFluidStack() {
    return fluid.getFluid();
  }

  @Override
  public void load(@NotNull CompoundTag nbt) {
    super.load(nbt);
    getComponentManager().markDirty();
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag nbt) {
    super.saveAdditional(nbt);
  }

  @Override
  public void onLoad() {
    super.onLoad();
    lazyFluidHandler = LazyOptional.of(() -> getComponentManager().getFluidHandler());
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    lazyFluidHandler.invalidate();
  }

  public FluidTankEntity copy(boolean dummy) {
    return new FluidTankEntity(getBlockPos(), getBlockState(), getTier()) {
      public boolean dummy() {
        return dummy;
      }
    };
  }
}
