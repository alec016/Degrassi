package es.degrassi.forge.core.common.component;

import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.network.component.BarPacket;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
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
  public void markDirty() {
    entity.setChanged();
    if(entity.getLevel() != null && !entity.getLevel().isClientSide())
      new BarPacket(amount, capacity, id, entity.getBlockPos())
        .sendToChunkListeners(entity.getLevel().getChunkAt(entity.getBlockPos()));
  }

  @Override
  public void fill(IRequirement<?> req) {}

  @Override
  public void serialize(@NotNull CompoundTag nbt) {
  }

  public CompoundTag serialize() {
    CompoundTag tag = new CompoundTag();
    tag.putDouble("amount", amount);
    tag.putDouble("capacity", capacity);
    tag.putString("mode", mode.serialize());
    tag.putString("id", id);
    return tag;
  }

  @Override
  public void deserialize(@NotNull CompoundTag nbt) {
    this.amount = nbt.getDouble("amount");
    this.capacity = nbt.getDouble("capacity");
    this.mode = ComponentIOMode.deserialize(nbt.getString("mode"));
  }

  @Override
  public String getTypeString() {
    return "bar";
  }

  public double getFilledPercentage() {
    return this.amount / this.capacity;
  }
}
