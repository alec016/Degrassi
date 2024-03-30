package es.degrassi.forge.core.data;

import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.init.ItemRegistration;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class DegrassiItemModelProvider extends ItemModelProvider {
  public DegrassiItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
    super(output, Degrassi.MODID, existingFileHelper);
  }

  @Override
  public void registerModels() {
    simpleBlockItem(BlockRegistration.IRON_FURNACE);
    simpleBlockItem(BlockRegistration.GOLD_FURNACE);
    simpleBlockItem(BlockRegistration.DIAMOND_FURNACE);
    simpleBlockItem(BlockRegistration.EMERALD_FURNACE);
    simpleBlockItem(BlockRegistration.NETHERITE_FURNACE);
    simpleBlockItem(BlockRegistration.MACHINE_CASING);

    simpleItem(ItemRegistration.WRENCH);
    simpleItem(ItemRegistration.RED_MATTER);
    simpleItem(ItemRegistration.BLACK_PEARL);
    itemsWithPathAndTexture();
  }

  private void itemsWithPathAndTexture() {
    simpleItem(ItemRegistration.PHOTOVOLTAIC_CELL_I, "panel", "pc1");
    simpleItem(ItemRegistration.PHOTOVOLTAIC_CELL_II, "panel", "pc2");
    simpleItem(ItemRegistration.PHOTOVOLTAIC_CELL_III, "panel", "pc3");
    simpleItem(ItemRegistration.PHOTOVOLTAIC_CELL_IV, "panel", "pc4");
    simpleItem(ItemRegistration.PHOTOVOLTAIC_CELL_V, "panel", "pc5");
    simpleItem(ItemRegistration.PHOTOVOLTAIC_CELL_VI, "panel", "pc6");
    simpleItem(ItemRegistration.PHOTOVOLTAIC_CELL_VII, "panel", "pc7");
    simpleItem(ItemRegistration.PHOTOVOLTAIC_CELL_VIII, "panel", "pc8");
  }

  private ItemModelBuilder simpleBlockItem(@NotNull RegistrySupplier<? extends Block> supplier) {
    return withExistingParent(
      supplier.getId().getPath(),
      new DegrassiLocation("block/" + supplier.getId().getPath())
    );
  }

  private ItemModelBuilder simpleItem(RegistrySupplier<? extends Item> item) {
    return withExistingParent(
      item.getId().getPath(),
      new ResourceLocation("item/generated")
    ).texture(
      "layer0",
      new DegrassiLocation("item/" + item.getId().getPath())
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
