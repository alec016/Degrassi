package es.degrassi.forge.init.registration;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.Degrassi;
import es.degrassi.comon.init.item.upgrade.*;
import es.degrassi.forge.init.item.upgrade.CapacityUpgrade;
import es.degrassi.forge.init.item.upgrade.EfficiencyUpgrade;
import es.degrassi.forge.init.item.upgrade.GenerationUpgrade;
import es.degrassi.forge.init.item.upgrade.TransferUpgrade;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class ItemRegister {
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Degrassi.MODID, Registry.ITEM_REGISTRY);

  public static final RegistrySupplier<Item> GOLD_COIN = ITEMS.register(
    "gold_coin", () -> new Item(new Item.Properties().tab(CreativeTabs.COMMON))
  );

  public static final RegistrySupplier<PanelUpgrade> EFFICIENCY_UPGRADE = registerPanelUpgrade(
    "efficiency_upgrade", () -> new EfficiencyUpgrade(new Item.Properties().tab(CreativeTabs.COMMON))
  );

  public static final RegistrySupplier<PanelUpgrade> TRANSFER_UPGRADE = registerPanelUpgrade(
    "transfer_upgrade", () -> new TransferUpgrade(new Item.Properties().tab(CreativeTabs.COMMON))
  );

  public static final RegistrySupplier<PanelUpgrade> GENERATION_UPGRADE = registerPanelUpgrade(
    "generation_upgrade", () -> new GenerationUpgrade(new Item.Properties().tab(CreativeTabs.COMMON))
  );

  public static final RegistrySupplier<PanelUpgrade> CAPACITY_UPGRADE = registerPanelUpgrade(
    "capacity_upgrade", () -> new CapacityUpgrade(new Item.Properties().tab(CreativeTabs.COMMON))
  );

  private static RegistrySupplier<PanelUpgrade> registerPanelUpgrade(String id, Supplier<? extends PanelUpgrade> supplier) {
    return ITEMS.register(id, supplier);
  }

  public static void register () {
    ITEMS.register();
  }
}
