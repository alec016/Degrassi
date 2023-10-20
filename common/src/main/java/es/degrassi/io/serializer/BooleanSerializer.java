package es.degrassi.io.serializer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import es.degrassi.comon.util.Cast;

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
