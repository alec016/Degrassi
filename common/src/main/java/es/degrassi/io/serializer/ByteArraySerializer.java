package es.degrassi.io.serializer;

import es.degrassi.io.NBTSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

@NBTSerializer(byte[].class)
public class ByteArraySerializer
  implements INBTSerializer<byte[]>
{
  @Override
  public void serialize(CompoundTag nbt, String key, byte[] value)
  {
    if(value != null)
      nbt.putByteArray(key, value);
  }

  @Override
  public byte[] deserialize(CompoundTag nbt, String key)
  {
    return nbt.contains(key, Tag.TAG_BYTE_ARRAY) ? nbt.getByteArray(key) : null;
  }
}