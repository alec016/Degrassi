package es.degrassi.forge.data;

import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.init.registration.BlockRegister;
import es.degrassi.forge.init.registration.ItemRegister;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class DegrassiItemModelProvider extends ItemModelProvider {
  public DegrassiItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
    super(generator, Degrassi.MODID, existingFileHelper);
  }

  @Override
  protected void registerModels() {
    items();
    blockItems();
  }

  private void items() {
    simpleItem(ItemRegister.GOLD_COIN);
    simpleItem(ItemRegister.RED_MATTER);
    simpleItem(ItemRegister.BLACK_PEARL);
    simpleItem(ItemRegister.UPGRADE_BASE);
    simpleItem(ItemRegister.MODIFIER_BASE);
    simpleItem(ItemRegister.GENERATION_UPGRADE);
    simpleItem(ItemRegister.TRANSFER_UPGRADE);
    simpleItem(ItemRegister.EFFICIENCY_UPGRADE);
    simpleItem(ItemRegister.CAPACITY_UPGRADE);
    simpleItem(ItemRegister.ENERGY_UPGRADE);
    simpleItem(ItemRegister.SPEED_UPGRADE);

    itemsWithPath();
    itemsWithTexture();
    itemsWithPathAndTexture();
  }

  private void itemsWithPath() {

  }

  private void itemsWithTexture() {
  }

  private void itemsWithPathAndTexture() {
    simpleItem(ItemRegister.PHOTOVOLTAIC_CELL_I, "panel", "pc1");
    simpleItem(ItemRegister.PHOTOVOLTAIC_CELL_II, "panel", "pc2");
    simpleItem(ItemRegister.PHOTOVOLTAIC_CELL_III, "panel", "pc3");
    simpleItem(ItemRegister.PHOTOVOLTAIC_CELL_IV, "panel", "pc4");
    simpleItem(ItemRegister.PHOTOVOLTAIC_CELL_V, "panel", "pc5");
    simpleItem(ItemRegister.PHOTOVOLTAIC_CELL_VI, "panel", "pc6");
    simpleItem(ItemRegister.PHOTOVOLTAIC_CELL_VII, "panel", "pc7");
    simpleItem(ItemRegister.PHOTOVOLTAIC_CELL_VIII, "panel", "pc8");
  }

  private void blockItems() {
    simpleBlockItem(BlockRegister.IRON_FURNACE_BLOCK);
    simpleBlockItem(BlockRegister.GOLD_FURNACE_BLOCK);
    simpleBlockItem(BlockRegister.DIAMOND_FURNACE_BLOCK);
    simpleBlockItem(BlockRegister.EMERALD_FURNACE_BLOCK);
    simpleBlockItem(BlockRegister.NETHERITE_FURNACE_BLOCK);

    simpleBlockItem(BlockRegister.MACHINE_CASING);
    simpleBlockItem(BlockRegister.MELTER_BLOCK);
    simpleBlockItem(BlockRegister.UPGRADE_MAKER);

    simpleBlockItem(BlockRegister.SP1_BLOCK);
    simpleBlockItem(BlockRegister.SP2_BLOCK);
    simpleBlockItem(BlockRegister.SP3_BLOCK);
    simpleBlockItem(BlockRegister.SP4_BLOCK);
    simpleBlockItem(BlockRegister.SP5_BLOCK);
    simpleBlockItem(BlockRegister.SP6_BLOCK);
    simpleBlockItem(BlockRegister.SP7_BLOCK);
    simpleBlockItem(BlockRegister.SP8_BLOCK);

    simpleBlockItem(BlockRegister.JEWELRY_GENERATOR);
    simpleBlockItem(BlockRegister.CIRCUIT_FABRICATOR);
  }

  private ItemModelBuilder simpleItem(@NotNull RegistrySupplier<? extends Item> item) {
    return simpleItem(item, null);
  }

  private ItemModelBuilder simpleItem(@NotNull RegistrySupplier<? extends Item> item, String path) {
    return simpleItem(item, path, null);
  }

  private ItemModelBuilder simpleItemTextured(RegistrySupplier<? extends Item> item, String texture) {
    return simpleItem(item, null, texture);
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

  private ItemModelBuilder simpleBlockItem(@NotNull RegistrySupplier<? extends Block> supplier) {
    return withExistingParent(
      supplier.getId().getPath(),
      new DegrassiLocation("block/" + supplier.getId().getPath())
    );
  }

  private ItemModelBuilder handheldItem(@NotNull RegistrySupplier<? extends Item> item) {
    return withExistingParent(item.getId().getPath(),
      new ResourceLocation("item/handheld")).texture("layer0",
      new DegrassiLocation("item/" + item.getId().getPath()));
  }
}
