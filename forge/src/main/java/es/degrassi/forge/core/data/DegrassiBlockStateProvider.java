package es.degrassi.forge.core.data;

import com.google.gson.JsonObject;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.client.model.loader.CableModelLoader;
import es.degrassi.forge.core.common.cables.CableType;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.tiers.CableTier;
import es.degrassi.forge.core.tiers.EnergyCableTier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class DegrassiBlockStateProvider extends BlockStateProvider {
  public DegrassiBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
    super(output, Degrassi.MODID, exFileHelper);
  }

  @Override
  public void registerStatesAndModels() {
    horizontalBlock(
      BlockRegistration.IRON_FURNACE,
      new DegrassiLocation("block/furnace/iron_furnace_side"),
      new DegrassiLocation("block/furnace/iron_furnace"),
      new DegrassiLocation("block/furnace/iron_furnace_side")
    );
    horizontalBlock(
      BlockRegistration.GOLD_FURNACE,
      new DegrassiLocation("block/furnace/gold_furnace_side"),
      new DegrassiLocation("block/furnace/gold_furnace"),
      new DegrassiLocation("block/furnace/gold_furnace_side")
    );
    horizontalBlock(
      BlockRegistration.DIAMOND_FURNACE,
      new DegrassiLocation("block/furnace/diamond_furnace_side"),
      new DegrassiLocation("block/furnace/diamond_furnace"),
      new DegrassiLocation("block/furnace/diamond_furnace_side")
    );
    horizontalBlock(
      BlockRegistration.EMERALD_FURNACE,
      new DegrassiLocation("block/furnace/emerald_furnace_side"),
      new DegrassiLocation("block/furnace/emerald_furnace"),
      new DegrassiLocation("block/furnace/emerald_furnace_side")
    );
    horizontalBlock(
      BlockRegistration.NETHERITE_FURNACE,
      new DegrassiLocation("block/furnace/netherite_furnace_side"),
      new DegrassiLocation("block/furnace/netherite_furnace"),
      new DegrassiLocation("block/furnace/netherite_furnace_side")
    );
    BlockModelBuilder basic_energy_cable = models().getBuilder("basic_energy_cable")
      .parent(models().getExistingFile(mcLoc("cube")))
      .customLoader((builder, helper) -> new CableLoaderBuilder(CableModelLoader.GENERATOR_LOADER, builder, helper, false, CableType.ENERGY, CableTier.BASIC))
      .end();
    BlockModelBuilder advance_energy_cable = models().getBuilder("advance_energy_cable")
      .parent(models().getExistingFile(mcLoc("cube")))
      .customLoader((builder, helper) -> new CableLoaderBuilder(CableModelLoader.GENERATOR_LOADER, builder, helper, false, CableType.ENERGY, CableTier.ADVANCE))
      .end();
    BlockModelBuilder extreme_energy_cable = models().getBuilder("extreme_energy_cable")
      .parent(models().getExistingFile(mcLoc("cube")))
      .customLoader((builder, helper) -> new CableLoaderBuilder(CableModelLoader.GENERATOR_LOADER, builder, helper, false, CableType.ENERGY, CableTier.EXTREME))
      .end();
    simpleBlock(BlockRegistration.BASIC_ENERGY_CABLE.get(), basic_energy_cable);
    simpleBlock(BlockRegistration.ADVANCE_ENERGY_CABLE.get(), advance_energy_cable);
    simpleBlock(BlockRegistration.EXTREME_ENERGY_CABLE.get(), extreme_energy_cable);
    BlockModelBuilder facade = models().getBuilder("facade")
      .parent(models().getExistingFile(mcLoc("cube")))
      .customLoader((builder, helper) -> new CableLoaderBuilder(CableModelLoader.GENERATOR_LOADER, builder, helper, true, CableType.FACADE, null))
      .end();
    simpleBlock(BlockRegistration.CABLE_FACADE.get(), facade);
  }

  private void horizontalBlock(RegistrySupplier<? extends Block> supplier, ResourceLocation side, ResourceLocation front, ResourceLocation top) {
    horizontalBlock(supplier.get(), side, front, top);
  }

  public static class CableLoaderBuilder extends CustomLoaderBuilder<BlockModelBuilder> {

    private final boolean facade;
    private final CableType type;
    private final CableTier tier;

    public CableLoaderBuilder(ResourceLocation loader, BlockModelBuilder parent, ExistingFileHelper existingFileHelper,
                              boolean facade, CableType type, CableTier tier) {
      super(loader, parent, existingFileHelper);
      this.facade = facade;
      this.type = type;
      this.tier = tier;
    }

    @Override
    public JsonObject toJson(JsonObject json) {
      JsonObject obj = super.toJson(json);
      obj.addProperty("facade", facade);
      if (type != null) obj.addProperty("cable_type", type.name().toLowerCase());
      if (tier != null) obj.addProperty("cable_tier", tier.name().toLowerCase());
      return obj;
    }
  }
}
