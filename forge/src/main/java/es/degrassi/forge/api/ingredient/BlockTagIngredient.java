package es.degrassi.forge.api.ingredient;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.codec.impl.DefaultCodecs;
import es.degrassi.forge.util.PartialBlockState;
import es.degrassi.forge.util.TagUtil;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BlockTagIngredient implements IIngredient<PartialBlockState> {

    public static final NamedCodec<BlockTagIngredient> CODEC = DefaultCodecs.tagKey(Registry.BLOCK_REGISTRY).xmap(BlockTagIngredient::new, ingredient -> ingredient.tag, "Block tag ingredient");

    private final TagKey<Block> tag;
    private final Supplier<List<PartialBlockState>> ingredients;

    private BlockTagIngredient(TagKey<Block> tag) {
        this.tag = tag;
        this.ingredients = Suppliers.memoize(() -> TagUtil.getBlocks(this.tag).map(PartialBlockState::new).collect(Collectors.collectingAndThen(Collectors.toList(), ImmutableList::copyOf)));
    }

    public BlockTagIngredient create(TagKey<Block> tag) {
        return new BlockTagIngredient(tag);
    }

    @Override
    public List<PartialBlockState> getAll() {
        return this.ingredients.get();
    }

    @Override
    public boolean test(PartialBlockState partialBlockState) {
        return this.ingredients.get().stream().anyMatch(state -> state.getBlockState() == partialBlockState.getBlockState());
    }

    @Override
    public String toString() {
        return "#" + this.tag.location();
    }
}
