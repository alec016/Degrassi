package es.degrassi.forge.io.serializer;

import es.degrassi.forge.io.NBTSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

@NBTSerializer(String.class)
public class StringSerializer
  implements INBTSerializer<String>
{
  @Override
  public void serialize(CompoundTag nbt, String key, String value)
  {
    if(value != null)
      nbt.putString(key, value);
  }

  @Override
  public String deserialize(CompoundTag nbt, String key)
  {
    return nbt.contains(key, Tag.TAG_STRING) ? nbt.getString(key) : null;
  }
}
