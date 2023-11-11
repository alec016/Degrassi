package es.degrassi.forge.init.registration;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.init.item.upgrade.*;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ItemRegister {
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Degrassi.MODID, Registry.ITEM_REGISTRY);

  public static final RegistrySupplier<Item> GOLD_COIN = ITEMS.register(
    "gold_coin",
    () -> new Item(new Item.Properties().tab(CreativeTabs.COMMON))
//    () -> Objects.requireNonNull(new Item(new Item.Properties().tab(CreativeTabs.COMMON)).kjs$getItemBuilder())
//      .tag(TagRegistry.AllItemTags.ITEMS.tag.location())
//      .get()
  );

  public static final RegistrySupplier<Item> RED_MATTER = ITEMS.register(
    "red_matter",
    () -> new Item(new Item.Properties().tab(CreativeTabs.COMMON))
//    () -> Objects.requireNonNull(new Item(new Item.Properties().tab(CreativeTabs.COMMON)).kjs$getItemBuilder())
//      .tag(TagRegistry.AllItemTags.ITEMS.tag.location())
//      .get()
  );

  public static final RegistrySupplier<Item> BLACK_PEARL = ITEMS.register(
    "black_pearl",
    () -> new Item(new Item.Properties().tab(CreativeTabs.COMMON))
//    () -> Objects.requireNonNull(new Item(new Item.Properties().tab(CreativeTabs.COMMON)).kjs$getItemBuilder())
//      .tag(TagRegistry.AllItemTags.ITEMS.tag.location())
//      .get()
  );

  public static final RegistrySupplier<Item> MODIFIER_BASE = ITEMS.register(
    "modifier_base",
    () -> new Item(new Item.Properties().tab(CreativeTabs.COMMON))
//    () -> Objects.requireNonNull(new Item(new Item.Properties().tab(CreativeTabs.COMMON)).kjs$getItemBuilder())
//      .tag(TagRegistry.AllItemTags.ITEMS.tag.location())
//      .get()
  );

  public static final RegistrySupplier<Item> UPGRADE_BASE = ITEMS.register(
    "upgrade_base",
    () -> new Item(new Item.Properties().tab(CreativeTabs.COMMON))
//    () -> Objects.requireNonNull(new Item(new Item.Properties().tab(CreativeTabs.COMMON)).kjs$getItemBuilder())
//      .tag(TagRegistry.AllItemTags.ITEMS.tag.location())
//      .get()
  );

  public static final RegistrySupplier<BaseUpgrade> EFFICIENCY_UPGRADE = registerUpgrade(
    "efficiency_upgrade",
    () -> new EfficiencyUpgrade(new Item.Properties().tab(CreativeTabs.COMMON).stacksTo(1))
//    () -> (BaseUpgrade) Objects.requireNonNull(new EfficiencyUpgrade(new Item.Properties().tab(CreativeTabs.COMMON).stacksTo(1)).kjs$getItemBuilder())
//      .tag(TagRegistry.AllItemTags.ITEMS.tag.location())
//      .tag(TagRegistry.AllItemTags.UPGRADES.tag.location())
//      .get()
  );

  public static final RegistrySupplier<BaseUpgrade> TRANSFER_UPGRADE = registerUpgrade(
    "transfer_upgrade",
    () -> new TransferUpgrade(new Item.Properties().tab(CreativeTabs.COMMON).stacksTo(1))
//    () -> (BaseUpgrade) Objects.requireNonNull(new TransferUpgrade(new Item.Properties().tab(CreativeTabs.COMMON).stacksTo(1)).kjs$getItemBuilder())
//      .tag(TagRegistry.AllItemTags.ITEMS.tag.location())
//      .tag(TagRegistry.AllItemTags.UPGRADES.tag.location())
//      .get()
  );

  public static final RegistrySupplier<BaseUpgrade> GENERATION_UPGRADE = registerUpgrade(
    "generation_upgrade",
    () -> new GenerationUpgrade(new Item.Properties().tab(CreativeTabs.COMMON).stacksTo(1))
//    () -> (BaseUpgrade) Objects.requireNonNull(new GenerationUpgrade(new Item.Properties().tab(CreativeTabs.COMMON).stacksTo(1)).kjs$getItemBuilder())
//      .tag(TagRegistry.AllItemTags.ITEMS.tag.location())
//      .tag(TagRegistry.AllItemTags.UPGRADES.tag.location())
//      .get()
  );

  public static final RegistrySupplier<BaseUpgrade> CAPACITY_UPGRADE = registerUpgrade(
    "capacity_upgrade",
    () -> new CapacityUpgrade(new Item.Properties().tab(CreativeTabs.COMMON).stacksTo(1))
//    () -> (BaseUpgrade) Objects.requireNonNull(new CapacityUpgrade(new Item.Properties().tab(CreativeTabs.COMMON).stacksTo(1)).kjs$getItemBuilder())
//      .tag(TagRegistry.AllItemTags.ITEMS.tag.location())
//      .tag(TagRegistry.AllItemTags.UPGRADES.tag.location())
//      .get()
  );

  public static final RegistrySupplier<BaseUpgrade> SPEED_UPGRADE = registerUpgrade(
    "speed_upgrade",
    () -> new SpeedUpgrade(new Item.Properties().tab(CreativeTabs.COMMON))
//    () -> (BaseUpgrade) Objects.requireNonNull(new SpeedUpgrade(new Item.Properties().tab(CreativeTabs.COMMON)).kjs$getItemBuilder())
//      .tag(TagRegistry.AllItemTags.ITEMS.tag.location())
//      .tag(TagRegistry.AllItemTags.UPGRADES.tag.location())
//      .get()
  );

  public static final RegistrySupplier<BaseUpgrade> ENERGY_UPGRADE = registerUpgrade(
    "energy_upgrade",
    () -> new EnergyUpgrade(new Item.Properties().tab(CreativeTabs.COMMON))
//    () -> (BaseUpgrade) Objects.requireNonNull(new EnergyUpgrade(new Item.Properties().tab(CreativeTabs.COMMON)).kjs$getItemBuilder())
//      .tag(TagRegistry.AllItemTags.ITEMS.tag.location())
//      .tag(TagRegistry.AllItemTags.UPGRADES.tag.location())
//      .get()
  );

  public static final RegistrySupplier<Item> PHOTOVOLTAIC_CELL_I = ITEMS.register(
    "photovoltaic_cell_1",
    () -> new Item(new Item.Properties().tab(CreativeTabs.COMMON))
//    () -> Objects.requireNonNull(new Item(new Item.Properties().tab(CreativeTabs.COMMON)).kjs$getItemBuilder())
//      .tag(TagRegistry.AllItemTags.PHOTOVOLTAIC_CELLS.tag.location())
//      .tag(TagRegistry.AllItemTags.ITEMS.tag.location())
//      .get()
  );

  public static final RegistrySupplier<Item> PHOTOVOLTAIC_CELL_II = ITEMS.register(
    "photovoltaic_cell_2",
    () -> new Item(new Item.Properties().tab(CreativeTabs.COMMON))
//    () -> Objects.requireNonNull(new Item(new Item.Properties().tab(CreativeTabs.COMMON)).kjs$getItemBuilder())
//      .tag(TagRegistry.AllItemTags.PHOTOVOLTAIC_CELLS.tag.location())
//      .tag(TagRegistry.AllItemTags.ITEMS.tag.location())
//      .get()
  );

  public static final RegistrySupplier<Item> PHOTOVOLTAIC_CELL_III = ITEMS.register(
    "photovoltaic_cell_3",
    () -> new Item(new Item.Properties().tab(CreativeTabs.COMMON))
//    () -> Objects.requireNonNull(new Item(new Item.Properties().tab(CreativeTabs.COMMON)).kjs$getItemBuilder())
//      .tag(TagRegistry.AllItemTags.PHOTOVOLTAIC_CELLS.tag.location())
//      .tag(TagRegistry.AllItemTags.ITEMS.tag.location())
//      .get()
  );

  public static final RegistrySupplier<Item> PHOTOVOLTAIC_CELL_IV = ITEMS.register(
    "photovoltaic_cell_4",
    () -> new Item(new Item.Properties().tab(CreativeTabs.COMMON))
//    () -> Objects.requireNonNull(new Item(new Item.Properties().tab(CreativeTabs.COMMON)).kjs$getItemBuilder())
//      .tag(TagRegistry.AllItemTags.PHOTOVOLTAIC_CELLS.tag.location())
//      .tag(TagRegistry.AllItemTags.ITEMS.tag.location())
//      .get()
  );

  public static final RegistrySupplier<Item> PHOTOVOLTAIC_CELL_V = ITEMS.register(
    "photovoltaic_cell_5",
    () -> new Item(new Item.Properties().tab(CreativeTabs.COMMON))
//    () -> Objects.requireNonNull(new Item(new Item.Properties().tab(CreativeTabs.COMMON)).kjs$getItemBuilder())
//      .tag(TagRegistry.AllItemTags.PHOTOVOLTAIC_CELLS.tag.location())
//      .tag(TagRegistry.AllItemTags.ITEMS.tag.location())
//      .get()
  );

  public static final RegistrySupplier<Item> PHOTOVOLTAIC_CELL_VI = ITEMS.register(
    "photovoltaic_cell_6",
    () -> new Item(new Item.Properties().tab(CreativeTabs.COMMON))
//    () -> Objects.requireNonNull(new Item(new Item.Properties().tab(CreativeTabs.COMMON)).kjs$getItemBuilder())
//      .tag(TagRegistry.AllItemTags.PHOTOVOLTAIC_CELLS.tag.location())
//      .tag(TagRegistry.AllItemTags.ITEMS.tag.location())
//      .get()
  );

  public static final RegistrySupplier<Item> PHOTOVOLTAIC_CELL_VII = ITEMS.register(
    "photovoltaic_cell_7",
    () -> new Item(new Item.Properties().tab(CreativeTabs.COMMON))
//    () -> Objects.requireNonNull(new Item(new Item.Properties().tab(CreativeTabs.COMMON)).kjs$getItemBuilder())
//      .tag(TagRegistry.AllItemTags.PHOTOVOLTAIC_CELLS.tag.location())
//      .tag(TagRegistry.AllItemTags.ITEMS.tag.location())
//      .get()
  );

  public static final RegistrySupplier<Item> PHOTOVOLTAIC_CELL_VIII = ITEMS.register(
    "photovoltaic_cell_8",
    () -> new Item(new Item.Properties().tab(CreativeTabs.COMMON))
//    () -> Objects.requireNonNull(new Item(new Item.Properties().tab(CreativeTabs.COMMON)).kjs$getItemBuilder())
//      .tag(TagRegistry.AllItemTags.PHOTOVOLTAIC_CELLS.tag.location())
//      .tag(TagRegistry.AllItemTags.ITEMS.tag.location())
//      .get()
  );

  private static RegistrySupplier<BaseUpgrade> registerUpgrade(String id, Supplier<? extends BaseUpgrade> supplier) {
    return ITEMS.register(id, supplier);
  }

  public static void register () {
    ITEMS.register();
  }
}
