package es.degrassi.forge.core.common.conduit.data;

import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.common.conduit.common.init.ConduitBlocks;
import es.degrassi.forge.core.common.conduit.common.tag.ConduitTags;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ConduitTagProvider extends BlockTagsProvider {
  public ConduitTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                            @Nullable ExistingFileHelper existingFileHelper) {
    super(output, lookupProvider, Degrassi.MODID, existingFileHelper);
  }

  @Override
  public void addTags(HolderLookup.Provider pProvider) {
    tag(ConduitTags.Blocks.REDSTONE_CONNECTABLE)
      .add(Blocks.PISTON, Blocks.STICKY_PISTON, Blocks.REDSTONE_LAMP, Blocks.NOTE_BLOCK, Blocks.DISPENSER, Blocks.DROPPER, Blocks.POWERED_RAIL, Blocks.ACTIVATOR_RAIL, Blocks.MOVING_PISTON)
      .addTags(BlockTags.DOORS, BlockTags.TRAPDOORS, BlockTags.REDSTONE_ORES);
    tag(ConduitTags.Blocks.ENERGY_CABLE)
      .addOptional(new ResourceLocation("mekanism", "basic_universal_cable"))
      .addOptional(new ResourceLocation("mekanism", "advanced_universal_cable"))
      .addOptional(new ResourceLocation("mekanism", "elite_universal_cable"))
      .addOptional(new ResourceLocation("mekanism", "ultimate_universal_cable"))
      .addOptional(new ResourceLocation("pipez", "energy_pipe"))
      .addOptional(new ResourceLocation("pipez", "universal_pipe"));

    tag(ConduitTags.Blocks.RELOCATION_NOT_SUPPORTED).add(ConduitBlocks.CONDUIT.get());
  }
}
