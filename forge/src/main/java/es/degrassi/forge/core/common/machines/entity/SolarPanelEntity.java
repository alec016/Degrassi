package es.degrassi.forge.core.common.machines.entity;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.api.core.common.ElementDirection;
import es.degrassi.common.utils.Utils;
import es.degrassi.forge.core.common.component.BarComponent;
import es.degrassi.forge.core.common.component.ComponentIOMode;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.common.machines.block.SolarPanelBlock;
import es.degrassi.forge.core.common.recipe.SolarPanelRecipe;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.tiers.SolarPanel;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;

public class SolarPanelEntity extends MachineEntity<SolarPanelRecipe> {
  public static final float RAIN_MULTIPLIER = 0.6F, THUNDER_MULTIPLIER = 0.4F;
  public static final ModelProperty<Level> WORLD_PROP = new ModelProperty<>();
  public static final ModelProperty<BlockPos> POS_PROP = new ModelProperty<>();
  @Setter
  @Getter
  private SolarPanel tier;
  int voxelTimer = 0;
  VoxelShape shape;
  protected int effCacheTime;
  protected double effCache;
  protected int transferCache;
  protected int transferCacheTime;
  protected int genCache;
  protected int genCacheTime;
  protected int capacityCache;
  protected int capacityCacheTime;
  public boolean cache$seeSky;
  public byte cache$seeSkyTimer;
  public SolarPanelEntity(BlockPos pos, BlockState blockState, SolarPanel tier) {
    super(EntityRegistration.SP.get(), pos, blockState);

    this.getComponentManager()
      .addEnergy(tier.getEnergyCapacity(), "energy", ComponentIOMode.OUTPUT)
      .addBar(100.0, "efficiency")
      .addBar(tier.getMaxGeneration(), "generation");

    this.getElementManager()
      .addPlayerInventory(
        7,
        97,
        Component.literal("player_inventory"),
        new DegrassiLocation("textures/gui/base_inventory.png")
      ).addEnergy(
        25,
        20,
        Component.literal("energy"),
        new DegrassiLocation("textures/gui/panel_energy_empty.png"),
        new DegrassiLocation("textures/gui/panel_energy_filled.png"),
        "energy"
      ).addBar(
        43,
        20,
        Component.literal("Efficiency"),
        new DegrassiLocation("textures/gui/panel_efficiency_empty.png"),
        new DegrassiLocation("textures/gui/panel_efficiency_filled.png"),
        "efficiency",
        ElementDirection.TOP
      );

    this.tier = tier;
  }

  @Override
  public @NotNull ModelData getModelData() {
    return ModelData.builder()
      .with(WORLD_PROP, level)
      .with(POS_PROP, worldPosition)
      .build();
  }

