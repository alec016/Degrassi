package es.degrassi.forge.api.codec.impl;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import dev.architectury.registry.registries.Registrar;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.init.gui.renderer.GuiElementType;
import es.degrassi.forge.requirements.RequirementType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

public class RegistrarCodec<V> implements NamedCodec<V> {

    /** Vanilla registries **/
    public static final NamedCodec<Item> ITEM = of(Degrassi.registrar(Registry.ITEM_REGISTRY), false);
    public static final NamedCodec<Block> BLOCK = of(Degrassi.registrar(Registry.BLOCK_REGISTRY), false);
    public static final NamedCodec<Fluid> FLUID = of(Degrassi.registrar(Registry.FLUID_REGISTRY), false);
    public static final NamedCodec<EntityType<?>> ENTITY = of(Degrassi.registrar(Registry.ENTITY_TYPE_REGISTRY), false);
    public static final NamedCodec<Enchantment> ENCHANTMENT = of(Degrassi.registrar(Registry.ENCHANTMENT_REGISTRY), false);
    public static final NamedCodec<MobEffect> EFFECT = of(Degrassi.registrar(Registry.MOB_EFFECT_REGISTRY), false);

    /**CM registries**/
    public static final NamedCodec<RequirementType<?>> REQUIREMENT = of(Degrassi.requirementRegistrar(), true);
    public static final NamedCodec<GuiElementType<?>> GUI_ELEMENT = of(Degrassi.guiElementRegistrar(), true);

    public static final NamedCodec<ResourceLocation> CM_LOC_CODEC = NamedCodec.STRING.comapFlatMap(
            s -> {
                try {
                    if(s.contains(":"))
                        return DataResult.success(new ResourceLocation(s));
                    else
                        return DataResult.success(new ResourceLocation(Degrassi.MODID, s));
                } catch (Exception e) {
                    return DataResult.error(e.getMessage());
                }
            },
            ResourceLocation::toString,
            "CM Resource location"
    );

    public static <V> RegistrarCodec<V> of(Registrar<V> registrar, boolean isCM) {
        return new RegistrarCodec<>(registrar, isCM);
    }

    private final Registrar<V> registrar;
    private final boolean isCM;

    private RegistrarCodec(Registrar<V> registrar, boolean isCM) {
        this.registrar = registrar;
        this.isCM = isCM;
    }

    @Override
    public <T> DataResult<Pair<V, T>> decode(DynamicOps<T> ops, T input) {
        return (this.isCM ? CM_LOC_CODEC : DefaultCodecs.RESOURCE_LOCATION).decode(ops, input).flatMap(keyValuePair ->
                !this.registrar.contains(keyValuePair.getFirst())
                        ? DataResult.error("Unknown registry key in " + this.registrar.key() + ": " + keyValuePair.getFirst())
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
