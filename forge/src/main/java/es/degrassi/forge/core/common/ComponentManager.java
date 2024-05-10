package es.degrassi.forge.core.common;

import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.core.common.component.BarComponent;
import es.degrassi.forge.core.common.component.ComponentIOMode;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.common.component.ExperienceComponent;
import es.degrassi.forge.core.common.component.FluidComponent;
import es.degrassi.forge.core.common.component.ItemComponent;
import es.degrassi.forge.core.common.component.ProgressComponent;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.wrapper.DegrassiFluidHandler;
import es.degrassi.forge.core.common.wrapper.DegrassiItemStackHandler;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.util.INBTSerializable;

@SuppressWarnings("unused")
@Getter
public final class ComponentManager extends Manager<IComponent> implements INBTSerializable<ListTag> {
  private final DegrassiItemStackHandler itemHandler;
  private final DegrassiFluidHandler fluidHandler;

  private final Map<String, IComponent> components = new LinkedHashMap<>();
  private boolean initialized;

  public ComponentManager(MachineEntity<?> entity) {
    super(entity);
    itemHandler = new DegrassiItemStackHandler(this);
    fluidHandler = new DegrassiFluidHandler(this);
    add(itemHandler);
    add(fluidHandler);
  }

  public ComponentManager(List<IComponent> components, MachineEntity<?> entity) {
    this(entity);
    components.forEach(component -> {
      if (component instanceof DegrassiItemStackHandler handler) {
        itemHandler.addAll(handler.getComponents());
      } else if (component instanceof DegrassiFluidHandler handler) {
        fluidHandler.addAll(handler.getComponents());
      } else {
        add(component);
      }
    });
  }

  public void init() {
    components.clear();
    get().forEach(component -> {
      if (component instanceof DegrassiItemStackHandler handler) {
        handler.getComponents().forEach(comp -> components.put(comp.getId(), comp));
      } else if (component instanceof DegrassiFluidHandler handler) {
        handler.getComponents().forEach(comp -> components.put(comp.getId(), comp));
      } else {
        components.put(component.getId(), component);
      }
    });
    initialized = true;
  }

  @Override
  public void add(IComponent value) {
    if (value instanceof ItemComponent component)
      itemHandler.add(component);
    else if (value instanceof FluidComponent component)
      fluidHandler.add(component);
    else
      super.add(value);
  }

  public ComponentManager addEnergy(int capacity, int maxInput, int maxOutput, String id) {
    return addEnergy(capacity, maxInput, maxOutput, id, ComponentIOMode.BOTH);
  }

  public ComponentManager addEnergy(int capacity, int maxInput, int maxOutput, String id, ComponentIOMode mode) {
    get().add(new EnergyComponent(this, capacity, maxInput, maxOutput, getEntity(), id, mode));
    initialized = false;
    return this;
  }

  public ComponentManager addEnergy(int capacity, int transfer, String id) {
    return addEnergy(capacity, transfer, transfer, id);
  }

  public ComponentManager addEnergy(int capacity, int transfer, String id, ComponentIOMode mode) {
    return addEnergy(capacity, transfer, transfer, id, mode);
  }

  public ComponentManager addEnergy(int capacity, String id) {
    return addEnergy(capacity, capacity, id);
  }

  public ComponentManager addEnergy(int capacity, String id, ComponentIOMode mode) {
    return addEnergy(capacity, capacity, id, mode);
  }

  public ComponentManager addItem(String id) {
    return addItem(id, ComponentIOMode.BOTH);
  }

  public ComponentManager addItem(String id, ComponentIOMode mode) {
    getItemHandler().add(new ItemComponent(this, id, getEntity(), mode));
    initialized = false;
    return this;
  }

  public ComponentManager addItem(String id, boolean whitelist, Item...filter) {
    return addItem(id, whitelist, ComponentIOMode.BOTH, filter);
  }

  public ComponentManager addItem(String id, boolean whitelist, ComponentIOMode mode, Item...filter) {
    getItemHandler().add(new ItemComponent(this, id, whitelist, getEntity(), mode, filter));
    initialized = false;
    return this;
  }

