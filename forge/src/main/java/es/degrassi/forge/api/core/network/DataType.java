package es.degrassi.forge.api.core.network;

import es.degrassi.forge.Degrassi;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class DataType<D extends IData<T>, T> {
  public static final ResourceKey<Registry<DataType<?, ?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(Degrassi.rl("data_type"));
  public static <T, D extends IData<T>> DataType<D, T> create(Class<T> type, BiFunction<Supplier<T>, Consumer<T>, ISyncable<D, T>> builder, BiFunction<Short, FriendlyByteBuf, D> reader) {
    return new DataType<>(type, builder, reader);
  }

  private final Class<T> type;
  private final BiFunction<Supplier<T>, Consumer<T>, ISyncable<D, T>> builder;
  private final BiFunction<Short, FriendlyByteBuf, D> reader;
  private DataType(Class<T> type, BiFunction<Supplier<T>, Consumer<T>, ISyncable<D, T>> builder, BiFunction<Short, FriendlyByteBuf, D> reader) {
    this.type = type;
    this.builder = builder;
    this.reader = reader;
  }
  public ISyncable<D, T> createSyncable(Supplier<T> supplier, Consumer<T> consumer) {
    return this.builder.apply(supplier, consumer);
  }
  public D readData(short id, FriendlyByteBuf buffer) {
    return this.reader.apply(id, buffer);
  }
  @SuppressWarnings("unchecked")
  public static <T> ISyncable<IData<T>, T> createSyncable(Class<T> type, Supplier<T> supplier, Consumer<T> consumer) {
    Optional<DataType<IData<T>, T>> dataType = Degrassi.dataRegistrar().entrySet().stream().filter(entry -> entry.getValue().type == type).map(entry -> (DataType<IData<T>, T>)entry.getValue()).findFirst();
    if(dataType.isPresent())
      return dataType.get().createSyncable(supplier, consumer);
    throw new IllegalArgumentException("Couldn't create Syncable for provided type: " + type.getName() + ". No registered DataType for this type.");
  }
  public ResourceLocation getId() {
    return Degrassi.dataRegistrar().getId(this);
  }
}
