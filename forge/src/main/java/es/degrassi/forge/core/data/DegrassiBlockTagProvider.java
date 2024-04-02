package es.degrassi.forge.core.data;

import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.tiers.CableTier;
import es.degrassi.forge.core.tiers.Chest;
import es.degrassi.forge.core.tiers.Furnace;
import es.degrassi.forge.core.tiers.SolarPanel;
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
    for (CableTier tier : CableTier.values()) {
      this.tag(DegrassiTags.Blocks.ENERGY_CABLE.get()).add(BlockRegistration.ENERGY_CABLE.get(tier));

      this.tag(DegrassiTags.Blocks.FLUID_CABLE.get()).add(BlockRegistration.FLUID_CABLE.get(tier));

      this.tag(DegrassiTags.Blocks.ITEM_CABLE.get())
//      .add()
      ;
    }

    this.tag(DegrassiTags.Blocks.CABLE.get())
      .addTag(DegrassiTags.Blocks.ENERGY_CABLE.get())
      .addTag(DegrassiTags.Blocks.FLUID_CABLE.get())
      .addTag(DegrassiTags.Blocks.ITEM_CABLE.get());

    for (Furnace tier : Furnace.values()) {
      this.tag(DegrassiTags.Blocks.FURNACE.get()).add(BlockRegistration.FURNACE.get(tier));
    }

    for (Chest tier : Chest.values()) {
      this.tag(DegrassiTags.Blocks.CHEST.get()).add(BlockRegistration.CHEST.get(tier));
    }

    for (SolarPanel tier : SolarPanel.values()) {
      this.tag(DegrassiTags.Blocks.SP.get()).add(BlockRegistration.SP.get(tier));
    }

    this.tag(DegrassiTags.Blocks.MACHINE.get())
      .addTag(DegrassiTags.Blocks.FURNACE.get())
      .addTag(DegrassiTags.Blocks.CHEST.get())
      .addTag(DegrassiTags.Blocks.SP.get());

    this.tag(BlockTags.NEEDS_STONE_TOOL)
      .addTag(DegrassiTags.Blocks.CABLE.get());

    this.tag(BlockTags.NEEDS_IRON_TOOL)
      .addTag(DegrassiTags.Blocks.FURNACE.get())
      .addTag(DegrassiTags.Blocks.CHEST.get())
      .addTag(DegrassiTags.Blocks.SP.get());

    this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .addTag(DegrassiTags.Blocks.FURNACE.get())
      .addTag(DegrassiTags.Blocks.SP.get())
      .addTag(DegrassiTags.Blocks.CHEST.get())
      .addTag(DegrassiTags.Blocks.CABLE.get());

    this.tag(BlockTags.MINEABLE_WITH_AXE)
      .addTag(DegrassiTags.Blocks.CHEST.get());
  }
}
