package es.degrassi.forge.core.common.wrapper;

import es.degrassi.forge.core.common.component.FluidComponent;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import java.util.List;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DegrassiFluidHandler implements IFluidHandler {
  private final List<FluidComponent> components;
  @Nullable
  private FluidComponent currentComponent;
  private int currentIndex = -1;

  public DegrassiFluidHandler(MachineEntity<?> entity) {
    this.components = entity.getComponentManager().getComponentsByType("fluid").stream().map(comp -> (FluidComponent) comp).toList();
  }
  @Override
  public int getTanks() {
    return components.size();
  }

  @Override
  public @NotNull FluidStack getFluidInTank(int i) {
    return components.get(i).getFluid();
  }

  @Override
  public int getTankCapacity(int i) {
    return components.get(i).getCapacity();
  }

  @Override
  public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
    return components.get(i).isFluidValid(fluidStack);
  }

  @Override
  public int fill(FluidStack resource, FluidAction action) {
    if (components.stream().anyMatch(comp -> comp.getMode().output())) return 0;
    for (int i = 0; i < components.size(); i++) {
      currentComponent = components.get(i);
      currentIndex = i;
      FluidStack fluid = currentComponent.getFluid();
      int capacity = currentComponent.getCapacity();
      if (resource.isEmpty() || !isFluidValid(i, resource)) {
        continue;
      }
      if (action.simulate()) {
        if (fluid.isEmpty()) {
          return Math.min(capacity, resource.getAmount());
        }
        if (!fluid.isFluidEqual(resource)) {
          return 0;
        }
        return Math.min(capacity - fluid.getAmount(), resource.getAmount());
      }
      if (fluid.isEmpty()) {
        fluid = new FluidStack(resource, Math.min(capacity, resource.getAmount()));
        currentComponent.setFluid(fluid);
        onContentsChanged();
        return fluid.getAmount();
      }
      if (!fluid.isFluidEqual(resource)) {
        continue;
      }
      int filled = capacity - fluid.getAmount();

      if (resource.getAmount() < filled) {
        fluid.grow(resource.getAmount());
        filled = resource.getAmount();
      }
      else {
        fluid.setAmount(capacity);
      }
      if (filled > 0)
        onContentsChanged();
      currentIndex = -1;
      currentComponent = null;
      return filled;
    }
    currentIndex = -1;
    currentComponent = null;
    return 0;
  }

  @Override
  public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
    if (components.stream().anyMatch(comp -> comp.getMode().input())) return FluidStack.EMPTY;
    for (int i = 0; i < components.size(); i++) {
      FluidComponent component = components.get(i);
      FluidStack fluid = component.getFluid();
      if (resource.isEmpty() || !resource.isFluidEqual(fluid)) {
        currentIndex = -1;
        continue;
      }
      currentIndex = i;
      currentComponent = component;
      return drain(resource.getAmount(), action);
    }
    currentIndex = -1;
    currentComponent = null;
    return FluidStack.EMPTY;
  }


  @Override
  public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
    if (components.stream().anyMatch(comp -> comp.getMode().input())) return FluidStack.EMPTY;
    if (currentComponent == null) return FluidStack.EMPTY;
    int drained = maxDrain;
    FluidStack fluid = currentComponent.getFluid();
    if (fluid.getAmount() < drained) {
      drained = fluid.getAmount();
    }
    FluidStack stack = new FluidStack(fluid, drained);
    if (action.execute() && drained > 0) {
      fluid.shrink(drained);
      onContentsChanged();
    }
    currentIndex = -1;
    currentComponent = null;
    return stack;
  }

  public void onContentsChanged() {
    currentIndex = -1;
    currentComponent = null;
  }
}
