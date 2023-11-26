package es.degrassi.forge.init.registration;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.init.block.generators.JewelryGenerator;
import es.degrassi.forge.init.entity.FurnaceEntity;
import es.degrassi.forge.init.entity.MelterEntity;
import es.degrassi.forge.init.entity.generators.JewelryGeneratorEntity;
import es.degrassi.forge.init.entity.panel.SolarPanelEntity;
import es.degrassi.forge.init.entity.UpgradeMakerEntity;
import es.degrassi.forge.init.geckolib.entity.CircuitFabricatorEntity;
import es.degrassi.forge.init.tiers.FurnaceTier;
import es.degrassi.forge.init.tiers.SolarPanelTier;
import es.degrassi.forge.integration.config.DegrassiConfig;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;

public class EntityRegister {
  public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
    DeferredRegister.create(Degrassi.MODID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);

  public static final RegistrySupplier<BlockEntityType<SolarPanelEntity>> SP_TIER_1 =
    BLOCK_ENTITIES.register("solar_panel_tier_1", () ->
      new BlockEntityType<>(
        (pos, state) -> new SolarPanelEntity(
          EntityRegister.SP_TIER_1.get(),
          pos,
          state,
          SolarPanelTier.I
        ),
        Set.of(BlockRegister.SP1_BLOCK.get()),
        null
      )
    );

  public static final RegistrySupplier<BlockEntityType<SolarPanelEntity>> SP_TIER_2 =
    BLOCK_ENTITIES.register("solar_panel_tier_2", () ->
      new BlockEntityType<>(
        (pos, state) -> new SolarPanelEntity(
          EntityRegister.SP_TIER_2.get(),
          pos,
          state,
          SolarPanelTier.II
        ),
        Set.of(BlockRegister.SP2_BLOCK.get()),
        null
      )
    );

  public static final RegistrySupplier<BlockEntityType<SolarPanelEntity>> SP_TIER_3 =
    BLOCK_ENTITIES.register("solar_panel_tier_3", () ->
      new BlockEntityType<>(
        (pos, state) -> new SolarPanelEntity(
          EntityRegister.SP_TIER_3.get(),
          pos,
          state,
          SolarPanelTier.III
        ),
        Set.of(BlockRegister.SP3_BLOCK.get()),
        null
      )
    );

  public static final RegistrySupplier<BlockEntityType<SolarPanelEntity>> SP_TIER_4 =
    BLOCK_ENTITIES.register("solar_panel_tier_4", () ->
      new BlockEntityType<>(
        (pos, state) -> new SolarPanelEntity(
          EntityRegister.SP_TIER_4.get(),
          pos,
          state,
          SolarPanelTier.IV
        ),
        Set.of(BlockRegister.SP4_BLOCK.get()),
        null
      )
    );

  public static final RegistrySupplier<BlockEntityType<SolarPanelEntity>> SP_TIER_5 =
    BLOCK_ENTITIES.register("solar_panel_tier_51", () ->
      new BlockEntityType<>(
        (pos, state) -> new SolarPanelEntity(
          EntityRegister.SP_TIER_5.get(),
          pos,
          state,
          SolarPanelTier.V
        ),
        Set.of(BlockRegister.SP5_BLOCK.get()),
        null
      )
    );

  public static final RegistrySupplier<BlockEntityType<SolarPanelEntity>> SP_TIER_6 =
    BLOCK_ENTITIES.register("solar_panel_tier_6", () ->
      new BlockEntityType<>(
        (pos, state) -> new SolarPanelEntity(
          EntityRegister.SP_TIER_6.get(),
          pos,
          state,
          SolarPanelTier.VI
        ),
        Set.of(BlockRegister.SP6_BLOCK.get()),
        null
      )
    );

  public static final RegistrySupplier<BlockEntityType<SolarPanelEntity>> SP_TIER_7 =
    BLOCK_ENTITIES.register("solar_panel_tier_7", () ->
      new BlockEntityType<>(
        (pos, state) -> new SolarPanelEntity(
          EntityRegister.SP_TIER_7.get(),
          pos,
          state,
          SolarPanelTier.VII
        ),
        Set.of(BlockRegister.SP7_BLOCK.get()),
        null
      )
    );

  public static final RegistrySupplier<BlockEntityType<SolarPanelEntity>> SP_TIER_8 =
    BLOCK_ENTITIES.register("solar_panel_tier_8", () ->
      new BlockEntityType<>(
        (pos, state) -> new SolarPanelEntity(
          EntityRegister.SP_TIER_8.get(),
          pos,
          state,
          SolarPanelTier.VIII
        ),
        Set.of(BlockRegister.SP8_BLOCK.get()),
        null
      )
    );

