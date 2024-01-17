package es.degrassi.forge.init.gui.component;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

@SuppressWarnings("unused")
public class FluidComponent implements IComponent, IFluidTank, IFluidHandler {
  private FluidStack fluid = FluidStack.EMPTY;
  protected Predicate<FluidStack> validator;
  private int capacity;

  private final ComponentManager manager;

  public FluidComponent(ComponentManager manager, int capacity) {
    this(manager, capacity, e -> true);
  }

  public FluidComponent(ComponentManager manager, int capacity, Predicate<FluidStack> validator) {
    this.capacity = capacity;
    this.validator = validator;
    this.manager = manager;
  }

  public FluidComponent(ComponentManager manager, Fluid fluid, int amount, int capacity) {
    this(manager, fluid, amount, capacity, e -> true);
  }

  public FluidComponent(ComponentManager manager, Fluid fluid, int amount, int capacity, Predicate<FluidStack> validator) {
    this(manager, new FluidStack(fluid, amount), capacity, validator);
  }

  public FluidComponent(ComponentManager manager, FluidStack fluid, int capacity) {
    this(manager, fluid, capacity, e -> true);
  }

  public FluidComponent(ComponentManager manager, FluidStack fluid, int capacity, Predicate<FluidStack> validator) {
    this.fluid = fluid;
    this.capacity = capacity;
    this.validator = validator;
    this.manager = manager;
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

  public FluidComponent setValidator(Predicate<FluidStack> validator) {
    if (validator != null) {
      this.validator = validator;
    }
    return this;
  }

  public FluidComponent setCapacity(int capacity) {
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
      onChanged();
      return fluid.getAmount();
    }
    if (!fluid.isFluidEqual(fluidStack)) return 0;
    int filled = capacity - fluid.getAmount();
    if (fluidStack.getAmount() < filled) {
      fluid.grow(fluidStack.getAmount());
      filled = fluidStack.getAmount();
    } else fluid.setAmount(capacity);
    if (filled > 0)
      onChanged();

    return filled;
  }

  @Override
  public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
    int drained = maxDrain;
    if (fluid.getAmount() < drained) drained = fluid.getAmount();
    FluidStack stack = new FluidStack(fluid, drained);
    if (action.execute() && drained > 0) {
      fluid.shrink(drained);
      onChanged();
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

  public void deserializeNBT(Tag tag) {
    if (tag instanceof CompoundTag nbt) {
      FluidStack fluid = FluidStack.loadFluidStackFromNBT(nbt);
      setFluid(fluid);
    }
  }

  public Tag serializeNBT() {
    CompoundTag nbt = new CompoundTag();
    fluid.writeToNBT(nbt);
    return nbt;
  }

  @Override
  public ComponentManager getManager() {
    return manager;
  }

  public void onChanged() {
    manager.markDirty();
  }
}
