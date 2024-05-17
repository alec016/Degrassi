package es.degrassi.forge.core.common.wrapper;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.component.ComponentIOMode;
import es.degrassi.forge.core.common.component.FluidComponent;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
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

@Getter
public class DegrassiFluidHandler implements IFluidHandler, IComponent {
  private final List<FluidComponent> components;
  private final ComponentManager manager;
  private final AtomicInteger count = new AtomicInteger(0);
  private final String id = "fluidHandler";

  public DegrassiFluidHandler(ComponentManager manager) {
    components = new LinkedList<>();
    this.manager = manager;
  }

  @Override
  public void markDirty() {
    components.forEach(IComponent::markDirty);
  }

  public void onContentsChanged() {
    markDirty();
  }

  public void add(FluidComponent component) {
    count.getAndIncrement();
    this.components.add(component);
  }

  public void addAll(Collection<FluidComponent> components) {
    count.getAndAdd(components.size());
    this.components.addAll(components);
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
    AtomicBoolean hasThisFluid = new AtomicBoolean(false);
    for (int i = 0; i < components.size(); i++) {
      if (components.get(i).getMode().output()) continue;
      FluidComponent component = components.get(i);
      if (component.getFluid().isFluidEqual(resource))
        hasThisFluid.set(true);
      else if (hasThisFluid.get()) continue;
      FluidStack fluid = component.getFluid();
      int capacity = component.getCapacity();
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
        component.setFluid(fluid);
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
      } else {
        fluid.setAmount(capacity);
      }
      onContentsChanged();
      return filled;
    }
    onContentsChanged();
    return 0;
  }

  @Override
  public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
    FluidStack stack = FluidStack.EMPTY;
    if (resource.isEmpty()) return stack;
    for (FluidComponent component : components) {
      if (!stack.isEmpty()) continue;
      if (component.getFluid().isEmpty()) continue;
      if (!component.getFluid().isFluidEqual(resource)) continue;
      FluidStack drained = component.drain(resource, action);
      if (!drained.isEmpty() && drained.getAmount() > 0)
        stack = drained;
    }
    return stack;
  }

  @Override
  public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
    return FluidStack.EMPTY;
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
  public void setMode(ComponentIOMode mode) {}

  @Override
  public CompoundTag serialize() {
    CompoundTag nbt = new CompoundTag();
    ListTag listTag = new ListTag();
    CompoundTag compound;
    for (int i = 0; i < components.size(); i++) {
      compound = components.get(i).serialize();
      compound.putInt("Slot", i);
      listTag.add(i, compound);
    }
    nbt.put("list", listTag);
    nbt.putString("id", getId());
    nbt.putInt("Size", components.size());
    return nbt;
  }

  @Override
  public void deserialize(CompoundTag nbt) {
    setSize(nbt.contains("Size") ? nbt.getInt("Size") : components.size());

    ListTag listTag = nbt.getList("list", Tag.TAG_COMPOUND);

    for (int i = 0; i < listTag.size(); i++) {
      CompoundTag itemNbt = listTag.getCompound(i);
      int slot = itemNbt.getInt("Slot");
      FluidComponent component = components.get(slot);
      if (component == null)
        components.set(slot, new FluidComponent(getManager(), itemNbt.getString("id"), false, 0, getManager().getEntity(), null));
      component = components.get(slot);
      component.deserialize(itemNbt);
    }
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
      FluidComponent component = components.get(i);
      FluidStack fluid = component.getFluid();
      int capacity = component.getCapacity();
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
        component.setFluid(fluid);
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
      return filled;
    }
    return 0;
  }

  public @NotNull FluidStack drainRecipe(int maxDrain, FluidAction action) {
    return FluidStack.EMPTY;
  }

  public @NotNull FluidStack drainRecipe(FluidStack resource, FluidAction action) {
    FluidStack stack = FluidStack.EMPTY;
    if (resource.isEmpty()) return stack;
    for (FluidComponent component : components) {
      if (!stack.isEmpty()) continue;
      if (component.getFluid().isEmpty()) continue;
      if (!component.getFluid().isFluidEqual(resource)) continue;
      FluidStack drained = component.drainRecipe(resource, action);
      if (!drained.isEmpty() && drained.getAmount() > 0)
        stack = drained;
    }
    return stack;
  }

  public void setSize(int size) {
    while (components.size() < size) {
      components.add(null);
    }
    count.set(components.size());
  }

  @Override
  public String toString() {
    return asJson().toString();
  }

  public JsonObject asJson() {
    JsonObject json = new JsonObject();
    JsonArray components = new JsonArray();
    this.components.forEach(component -> components.add(component.asJson()));
    json.add("components", components);
    json.addProperty("size", components.size());
    json.addProperty("type", "fluidHandler");
    return json;
  }

  @Override
  public DegrassiFluidHandler copy(MachineEntity<?> entity, ComponentManager manager) {
    DegrassiFluidHandler handler = new DegrassiFluidHandler(manager);
    handler.addAll(components.stream().map(component -> component.copy(entity, manager)).toList());
    return handler;
  }
}
