package es.degrassi.forge.io.serializer;

import es.degrassi.forge.util.Cast;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class BooleanSerializer<T extends Boolean>
  implements INBTSerializer<T>
{
  @Override
  public void serialize(CompoundTag nbt, String key, T value)
  {
    if(value != null)
      nbt.putBoolean(key, value);
  }

  @Override
  public T deserialize(CompoundTag nbt, String key)
  {
    return nbt.contains(key, Tag.TAG_BYTE) ? Cast.cast(nbt.getBoolean(key)) : Cast.cast(false);
  }
}