  public ComponentManager addItem(String id, Item... filter) {
    return addItem(id, false, ComponentIOMode.BOTH, filter);
  }

  public ComponentManager addItem(String id, ComponentIOMode mode, Item... filter) {
    return addItem(id, false, mode, filter);
  }

  public ComponentManager addExperience(float capacity, String id) {
    return addExperience(capacity, id, ComponentIOMode.BOTH);
  }

  public ComponentManager addExperience(float capacity, String id, ComponentIOMode mode) {
    get().add(new ExperienceComponent(this, capacity, getEntity(), id, mode));
    initialized = false;
    return this;
  }

  public ComponentManager addProgress() {
    get().add(new ProgressComponent(this, getEntity()));
    initialized = false;
    return this;
  }

  public ComponentManager addFluid(int capacity, String id, boolean whiteList, Fluid...filter) {
    return addFluid(capacity, id, whiteList, ComponentIOMode.BOTH, filter);
  }
  public ComponentManager addFluid(int capacity, String id, boolean whiteList, ComponentIOMode mode, Fluid...filter) {
    getFluidHandler().add(new FluidComponent(this, id, whiteList, capacity, getEntity(), mode, filter));
    initialized = false;
    return this;
  }

  public ComponentManager addFluid(int capacity, String id) {
    return addFluid(capacity, id, false);
  }

  public ComponentManager addFluid(int capacity, String id, ComponentIOMode mode) {
    return addFluid(capacity, id, false, mode);
  }

  public ComponentManager addBar(double capacity, String id, ComponentIOMode mode) {
    get().add(new BarComponent(this, capacity, id, mode, getEntity()));
    initialized = false;
    return this;
  }

  public ComponentManager addBar(double capacity, String id) {
    return addBar(capacity, id, ComponentIOMode.BOTH);
  }

  public Optional<IComponent> getComponent(String id) {
    if (!initialized) init();
    return components.containsKey(id) ? Optional.of(components.get(id)) : Optional.empty();
  }

  public List<? extends IComponent> getComponentsByType(String type) {
    if (!initialized) init();
    return switch (type) {
      case "item", "ITEM" -> getItemHandler().getComponents();
      case "energy", "ENERGY" -> get().stream().filter(component -> component instanceof EnergyComponent).toList();
      case "experience", "EXPERIENCE" -> get().stream().filter(component -> component instanceof ExperienceComponent).toList();
      case "fluid", "FLUID" -> getFluidHandler().getComponents();
      default -> throw new IllegalStateException("Unexpected value: " + type);
    };
  }

  public void clientTick() {
    get().forEach(IComponent::clientTick);
  }

  public void serverTick() {
    get().forEach(IComponent::serverTick);
  }

  @Override
  public ListTag serializeNBT() {
    ListTag nbt = new ListTag();
    get().forEach(component -> nbt.add(component.serialize()));
    return nbt;
  }

  @Override
  public void deserializeNBT(ListTag nbt) {
    nbt.forEach(tag -> {
      if (tag instanceof CompoundTag compound)
        get().forEach(component -> {
          if (compound.contains("id", Tag.TAG_STRING) && component.getId().equals(compound.getString("id")))
            component.deserialize(compound);
        });
    });
    initialized = false;
    init();
  }

  public void markDirty() {
    get().forEach(IComponent::markDirty);
  }

  @Override
  public String toString() {
    return "Component" + super.toString();
  }

  public ComponentManager mergeWith(ComponentManager other) {
    ComponentManager newManager = new ComponentManager(get(), getEntity());
    other.get().forEach(component -> {
      if (component instanceof DegrassiItemStackHandler handler) {
        newManager.getItemHandler().addAll(handler.getComponents());
      } else if (component instanceof DegrassiFluidHandler handler) {
        newManager.getFluidHandler().addAll(handler.getComponents());
      } else {
        newManager.add(component);
      }
    });
    newManager.initialized = false;
    newManager.init();
    return newManager;
  }
}
