package es.degrassi.forge.init.registration;

import dev.architectury.registry.CreativeTabRegistry;
import es.degrassi.common.DegrassiLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CreativeTabs {
  public static final CreativeModeTab COMMON = CreativeTabRegistry.create(
    new DegrassiLocation("common"),
    () -> new ItemStack(ItemRegister.GOLD_COIN.get())
  );

  public static final CreativeModeTab MACHINES = CreativeTabRegistry.create(
    new DegrassiLocation("machines"),
    () -> new ItemStack(BlockRegister.MACHINE_CASING.get())
  );

  public static void register() {}
}
