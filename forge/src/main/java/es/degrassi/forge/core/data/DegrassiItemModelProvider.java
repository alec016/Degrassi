package es.degrassi.forge.core.data;

import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.init.BlockRegistration;
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
    simpleBlockItem(BlockRegistration.SP1);
    simpleBlockItem(BlockRegistration.SP2);
    simpleBlockItem(BlockRegistration.SP3);
    simpleBlockItem(BlockRegistration.SP4);
    simpleBlockItem(BlockRegistration.SP5);
    simpleBlockItem(BlockRegistration.SP6);
    simpleBlockItem(BlockRegistration.SP7);
    simpleBlockItem(BlockRegistration.SP8);
  }

  private void simpleBlockItem(@NotNull RegistrySupplier<? extends Block> supplier) {
    withExistingParent(
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
}
