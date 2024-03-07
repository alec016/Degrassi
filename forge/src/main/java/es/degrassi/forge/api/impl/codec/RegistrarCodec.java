package es.degrassi.forge.api.impl.codec;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrarManager;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.core.common.RequirementType;
import es.degrassi.forge.core.init.Registration;
import es.degrassi.forge.core.init.RequirementRegistration;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class RegistrarCodec<V> implements NamedCodec<V> {

    /** Vanilla registries **/
    public static final NamedCodec<Item> ITEM = of(registrar(Registries.ITEM), false);
    public static final NamedCodec<Block> BLOCK = of(registrar(Registries.BLOCK), false);
    public static final NamedCodec<Fluid> FLUID = of(registrar(Registries.FLUID), false);
    public static final NamedCodec<EntityType<?>> ENTITY = of(registrar(Registries.ENTITY_TYPE), false);
    public static final NamedCodec<Enchantment> ENCHANTMENT = of(registrar(Registries.ENCHANTMENT), false);
    public static final NamedCodec<MobEffect> EFFECT = of(registrar(Registries.MOB_EFFECT), false);

    /**Degrassi registries**/
    public static final NamedCodec<RequirementType<?>> REQUIREMENT = of(RequirementType.requirementRegistrar(), true);

    public static <T> Registrar<T> registrar(ResourceKey<Registry<T>> registryKey) {
        return Registration.REGISTRIES.get(registryKey);
    }

    public static final NamedCodec<ResourceLocation> DEGRASSI_LOC_CODEC = NamedCodec.STRING.comapFlatMap(
            s -> {
                try {
                    if(s.contains(":"))
                        return DataResult.success(new ResourceLocation(s));
                    else
                        return DataResult.success(new ResourceLocation(Degrassi.MODID, s));
                } catch (Exception e) {
                    return DataResult.error(e::getMessage);
                }
            },
            ResourceLocation::toString,
            "Degrassi Resource location"
    );

    public static <V> RegistrarCodec<V> of(Registrar<V> registrar, boolean isCM) {
        return new RegistrarCodec<>(registrar, isCM);
    }

    private final Registrar<V> registrar;
    private final boolean isDegrassi;

    private RegistrarCodec(Registrar<V> registrar, boolean isDegrassi) {
        this.registrar = registrar;
        this.isDegrassi = isDegrassi;
    }

    @Override
    public <T> DataResult<Pair<V, T>> decode(DynamicOps<T> ops, T input) {
        return (this.isDegrassi ? DEGRASSI_LOC_CODEC : DefaultCodecs.RESOURCE_LOCATION).decode(ops, input).flatMap(keyValuePair ->
                !this.registrar.contains(keyValuePair.getFirst())
                        ? DataResult.error(() -> "Unknown registry key in " + this.registrar.key() + ": " + keyValuePair.getFirst())
                        : DataResult.success(keyValuePair.mapFirst(this.registrar::get))
        );
    }

    @Override
    public <T> DataResult<T> encode(DynamicOps<T> ops, V input, T prefix) {
        return DefaultCodecs.RESOURCE_LOCATION.encode(ops, this.registrar.getId(input), prefix);
    }

    @Override
    public String name() {
        return this.registrar.key().location().toString();
    }
}
