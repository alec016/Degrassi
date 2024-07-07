package es.degrassi.forge.core.data;

import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.init.BlockRegistration;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DegrassiBlockStateProvider extends BlockStateProvider {
  public DegrassiBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
    super(output, Degrassi.MODID, exFileHelper);
  }

  @Override
  public void registerStatesAndModels() {
    simpleBlock(BlockRegistration.MACHINE_CASING.get());
    simpleBlock(BlockRegistration.DIGITAL_CONTROLLER.get());
    frame(BlockRegistration.MELTER_FRAME.get(), "melter_frame");
  }

  private void horizontalBlock(RegistrySupplier<? extends Block> supplier, ResourceLocation side, ResourceLocation front, ResourceLocation top) {
    horizontalBlock(supplier.get(), side, front, top);
  }

  private void frame(Block frame, String texture) {
    simpleBlock(frame, "block/multiblock/frame", texture);
  }

  private void simpleBlock(Block block, String path, String texture) {
    getVariantBuilder(block)
      .partialState().setModels(new ConfiguredModel(cubeAll(block, new DegrassiLocation(path + "/" + texture))));
  }

  public ModelFile cubeAll(Block block, ResourceLocation texture) {
    return models().cubeAll(name(block), texture);
  }
}
