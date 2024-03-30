package es.degrassi.forge.core.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.common.machines.item.WrenchItem;
import es.degrassi.forge.core.common.machines.item.BookItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class ItemRegistration {
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Degrassi.MODID, Registries.ITEM);

  // ITEMS
  public static final RegistrySupplier<WrenchItem> WRENCH = ITEMS.register("wrench", WrenchItem::new);
  public static final RegistrySupplier<BookItem> BOOK = ITEMS.register("book", () -> new BookItem(new Item.Properties().stacksTo(1)));
  public static final RegistrySupplier<Item> RED_MATTER = ITEMS.register("red_matter", () -> new Item(new Item.Properties()));
  public static final RegistrySupplier<Item> BLACK_PEARL = ITEMS.register("black_pearl", () -> new Item(new Item.Properties()));
  public static final RegistrySupplier<Item> PHOTOVOLTAIC_CELL_I = ITEMS.register("photovoltaic_cell_1", () -> new Item(new Item.Properties()));
  public static final RegistrySupplier<Item> PHOTOVOLTAIC_CELL_II = ITEMS.register("photovoltaic_cell_2", () -> new Item(new Item.Properties()));
  public static final RegistrySupplier<Item> PHOTOVOLTAIC_CELL_III = ITEMS.register("photovoltaic_cell_3", () -> new Item(new Item.Properties()));
  public static final RegistrySupplier<Item> PHOTOVOLTAIC_CELL_IV = ITEMS.register("photovoltaic_cell_4", () -> new Item(new Item.Properties()));
  public static final RegistrySupplier<Item> PHOTOVOLTAIC_CELL_V = ITEMS.register("photovoltaic_cell_5", () -> new Item(new Item.Properties()));
  public static final RegistrySupplier<Item> PHOTOVOLTAIC_CELL_VI = ITEMS.register("photovoltaic_cell_6", () -> new Item(new Item.Properties()));
  public static final RegistrySupplier<Item> PHOTOVOLTAIC_CELL_VII = ITEMS.register("photovoltaic_cell_7", () -> new Item(new Item.Properties()));
  public static final RegistrySupplier<Item> PHOTOVOLTAIC_CELL_VIII = ITEMS.register("photovoltaic_cell_8", () -> new Item(new Item.Properties()));

  // BLOCK ITEMS
  public static final RegistrySupplier<BlockItem> MACHINE_CASING = ITEMS.register("machine_casing", () -> new BlockItem(BlockRegistration.MACHINE_CASING.get(), new Item.Properties()));
  public static final RegistrySupplier<BlockItem> IRON_FURNACE;
  public static final RegistrySupplier<BlockItem> GOLD_FURNACE;
  public static final RegistrySupplier<BlockItem> DIAMOND_FURNACE;
  public static final RegistrySupplier<BlockItem> EMERALD_FURNACE;
  public static final RegistrySupplier<BlockItem> NETHERITE_FURNACE;

//  public static final RegistrySupplier<BlockItem> SP1;
//  public static final RegistrySupplier<BlockItem> SP2;
//  public static final RegistrySupplier<BlockItem> SP3;
//  public static final RegistrySupplier<BlockItem> SP4;
//  public static final RegistrySupplier<BlockItem> SP5;
//  public static final RegistrySupplier<BlockItem> SP6;
//  public static final RegistrySupplier<BlockItem> SP7;
//  public static final RegistrySupplier<BlockItem> SP8;

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
//  static {
//    SP1 = ITEMS.register("sp1", () -> new SolarPanelItem(
//      BlockRegistration.SP1.get(),
//      new Item.Properties()
//    ));
//    SP2 = ITEMS.register("sp2", () -> new SolarPanelItem(
//      BlockRegistration.SP2.get(),
//      new Item.Properties()
//    ));
//    SP3 = ITEMS.register("sp3", () -> new SolarPanelItem(
//      BlockRegistration.SP3.get(),
//      new Item.Properties()
//    ));
//    SP4 = ITEMS.register("sp4", () -> new SolarPanelItem(
//      BlockRegistration.SP4.get(),
//      new Item.Properties()
//    ));
//    SP5 = ITEMS.register("sp5", () -> new SolarPanelItem(
//      BlockRegistration.SP5.get(),
//      new Item.Properties()
//    ));
//    SP6 = ITEMS.register("sp6", () -> new SolarPanelItem(
//      BlockRegistration.SP6.get(),
//      new Item.Properties()
//    ));
//    SP7 = ITEMS.register("sp7", () -> new SolarPanelItem(
//      BlockRegistration.SP7.get(),
//      new Item.Properties()
//    ));
//    SP8 = ITEMS.register("sp8", () -> new SolarPanelItem(
//      BlockRegistration.SP8.get(),
//      new Item.Properties()
//    ));
//  }
}
