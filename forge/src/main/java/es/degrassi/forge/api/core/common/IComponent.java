package es.degrassi.forge.api.core.common;

import es.degrassi.forge.core.common.component.ComponentIOMode;
import net.minecraft.nbt.CompoundTag;

public interface IComponent extends IType {

  IManager<IComponent> getManager();
  void markDirty();
  String getId();

  ComponentIOMode getMode();
  void setMode(ComponentIOMode mode);

  default void clientTick() {}
  default void serverTick() {}

  void serialize(CompoundTag nbt);
  void deserialize(CompoundTag nbt);
}
