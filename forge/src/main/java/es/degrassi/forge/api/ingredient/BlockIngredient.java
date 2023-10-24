package es.degrassi.forge.api.ingredient;

import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.util.PartialBlockState;

import java.util.Collections;
import java.util.List;

public class BlockIngredient implements IIngredient<PartialBlockState> {

    public static final BlockIngredient AIR = new BlockIngredient(PartialBlockState.AIR);
    public static final BlockIngredient ANY = new BlockIngredient(PartialBlockState.ANY);
    public static final BlockIngredient MACHINE = new BlockIngredient(PartialBlockState.MACHINE);

    public static final NamedCodec<BlockIngredient> CODEC = PartialBlockState.CODEC.xmap(BlockIngredient::new, ingredient -> ingredient.partialBlockState, "Block ingredient");

    private final PartialBlockState partialBlockState;

    public BlockIngredient(PartialBlockState partialBlockState) {
        this.partialBlockState = partialBlockState;
    }

    @Override
    public List<PartialBlockState> getAll() {
        return Collections.singletonList(this.partialBlockState);
    }

    @Override
    public boolean test(PartialBlockState partialBlockState) {
        return this.partialBlockState.getBlockState() == partialBlockState.getBlockState();
    }

    @Override
    public String toString() {
        return this.partialBlockState.toString();
    }
}
