package es.degrassi.forge.core.data;

import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.common.machines.item.PhotovoltaicCellItem;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.init.ItemRegistration;
import es.degrassi.forge.core.tiers.CableTier;
import es.degrassi.forge.core.tiers.Furnace;
import es.degrassi.forge.core.tiers.PhotovoltaicCell;
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
    for(CableTier tier : CableTier.values()) {
      this.tag(DegrassiTags.Items.ENERGY_CABLE.get()).add(BlockRegistration.ENERGY_CABLE.get(tier).asItem());
      this.tag(DegrassiTags.Items.FLUID_CABLE.get()).add(BlockRegistration.FLUID_CABLE.get(tier).asItem());
      this.tag(DegrassiTags.Items.ITEM_CABLE.get())
//      .add()
      ;
    }

    this.tag(DegrassiTags.Items.CABLE.get())
      .addTag(DegrassiTags.Items.ENERGY_CABLE.get())
      .addTag(DegrassiTags.Items.FLUID_CABLE.get())
      .addTag(DegrassiTags.Items.ITEM_CABLE.get());

    for (Furnace tier : Furnace.values()) {
      this.tag(DegrassiTags.Items.FURNACE.get()).add(BlockRegistration.FURNACE.get(tier).asItem());
    }

    for (SolarPanel tier : SolarPanel.values()) {
      this.tag(DegrassiTags.Items.SP.get()).add(BlockRegistration.SP.get(tier).asItem());
    }

    for (PhotovoltaicCell tier : PhotovoltaicCell.values()) {
      this.tag(DegrassiTags.Items.PHOTOVOLTAIC_CELL.get()).add(ItemRegistration.PHOTOVOLTAIC_CELL.get(tier));
    }

    this.tag(DegrassiTags.Items.PANEL.get())
      .addTag(DegrassiTags.Items.SP.get())
      .addTag(DegrassiTags.Items.PHOTOVOLTAIC_CELL.get());

    this.tag(DegrassiTags.Items.MACHINE.get())
      .addTag(DegrassiTags.Items.FURNACE.get())
      .addTag(DegrassiTags.Items.SP.get());
  }
}
