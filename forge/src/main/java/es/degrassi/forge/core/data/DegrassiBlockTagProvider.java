package es.degrassi.forge.core.data;

import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.common.conduit.data.ConduitTagProvider;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.tiers.Chest;
import es.degrassi.forge.core.tiers.Furnace;
import es.degrassi.forge.core.tiers.SolarPanel;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class DegrassiBlockTagProvider extends BlockTagsProvider {
  private final ConduitTagProvider conduits;
  public DegrassiBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
    super(output, lookupProvider, Degrassi.MODID, existingFileHelper);
    conduits = new ConduitTagProvider(output, lookupProvider, existingFileHelper);
  }

  @Override
  public void addTags(HolderLookup.Provider provider) {
    conduits.addTags(provider);

    for (Furnace tier : Furnace.values()) {
      this.tag(DegrassiTags.Blocks.FURNACE.get()).add(BlockRegistration.FURNACE.get(tier));
    }

    for (Chest tier : Chest.values()) {
      this.tag(DegrassiTags.Blocks.CHEST.get()).add(BlockRegistration.CHEST.get(tier));
    }

    for (SolarPanel tier : SolarPanel.values()) {
      this.tag(DegrassiTags.Blocks.SP.get()).add(BlockRegistration.SP.get(tier));
    }

    this.tag(DegrassiTags.Blocks.DIGITAL_CONTROLLER.get())
      .add(BlockRegistration.DIGITAL_CONTROLLER.get());

    this.tag(DegrassiTags.Blocks.DIGITAL.get())
      .addTag(DegrassiTags.Blocks.DIGITAL_CONTROLLER.get());

    this.tag(DegrassiTags.Blocks.MACHINE.get())
      .addTag(DegrassiTags.Blocks.FURNACE.get())
      .addTag(DegrassiTags.Blocks.CHEST.get())
      .addTag(DegrassiTags.Blocks.SP.get())
      .addTag(DegrassiTags.Blocks.DIGITAL_CONTROLLER.get())
      .addTag(DegrassiTags.Blocks.CONTROLLER.get());

    this.tag(DegrassiTags.Blocks.FRAME.get())
      .add(BlockRegistration.MELTER_FRAME.get());

    this.tag(DegrassiTags.Blocks.MBPARTS.get())
      .add(BlockRegistration.ENERGY_HATCH.getArr(Block[]::new))
      .add(BlockRegistration.FLUID_INPUT_TANK.getArr(Block[]::new))
      .add(BlockRegistration.QUADRUPLE_FLUID_INPUT_TANK.getArr(Block[]::new))
      .add(BlockRegistration.FLUID_OUTPUT_TANK.getArr(Block[]::new))
      .add(BlockRegistration.INPUT_BUS.getArr(Block[]::new))
      .add(BlockRegistration.OUTPUT_BUS.getArr(Block[]::new));

    this.tag(DegrassiTags.Blocks.CONTROLLER.get())
      .add(BlockRegistration.MELTER_CONTROLLER.get());

    this.tag(DegrassiTags.Blocks.MULTIBLOCK.get())
      .addTag(DegrassiTags.Blocks.CONTROLLER.get())
      .addTag(DegrassiTags.Blocks.MBPARTS.get())
      .addTag(DegrassiTags.Blocks.FRAME.get());

    this.tag(BlockTags.NEEDS_STONE_TOOL)
      .addTag(DegrassiTags.Blocks.DIGITAL.get());

    this.tag(BlockTags.NEEDS_IRON_TOOL)
      .addTag(DegrassiTags.Blocks.FURNACE.get())
      .addTag(DegrassiTags.Blocks.CHEST.get())
      .addTag(DegrassiTags.Blocks.SP.get())
      .addTag(DegrassiTags.Blocks.MULTIBLOCK.get());

    this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
      .addTag(DegrassiTags.Blocks.FURNACE.get())
      .addTag(DegrassiTags.Blocks.SP.get())
      .addTag(DegrassiTags.Blocks.CHEST.get())
      .addTag(DegrassiTags.Blocks.DIGITAL.get())
      .addTag(DegrassiTags.Blocks.MULTIBLOCK.get());

    this.tag(BlockTags.MINEABLE_WITH_AXE)
      .addTag(DegrassiTags.Blocks.CHEST.get());
  }
}