  public static void serverTick(
    @NotNull Level level,
    BlockPos pos,
    BlockState state,
    @NotNull SolarPanelEntity entity
  ) {
    double eff, effMod = 1, genMod = 1, capacityMod = 1, transferMod = 1;
    if (entity.voxelTimer > 0)
      --entity.voxelTimer;
    entity.getComponentManager().serverTick();
    entity.getElementManager().serverTick();
    if (entity.cache$seeSkyTimer > 0) --entity.cache$seeSkyTimer;
    if (entity.effCacheTime <= 0 || entity.genCacheTime <= 0 || entity.capacityCacheTime <= 0 || entity.transferCacheTime <= 0) {
      eff = calcEfficiency(entity);
      {
        float raining = level.getRainLevel(1F);
        raining = raining > 0.2F ? (raining - 0.2F) / 0.8F : 0F;
        raining = (float) Math.sin(raining * Math.PI / 2F);
        eff *= 1F - raining * (1F - RAIN_MULTIPLIER);

        float thundering = level.getThunderLevel(1F);
        thundering = thundering > 0.75F ? (thundering - 0.75F) / 0.25F : 0F;
        thundering = (float) Math.sin(thundering * Math.PI / 2F);
        eff *= 1F - thundering * (1F - THUNDER_MULTIPLIER);
//        for(int i = 0; i < tile.itemHandler.getSlots(); i++) {
//          ItemStack stack = tile.itemHandler.getStackInSlot(i);
//          if (stack.getCount() > 0 && stack.getItem() instanceof IPanelUpgrade upgrade) {
//            switch(upgrade.getUpgradeType()) {
//              case EFFICIENCY -> effMod = upgrade.getModifier();
//              case TRANSFER -> transferMod = upgrade.getModifier();
//              case CAPACITY -> capacityMod = upgrade.getModifier();
//              case GENERATION -> genMod = upgrade.getModifier();
//            }
//          }
//        }
      }
      entity.effCache = eff * effMod;
      entity.genCache = (int) (entity.getTier().getMaxGeneration() * entity.effCache * genMod);
      entity.capacityCache = (int) (entity.getTier().getEnergyCapacity() * capacityMod);
      entity.transferCache = (int) (entity.getTier().getEnergyTransfer() * transferMod);
      entity.effCacheTime = 5;
      entity.genCacheTime = 5;
      entity.capacityCacheTime = 5;
      entity.transferCacheTime = 5;
    }
    entity.getComponentManager().getComponent("efficiency").map(comp -> (BarComponent) comp)
      .ifPresent(comp -> comp.setAmount(Double.parseDouble(Utils.format(entity.effCache * 100))));
    entity.getComponentManager().getComponent("generation").map(comp -> (BarComponent) comp)
        .ifPresent(comp -> {
          comp.setAmount(Double.parseDouble(Utils.format(entity.genCache)));
          comp.setCapacity(entity.getTier().getMaxGeneration());
        });
    entity.getComponentManager().getComponent("energy").map(comp -> (EnergyComponent) comp).ifPresent(comp -> {
      comp.setCapacity(entity.capacityCache);
      comp.setTransfer(entity.transferCache);
      comp.receiveEnergy(entity.genCache, false);
    });
    if (entity.effCacheTime > 0) --entity.effCacheTime;
    if (entity.genCacheTime > 0) --entity.genCacheTime;
    if (entity.capacityCacheTime > 0) --entity.capacityCacheTime;
    if (entity.transferCacheTime > 0) --entity.transferCacheTime;
    level.updateNeighbourForOutputSignal(pos, state.getBlock());
    setChanged(level, pos, state);
  }

  private static double calcEfficiency(@NotNull SolarPanelEntity solar) {
    if(!solar.doesSeeSky())
      return 0F;

    assert solar.getLevel() != null;
    float celestialAngleRadians = solar.getLevel().getSunAngle(1F);
    if(celestialAngleRadians > Math.PI)
      celestialAngleRadians = (float) (2 * Math.PI - celestialAngleRadians);
    int lowLightCount = 0;
    float multiplicator = 1.5F - (lowLightCount * .122F);
    float displacement = 1.2F + (lowLightCount * .08F);

    return Mth.clamp(multiplicator * Mth.cos(celestialAngleRadians / displacement), 0, 1);
  }


  @Override
  public Component getName() {
    return getTier().getTranslation();
  }

  public VoxelShape getShape(SolarPanelBlock block) {
    if (shape == null || voxelTimer <= 0) {
      shape = block.recalcShape(Objects.requireNonNull(getLevel()), worldPosition);
      voxelTimer = 20;
    }
    return shape;
  }

  public void resetVoxelShape() {
    shape = null;
  }

  @Override
  protected void saveAdditional(@NotNull CompoundTag tag) {
    super.saveAdditional(tag);
    tag.putString("tier", tier.name().toLowerCase());
  }

  @Override
  public void load(@NotNull CompoundTag tag) {
    super.load(tag);
    tier = SolarPanel.value(tag.getString("tier"));
  }

  public boolean doesSeeSky() {
    if (cache$seeSkyTimer < 1) {
      cache$seeSkyTimer = 20;
      cache$seeSky =
        level != null &&
          level.getBrightness(LightLayer.SKY, worldPosition) > 0 &&
          level.canSeeSky(worldPosition.above());
    }
    return cache$seeSky;
  }
}
