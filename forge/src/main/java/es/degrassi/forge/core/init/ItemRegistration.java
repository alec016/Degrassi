package es.degrassi.forge.core.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class ItemRegistration {
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Degrassi.MODID, Registries.ITEM);
  public static final RegistrySupplier<BlockItem> IRON_FURNACE;
  public static final RegistrySupplier<BlockItem> GOLD_FURNACE;
  public static final RegistrySupplier<BlockItem> DIAMOND_FURNACE;
  public static final RegistrySupplier<BlockItem> EMERALD_FURNACE;
  public static final RegistrySupplier<BlockItem> NETHERITE_FURNACE;
  public static final RegistrySupplier<BlockItem> SP1;
  public static final RegistrySupplier<BlockItem> SP2;
  public static final RegistrySupplier<BlockItem> SP3;
  public static final RegistrySupplier<BlockItem> SP4;
  public static final RegistrySupplier<BlockItem> SP5;
  public static final RegistrySupplier<BlockItem> SP6;
  public static final RegistrySupplier<BlockItem> SP7;
  public static final RegistrySupplier<BlockItem> SP8;

  // Furnaces
  static {
    IRON_FURNACE = ITEMS.register("iron_furnace", () -> new BlockItem(
      BlockRegistration.IRON_FURNACE.get(),
      new Item.Properties()
    ));
    GOLD_FURNACE = ITEMS.register("gold_furnace", () -> new BlockItem(
      BlockRegistration.GOLD_FURNACE.get(),
      new Item.Properties()
    ));
    DIAMOND_FURNACE = ITEMS.register("diamond_furnace", () -> new BlockItem(
      BlockRegistration.DIAMOND_FURNACE.get(),
      new Item.Properties()
    ));
    EMERALD_FURNACE = ITEMS.register("emerald_furnace", () -> new BlockItem(
      BlockRegistration.EMERALD_FURNACE.get(),
      new Item.Properties()
    ));
    NETHERITE_FURNACE = ITEMS.register("netherite_furnace", () -> new BlockItem(
      BlockRegistration.NETHERITE_FURNACE.get(),
      new Item.Properties()
    ));
  }

  // Solar Panels
  static {
    SP1 = ITEMS.register("sp1", () -> new BlockItem(
      BlockRegistration.SP1.get(),
      new Item.Properties()
    ));
    SP2 = ITEMS.register("sp2", () -> new BlockItem(
      BlockRegistration.SP2.get(),
      new Item.Properties()
    ));
    SP3 = ITEMS.register("sp3", () -> new BlockItem(
      BlockRegistration.SP3.get(),
      new Item.Properties()
    ));
    SP4 = ITEMS.register("sp4", () -> new BlockItem(
      BlockRegistration.SP4.get(),
      new Item.Properties()
    ));
    SP5 = ITEMS.register("sp5", () -> new BlockItem(
      BlockRegistration.SP5.get(),
      new Item.Properties()
    ));
    SP6 = ITEMS.register("sp6", () -> new BlockItem(
      BlockRegistration.SP6.get(),
      new Item.Properties()
    ));
    SP7 = ITEMS.register("sp7", () -> new BlockItem(
      BlockRegistration.SP7.get(),
      new Item.Properties()
    ));
    SP8 = ITEMS.register("sp8", () -> new BlockItem(
      BlockRegistration.SP8.get(),
      new Item.Properties()
    ));
  }
}
