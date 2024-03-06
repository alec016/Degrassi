package es.degrassi.forge.core.common;

import es.degrassi.forge.api.core.common.IManager;
import es.degrassi.forge.api.core.common.IType;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;

public abstract class Manager<T extends IType> implements IManager<T> {
  private final List<T> list;
  private final MachineEntity entity;

  protected Manager(MachineEntity entity) {
    list = new LinkedList<>();
    this.entity = entity;
  }

  protected Manager(List<T> list, MachineEntity entity) {
    this.list = list;
    this.entity = entity;
  }

  @Override
  public List<T> get() {
    return list;
  }

  @Override
  public void add(T value) {
    list.add(value);
  }

  @Override
  public MachineEntity getEntity() {
    return entity;
  }

  @Override
  public void clientTick() {
    get().forEach(IType::clientTick);
  }

  @Override
  public void serverTick() {
    get().forEach(IType::serverTick);
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

  @Override
  public String toString() {
    return "Manager{" +
      "list=" + list +
      '}';
  }
}
