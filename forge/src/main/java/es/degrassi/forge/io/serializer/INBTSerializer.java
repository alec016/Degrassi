package es.degrassi.forge.io.serializer;

import net.minecraft.nbt.CompoundTag;

public interface INBTSerializer<T>
{
  void serialize(CompoundTag nbt, String key, T value);

  T deserialize(CompoundTag nbt, String key);
}
