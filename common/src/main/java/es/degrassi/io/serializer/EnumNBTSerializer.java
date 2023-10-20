package es.degrassi.io.serializer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class EnumNBTSerializer<ET extends Enum<ET>>
  implements INBTSerializer<ET>
{
  final Class<ET> type;
  final ET[] constants;

  public EnumNBTSerializer(Class<ET> type)
  {
    this.type = type;
    this.constants = type.getEnumConstants();
  }

  @Override
  public void serialize(CompoundTag nbt, String key, ET value)
  {
    if(value != null)
      nbt.putInt(key, value.ordinal());
  }

  @Override
  public ET deserialize(CompoundTag nbt, String key)
  {
    if(nbt.contains(key, Tag.TAG_INT))
      return constants[Math.abs(nbt.getInt(key)) % constants.length];
    return null;
  }
}
