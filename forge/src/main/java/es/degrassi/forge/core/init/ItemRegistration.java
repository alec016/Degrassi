package es.degrassi.forge.core.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class ItemRegistration {
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Degrassi.MODID, Registries.ITEM);
  public static final RegistrySupplier<BlockItem> IRON_FURNACE = ITEMS.register("iron_furnace", () -> new BlockItem(
    BlockRegistration.IRON_FURNACE.get(),
    new Item.Properties()
  ));
  public static final RegistrySupplier<BlockItem> GOLD_FURNACE = ITEMS.register("gold_furnace", () -> new BlockItem(
    BlockRegistration.GOLD_FURNACE.get(),
    new Item.Properties()
  ));
  public static final RegistrySupplier<BlockItem> DIAMOND_FURNACE = ITEMS.register("diamond_furnace", () -> new BlockItem(
    BlockRegistration.DIAMOND_FURNACE.get(),
    new Item.Properties()
  ));
  public static final RegistrySupplier<BlockItem> EMERALD_FURNACE = ITEMS.register("emerald_furnace", () -> new BlockItem(
    BlockRegistration.EMERALD_FURNACE.get(),
    new Item.Properties()
  ));
  public static final RegistrySupplier<BlockItem> NETHERITE_FURNACE = ITEMS.register("netherite_furnace", () -> new BlockItem(
    BlockRegistration.NETHERITE_FURNACE.get(),
    new Item.Properties()
  ));
}
