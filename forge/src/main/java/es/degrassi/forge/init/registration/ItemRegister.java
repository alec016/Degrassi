package es.degrassi.forge.init.registration;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.init.item.upgrade.*;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class ItemRegister {
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Degrassi.MODID, Registry.ITEM_REGISTRY);

  public static final RegistrySupplier<Item> GOLD_COIN = ITEMS.register(
    "gold_coin", () -> new Item(new Item.Properties().tab(CreativeTabs.COMMON))
  );

  public static final RegistrySupplier<BaseUpgrade> EFFICIENCY_UPGRADE = registerUpgrade(
    "efficiency_upgrade", () -> new EfficiencyUpgrade(new Item.Properties().tab(CreativeTabs.COMMON).stacksTo(1))
  );

  public static final RegistrySupplier<BaseUpgrade> TRANSFER_UPGRADE = registerUpgrade(
    "transfer_upgrade", () -> new TransferUpgrade(new Item.Properties().tab(CreativeTabs.COMMON).stacksTo(1))
  );

  public static final RegistrySupplier<BaseUpgrade> GENERATION_UPGRADE = registerUpgrade(
    "generation_upgrade", () -> new GenerationUpgrade(new Item.Properties().tab(CreativeTabs.COMMON).stacksTo(1))
  );

  public static final RegistrySupplier<BaseUpgrade> CAPACITY_UPGRADE = registerUpgrade(
    "capacity_upgrade", () -> new CapacityUpgrade(new Item.Properties().tab(CreativeTabs.COMMON).stacksTo(1))
  );

  public static final RegistrySupplier<BaseUpgrade> SPEED_UPGRADE = registerUpgrade(
    "speed_upgrade", () -> new SpeedUpgrade(new Item.Properties().tab(CreativeTabs.COMMON))
  );

  public static final RegistrySupplier<BaseUpgrade> ENERGY_UPGRADE = registerUpgrade(
    "energy_upgrade", () -> new EnergyUpgrade(new Item.Properties().tab(CreativeTabs.COMMON))
  );

  private static RegistrySupplier<BaseUpgrade> registerUpgrade(String id, Supplier<? extends BaseUpgrade> supplier) {
    return ITEMS.register(id, supplier);
  }

  public static void register () {
    ITEMS.register();
  }
}
