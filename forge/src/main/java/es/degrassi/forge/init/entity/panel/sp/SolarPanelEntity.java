package es.degrassi.forge.init.entity.panel.sp;

import es.degrassi.forge.init.entity.panel.PanelEntity;
import es.degrassi.forge.init.item.upgrade.types.IPanelUpgrade;
import es.degrassi.forge.util.storage.EfficiencyStorage;
import es.degrassi.forge.network.panel.PanelEfficiencyPacket;
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

    double eff, effMod = 1, genMod = 1, capacityMod = 1, transferMod = 1;

    if (tile.cache$seeSkyTimer > 0) --tile.cache$seeSkyTimer;

    if (tile.effCacheTime <= 0 || tile.genCacheTime <= 0 || tile.capacityCacheTime <= 0 || tile.transferCacheTime <= 0) {
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
        for(int i = 0; i < tile.itemHandler.getSlots(); i++) {
          ItemStack stack = tile.itemHandler.getStackInSlot(i);
          if (stack.getCount() > 0 && stack.getItem() instanceof IPanelUpgrade upgrade) {
            switch(upgrade.getUpgradeType()) {
              case EFFICIENCY -> effMod = upgrade.getModifier();
              case TRANSFER -> transferMod = upgrade.getModifier();
              case CAPACITY -> capacityMod = upgrade.getModifier();
              case GENERATION -> genMod = upgrade.getModifier();
            }
          }
        }
      }
      tile.effCache = eff * effMod;
      tile.genCache = (int) (tile.getGeneration() * tile.effCache * genMod);
      tile.capacityCache = (int) (tile.defaultCapacity * capacityMod);
      tile.transferCache = (int) (tile.defaultTransfer * transferMod);
      tile.effCacheTime = 5;
      tile.genCacheTime = 5;
      tile.capacityCacheTime = 5;
      tile.transferCacheTime = 5;
    }
    tile.efficiency.setEfficiency(tile.effCache);
    tile.currentGen.setGeneration(tile.genCache);
    tile.setCapacityLevel(tile.capacityCache);
    tile.setTransferRate(tile.transferCache);
    tile.receiveEnergy();
    setChanged(level, pos, state);
    if (tile.effCacheTime > 0) --tile.effCacheTime;
    if (tile.genCacheTime > 0) --tile.genCacheTime;
    if (tile.capacityCacheTime > 0) --tile.capacityCacheTime;
    if (tile.transferCacheTime > 0) --tile.transferCacheTime;
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
