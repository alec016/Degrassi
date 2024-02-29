package es.degrassi.forge.api.impl.codec;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import es.degrassi.forge.api.codec.NamedCodec;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class DefaultOptionalFieldCodec<A> extends NamedMapCodec<A> {

    public static <A> DefaultOptionalFieldCodec<A> of(String fieldName, NamedCodec<A> elementCodec, Supplier<A> defaultValue, String name) {
        return new DefaultOptionalFieldCodec<>(fieldName, elementCodec, defaultValue, name);
    }

    private final String fieldName;
    private final NamedCodec<A> elementCodec;
    private final Supplier<A> defaultValue;
    private final String name;

    private DefaultOptionalFieldCodec(String fieldName, NamedCodec<A> elementCodec, Supplier<A> defaultValue, String name) {
        this.fieldName = FieldCodec.toSnakeCase(fieldName);
        this.elementCodec = elementCodec;
        this.defaultValue = defaultValue;
        this.name = name;
    }

    @Override
    public <T> DataResult<A> decode(DynamicOps<T> ops, MapLike<T> input) {
        final T value = FieldCodec.tryGetValue(ops, input, fieldName);
        if (value == null) {
            return DataResult.success(defaultValue.get());
        }
        DataResult<A> result = elementCodec.read(ops, value);
        if(result.result().isPresent())
            return result;
//        if(result.error().isPresent())
//            DegrassiLogger.INSTANCE.warn("Couldn't parse \"{}\" for key \"{}\", using default value: {}\nError: {}", name, fieldName, defaultValue.get(), result.error().get().message());
        return DataResult.success(defaultValue.get());
    }

    @Override
    public <T> RecordBuilder<T> encode(A input, DynamicOps<T> ops, RecordBuilder<T> prefix) {
        if(!Objects.equals(input, defaultValue))
            return prefix.add(fieldName, elementCodec.encodeStart(ops, input));
        return prefix;
    }

    @Override
    public <T> Stream<T> keys(DynamicOps<T> ops) {
        return Stream.of(ops.createString(this.fieldName));
    }

    @Override
    public String name() {
        return this.name;
    }
}
