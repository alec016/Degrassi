package es.degrassi.forge.core.data;

import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.init.ItemRegistration;
import es.degrassi.forge.core.tiers.PhotovoltaicCell;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import es.degrassi.forge.core.tiers.*;

@SuppressWarnings("deprecation")
public class DegrassiItemModelProvider extends ItemModelProvider {
  public DegrassiItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
    super(output, Degrassi.MODID, existingFileHelper);
  }

  @Override
  public void registerModels() {
    registerBlockItems();
    simpleItem(ItemRegistration.WRENCH);
    simpleItem(ItemRegistration.RED_MATTER);
    simpleItem(ItemRegistration.BLACK_PEARL);
    itemsWithPathAndTexture();
  }

  private void registerBlockItems() {
    registerTieredBlocks();
    simpleBlockItem(BlockRegistration.MACHINE_CASING);
    simpleBlockItem(BlockRegistration.MELTER_FRAME);
    simpleBlockItem(BlockRegistration.MELTER_CONTROLLER);
  }

  private void registerTieredBlocks() {
    for (Chest variant : Chest.values()) {
      simpleBlockItem(new DegrassiLocation("item/chest"), BlockRegistration.CHEST.get(variant), "1", "block/chest", "chest_" + variant.nameL());
    }
    for (Furnace variant : Furnace.values()) {
      simpleBlockItem(BlockRegistration.FURNACE.get(variant));
    }
    for (Storage.Energy variant : Storage.Energy.values()) {
      simpleBlockItem(BlockRegistration.ENERGY_CELL.get(variant));
    }
    for (Storage.Fluid variant : Storage.Fluid.values()) {
      simpleBlockItem(BlockRegistration.FLUID_TANK.get(variant));
    }
    for (MultiblockPartStorage.Energy variant : MultiblockPartStorage.Energy.values()) {
      simpleBlockItem(BlockRegistration.ENERGY_HATCH.get(variant));
    }
    for (MultiblockPartStorage.Fluid.Input variant : MultiblockPartStorage.Fluid.Input.values()) {
      simpleBlockItem(BlockRegistration.FLUID_INPUT_TANK.get(variant));
    }
    for (MultiblockPartStorage.Fluid.QuadrupleInput variant : MultiblockPartStorage.Fluid.QuadrupleInput.values()) {
      simpleBlockItem(BlockRegistration.QUADRUPLE_FLUID_INPUT_TANK.get(variant));
    }
    for (MultiblockPartStorage.Fluid.Output variant : MultiblockPartStorage.Fluid.Output.values()) {
      simpleBlockItem(BlockRegistration.FLUID_OUTPUT_TANK.get(variant));
    }
    for (MultiblockPartStorage.Item.Input variant : MultiblockPartStorage.Item.Input.values()) {
      simpleBlockItem(BlockRegistration.INPUT_BUS.get(variant));
    }
    for (MultiblockPartStorage.Item.Output variant : MultiblockPartStorage.Item.Output.values()) {
      simpleBlockItem(BlockRegistration.OUTPUT_BUS.get(variant));
    }
  }

  private void itemsWithPathAndTexture() {
    simpleItem(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.I), "panel", "pc1");
    simpleItem(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.II), "panel", "pc2");
    simpleItem(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.III), "panel", "pc3");
    simpleItem(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.IV), "panel", "pc4");
    simpleItem(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.V), "panel", "pc5");
    simpleItem(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.VI), "panel", "pc6");
    simpleItem(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.VII), "panel", "pc7");
    simpleItem(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.VIII), "panel", "pc8");
  }

  private void simpleBlockItem(@NotNull RegistrySupplier<? extends Block> supplier) {
    withExistingParent(
      supplier.getId().getPath(),
      new DegrassiLocation("block/" + supplier.getId().getPath())
    );
  }

  private void simpleBlockItem(@NotNull Block supplier) {
    withExistingParent(
      supplier.builtInRegistryHolder().key().location().getPath(),
      new DegrassiLocation("block/" + supplier.builtInRegistryHolder().key().location().getPath())
    );
  }

  private void simpleBlockItem(@NotNull RegistrySupplier<? extends Block> block, String key, String path, String texture) {
    withExistingParent(
      block.getId().getPath(),
      new DegrassiLocation("block/" + block.getId().getPath())
    ).texture(
      key,
      new DegrassiLocation(
        (path != null && !path.trim().isEmpty() ? path + "/" : "") +
        (texture != null && !texture.trim().isEmpty() ? texture : block.getId().getPath())
      )
    );
  }

  private void simpleBlockItem(@NotNull Block block, String key, String path, String texture) {
    withExistingParent(
      block.builtInRegistryHolder().key().location().getPath(),
      new DegrassiLocation("block/" + block.builtInRegistryHolder().key().location().getPath())
    ).texture(
      key,
      new DegrassiLocation(
        (path != null && !path.trim().isEmpty() ? path + "/" : "") +
        (texture != null && !texture.trim().isEmpty() ? texture : block.builtInRegistryHolder().key().location().getPath())
      )
    );
  }

  private void simpleBlockItem(ResourceLocation parent, @NotNull RegistrySupplier<? extends Block> block, String key, String path, String texture) {
    withExistingParent(
      block.getId().getPath(),
      parent
    ).texture(
      key,
      new DegrassiLocation(
        (path != null && !path.trim().isEmpty() ? path + "/" : "") +
        (texture != null && !texture.trim().isEmpty() ? texture : block.getId().getPath())
      )
    );
  }

  private void simpleBlockItem(ResourceLocation parent, @NotNull Block block, String key, String path, String texture) {
    withExistingParent(
      block.builtInRegistryHolder().key().location().getPath(),
      parent
    ).texture(
      key,
      new DegrassiLocation(
        (path != null && !path.trim().isEmpty() ? path + "/" : "") +
        (texture != null && !texture.trim().isEmpty() ? texture : block.builtInRegistryHolder().key().location().getPath())
      )
    );
  }

  private void simpleItem(RegistrySupplier<? extends Item> item) {
    withExistingParent(
      item.getId().getPath(),
      new ResourceLocation("item/generated")
    ).texture(
      "layer0",
      new DegrassiLocation("item/" + item.getId().getPath())
    );
  }

  private void simpleItem(@NotNull Item item, String path, String texture) {
    withExistingParent(item.builtInRegistryHolder().key().location().getPath(),
      new ResourceLocation("item/generated")).texture("layer0",
      new DegrassiLocation(
        "item/" +
          (path != null && !path.trim().isEmpty() ? path + "/" : "") +
          (texture != null && !texture.trim().isEmpty() ? texture : item.builtInRegistryHolder().key().location().getPath())
      )
    );
  }

  private ItemModelBuilder simpleItem(@NotNull RegistrySupplier<? extends Item> item, String path, String texture) {
    return withExistingParent(item.getId().getPath(),
      new ResourceLocation("item/generated")).texture("layer0",
      new DegrassiLocation(
        "item/" +
          (path != null && !path.trim().isEmpty() ? path + "/" : "") +
          (texture != null && !texture.trim().isEmpty() ? texture : item.getId().getPath())
      )
    );
  }
}
