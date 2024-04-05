package es.degrassi.forge.core.data;

import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.init.BlockRegistration;
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
  }

  private void horizontalBlock(RegistrySupplier<? extends Block> supplier, ResourceLocation side, ResourceLocation front, ResourceLocation top) {
    horizontalBlock(supplier.get(), side, front, top);
  }
}
