package es.degrassi.forge.init.registration;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.Degrassi;
import es.degrassi.comon.init.block.panel.SolarPanelBlock;
import es.degrassi.forge.init.block.*;
import es.degrassi.forge.init.block.furnace.*;
import es.degrassi.forge.init.block.sp.*;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;


public class BlockRegister {

  public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Degrassi.MODID, Registry.BLOCK_REGISTRY);
  public static final Map<String, RegistrySupplier<BlockItem>> BLOCK_ITEMS = new HashMap<>();

  public static final RegistrySupplier<Block> SP1_BLOCK = registerBlock(
    "solar_panel_tier_1",
    SP1Block::new,
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> SP2_BLOCK = registerBlock(
    "solar_panel_tier_2",
    SP2Block::new,
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> SP3_BLOCK = registerBlock(
    "solar_panel_tier_3",
    SP3Block::new,
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> SP4_BLOCK = registerBlock(
    "solar_panel_tier_4",
    SP4Block::new,
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> SP5_BLOCK = registerBlock(
    "solar_panel_tier_5",
    SP5Block::new,
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> SP6_BLOCK = registerBlock(
    "solar_panel_tier_6",
    SP6Block::new,
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> SP7_BLOCK = registerBlock(
    "solar_panel_tier_7",
    SP7Block::new,
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> SP8_BLOCK = registerBlock(
    "solar_panel_tier_8",
    SP8Block::new,
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> IRON_FURNACE_BLOCK = registerBlock(
    "iron_furnace",
    IronFurnace::new,
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> GOLD_FURNACE_BLOCK = registerBlock(
    "gold_furnace",
    GoldFurnace::new,
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> DIAMOND_FURNACE_BLOCK = registerBlock(
    "diamond_furnace",
    DiamondFurnace::new,
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> EMERALD_FURNACE_BLOCK = registerBlock(
    "emerald_furnace",
    EmeraldFurnace::new,
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> NETHERITE_FURNACE_BLOCK = registerBlock(
    "netherite_furnace",
    NetheriteFurnace::new,
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> MACHINE_CASING = registerBlock(
    "machine_casing",
    MachineCasing::new,
    CreativeTabs.COMMON
  );

  private static <T extends Block> RegistrySupplier<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
    RegistrySupplier<T> toReturn = BLOCKS.register(name, block);
    registerBlockItem(name, toReturn, tab);
    return toReturn;
  }

  private static <T extends Block> void registerBlockItem(String name, RegistrySupplier<T> block, CreativeModeTab tab) {
    RegistrySupplier<BlockItem> toReturn = ItemRegister.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(tab)));
    BLOCK_ITEMS.putIfAbsent(name, toReturn);
  }

  public static void register() {
    BLOCKS.register();
  }

  public static @NotNull List<ItemStack> getSolarPanelItemStacks() {
    List<ItemStack> itemStacks = new LinkedList<>();
    BLOCK_ITEMS.values().forEach(blockItem -> {
      if (blockItem.get().getBlock() instanceof SolarPanelBlock) {
        itemStacks.add(blockItem.get().getDefaultInstance());
      }
    });
    return itemStacks;
  }

  public static @NotNull ItemStack getDefaultInstance(String name) {
    return BLOCK_ITEMS.get(name).get().getDefaultInstance();
  }

}
