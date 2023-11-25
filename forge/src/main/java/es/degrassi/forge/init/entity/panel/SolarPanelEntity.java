package es.degrassi.forge.init.entity.panel;

import es.degrassi.forge.init.block.panel.SolarPanelBlock;
import es.degrassi.forge.init.item.upgrade.types.IPanelUpgrade;
import es.degrassi.forge.init.registration.ItemRegister;
import es.degrassi.forge.init.tiers.SolarPanelTier;
import es.degrassi.forge.integration.config.DegrassiConfig;
import es.degrassi.forge.network.ItemPacket;
import es.degrassi.forge.util.storage.AbstractEnergyStorage;
import es.degrassi.forge.util.storage.EfficiencyStorage;
import es.degrassi.forge.network.EfficiencyPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SolarPanelEntity extends PanelEntity {
  public SolarPanelBlock delegate;
  protected EfficiencyStorage efficiency = createEfficiencyStorage(this);
  private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
  private LazyOptional<AbstractEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
  private final SolarPanelTier tier;

  @Contract("_ -> new")
  protected static @NotNull EfficiencyStorage createEfficiencyStorage(SolarPanelEntity entity) {
    return new EfficiencyStorage() {
      @Override
      public void onEfficiencyChanged() {
        entity.setChanged();
        if (entity.level != null && !entity.level.isClientSide()) {
          new EfficiencyPacket(this.getEfficiency(), entity.worldPosition)
            .sendToChunkListeners(entity.level.getChunkAt(entity.getBlockPos()));
        }
      }
    };
  }

  public SolarPanelEntity(BlockEntityType<?> type, BlockPos pos, BlockState state, SolarPanelTier tier) {
    super(type, pos, state, getName(tier), defaultCapacity(tier), defaultTransfer(tier));
    this.tier = tier;
    this.itemHandler = new ItemStackHandler(4) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
        if (level != null && !level.isClientSide() && level.getServer() != null) {
          new ItemPacket(this, worldPosition)
            .sendToAll(level.getServer());
          // .sendToChunkListeners(level.getChunkAt(pos));
        }
      }

      @Override
      public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return switch(slot) {
          case 0 -> stack.is(ItemRegister.EFFICIENCY_UPGRADE.get());
          case 1 -> stack.is(ItemRegister.TRANSFER_UPGRADE.get());
          case 2 -> stack.is(ItemRegister.GENERATION_UPGRADE.get());
          case 3 -> stack.is(ItemRegister.CAPACITY_UPGRADE.get());
          default -> isItemValid(slot, stack);
        };
      }
    };
  }

  private static int defaultCapacity(SolarPanelTier tier) {
    if (tier == null) return 0;
    return switch(tier) {
      case I -> DegrassiConfig.get().solarPanelConfig.sp1_capacity;
      case II -> DegrassiConfig.get().solarPanelConfig.sp2_capacity;
      case III -> DegrassiConfig.get().solarPanelConfig.sp3_capacity;
      case IV -> DegrassiConfig.get().solarPanelConfig.sp4_capacity;
      case V -> DegrassiConfig.get().solarPanelConfig.sp5_capacity;
      case VI -> DegrassiConfig.get().solarPanelConfig.sp6_capacity;
      case VII -> DegrassiConfig.get().solarPanelConfig.sp7_capacity;
      case VIII -> DegrassiConfig.get().solarPanelConfig.sp8_capacity;
    };
  }

  private static int defaultTransfer(SolarPanelTier tier) {
    if (tier == null) return 0;
    return switch(tier) {
      case I -> DegrassiConfig.get().solarPanelConfig.sp1_transfer;
      case II -> DegrassiConfig.get().solarPanelConfig.sp2_transfer;
      case III -> DegrassiConfig.get().solarPanelConfig.sp3_transfer;
      case IV -> DegrassiConfig.get().solarPanelConfig.sp4_transfer;
      case V -> DegrassiConfig.get().solarPanelConfig.sp5_transfer;
      case VI -> DegrassiConfig.get().solarPanelConfig.sp6_transfer;
      case VII -> DegrassiConfig.get().solarPanelConfig.sp7_transfer;
      case VIII -> DegrassiConfig.get().solarPanelConfig.sp8_transfer;
    };
  }

  private static Component getName(SolarPanelTier tier) {
    return switch(tier) {
      case I -> Component.translatable("block.degrassi.solar_panel_tier_1");
      case II -> Component.translatable("block.degrassi.solar_panel_tier_2");
      case III -> Component.translatable("block.degrassi.solar_panel_tier_3");
      case IV -> Component.translatable("block.degrassi.solar_panel_tier_4");
      case V -> Component.translatable("block.degrassi.solar_panel_tier_5");
      case VI -> Component.translatable("block.degrassi.solar_panel_tier_6");
      case VII -> Component.translatable("block.degrassi.solar_panel_tier_7");
      case VIII -> Component.translatable("block.degrassi.solar_panel_tier_8");
    };
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

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ENERGY) return lazyEnergyHandler.cast();
    if (cap == ForgeCapabilities.ITEM_HANDLER) return lazyItemHandler.cast();

    return super.getCapability(cap, side);
  }

  @Override
  public void onLoad() {
    super.onLoad();
    lazyItemHandler = LazyOptional.of(() -> itemHandler);
    lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    lazyItemHandler.invalidate();
    lazyEnergyHandler.invalidate();
  }

  @Override
  public int getGeneration() {
    return switch(tier) {
      case I -> DegrassiConfig.get().solarPanelConfig.sp1_generation;
      case II -> DegrassiConfig.get().solarPanelConfig.sp2_generation;
      case III -> DegrassiConfig.get().solarPanelConfig.sp3_generation;
      case IV -> DegrassiConfig.get().solarPanelConfig.sp4_generation;
      case V -> DegrassiConfig.get().solarPanelConfig.sp5_generation;
      case VI -> DegrassiConfig.get().solarPanelConfig.sp6_generation;
      case VII -> DegrassiConfig.get().solarPanelConfig.sp7_generation;
      case VIII -> DegrassiConfig.get().solarPanelConfig.sp8_generation;
    };
  }

  @Override
  public @NotNull CompoundTag getUpdateTag() {
    CompoundTag nbt = super.getUpdateTag();
    nbt.put("panel.inventory", itemHandler.serializeNBT());
    nbt.put("panel.energy", ENERGY_STORAGE.serializeNBT());
    return nbt;
  }

  @Override
  public void handleUpdateTag(CompoundTag tag) {
    load(tag);
  }

  public SolarPanelTier tier() {
    return tier;
  }

}
