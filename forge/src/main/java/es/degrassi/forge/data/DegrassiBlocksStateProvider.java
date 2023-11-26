package es.degrassi.forge.data;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.init.registration.BlockRegister;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DegrassiBlocksStateProvider extends BlockStateProvider {
  public DegrassiBlocksStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
    super(gen, Degrassi.MODID, exFileHelper);
  }

  @Override
  protected void registerStatesAndModels() {
    simpleBlock(BlockRegister.MACHINE_CASING.get());
    horizontalBlock(
      BlockRegister.IRON_FURNACE_BLOCK.get(),
      new DegrassiLocation("block/furnace/iron_furnace_side"),
      new DegrassiLocation("block/furnace/iron_furnace"),
      new DegrassiLocation("block/furnace/iron_furnace_side")
    );
    horizontalBlock(
      BlockRegister.GOLD_FURNACE_BLOCK.get(),
      new DegrassiLocation("block/furnace/gold_furnace_side"),
      new DegrassiLocation("block/furnace/gold_furnace"),
      new DegrassiLocation("block/furnace/gold_furnace_side")
    );
    horizontalBlock(
      BlockRegister.DIAMOND_FURNACE_BLOCK.get(),
      new DegrassiLocation("block/furnace/diamond_furnace_side"),
      new DegrassiLocation("block/furnace/diamond_furnace"),
      new DegrassiLocation("block/furnace/diamond_furnace_side")
    );
    horizontalBlock(
      BlockRegister.EMERALD_FURNACE_BLOCK.get(),
      new DegrassiLocation("block/furnace/emerald_furnace_side"),
      new DegrassiLocation("block/furnace/emerald_furnace"),
      new DegrassiLocation("block/furnace/emerald_furnace_side")
    );
    horizontalBlock(
      BlockRegister.NETHERITE_FURNACE_BLOCK.get(),
      new DegrassiLocation("block/furnace/netherite_furnace_side"),
      new DegrassiLocation("block/furnace/netherite_furnace"),
      new DegrassiLocation("block/furnace/netherite_furnace_side")
    );
  }

  public void horizontalBlock(Block block, ResourceLocation texture) {
    horizontalBlock(block, texture, texture, texture);
  }

  public BlockModelBuilder withGeckoParent(Block block, ResourceLocation texture) {
    return withParent("geckolib3:block/empty", block, texture, "0");
  }

  public BlockModelBuilder withParent(String parent, Block block, ResourceLocation texture, String key) {
    return models().withExistingParent(name(block), parent).texture(key, texture).texture("particle", texture);
  }
}
