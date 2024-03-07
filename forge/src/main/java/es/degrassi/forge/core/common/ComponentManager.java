package es.degrassi.forge.core.common;

import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.common.component.ExperienceComponent;
import es.degrassi.forge.core.common.component.ItemComponent;
import es.degrassi.forge.core.common.component.ProgressComponent;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import java.util.List;
import java.util.Optional;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public class ComponentManager extends Manager<IComponent> implements INBTSerializable<CompoundTag> {
  public ComponentManager(MachineEntity entity) {
    super(entity);
  }

  public ComponentManager(List<IComponent> components, MachineEntity entity) {
    super(components, entity);
  }

  public ComponentManager addEnergy(int capacity, int maxInput, int maxOutput, String id) {
    get().add(new EnergyComponent(this, capacity, maxInput, maxOutput, getEntity(), id));
    return this;
  }

  public ComponentManager addEnergy(int capacity, int transfer, String id) {
    get().add(new EnergyComponent(this, capacity, transfer, getEntity(), id));
    return this;
  }

  public ComponentManager addEnergy(int capacity, String id) {
    get().add(new EnergyComponent(this, capacity, getEntity(), id));
    return this;
  }

  public ComponentManager addItem(String id) {
    get().add(new ItemComponent(this, id, getEntity()));
    return this;
  }

  public ComponentManager addExperience(float capacity, String id) {
    get().add(new ExperienceComponent(this, capacity, getEntity(), id));
    return this;
  }

  public ComponentManager addProgress() {
    get().add(new ProgressComponent(this, getEntity()));
    return this;
  }

  public Optional<IComponent> getComponent(String id) {
    return get().stream().filter(component -> component.getId().equals(id)).findFirst();
  }

  public List<IComponent> getComponentsByType(String type) {
    return switch (type) {
      case "item", "ITEM" -> get().stream().filter(component -> component instanceof ItemComponent).toList();
      case "energy", "ENERGY" -> get().stream().filter(component -> component instanceof EnergyComponent).toList();
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
