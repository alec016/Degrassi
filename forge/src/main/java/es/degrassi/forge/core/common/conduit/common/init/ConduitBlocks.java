package es.degrassi.forge.core.common.conduit.common.init;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.BlockEntry;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.common.conduit.common.blocks.ConduitBlock;
import es.degrassi.forge.core.common.conduit.data.model.ConduitBlockState;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.material.MapColor;

public class ConduitBlocks {
    private static final Registrate REGISTRATE = Degrassi.registrate();

    public static final BlockEntry<ConduitBlock> CONDUIT = REGISTRATE
        .block("conduit", ConduitBlock::new)
        .properties(props -> props.strength(1.5f, 10).noLootTable().noOcclusion().dynamicShape().mapColor(MapColor.STONE))
        .blockstate(ConduitBlockState::conduit)
        .tag(BlockTags.MINEABLE_WITH_PICKAXE)
        .register();


    public static void register() {}
}
