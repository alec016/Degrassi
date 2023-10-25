package es.degrassi.forge.api.ingredient;

import com.mojang.datafixers.util.Either;
import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.codec.impl.RegistrarCodec;
import es.degrassi.forge.util.Codecs;
import net.minecraft.core.Registry;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class FluidIngredient implements IIngredient<Fluid> {

    private static final NamedCodec<FluidIngredient> CODEC_FOR_DATAPACK = RegistrarCodec.FLUID.xmap(FluidIngredient::new, ingredient -> ingredient.fluid, "Fluid ingredient");
    private static final NamedCodec<FluidIngredient> CODEC_FOR_KUBEJS = RegistrarCodec.FLUID.fieldOf("fluid").xmap(FluidIngredient::new, ingredient -> ingredient.fluid, "Fluid ingredient");
    public static final NamedCodec<FluidIngredient> CODEC = Codecs.either(CODEC_FOR_DATAPACK, CODEC_FOR_KUBEJS, "Fluid Ingredient")
            .xmap(either -> either.map(Function.identity(), Function.identity()), Either::left, "Fluid ingredient");

    private final Fluid fluid;

    public FluidIngredient(Fluid fluid) {
        this.fluid = fluid;
    }

    @Override
    public List<Fluid> getAll() {
        return Collections.singletonList(this.fluid);
    }

    @Override
    public boolean test(Fluid fluid) {
        return this.fluid == fluid;
    }

    @Override
    public String toString() {
        return Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(this.fluid)).toString();
    }
}
