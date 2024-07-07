package es.degrassi.forge.core.data;

import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.init.ItemRegistration;
import es.degrassi.forge.core.tiers.Chest;
import es.degrassi.forge.core.tiers.Furnace;
import es.degrassi.forge.core.tiers.PhotovoltaicCell;
import es.degrassi.forge.core.tiers.SolarPanel;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
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
    for (Furnace tier : Furnace.values()) {
      this.tag(DegrassiTags.Items.FURNACE.get()).add(BlockRegistration.FURNACE.get(tier).asItem());
    }

    for (Chest tier : Chest.values()) {
      this.tag(DegrassiTags.Items.CHEST.get()).add(BlockRegistration.CHEST.get(tier).asItem());
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

    this.tag(DegrassiTags.Items.DIGITAL_CONTROLLER.get())
      .add(BlockRegistration.DIGITAL_CONTROLLER.get().asItem());

    this.tag(DegrassiTags.Items.DIGITAL.get())
      .addTag(DegrassiTags.Items.DIGITAL_CONTROLLER.get());

    this.tag(DegrassiTags.Items.MACHINE.get())
      .addTag(DegrassiTags.Items.FURNACE.get())
      .addTag(DegrassiTags.Items.SP.get())
      .addTag(DegrassiTags.Items.DIGITAL_CONTROLLER.get())
      .addTag(DegrassiTags.Items.CHEST.get());

    this.tag(DegrassiTags.Items.FRAME.get())
      .add(BlockRegistration.MELTER_FRAME.get().asItem());

    this.tag(DegrassiTags.Items.MBPARTS.get())
      .add(BlockRegistration.ENERGY_HATCH.getAll().stream().map(Block::asItem).toArray(Item[]::new))
      .add(BlockRegistration.FLUID_INPUT_TANK.getAll().stream().map(Block::asItem).toArray(Item[]::new))
      .add(BlockRegistration.QUADRUPLE_FLUID_INPUT_TANK.getAll().stream().map(Block::asItem).toArray(Item[]::new))
      .add(BlockRegistration.FLUID_OUTPUT_TANK.getAll().stream().map(Block::asItem).toArray(Item[]::new))
      .add(BlockRegistration.INPUT_BUS.getAll().stream().map(Block::asItem).toArray(Item[]::new))
      .add(BlockRegistration.OUTPUT_BUS.getAll().stream().map(Block::asItem).toArray(Item[]::new));

    this.tag(DegrassiTags.Items.CONTROLLER.get())
      .add(BlockRegistration.MELTER_CONTROLLER.get().asItem());

    this.tag(DegrassiTags.Items.MULTIBLOCK.get())
      .addTag(DegrassiTags.Items.CONTROLLER.get())
      .addTag(DegrassiTags.Items.MBPARTS.get())
      .addTag(DegrassiTags.Items.FRAME.get());

    this.tag(DegrassiTags.Items.WRENCH.get())
      .add(ItemRegistration.WRENCH.get());

    this.tag(DegrassiTags.Items.WRENCHES.get())
      .addTag(DegrassiTags.Items.WRENCH.get());
  }
}
