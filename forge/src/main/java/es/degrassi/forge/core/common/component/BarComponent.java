package es.degrassi.forge.core.common.component;

import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.network.component.BarPacket;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

public class BarComponent implements IComponent {
  private final ComponentManager manager;
  private ComponentIOMode mode;
  private final String id;
  private double amount = 50, capacity;
  private final MachineEntity<?> entity;

  public BarComponent(ComponentManager manager, double capacity, String id, ComponentIOMode mode, MachineEntity<?> entity) {
    this.manager = manager;
    this.mode = mode;
    this.capacity = capacity;
    this.id = id;
    this.entity = entity;
  }

  @Override
  public ComponentManager getManager() {
    return manager;
  }

  @Override
  public void markDirty() {
    entity.setChanged();
    if(entity.getLevel() != null && !entity.getLevel().isClientSide())
      new BarPacket(amount, capacity, id, entity.getBlockPos())
        .sendToChunkListeners(entity.getLevel().getChunkAt(entity.getBlockPos()));
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void fill(IRequirement<?> req) {}

  @Override
  public ComponentIOMode getMode() {
    return mode;
  }

  @Override
  public void setMode(ComponentIOMode mode) {
    this.mode = mode;
  }

  @Override
  public void serialize(@NotNull CompoundTag nbt) {
    CompoundTag tag = new CompoundTag();
    tag.putDouble("amount", amount);
    tag.putDouble("capacity", capacity);
    tag.putString("mode", mode.serialize());
    nbt.put(id, tag);
  }

  @Override
  public void deserialize(@NotNull CompoundTag nbt) {
    if (nbt.contains(id)) {
      CompoundTag tag = nbt.getCompound(id);
      this.amount = tag.getDouble("amount");
      this.capacity = tag.getDouble("capacity");
      this.mode = ComponentIOMode.deserialize(tag.getString("mode"));
    }
  }

  public double getFilledPercentage() {
    return this.amount / this.capacity;
  }

  public double getAmount() {
    return this.amount;
  }

  public double getCapacity() {
    return this.capacity;
  }

  public void setAmount(double amount) {
    this.amount = amount;
  }

  public void setCapacity(double capacity) {
    this.capacity = capacity;
  }
}
