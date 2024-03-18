package es.degrassi.forge.core.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.common.machines.entity.FurnaceEntity;
import es.degrassi.forge.core.common.machines.entity.SolarPanelEntity;
import es.degrassi.forge.core.tiers.Furnace;
import es.degrassi.forge.core.tiers.SolarPanel;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class EntityRegistration {
  public static final DeferredRegister<BlockEntityType<?>> ENTITIES = DeferredRegister.create(Degrassi.MODID, Registries.BLOCK_ENTITY_TYPE);
  public static final RegistrySupplier<BlockEntityType<FurnaceEntity>> IRON_FURNACE;
  public static final RegistrySupplier<BlockEntityType<FurnaceEntity>> GOLD_FURNACE;
  public static final RegistrySupplier<BlockEntityType<FurnaceEntity>> DIAMOND_FURNACE;
  public static final RegistrySupplier<BlockEntityType<FurnaceEntity>> EMERALD_FURNACE;
  public static final RegistrySupplier<BlockEntityType<FurnaceEntity>> NETHERITE_FURNACE;

  public static final RegistrySupplier<BlockEntityType<SolarPanelEntity>> SP1;
  public static final RegistrySupplier<BlockEntityType<SolarPanelEntity>> SP2;
  public static final RegistrySupplier<BlockEntityType<SolarPanelEntity>> SP3;
  public static final RegistrySupplier<BlockEntityType<SolarPanelEntity>> SP4;
  public static final RegistrySupplier<BlockEntityType<SolarPanelEntity>> SP5;
  public static final RegistrySupplier<BlockEntityType<SolarPanelEntity>> SP6;
  public static final RegistrySupplier<BlockEntityType<SolarPanelEntity>> SP7;
  public static final RegistrySupplier<BlockEntityType<SolarPanelEntity>> SP8;

  // Furnaces
  static {
    IRON_FURNACE = ENTITIES.register(
      "iron_furnace",
      () -> BlockEntityType.Builder.of(
        (pos, state) -> new FurnaceEntity(pos, state, Furnace.IRON),
        BlockRegistration.IRON_FURNACE.get()
      ).build(null)
    );
    GOLD_FURNACE = ENTITIES.register(
      "gold_furnace",
      () -> BlockEntityType.Builder.of(
        (pos, state) -> new FurnaceEntity(pos, state, Furnace.GOLD),
        BlockRegistration.GOLD_FURNACE.get()
      ).build(null)
    );
    DIAMOND_FURNACE = ENTITIES.register(
      "diamond_furnace",
      () -> BlockEntityType.Builder.of(
        (pos, state) -> new FurnaceEntity(pos, state, Furnace.DIAMOND),
        BlockRegistration.DIAMOND_FURNACE.get()
      ).build(null)
    );
    EMERALD_FURNACE = ENTITIES.register(
      "emerald_furnace",
      () -> BlockEntityType.Builder.of(
        (pos, state) -> new FurnaceEntity(pos, state, Furnace.EMERALD),
        BlockRegistration.EMERALD_FURNACE.get()
      ).build(null)
    );
    NETHERITE_FURNACE = ENTITIES.register(
      "netherite_furnace",
      () -> BlockEntityType.Builder.of(
        (pos, state) -> new FurnaceEntity(pos, state, Furnace.NETHERITE),
        BlockRegistration.NETHERITE_FURNACE.get()
      ).build(null)
    );
  }

  // Solar Panels
  static {
    SP1 = ENTITIES.register(
      "sp1",
      () -> BlockEntityType.Builder.of(
        (pos, state) -> new SolarPanelEntity(pos, state, SolarPanel.T1),
        BlockRegistration.SP1.get()
      ).build(null)
    );
    SP2 = ENTITIES.register(
      "sp2",
      () -> BlockEntityType.Builder.of(
        (pos, state) -> new SolarPanelEntity(pos, state, SolarPanel.T2),
        BlockRegistration.SP2.get()
      ).build(null)
    );
    SP3 = ENTITIES.register(
      "sp3",
      () -> BlockEntityType.Builder.of(
        (pos, state) -> new SolarPanelEntity(pos, state, SolarPanel.T3),
        BlockRegistration.SP3.get()
      ).build(null)
    );
    SP4 = ENTITIES.register(
      "sp4",
      () -> BlockEntityType.Builder.of(
        (pos, state) -> new SolarPanelEntity(pos, state, SolarPanel.T4),
        BlockRegistration.SP4.get()
      ).build(null)
    );
    SP5 = ENTITIES.register(
      "sp5",
      () -> BlockEntityType.Builder.of(
        (pos, state) -> new SolarPanelEntity(pos, state, SolarPanel.T5),
        BlockRegistration.SP5.get()
      ).build(null)
    );
    SP6 = ENTITIES.register(
      "sp6",
      () -> BlockEntityType.Builder.of(
        (pos, state) -> new SolarPanelEntity(pos, state, SolarPanel.T6),
        BlockRegistration.SP6.get()
      ).build(null)
    );
    SP7 = ENTITIES.register(
      "sp7",
      () -> BlockEntityType.Builder.of(
        (pos, state) -> new SolarPanelEntity(pos, state, SolarPanel.T7),
        BlockRegistration.SP7.get()
      ).build(null)
    );
    SP8 = ENTITIES.register(
      "sp8",
      () -> BlockEntityType.Builder.of(
        (pos, state) -> new SolarPanelEntity(pos, state, SolarPanel.T8),
        BlockRegistration.SP8.get()
      ).build(null)
    );
  }
}
