package es.degrassi.forge.core.data;

import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.init.BlockRegistration;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DegrassiItemTagProvider extends ItemTagsProvider {
  public DegrassiItemTagProvider(PackOutput arg, CompletableFuture<HolderLookup.Provider> completableFuture, CompletableFuture<TagLookup<Block>> completableFuture2, @Nullable ExistingFileHelper existingFileHelper) {
    super(arg, completableFuture, completableFuture2, Degrassi.MODID, existingFileHelper);
  }

  @Override
  public void addTags(HolderLookup.@NotNull Provider provider) {
    this.tag(DegrassiTags.Items.FURNACE.get())
      .add(
        BlockRegistration.IRON_FURNACE.get().asItem(),
        BlockRegistration.GOLD_FURNACE.get().asItem(),
        BlockRegistration.DIAMOND_FURNACE.get().asItem(),
        BlockRegistration.EMERALD_FURNACE.get().asItem(),
        BlockRegistration.NETHERITE_FURNACE.get().asItem()
      );

    this.tag(DegrassiTags.Items.MACHINE.get())
      .addTag(DegrassiTags.Items.FURNACE.get());
  }
}
