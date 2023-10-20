package es.degrassi.comon.init.entity.panel;

import es.degrassi.comon.init.item.upgrade.PanelUpgrade;
import es.degrassi.comon.util.storage.EfficiencyStorage;
import es.degrassi.network.panel.PanelEfficiencyPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public abstract class SolarPanelEntity extends PanelEntity {
  protected EfficiencyStorage efficiency = createEfficiencyStorage(this);

  @Contract("_ -> new")
  protected static @NotNull EfficiencyStorage createEfficiencyStorage(SolarPanelEntity entity) {
    return new EfficiencyStorage() {
      @Override
      public void onEfficiencyChanged() {
        entity.setChanged();
        if (entity.level != null && !entity.level.isClientSide()) {
          new PanelEfficiencyPacket(this.getEfficiency(), entity.worldPosition)
            .sendToChunkListeners(entity.level.getChunkAt(entity.getBlockPos()));
        }
      }
    };
  }

  public SolarPanelEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, Component name, int defaultCapacity, int defaultTransfer) {
    super(blockEntityType, pos, state, name, defaultCapacity, defaultTransfer);
  }

  public static void tick(@NotNull Level level, BlockPos pos, BlockState state, @NotNull SolarPanelEntity tile) {
    if (level.isClientSide()) return;

    double eff = tile.effCache;
    ItemStack efficiencyItem = tile.itemHandler.getStackInSlot(0);
    ItemStack transferItem = tile.itemHandler.getStackInSlot(1);
    ItemStack generationItem = tile.itemHandler.getStackInSlot(2);
    ItemStack capacityItem = tile.itemHandler.getStackInSlot(3);

    if(tile.cache$seeSkyTimer > 0)
      --tile.cache$seeSkyTimer;

    if(tile.effCacheTime <= 0) {
      eff = calcEfficiency(tile);
      {
        float raining = level.getRainLevel(1F);
        raining = raining > 0.2F ? (raining - 0.2F) / 0.8F : 0F;
        raining = (float) Math.sin(raining * Math.PI / 2F);
        eff *= 1F - raining * (1F - RAIN_MULTIPLIER);

        float thundering = level.getThunderLevel(1F);
        thundering = thundering > 0.75F ? (thundering - 0.75F) / 0.25F : 0F;
        thundering = (float) Math.sin(thundering * Math.PI / 2F);
        eff *= 1F - thundering * (1F - THUNDER_MULTIPLIER);
        if (efficiencyItem.getItem() instanceof PanelUpgrade upgrade) {
          if (efficiencyItem.getDisplayName().contains(Component.translatable("item.degrassi.efficiency_upgrade"))) {
            eff *= upgrade.getModifier();
          }
        }
        if (transferItem.getItem() instanceof PanelUpgrade upgrade) {
          if (transferItem.getDisplayName().contains(Component.translatable("item.degrassi.transfer_upgrade"))) {
            if (tile.getEnergyStorage().getMaxExtract() <= tile.defaultTransfer) {
              tile.getEnergyStorage().setMaxExtract((int) Math.floor(tile.getEnergyStorage().getMaxExtract() * upgrade.getModifier()));
            }
          } else {
            if (tile.getEnergyStorage().getMaxExtract() > tile.defaultTransfer) {
              tile.getEnergyStorage().setMaxExtract(tile.defaultTransfer);
            }
          }
        } else {
          tile.getEnergyStorage().setMaxExtract(tile.defaultTransfer);
        }
        if (capacityItem.getItem() instanceof PanelUpgrade upgrade) {
          if (capacityItem.getDisplayName().contains(Component.translatable("item.degrassi.capacity_upgrade"))) {
            if (tile.getEnergyStorage().getMaxEnergyStored() <= tile.defaultCapacity) {
              tile.setCapacityLevel((int) Math.floor(tile.getEnergyStorage().getMaxEnergyStored() * upgrade.getModifier()));
              tile.setEnergyToCapacity();
            }
          } else {
            tile.setCapacityLevel(tile.defaultCapacity);
            tile.setEnergyToCapacity();
          }
        } else {
          tile.setCapacityLevel(tile.defaultCapacity);
          tile.setEnergyToCapacity();
        }
      }
      tile.effCache = eff;
      tile.effCacheTime = 5;
    }
    tile.efficiency.setEfficiency(eff);
    double gen = tile.getGeneration() * eff;
    if (generationItem.getItem() instanceof PanelUpgrade upgrade) {
      if (generationItem.getDisplayName().contains(Component.translatable("item.degrassi.generation_upgrade"))) {
        gen *= upgrade.getModifier();
      }
    }
    tile.currentGen.setGeneration((int) gen);
    tile.receiveEnergy();
    setChanged(level, pos, state);
    if(tile.effCacheTime > 0) --tile.effCacheTime;
  }

  private static double calcEfficiency(@NotNull PanelEntity solar) {
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

  public EfficiencyStorage getCurrentEfficiency() {
    return this.efficiency;
  }

}
