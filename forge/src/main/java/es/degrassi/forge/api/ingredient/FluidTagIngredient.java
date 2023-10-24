package es.degrassi.forge.api.ingredient;

import com.mojang.datafixers.util.Either;
import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.codec.impl.DefaultCodecs;
import es.degrassi.forge.util.Codecs;
import es.degrassi.forge.util.TagUtil;
import es.degrassi.forge.util.Utils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;

import java.util.List;
import java.util.function.Function;

public class FluidTagIngredient implements IIngredient<Fluid> {

    private static final NamedCodec<FluidTagIngredient> CODEC_FOR_DATAPACK = NamedCodec.STRING.xmap(FluidTagIngredient::create, FluidTagIngredient::toString, "Fluid tag ingredient");
    private static final NamedCodec<FluidTagIngredient> CODEC_FOR_KUBEJS = DefaultCodecs.tagKey(Registry.FLUID_REGISTRY).fieldOf("tag").xmap(FluidTagIngredient::new, ingredient -> ingredient.tag, "Fluid tag ingredient");
    public static final NamedCodec<FluidTagIngredient> CODEC = Codecs.either(CODEC_FOR_DATAPACK, CODEC_FOR_KUBEJS, "Fluid Tag Ingredient")
            .xmap(either -> either.map(Function.identity(), Function.identity()), Either::left, "Fluid tag ingredient");

    private final TagKey<Fluid> tag;

    private FluidTagIngredient(TagKey<Fluid> tag) {
        this.tag = tag;
    }

    public  static FluidTagIngredient create(String s) throws IllegalArgumentException {
        if(s.startsWith("#"))
            s = s.substring(1);
        if(!Utils.isResourceNameValid(s))
            throw new IllegalArgumentException(String.format("Invalid tag id : %s", s));
        TagKey<Fluid> tag = TagKey.create(Registry.FLUID_REGISTRY, new ResourceLocation(s));
        return new FluidTagIngredient(tag);
    }

    public static FluidTagIngredient create(TagKey<Fluid> tag) throws IllegalArgumentException {
        return new FluidTagIngredient(tag);
    }

    @Override
    public List<Fluid> getAll() {
        return TagUtil.getFluids(this.tag).toList();
    }

    @Override
    public boolean test(Fluid fluid) {
        return this.getAll().contains(fluid);
    }

    @Override
    public String toString() {
        return "#" + this.tag.location();
    }
}
