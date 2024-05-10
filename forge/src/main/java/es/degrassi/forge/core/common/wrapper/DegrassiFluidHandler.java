package es.degrassi.forge.core.common.wrapper;

import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.component.ComponentIOMode;
import es.degrassi.forge.core.common.component.FluidComponent;
import es.degrassi.forge.core.common.requirement.FluidRequirement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class DegrassiFluidHandler implements IFluidHandler, IComponent {
  private final List<FluidComponent> components;
  private final ComponentManager manager;
  @Nullable
  private FluidComponent currentComponent;
  private int currentIndex = -1;

  public DegrassiFluidHandler(ComponentManager manager) {
    components = new LinkedList<>();
    this.manager = manager;
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
    markDirty();
    currentIndex = -1;
    currentComponent = null;
  }

  public void add(FluidComponent component) {
    this.components.add(component);
  }

  public void addAll(Collection<FluidComponent> components) {
    this.components.addAll(components);
  }

  @Override
  public void markDirty() {
    components.forEach(IComponent::markDirty);
  }

  @Override
  public String getId() {
    return "fluidHandler";
  }

  @Override
  public void fill(IRequirement<?> requirement) {
    if (!(requirement instanceof FluidRequirement fluidReq)) return;
    AtomicBoolean inserted = new AtomicBoolean(false);
    components.forEach(component -> {
      if (inserted.get()) return;
      if (component.getFluid().isEmpty()) {
        inserted.set(true);
        component.fill(requirement);
      } else if (component.getFluid().getFluid().isSame(fluidReq.getFluid())) {
        inserted.set(true);
        component.fill(requirement);
      }
    });
  }

  @Override
  public ComponentIOMode getMode() {
    return ComponentIOMode.BOTH;
  }

  @Override
  public void setMode(ComponentIOMode mode) {

  }

  @Override
  public void serialize(CompoundTag nbt) {
  }

  @Override
  public CompoundTag serialize() {
    CompoundTag nbt = new CompoundTag();
    ListTag listTag = new ListTag();
    components.forEach(component -> listTag.add(component.serialize()));
    nbt.put("list", listTag);
    nbt.putString("id", getId());
    nbt.putInt("Size", components.size());
    return nbt;
  }

  @Override
  public void deserialize(CompoundTag nbt) {
    ListTag listTag = nbt.getList("list", Tag.TAG_COMPOUND);
    listTag.forEach(tag -> {
      if (tag instanceof CompoundTag fluidNbt) {
        if (fluidNbt.contains("id", Tag.TAG_STRING)) {
          components.stream().filter(component -> component.getId().equals(fluidNbt.getString("id"))).findFirst().ifPresent(component -> component.deserialize(fluidNbt));
        }
      }
    });
  }

  @Override
  public String getTypeString() {
    return "fluidHandler";
  }

  public int toComparatorPower() {
    AtomicInteger toComparatorPower = new AtomicInteger(0);
    components.forEach(comp -> toComparatorPower.set(toComparatorPower.get() + comp.toComparatorPower()));
    return toComparatorPower.get() / getTanks();
  }

  public float subSized() {
    AtomicReference<Float> subSized = new AtomicReference<>(0f);
    components.forEach(comp -> subSized.set(subSized.get() + comp.subSized()));
    return subSized.get() / getTanks();
  }

  public float getFillState() {
    AtomicReference<Float> fillState = new AtomicReference<>(0f);
    components.forEach(comp -> fillState.set(fillState.get() + comp.getFillState()));
    return fillState.get() / getTanks();
  }

  // recipe stuff
  public int fillRecipe(FluidStack resource, FluidAction action) {
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

  public @NotNull FluidStack drainRecipe(int maxDrain, FluidAction action) {
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

  public @NotNull FluidStack drainRecipe(FluidStack resource, FluidAction action) {
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
  public String toString() {
    return "DegrassiFluidHandler{" +
      "components=" + components +
      '}';
  }
}
