package es.degrassi.forge.api.core.common;

import net.minecraft.nbt.CompoundTag;

public interface IType {
  void markDirty();
  String getId();

  default void clientTick() {}
  default void serverTick() {}

  void serialize(CompoundTag nbt);
  void deserialize(CompoundTag nbt);
}
