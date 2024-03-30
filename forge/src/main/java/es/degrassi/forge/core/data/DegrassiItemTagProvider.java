package es.degrassi.forge.core.data;

import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.tiers.CableTier;
import es.degrassi.forge.core.tiers.SolarPanel;
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
    this.tag(DegrassiTags.Items.ENERGY_CABLE.get())
      .add(
        BlockRegistration.ENERGY_CABLE.get(CableTier.BASIC).asItem(),
        BlockRegistration.ENERGY_CABLE.get(CableTier.ADVANCE).asItem(),
        BlockRegistration.ENERGY_CABLE.get(CableTier.EXTREME).asItem()
      );

    this.tag(DegrassiTags.Items.FLUID_CABLE.get())
      .add(
        BlockRegistration.FLUID_CABLE.get(CableTier.BASIC).asItem(),
        BlockRegistration.FLUID_CABLE.get(CableTier.ADVANCE).asItem(),
        BlockRegistration.FLUID_CABLE.get(CableTier.EXTREME).asItem()
      )
    ;

    this.tag(DegrassiTags.Items.ITEM_CABLE.get())
//      .add()
      ;

    this.tag(DegrassiTags.Items.CABLE.get())
      .addTag(DegrassiTags.Items.ENERGY_CABLE.get())
      .addTag(DegrassiTags.Items.FLUID_CABLE.get())
      .addTag(DegrassiTags.Items.ITEM_CABLE.get());

    this.tag(DegrassiTags.Items.FURNACE.get())
      .add(
        BlockRegistration.IRON_FURNACE.get().asItem(),
        BlockRegistration.GOLD_FURNACE.get().asItem(),
        BlockRegistration.DIAMOND_FURNACE.get().asItem(),
        BlockRegistration.EMERALD_FURNACE.get().asItem(),
        BlockRegistration.NETHERITE_FURNACE.get().asItem()
      );

    this.tag(DegrassiTags.Items.SP.get())
      .add(
        BlockRegistration.SP.get(SolarPanel.T1).asItem(),
        BlockRegistration.SP.get(SolarPanel.T2).asItem(),
        BlockRegistration.SP.get(SolarPanel.T3).asItem(),
        BlockRegistration.SP.get(SolarPanel.T4).asItem(),
        BlockRegistration.SP.get(SolarPanel.T5).asItem(),
        BlockRegistration.SP.get(SolarPanel.T6).asItem(),
        BlockRegistration.SP.get(SolarPanel.T7).asItem(),
        BlockRegistration.SP.get(SolarPanel.T8).asItem()
      );

    this.tag(DegrassiTags.Items.MACHINE.get())
      .addTag(DegrassiTags.Items.FURNACE.get())
      .addTag(DegrassiTags.Items.SP.get())
      .addTag(DegrassiTags.Items.CABLE.get());
  }
}
