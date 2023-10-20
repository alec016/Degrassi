package es.degrassi.io.serializer;

import es.degrassi.io.NBTSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import java.util.UUID;

@NBTSerializer(UUID.class)
public class UUIDSerializer
  implements INBTSerializer<UUID>
{
  @Override
  public void serialize(CompoundTag nbt, String key, UUID value)
  {
    if(value != null)
      nbt.putUUID(key, value);
  }

  @Override
  public UUID deserialize(CompoundTag nbt, String key)
  {
    return nbt.contains(key, Tag.TAG_INT_ARRAY) ? nbt.getUUID(key) : null;
  }
}
