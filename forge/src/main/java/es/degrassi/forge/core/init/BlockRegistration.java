package es.degrassi.forge.core.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.common.machines.block.FurnaceBlock;
import es.degrassi.forge.core.tiers.Furnace;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class BlockRegistration {
  public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Degrassi.MODID, Registries.BLOCK);
  public static final RegistrySupplier<FurnaceBlock> IRON_FURNACE = BLOCKS.register("iron_furnace", () -> new FurnaceBlock(
    BlockBehaviour.Properties.of().destroyTime(500),
    Furnace.IRON
  ));
  public static final RegistrySupplier<FurnaceBlock> GOLD_FURNACE = BLOCKS.register("gold_furnace", () -> new FurnaceBlock(
    BlockBehaviour.Properties.of().destroyTime(500),
    Furnace.GOLD
  ));
  public static final RegistrySupplier<FurnaceBlock> DIAMOND_FURNACE = BLOCKS.register("diamond_furnace", () -> new FurnaceBlock(
    BlockBehaviour.Properties.of().destroyTime(500),
    Furnace.DIAMOND
  ));
  public static final RegistrySupplier<FurnaceBlock> EMERALD_FURNACE = BLOCKS.register("emerald_furnace", () -> new FurnaceBlock(
    BlockBehaviour.Properties.of().destroyTime(500),
    Furnace.EMERALD
  ));
  public static final RegistrySupplier<FurnaceBlock> NETHERITE_FURNACE = BLOCKS.register("netherite_furnace", () -> new FurnaceBlock(
    BlockBehaviour.Properties.of().destroyTime(500),
    Furnace.NETHERITE
  ));
}