  public static final RegistrySupplier<BlockEntityType<FurnaceEntity>> IRON_FURNACE =
    BLOCK_ENTITIES.register("iron_furnace", () ->
      new BlockEntityType<>(
        (pos, state) -> new FurnaceEntity(
          EntityRegister.IRON_FURNACE.get(),
          pos,
          state,
          Component.translatable(
            "block.degrassi.iron_furnace"
          ),
          DegrassiConfig.get().furnaceConfig.iron_furnace_capacity,
          DegrassiConfig.get().furnaceConfig.iron_furnace_transfer,
          FurnaceTier.IRON
        ),
        Set.of(BlockRegister.IRON_FURNACE_BLOCK.get()),
        null
      )
    );
  public static final RegistrySupplier<BlockEntityType<FurnaceEntity>> GOLD_FURNACE =
    BLOCK_ENTITIES.register("gold_furnace", () ->
      new BlockEntityType<>(
        (pos, state) -> new FurnaceEntity(
          EntityRegister.GOLD_FURNACE.get(),
          pos,
          state,
          Component.translatable(
            "block.degrassi.gold_furnace"
          ),
          DegrassiConfig.get().furnaceConfig.gold_furnace_capacity,
          DegrassiConfig.get().furnaceConfig.gold_furnace_transfer,
          FurnaceTier.GOLD
        ),
        Set.of(BlockRegister.GOLD_FURNACE_BLOCK.get()),
        null
      )
    );
  public static final RegistrySupplier<BlockEntityType<FurnaceEntity>> DIAMOND_FURNACE =
    BLOCK_ENTITIES.register("diamond_furnace", () ->
      new BlockEntityType<>(
        (pos, state) -> new FurnaceEntity(
          EntityRegister.DIAMOND_FURNACE.get(),
          pos,
          state,
          Component.translatable(
            "block.degrassi.diamond_furnace"
          ),
          DegrassiConfig.get().furnaceConfig.diamond_furnace_capacity,
          DegrassiConfig.get().furnaceConfig.diamond_furnace_transfer,
          FurnaceTier.DIAMOND
        ),
        Set.of(BlockRegister.DIAMOND_FURNACE_BLOCK.get()),
        null
      )
    );
  public static final RegistrySupplier<BlockEntityType<FurnaceEntity>> EMERALD_FURNACE =
    BLOCK_ENTITIES.register("emerald_furnace", () ->
      new BlockEntityType<>(
        (pos, state) -> new FurnaceEntity(
          EntityRegister.EMERALD_FURNACE.get(),
          pos,
          state,
          Component.translatable(
            "block.degrassi.emerald_furnace"
          ),
          DegrassiConfig.get().furnaceConfig.emerald_furnace_capacity,
          DegrassiConfig.get().furnaceConfig.emerald_furnace_transfer,
          FurnaceTier.EMERALD
        ),
        Set.of(BlockRegister.EMERALD_FURNACE_BLOCK.get()),
        null
      )
    );
  public static final RegistrySupplier<BlockEntityType<FurnaceEntity>> NETHERITE_FURNACE =
    BLOCK_ENTITIES.register("netherite_furnace", () ->
      new BlockEntityType<>(
        (pos, state) -> new FurnaceEntity(
          EntityRegister.NETHERITE_FURNACE.get(),
          pos,
          state,
          Component.translatable(
            "block.degrassi.netherite_furnace"
          ),
          DegrassiConfig.get().furnaceConfig.netherite_furnace_capacity,
          DegrassiConfig.get().furnaceConfig.netherite_furnace_transfer,
          FurnaceTier.NETHERITE
        ),
        Set.of(BlockRegister.NETHERITE_FURNACE_BLOCK.get()),
        null
      )
    );

  public static final RegistrySupplier<BlockEntityType<MelterEntity>> MELTER =
    BLOCK_ENTITIES.register("melter", () ->
      new BlockEntityType<>(
        MelterEntity::new,
        Set.of(BlockRegister.MELTER_BLOCK.get()),
        null
      )
    );

  public static final RegistrySupplier<BlockEntityType<UpgradeMakerEntity>> UPGRADE_MAKER = BLOCK_ENTITIES.register("upgrade_maker", () ->
    new BlockEntityType<>(
      UpgradeMakerEntity::new,
      Set.of(BlockRegister.UPGRADE_MAKER.get()),
      null
    )
  );

  public static final RegistrySupplier<BlockEntityType<JewelryGeneratorEntity>> JEWELRY_GENERATOR = BLOCK_ENTITIES.register("jewelry_generator", () ->
    new BlockEntityType<>(
      JewelryGeneratorEntity::new,
      Set.of(BlockRegister.JEWELRY_GENERATOR.get()),
      null
    )
  );

  public static final RegistrySupplier<BlockEntityType<CircuitFabricatorEntity>> CIRCUIT_FABRICATOR = BLOCK_ENTITIES.register("circuit_fabricator", () ->
    new BlockEntityType<>(
      CircuitFabricatorEntity::new,
      Set.of(BlockRegister.CIRCUIT_FABRICATOR.get()),
      null
    )
  );

  public static void register() {
//    BLOCK_ENTITIES.register();
  }
}
