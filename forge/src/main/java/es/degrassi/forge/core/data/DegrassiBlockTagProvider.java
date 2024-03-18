package es.degrassi.forge.core.data;

import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.init.BlockRegistration;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class DegrassiBlockTagProvider extends BlockTagsProvider {
  public DegrassiBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
    super(output, lookupProvider, Degrassi.MODID, existingFileHelper);
  }

  @Override
  public void addTags(HolderLookup.Provider provider) {
    this.tag(DegrassiTags.Blocks.FURNACE.get())
      .add(
        BlockRegistration.IRON_FURNACE.get(),
        BlockRegistration.GOLD_FURNACE.get(),
        BlockRegistration.DIAMOND_FURNACE.get(),
        BlockRegistration.EMERALD_FURNACE.get(),
        BlockRegistration.NETHERITE_FURNACE.get()
      );

    this.tag(DegrassiTags.Blocks.SP.get())
      .add(
        BlockRegistration.SP1.get(),
        BlockRegistration.SP2.get(),
        BlockRegistration.SP3.get(),
        BlockRegistration.SP4.get(),
        BlockRegistration.SP5.get(),
        BlockRegistration.SP6.get(),
        BlockRegistration.SP7.get(),
        BlockRegistration.SP8.get()
      );

    this.tag(DegrassiTags.Blocks.MACHINE.get())
      .addTag(DegrassiTags.Blocks.FURNACE.get())
      .addTag(DegrassiTags.Blocks.SP.get());

    this.tag(BlockTags.NEEDS_IRON_TOOL)
      .addTag(DegrassiTags.Blocks.FURNACE.get())
      .addTag(DegrassiTags.Blocks.SP.get());

    this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .addTag(DegrassiTags.Blocks.FURNACE.get())
      .addTag(DegrassiTags.Blocks.SP.get());
  }
}
