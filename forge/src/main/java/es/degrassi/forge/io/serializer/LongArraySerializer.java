package es.degrassi.forge.io.serializer;

import es.degrassi.forge.io.NBTSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

@NBTSerializer(long[].class)
public class LongArraySerializer
  implements INBTSerializer<long[]>
{
  @Override
  public void serialize(CompoundTag nbt, String key, long[] value)
  {
    if(value != null)
      nbt.putLongArray(key, value);
  }

  @Override
  public long[] deserialize(CompoundTag nbt, String key)
  {
    return nbt.contains(key, Tag.TAG_LONG_ARRAY) ? nbt.getLongArray(key) : null;
  }
}
