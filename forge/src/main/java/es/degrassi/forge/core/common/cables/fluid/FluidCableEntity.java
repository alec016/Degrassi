package es.degrassi.forge.core.common.cables.fluid;

import com.google.common.collect.Iterables;
import es.degrassi.forge.api.utils.DegrassiLogger;
import es.degrassi.forge.core.common.cables.CableEntity;
import es.degrassi.forge.core.common.component.FluidComponent;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.tiers.CableTier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidCableEntity extends CableEntity<FluidCableNet> {
  protected final FluidSideConfig sideConfig = new FluidSideConfig(this);

  public FluidCableEntity(BlockPos pos, BlockState blockState, CableTier tier) {
    super(EntityRegistration.FLUID_CABLE.get(), pos, blockState, tier);
    getComponentManager().addFluid(0, "fluid");
  }

  public void clearRemoved() {
    super.clearRemoved();
    FluidCableNet.addCable(this);
  }

  public void setRemoved() {
    super.setRemoved();
    FluidCableNet.removeCable(this);
  }

  protected Iterable<FluidCableEntity> getCables() {
    if (net == null) {
      FluidCableNet.calculateNetwork(this);
    }
    startIndex %= net.cableList.size();
    return Iterables.concat(net.cableList.subList(startIndex, net.cableList.size()), net.cableList.subList(0, startIndex));
  }

  public void readSync(CompoundTag nbt) {
    this.sideConfig.read(nbt);
    super.readSync(nbt);
    readSides(nbt);
  }

  public CompoundTag writeSync(CompoundTag nbt) {
    writeSides(nbt);
    this.sideConfig.write(nbt);
    return super.writeSync(nbt);
  }

  @Override
  public Component getName() {
    return Component.translatable("block.degrassi." + tier.getName() + "_fluid_cable");
  }

  public boolean isFluidPresent(Direction direction) {
    return true;
  }

  public FluidSideConfig getSideConfig() {
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
      if (getSideConfig().getType(direction).canExtract) {
        BlockEntity te = world.getBlockEntity(worldPosition.relative(direction));
        if (te == null) continue;
        te.getCapability(ForgeCapabilities.FLUID_HANDLER).ifPresent(fluidHandler -> {
          for (int i = 0; i < fluidHandler.getTanks(); i++) {
            FluidStack fluid = fluidHandler.getFluidInTank(i).copy();
            if (fluid.isEmpty()) continue;
            fluid.setAmount(Math.min(fluid.getAmount(), tier.getFluidTransfer()));
            FluidStack stack = fluidHandler.drain(fluid, IFluidHandler.FluidAction.SIMULATE);
            if (!stack.isEmpty() && receiveFluid(stack, IFluidHandler.FluidAction.SIMULATE, direction) > 0) {
              fluidHandler.drain(stack, IFluidHandler.FluidAction.EXECUTE);
              receiveFluid(stack, IFluidHandler.FluidAction.EXECUTE, direction);
            }
          }
        });
      }
    }
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.FLUID_HANDLER) {
      if (isFluidPresent(side)) {
        return LazyOptional.of(() -> new IFluidHandler() {
          @Override
          public int getTanks() {
            return 1;
          }

          @Override
          public @NotNull FluidStack getFluidInTank(int i) {
            AtomicReference<FluidStack> toReturn = new AtomicReference<>(FluidStack.EMPTY);
            getComponentManager().getComponent("fluid").map(comp -> (FluidComponent) comp).ifPresent(comp -> toReturn.set(comp.getFluid()));
            return toReturn.get().copy();
          }

          @Override
          public int getTankCapacity(int i) {
            AtomicInteger toReturn = new AtomicInteger(0);
            getComponentManager().getComponent("fluid").map(comp -> (FluidComponent) comp).ifPresent(comp -> toReturn.set(comp.getCapacity()));
            return toReturn.get();
          }

          @Override
          public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
            AtomicBoolean toReturn = new AtomicBoolean(false);
            getComponentManager().getComponent("fluid").map(comp -> (FluidComponent) comp).ifPresent(comp -> toReturn.set(comp.isFluidValid(fluidStack)));
            return toReturn.get();
          }

          @Override
          public int fill(FluidStack fluidStack, IFluidHandler.FluidAction fluidAction) {
            if (!canReceiveFluid(side)) return 0;
            int toReturn = (int) receiveFluid(fluidStack, fluidAction, side);
            DegrassiLogger.INSTANCE.info("FluidCableEntity$getCapability<IFluidHandler>({}).fill() -> {}", side, toReturn);
            return toReturn;
          }

          @Override
          public @NotNull FluidStack drain(int i, IFluidHandler.FluidAction fluidAction) {
            if (!canExtractFluid(side)) return FluidStack.EMPTY;
            FluidStack toReturn = extractFluid(i, fluidAction, side);
            DegrassiLogger.INSTANCE.info("FluidCableEntity$getCapability<IFluidHandler>({}).drain({}) -> fluid: {}, amount: {}", side, i, toReturn.getFluid().getFluidType().getDescription(), toReturn.getAmount());
            return toReturn;
          }

          @Override
          public @NotNull FluidStack drain(FluidStack fluidStack, IFluidHandler.FluidAction fluidAction) {
            if (!canExtractFluid(side)) return FluidStack.EMPTY;
            FluidStack toReturn = extractFluid(fluidStack, fluidAction, side);
            DegrassiLogger.INSTANCE.info("FluidCableEntity$getCapability<IFluidHandler>({}).drain({}) -> fluid: {}, amount: {}", side, fluidStack.getFluid().getFluidType().getDescription(), toReturn.getFluid().getFluidType().getDescription(), toReturn.getAmount());
            return toReturn;
          }
        }).cast();
      }
    }
    return LazyOptional.empty();
  }

  public FluidStack extractFluid(FluidStack fluidStack, IFluidHandler.FluidAction simulate, @Nullable Direction side) {
    if (!canExtractFluid(side))
      return FluidStack.EMPTY;
    final FluidComponent fluid = (FluidComponent) getComponentManager().getComponent("fluid").orElse(null);
    if (fluid == null || !fluid.isFluidValid(fluidStack)) return FluidStack.EMPTY;
    long extracted = Math.min(fluid.getFluidAmount(), Math.min(tier.getFluidTransfer(), fluidStack.getAmount()));
    FluidStack extract = FluidStack.EMPTY;
    if (simulate.execute() && extracted > 0) {
      extract = fluid.drain((int) extracted, simulate);
      sync(10);
    }
    return extract;
  }

  public FluidStack extractFluid(int maxExtract, IFluidHandler.FluidAction simulate, @Nullable Direction side) {
    if (!canExtractFluid(side))
      return FluidStack.EMPTY;
    final FluidComponent fluid = (FluidComponent) getComponentManager().getComponent("fluid").orElse(null);
    if (fluid == null || fluid.getFluid().isEmpty()) return FluidStack.EMPTY;
    FluidStack f = fluid.getFluid().copy();
    f.setAmount(Math.min(fluid.getFluidAmount(), Math.min(tier.getFluidTransfer(), maxExtract)));
    return extractFluid(f, simulate, side);
  }

  public long receiveFluid(FluidStack fluid, IFluidHandler.FluidAction simulate, @Nullable Direction direction) {
    if (this.level == null || isRemote() || direction == null || !checkRedstone() || !canReceiveFluid(direction))
      return 0;
    int received = 0;
    var cables = getCables();

    var insertionGuard = this.netInsertionGuard;
    if (insertionGuard.isTrue())
      return 0;
    insertionGuard.setTrue();

    try {
      if (simulate.execute()) {
        startIndex++; // round robin!
      }

      for (var cable : cables) {
        int amount = fluid.getAmount() - received;
        if (amount <= 0)
          break;
        fluid.setAmount(amount);
        if (!cable.sides.isEmpty() && cable.isActive()) {
          received += cable.pushFluid(fluid, simulate, direction, this);
        }
      }
      return received;
    } finally {
      insertionGuard.setFalse();
    }
  }

  private int pushFluid(FluidStack fluid, IFluidHandler.FluidAction simulate, @Nullable Direction direction, FluidCableEntity cable) {
    if (!(getLevel() instanceof ServerLevel serverLevel))
      throw new RuntimeException("Expected server level");

    int received = 0;
    for (int i = 0; i < 6; ++i) {
      // Shift by tick count to ensure that it distributes evenly on average
      Direction side = Direction.from3DDataValue((i + serverLevel.getServer().getTickCount()) % 6);
      if (!this.sides.contains(side))
        continue;

      int amount = Math.min(fluid.getAmount() - received, this.tier.getFluidTransfer());
      if (amount <= 0)
        break;
      if (cable.equals(this) && side.equals(direction) || !canExtractFluid(side))
        continue;
      BlockPos pos = this.worldPosition.relative(side);
      if (direction != null && cable.getBlockPos().relative(direction).equals(pos))
        continue;
      fluid.setAmount(amount);
      received += receive(getLevel(), pos, side.getOpposite(), fluid, simulate);
    }
    return received;
  }

  private int receive(Level level, BlockPos pos, Direction side, FluidStack fluidStack, IFluidHandler.FluidAction simulate) {
    var tile = level.getBlockEntity(pos);
    var fluid = tile != null ? tile.getCapability(ForgeCapabilities.FLUID_HANDLER, side).orElse(null) : null;
    if (fluid == null) return 0;
    return fluid.isFluidValid(0, fluidStack) ? fluid.fill(fluidStack, simulate) : 0;
  }

  public boolean canExtractFluid(@Nullable Direction side) {
    boolean value = side == null || isFluidPresent(side) && this.sideConfig.getType(side).canExtract;
    return value;
  }

  public boolean canReceiveFluid(@Nullable Direction side) {
    boolean value = side == null || isFluidPresent(side) && this.sideConfig.getType(side).canReceive;
    return value;
  }
}
