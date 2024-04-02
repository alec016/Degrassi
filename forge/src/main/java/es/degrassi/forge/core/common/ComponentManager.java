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
import java.util.List;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.util.INBTSerializable;

@SuppressWarnings("unused")
public final class ComponentManager extends Manager<IComponent> implements INBTSerializable<CompoundTag> {
  public ComponentManager(MachineEntity<?> entity) {
    super(entity);
  }

  public ComponentManager(List<IComponent> components, MachineEntity<?> entity) {
    super(components, entity);
  }

  public ComponentManager addEnergy(int capacity, int maxInput, int maxOutput, String id) {
    return addEnergy(capacity, maxInput, maxOutput, id, ComponentIOMode.BOTH);
  }

  public ComponentManager addEnergy(int capacity, int maxInput, int maxOutput, String id, ComponentIOMode mode) {
    get().add(new EnergyComponent(this, capacity, maxInput, maxOutput, getEntity(), id, mode));
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
    get().add(new ItemComponent(this, id, getEntity(), mode));
    return this;
  }

  public ComponentManager addItem(String id, boolean whitelist, Item...filter) {
    return addItem(id, whitelist, ComponentIOMode.BOTH, filter);
  }

  public ComponentManager addItem(String id, boolean whitelist, ComponentIOMode mode, Item...filter) {
    get().add(new ItemComponent(this, id, whitelist, getEntity(), mode, filter));
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
    return this;
  }

  public ComponentManager addProgress() {
    get().add(new ProgressComponent(this, getEntity()));
    return this;
  }

  public ComponentManager addFluid(int capacity, String id, boolean whiteList, Fluid...filter) {
    return addFluid(capacity, id, whiteList, ComponentIOMode.BOTH, filter);
  }
  public ComponentManager addFluid(int capacity, String id, boolean whiteList, ComponentIOMode mode, Fluid...filter) {
    get().add(new FluidComponent(this, id, whiteList, capacity, getEntity(), mode, filter));
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
    return this;
  }

  public ComponentManager addBar(double capacity, String id) {
    return addBar(capacity, id, ComponentIOMode.BOTH);
  }

  public Optional<IComponent> getComponent(String id) {
    return get().stream().filter(component -> component.getId().equals(id)).findFirst();
  }

  public List<IComponent> getComponentsByType(String type) {
    return switch (type) {
      case "item", "ITEM" -> get().stream().filter(component -> component instanceof ItemComponent).toList();
      case "energy", "ENERGY" -> get().stream().filter(component -> component instanceof EnergyComponent).toList();
      case "experience", "EXPERIENCE" -> get().stream().filter(component -> component instanceof ExperienceComponent).toList();
      case "fluid", "FLUID" -> get().stream().filter(component -> component instanceof FluidComponent).toList();
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
  public CompoundTag serializeNBT() {
    CompoundTag nbt = new CompoundTag();
    get().forEach(type -> type.serialize(nbt));
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    get().forEach(type -> type.deserialize(nbt));
  }

  public void markDirty() {
    get().forEach(IComponent::markDirty);
  }

  @Override
  public String toString() {
    return "Component" + super.toString();
  }
}
