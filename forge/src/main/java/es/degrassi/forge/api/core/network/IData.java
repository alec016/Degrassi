package es.degrassi.forge.api.core.network;

import es.degrassi.forge.Degrassi;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public interface IData<T> {
  DataType<?, T> getType();
  short getID();
  T getValue();
  default void writeData(FriendlyByteBuf buffer) {
    if(getType().getId() == null)
      throw new IllegalStateException("Attempting to write invalid data to Custom Machine container syncing packet : " + getType().toString() + " is not registered !");
    buffer.writeResourceLocation(getType().getId());
    buffer.writeShort(getID());
  }
  static IData<?> readData(FriendlyByteBuf buffer) {
    ResourceLocation typeId = buffer.readResourceLocation();
    DataType<?, ?> type = Degrassi.dataRegistrar().get(typeId);
    if(type == null)
      throw new IllegalStateException("Attempting to read invalid IData : " + typeId + " is not a valid registered DataType !");
    short id = buffer.readShort();
    return type.readData(id, buffer);
  }
}
