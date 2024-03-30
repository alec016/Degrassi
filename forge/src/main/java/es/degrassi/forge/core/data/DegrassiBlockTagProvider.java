package es.degrassi.forge.core.data;

import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.tiers.CableTier;
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
    this.tag(DegrassiTags.Blocks.ENERGY_CABLE.get())
      .add(
        BlockRegistration.ENERGY_CABLE.get(CableTier.BASIC),
        BlockRegistration.ENERGY_CABLE.get(CableTier.ADVANCE),
        BlockRegistration.ENERGY_CABLE.get(CableTier.EXTREME)
      );

    this.tag(DegrassiTags.Blocks.FLUID_CABLE.get())
      .add(
        BlockRegistration.FLUID_CABLE.get(CableTier.BASIC),
        BlockRegistration.FLUID_CABLE.get(CableTier.ADVANCE),
        BlockRegistration.FLUID_CABLE.get(CableTier.EXTREME)
      )
    ;

    this.tag(DegrassiTags.Blocks.ITEM_CABLE.get())
//      .add()
    ;

    this.tag(DegrassiTags.Blocks.CABLE.get())
      .addTag(DegrassiTags.Blocks.ENERGY_CABLE.get())
      .addTag(DegrassiTags.Blocks.FLUID_CABLE.get())
      .addTag(DegrassiTags.Blocks.ITEM_CABLE.get());

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
        BlockRegistration.SP.get(SolarPanel.T1),
        BlockRegistration.SP.get(SolarPanel.T2),
        BlockRegistration.SP.get(SolarPanel.T3),
        BlockRegistration.SP.get(SolarPanel.T4),
        BlockRegistration.SP.get(SolarPanel.T5),
        BlockRegistration.SP.get(SolarPanel.T6),
        BlockRegistration.SP.get(SolarPanel.T7),
        BlockRegistration.SP.get(SolarPanel.T8)
      );

    this.tag(DegrassiTags.Blocks.MACHINE.get())
      .addTag(DegrassiTags.Blocks.FURNACE.get())
      .addTag(DegrassiTags.Blocks.SP.get())
      .addTag(DegrassiTags.Blocks.CABLE.get());

    this.tag(BlockTags.NEEDS_STONE_TOOL)
      .addTag(DegrassiTags.Blocks.CABLE.get());

    this.tag(BlockTags.NEEDS_IRON_TOOL)
      .addTag(DegrassiTags.Blocks.FURNACE.get())
      .addTag(DegrassiTags.Blocks.SP.get());

    this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .addTag(DegrassiTags.Blocks.FURNACE.get())
      .addTag(DegrassiTags.Blocks.SP.get())
      .addTag(DegrassiTags.Blocks.CABLE.get());
  }
}
