package es.degrassi.forge.io.serializer;

import es.degrassi.forge.io.NBTSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

@NBTSerializer(BlockPos.class)
public class BlockPosSerializer
  implements INBTSerializer<BlockPos>
{
  @Override
  public void serialize(CompoundTag nbt, String key, BlockPos value)
  {
    if(value != null)
    {
      CompoundTag tag = new CompoundTag();
      tag.putInt("x", value.getX());
      tag.putInt("y", value.getY());
      tag.putInt("z", value.getZ());
      nbt.put(key, tag);
    }
  }

  @Override
  public BlockPos deserialize(CompoundTag nbt, String key)
  {
    if(nbt.contains(key, Tag.TAG_COMPOUND))
    {
      CompoundTag tag = nbt.getCompound(key);
      return new BlockPos(tag.getInt("x"), tag.getInt("y"), tag.getInt("z"));
    }
    return null;
  }
}
