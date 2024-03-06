package es.degrassi.forge.core.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.common.machines.entity.FurnaceEntity;
import es.degrassi.forge.core.tiers.Furnace;
import java.util.Set;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class EntityRegistration {
  public static final DeferredRegister<BlockEntityType<?>> ENTITIES = DeferredRegister.create(Degrassi.MODID, Registries.BLOCK_ENTITY_TYPE);
  public static final RegistrySupplier<BlockEntityType<FurnaceEntity>> IRON_FURNACE = ENTITIES.register(
    "iron_furnace",
//    () -> BlockEntityType.Builder.of(
//      (pos, state) -> new FurnaceEntity(pos, state, Furnace.IRON),
//      BlockRegistration.IRON_FURNACE.get()
//    ).build(null)
    () -> new BlockEntityType<>(
      (pos, state) -> new FurnaceEntity(pos, state, Furnace.IRON),
      Set.of(BlockRegistration.IRON_FURNACE.get()),
      null
    )
  );
  public static final RegistrySupplier<BlockEntityType<FurnaceEntity>> GOLD_FURNACE = ENTITIES.register("gold_furnace", () -> BlockEntityType.Builder.of(
    (pos, state) -> new FurnaceEntity(pos, state, Furnace.GOLD),
    BlockRegistration.GOLD_FURNACE.get()
  ).build(null));
  public static final RegistrySupplier<BlockEntityType<FurnaceEntity>> DIAMOND_FURNACE = ENTITIES.register("diamond_furnace", () -> BlockEntityType.Builder.of(
    (pos, state) -> new FurnaceEntity(pos, state, Furnace.DIAMOND),
    BlockRegistration.DIAMOND_FURNACE.get()
  ).build(null));
  public static final RegistrySupplier<BlockEntityType<FurnaceEntity>> EMERALD_FURNACE = ENTITIES.register("emerald_furnace", () -> BlockEntityType.Builder.of(
    (pos, state) -> new FurnaceEntity(pos, state, Furnace.EMERALD),
    BlockRegistration.EMERALD_FURNACE.get()
  ).build(null));
  public static final RegistrySupplier<BlockEntityType<FurnaceEntity>> NETHERITE_FURNACE = ENTITIES.register("netherite_furnace", () -> BlockEntityType.Builder.of(
    (pos, state) -> new FurnaceEntity(pos, state, Furnace.NETHERITE),
    BlockRegistration.NETHERITE_FURNACE.get()
  ).build(null));
}
