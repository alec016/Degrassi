package es.degrassi.forge.init.registration;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.init.block.generators.JewelryGenerator;
import es.degrassi.forge.init.block.Melter;
import es.degrassi.forge.init.block.panel.SolarPanelBlock;
import es.degrassi.forge.init.block.*;
import es.degrassi.forge.init.block.UpgradeMaker;
import es.degrassi.forge.init.geckolib.block.CircuitFabricatorBlock;
import es.degrassi.forge.init.tiers.FurnaceTier;
import es.degrassi.forge.init.tiers.SolarPanelTier;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class BlockRegister {

  public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Degrassi.MODID, Registry.BLOCK_REGISTRY);
  public static final Map<String, RegistrySupplier<BlockItem>> BLOCK_ITEMS = new HashMap<>();

  public static final RegistrySupplier<Block> SP1_BLOCK = registerBlock(
    "solar_panel_tier_1",
    () -> new SolarPanelBlock(SolarPanelTier.I),
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> SP2_BLOCK = registerBlock(
    "solar_panel_tier_2",
    () -> new SolarPanelBlock(SolarPanelTier.II),
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> SP3_BLOCK = registerBlock(
    "solar_panel_tier_3",
    () -> new SolarPanelBlock(SolarPanelTier.III),
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> SP4_BLOCK = registerBlock(
    "solar_panel_tier_4",
    () -> new SolarPanelBlock(SolarPanelTier.IV),
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> SP5_BLOCK = registerBlock(
    "solar_panel_tier_5",
    () -> new SolarPanelBlock(SolarPanelTier.V),
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> SP6_BLOCK = registerBlock(
    "solar_panel_tier_6",
    () -> new SolarPanelBlock(SolarPanelTier.VI),
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> SP7_BLOCK = registerBlock(
    "solar_panel_tier_7",
    () -> new SolarPanelBlock(SolarPanelTier.VII),
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> SP8_BLOCK = registerBlock(
    "solar_panel_tier_8",
    () -> new SolarPanelBlock(SolarPanelTier.VIII),
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> IRON_FURNACE_BLOCK = registerBlock(
    "iron_furnace",
    () -> new FurnaceBlock(FurnaceTier.IRON),
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> GOLD_FURNACE_BLOCK = registerBlock(
    "gold_furnace",
    () -> new FurnaceBlock(FurnaceTier.GOLD),
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> DIAMOND_FURNACE_BLOCK = registerBlock(
    "diamond_furnace",
    () -> new FurnaceBlock(FurnaceTier.DIAMOND),
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> EMERALD_FURNACE_BLOCK = registerBlock(
    "emerald_furnace",
    () -> new FurnaceBlock(FurnaceTier.EMERALD),
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> NETHERITE_FURNACE_BLOCK = registerBlock(
    "netherite_furnace",
    () -> new FurnaceBlock(FurnaceTier.NETHERITE),
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> MELTER_BLOCK = registerBlock(
    "melter",
    Melter::new,
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> MACHINE_CASING = registerBlock(
    "machine_casing",
    MachineCasing::new,
    CreativeTabs.COMMON
  );

  public static final RegistrySupplier<Block> UPGRADE_MAKER = registerBlock(
    "upgrade_maker",
    UpgradeMaker::new,
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> JEWELRY_GENERATOR = registerBlock(
    "jewelry_generator",
    JewelryGenerator::new,
    CreativeTabs.MACHINES
  );

  public static final RegistrySupplier<Block> CIRCUIT_FABRICATOR = BLOCKS.register(
    "circuit_fabricator",
    CircuitFabricatorBlock::new
  );

  public static final RegistrySupplier<LiquidBlock> MOLTEN_RED_MATTER_BLOCK = BLOCKS.register(
    "molten_red_matter",
    () -> new LiquidBlock(FluidRegister.MOLTEN_RED_MATTER, BlockBehaviour.Properties.of(Material.WATER).noCollission().strength(100.0F).lightLevel(arg -> 13).noLootTable())
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
//    BLOCKS.register();
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
