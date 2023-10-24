package es.degrassi.forge.io.serializer;

import es.degrassi.forge.io.NBTSerializer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

@NBTSerializer(ResourceLocation.class)
public class ResourceLocationSerializer
  implements INBTSerializer<ResourceLocation>
{
  @Override
  public void serialize(CompoundTag nbt, String key, ResourceLocation value)
  {
    if(value != null)
      nbt.putString(key, value.toString());
  }

  @Override
  public ResourceLocation deserialize(CompoundTag nbt, String key)
  {
    return nbt.contains(key, Tag.TAG_STRING) ? new ResourceLocation(nbt.getString(key)) : null;
  }
}
