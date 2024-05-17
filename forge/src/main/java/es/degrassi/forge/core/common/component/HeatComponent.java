package es.degrassi.forge.core.common.component;

import com.google.gson.JsonObject;
import es.degrassi.forge.api.core.capability.IHeatStorage;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.network.component.HeatPacket;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;

@Getter
@Setter
public class HeatComponent implements IComponent, IHeatStorage {
  public static final HeatComponent DUMMY = new HeatComponent(new ComponentManager(null), 0, "heat", ComponentIOMode.BOTH, null);

  private final MachineEntity<?> entity;
  private final ComponentManager manager;
  private final String id;
  private ComponentIOMode mode;
  private double heat = 0.0, heatCapacity;

  public HeatComponent(ComponentManager manager, double capacity, String id, ComponentIOMode mode, MachineEntity<?> entity) {
    this.manager = manager;
    this.id = id;
    this.mode = mode;
    this.entity = entity;
    this.heatCapacity = capacity;
  }

  @Override
  public void markDirty() {
    entity.setChanged();
    if(entity.getLevel() != null && !entity.getLevel().isClientSide())
      new HeatPacket(heat, heatCapacity, id, entity.getBlockPos())
        .sendToChunkListeners(entity.getLevel().getChunkAt(entity.getBlockPos()));
  }

  @Override
  public void fill(IRequirement<?> requirement) {

  }

  public void setHeat(double heat) {
    this.heat = heat;
    markDirty();
  }

  public void setHeatCapacity(double heatCapacity) {
    this.heatCapacity = heatCapacity;
    markDirty();
  }

  @Override
  public void setMode(ComponentIOMode mode) {
    this.mode = mode;
    markDirty();
  }

  @Override
  public CompoundTag serialize() {
    CompoundTag nbt = new CompoundTag();
    nbt.putString("id", id);
    nbt.putString("mode", mode.serialize());
    nbt.putDouble("heat", heat);
    nbt.putDouble("heatCapacity", heatCapacity);
    return nbt;
  }

  @Override
  public void deserialize(CompoundTag nbt) {
    mode = ComponentIOMode.deserialize(nbt.getString("mode"));
    heat = nbt.getDouble("heat");
    heatCapacity = nbt.getDouble("heatCapacity");
  }

  @Override
  public String getTypeString() {
    return "heat";
  }

  @Override
  public double receive(double maxReceive, boolean simulate) {
    if (!canInsert()) return 0;
    double toReceive = Math.min(maxReceive, heatCapacity - heat);
    if (!simulate)
      setHeat(heat + toReceive);
    return toReceive;
  }

  @Override
  public double extract(double maxExtract, boolean simulate) {
    if (!canExtract()) return 0;
    double toExtract = Math.min(maxExtract, heat);
    if (!simulate)
      setHeat(heat - toExtract);
    return toExtract;
  }

  public double receiveRecipe(double maxReceive, boolean simulate) {
    double toReceive = Math.min(maxReceive, heatCapacity - heat);
    if (!simulate)
      setHeat(heat + toReceive);
    return toReceive;
  }

  public double extractRecipe(double maxExtract, boolean simulate) {
    double toExtract = Math.min(maxExtract, heat);
    if (!simulate)
      setHeat(heat - toExtract);
    return toExtract;
  }

  public boolean canExtract() {
    return mode.outputWithAll();
  }

  public boolean canInsert() {
    return mode.inputWillAll();
  }

  public double getFilledPercentage() {
    return getHeat() / getHeatCapacity();
  }

  public HeatComponent copy(MachineEntity<?> entity, ComponentManager manager) {
    return new HeatComponent(manager, heatCapacity, id, mode, entity);
  }

  @Override
  public String toString() {
    return asJson().toString();
  }

  public JsonObject asJson() {
    JsonObject json = new JsonObject();
    json.addProperty("id", id);
    json.addProperty("mode", mode.serialize());
    json.addProperty("heat", heat);
    json.addProperty("heatCapacity", heatCapacity);
    json.addProperty("type", "heat");
    return json;
  }
}
