package es.degrassi.forge.util.storage;

import es.degrassi.forge.init.gui.IComponent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

@SuppressWarnings("unused")
public abstract class FluidStorage implements IComponent, IFluidTank, IFluidHandler {
  private FluidStack fluid = FluidStack.EMPTY;
  protected Predicate<FluidStack> validator;
  private int capacity;

  public FluidStorage(int capacity) {
    this(capacity, e -> true);
  }

  public FluidStorage(int capacity, Predicate<FluidStack> validator) {
    this.capacity = capacity;
    this.validator = validator;
  }

  public FluidStorage(Fluid fluid, int amount, int capacity) {
    this(fluid, amount, capacity, e -> true);
  }

  public FluidStorage(Fluid fluid, int amount, int capacity, Predicate<FluidStack> validator) {
    this(new FluidStack(fluid, amount), capacity, validator);
  }

  public FluidStorage(FluidStack fluid, int capacity) {
    this(fluid, capacity, e -> true);
  }

  public FluidStorage(FluidStack fluid, int capacity, Predicate<FluidStack> validator) {
    this.fluid = fluid;
    this.capacity = capacity;
    this.validator = validator;
  }

  @Override
  public @NotNull FluidStack getFluid() {
    return fluid;
  }

  @Override
  public int getFluidAmount() {
    return fluid.getAmount();
  }

  @Override
  public int getCapacity() {
    return capacity;
  }

  public FluidStorage setValidator(Predicate<FluidStack> validator) {
    if (validator != null) {
      this.validator = validator;
    }
    return this;
  }

  public FluidStorage setCapacity(int capacity) {
    if (this.capacity == capacity) return this;
    this.capacity = capacity;
    if (fluid.getAmount() > capacity) fluid.setAmount(capacity);
    return this;
  }

  @Override
  public boolean isFluidValid(FluidStack fluidStack) {
    return validator.test(fluidStack);
  }

  @Override
  public int fill(@NotNull FluidStack fluidStack, FluidAction action) {
    if (fluidStack.isEmpty() || !isFluidValid(fluidStack)) return 0;
    if (action.simulate()) {
      if (fluid.isEmpty()) return Math.min(capacity, fluidStack.getAmount());
      if (!fluid.isFluidEqual(fluidStack))  return 0;
      return Math.min(capacity - fluid.getAmount(), fluidStack.getAmount());
    }
    if (fluid.isEmpty()) {
      fluid = new FluidStack(fluidStack, Math.min(capacity, fluidStack.getAmount()));
      onFluidChanged();
      return fluid.getAmount();
    }
    if (!fluid.isFluidEqual(fluidStack)) return 0;
    int filled = capacity - fluid.getAmount();
    if (fluidStack.getAmount() < filled) {
      fluid.grow(fluidStack.getAmount());
      filled = fluidStack.getAmount();
    } else fluid.setAmount(capacity);
    if (filled > 0)
      onFluidChanged();

    return filled;
  }

  @Override
  public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
    int drained = maxDrain;
    if (fluid.getAmount() < drained) drained = fluid.getAmount();
    FluidStack stack = new FluidStack(fluid, drained);
    if (action.execute() && drained > 0) {
      fluid.shrink(drained);
      onFluidChanged();
    }
    return stack;
  }

  public void setFluid(FluidStack fluid) {
    if(!this.fluid.isFluidEqual(fluid)) this.fluid = fluid;
  }

  @Override
  public @NotNull FluidStack drain(@NotNull FluidStack resource, FluidAction action) {
    if (resource.isEmpty() || !resource.isFluidEqual(fluid)) return FluidStack.EMPTY;
    return drain(resource.getAmount(), action);
  }

  @Override
  public int getTanks() {
    return 1;
  }

  @Override
  public @NotNull FluidStack getFluidInTank(int i) {
    return getFluid();
  }

  @Override
  public int getTankCapacity(int i) {
    return getCapacity();
  }

  @Override
  public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
    return isFluidValid(fluidStack);
  }

  public FluidStorage readFromNBT(CompoundTag nbt) {
    FluidStack fluid = FluidStack.loadFluidStackFromNBT(nbt);
    setFluid(fluid);
    return this;
  }

  public CompoundTag writeToNBT(CompoundTag nbt) {
    fluid.writeToNBT(nbt);
    return nbt;
  }

  protected void onFluidChanged() {

  }
}
