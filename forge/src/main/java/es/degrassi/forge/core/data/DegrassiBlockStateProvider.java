package es.degrassi.forge.core.data;

import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.tiers.Chest;
import es.degrassi.forge.core.tiers.Furnace;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DegrassiBlockStateProvider extends BlockStateProvider {
  public DegrassiBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
    super(output, Degrassi.MODID, exFileHelper);
  }

  @Override
  public void registerStatesAndModels() {
    simpleBlock(BlockRegistration.MACHINE_CASING.get());
    horizontalBlock(
      BlockRegistration.FURNACE.get(Furnace.IRON),
      new DegrassiLocation("block/furnace/iron_furnace_side"),
      new DegrassiLocation("block/furnace/iron_furnace"),
      new DegrassiLocation("block/furnace/iron_furnace_side")
    );
    horizontalBlock(
      BlockRegistration.FURNACE.get(Furnace.GOLD),
      new DegrassiLocation("block/furnace/gold_furnace_side"),
      new DegrassiLocation("block/furnace/gold_furnace"),
      new DegrassiLocation("block/furnace/gold_furnace_side")
    );
    horizontalBlock(
      BlockRegistration.FURNACE.get(Furnace.DIAMOND),
      new DegrassiLocation("block/furnace/diamond_furnace_side"),
      new DegrassiLocation("block/furnace/diamond_furnace"),
      new DegrassiLocation("block/furnace/diamond_furnace_side")
    );
    horizontalBlock(
      BlockRegistration.FURNACE.get(Furnace.EMERALD),
      new DegrassiLocation("block/furnace/emerald_furnace_side"),
      new DegrassiLocation("block/furnace/emerald_furnace"),
      new DegrassiLocation("block/furnace/emerald_furnace_side")
    );
    horizontalBlock(
      BlockRegistration.FURNACE.get(Furnace.NETHERITE),
      new DegrassiLocation("block/furnace/netherite_furnace_side"),
      new DegrassiLocation("block/furnace/netherite_furnace"),
      new DegrassiLocation("block/furnace/netherite_furnace_side")
    );
  }

  private void horizontalBlock(RegistrySupplier<? extends Block> supplier, ResourceLocation side, ResourceLocation front, ResourceLocation top) {
    horizontalBlock(supplier.get(), side, front, top);
  }
}
