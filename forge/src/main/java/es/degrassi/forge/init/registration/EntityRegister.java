package es.degrassi.forge.init.registration;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.init.entity.furnace.*;
import es.degrassi.forge.init.entity.melter.MelterEntity;
import es.degrassi.forge.init.entity.panel.sp.*;
import es.degrassi.forge.init.entity.upgrade_maker.UpgradeMakerEntity;
import es.degrassi.forge.integration.config.DegrassiConfig;
import net.minecraft.core.Registry;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Set;

public class EntityRegister {
  public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
    DeferredRegister.create(Degrassi.MODID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);

  public static final RegistrySupplier<BlockEntityType<SP1Entity>> SP_TIER_1 =
    BLOCK_ENTITIES.register("solar_panel_tier_1", () ->
      new BlockEntityType<>(
        SP1Entity::new,
        Set.of(BlockRegister.SP1_BLOCK.get()),
        null
      )
    );

  public static final RegistrySupplier<BlockEntityType<SP2Entity>> SP_TIER_2 =
    BLOCK_ENTITIES.register("solar_panel_tier_2", () ->
      new BlockEntityType<>(
        SP2Entity::new,
        Set.of(BlockRegister.SP2_BLOCK.get()),
        null
      )
    );

  public static final RegistrySupplier<BlockEntityType<SP3Entity>> SP_TIER_3 =
    BLOCK_ENTITIES.register("solar_panel_tier_3", () ->
      new BlockEntityType<>(
        SP3Entity::new,
        Set.of(BlockRegister.SP3_BLOCK.get()),
        null
      )
    );

  public static final RegistrySupplier<BlockEntityType<SP4Entity>> SP_TIER_4 =
    BLOCK_ENTITIES.register("solar_panel_tier_4", () ->
      new BlockEntityType<>(
        SP4Entity::new,
        Set.of(BlockRegister.SP4_BLOCK.get()),
        null
      )
    );

  public static final RegistrySupplier<BlockEntityType<SP5Entity>> SP_TIER_5 =
    BLOCK_ENTITIES.register("solar_panel_tier_51", () ->
      new BlockEntityType<>(
        SP5Entity::new,
        Set.of(BlockRegister.SP5_BLOCK.get()),
        null
      )
    );

  public static final RegistrySupplier<BlockEntityType<SP6Entity>> SP_TIER_6 =
    BLOCK_ENTITIES.register("solar_panel_tier_6", () ->
      new BlockEntityType<>(
        SP6Entity::new,
        Set.of(BlockRegister.SP6_BLOCK.get()),
        null
      )
    );

  public static final RegistrySupplier<BlockEntityType<SP7Entity>> SP_TIER_7 =
    BLOCK_ENTITIES.register("solar_panel_tier_7", () ->
      new BlockEntityType<>(
        SP7Entity::new,
        Set.of(BlockRegister.SP7_BLOCK.get()),
        null
      )
    );

  public static final RegistrySupplier<BlockEntityType<SP8Entity>> SP_TIER_8 =
    BLOCK_ENTITIES.register("solar_panel_tier_8", () ->
      new BlockEntityType<>(
        SP8Entity::new,
        Set.of(BlockRegister.SP8_BLOCK.get()),
        null
      )
    );

  public static final RegistrySupplier<BlockEntityType<IronFurnaceEntity>> IRON_FURNACE =
    BLOCK_ENTITIES.register("iron_furnace.json", () ->
      new BlockEntityType<>(
        IronFurnaceEntity::new,
        Set.of(BlockRegister.IRON_FURNACE_BLOCK.get()),
        null
      )
    );
  public static final RegistrySupplier<BlockEntityType<GoldFurnaceEntity>> GOLD_FURNACE =
    BLOCK_ENTITIES.register("gold_furnace", () ->
      new BlockEntityType<>(
        GoldFurnaceEntity::new,
        Set.of(BlockRegister.GOLD_FURNACE_BLOCK.get()),
        null
      )
    );
  public static final RegistrySupplier<BlockEntityType<DiamondFurnaceEntity>> DIAMOND_FURNACE =
    BLOCK_ENTITIES.register("diamond_furnace", () ->
      new BlockEntityType<>(
        DiamondFurnaceEntity::new,
        Set.of(BlockRegister.DIAMOND_FURNACE_BLOCK.get()),
        null
      )
    );
  public static final RegistrySupplier<BlockEntityType<EmeraldFurnaceEntity>> EMERALD_FURNACE =
    BLOCK_ENTITIES.register("emerald_furnace", () ->
      new BlockEntityType<>(
        EmeraldFurnaceEntity::new,
        Set.of(BlockRegister.EMERALD_FURNACE_BLOCK.get()),
        null
      )
    );
  public static final RegistrySupplier<BlockEntityType<NetheriteFurnaceEntity>> NETHERITE_FURNACE =
    BLOCK_ENTITIES.register("netherite_furnace", () ->
      new BlockEntityType<>(
        NetheriteFurnaceEntity::new,
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

  public static void register() {
    BLOCK_ENTITIES.register();
  }
}
