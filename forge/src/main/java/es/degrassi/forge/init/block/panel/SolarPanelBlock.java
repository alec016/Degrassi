package es.degrassi.forge.init.block.panel;

import dev.architectury.registry.menu.MenuRegistry;
import es.degrassi.forge.init.entity.panel.SolarPanelEntity;
import es.degrassi.forge.init.gui.container.panel.SolarPanelContainer;
import es.degrassi.forge.init.registration.EntityRegister;
import es.degrassi.forge.init.tiers.SolarPanelTier;
import es.degrassi.forge.network.EfficiencyPacket;
import es.degrassi.forge.network.EnergyPacket;
import es.degrassi.forge.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SolarPanelBlock extends PanelBlock {
  private final SolarPanelTier tier;
  public SolarPanelBlock(SolarPanelTier tier) {
    super();
    this.tier = tier;
  }

  @Override
  public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
    BlockEntityType<?> type = switch(tier) {
      case I -> EntityRegister.SP_TIER_1.get();
      case II -> EntityRegister.SP_TIER_2.get();
      case III -> EntityRegister.SP_TIER_3.get();
      case IV -> EntityRegister.SP_TIER_4.get();
      case V -> EntityRegister.SP_TIER_5.get();
      case VI -> EntityRegister.SP_TIER_6.get();
      case VII -> EntityRegister.SP_TIER_7.get();
      case VIII -> EntityRegister.SP_TIER_8.get();
    };
    SolarPanelEntity entity = new SolarPanelEntity(
      type,
      pos,
      state,
      tier
    );
    entity.delegate = this;
    return entity;
  }

  @SuppressWarnings("deprecation")
  @Override
  public @NotNull InteractionResult use(
    @NotNull BlockState state,
    @NotNull Level level,
    @NotNull BlockPos pos,
    @NotNull Player player,
    @NotNull InteractionHand hand,
    @NotNull BlockHitResult hit
  ) {
    BlockEntity tile = level.getBlockEntity(pos);
    if (tile instanceof SolarPanelEntity entity) {
      if (!level.isClientSide()) {
        MenuRegistry.openExtendedMenu((ServerPlayer) player, new MenuProvider() {
          @Override
          public @NotNull Component getDisplayName() {
            return entity.getName();
          }

          @Override
          public AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player) {
            new EnergyPacket(entity.ENERGY_STORAGE.getEnergyStored(), entity.ENERGY_STORAGE.getMaxEnergyStored(), entity.ENERGY_STORAGE.getMaxEnergyStored(), pos);
            new EfficiencyPacket(entity.getCurrentEfficiency().getEfficiency(), pos);
            return new SolarPanelContainer(id, inv, entity);
          }
        }, buf -> buf.writeBlockPos(pos));
        return InteractionResult.SUCCESS;
      }
      return InteractionResult.SUCCESS;
    }
    return super.use(state, level, pos, player, hand, hit);
  }

  @Override
  public float getFriction(BlockState state, LevelReader level, BlockPos pos, @Nullable Entity entity) {
    return this.getFriction();
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
    @NotNull Level level,
    @NotNull BlockState state,
    @NotNull BlockEntityType<T> type
  ) {
    return switch (tier) {
      case I -> Utils.createTickerHelper(type, EntityRegister.SP_TIER_1.get(), SolarPanelEntity::tick);
      case II -> Utils.createTickerHelper(type, EntityRegister.SP_TIER_2.get(), SolarPanelEntity::tick);
      case III -> Utils.createTickerHelper(type, EntityRegister.SP_TIER_3.get(), SolarPanelEntity::tick);
      case IV -> Utils.createTickerHelper(type, EntityRegister.SP_TIER_4.get(), SolarPanelEntity::tick);
      case V -> Utils.createTickerHelper(type, EntityRegister.SP_TIER_5.get(), SolarPanelEntity::tick);
      case VI -> Utils.createTickerHelper(type, EntityRegister.SP_TIER_6.get(), SolarPanelEntity::tick);
      case VII -> Utils.createTickerHelper(type, EntityRegister.SP_TIER_7.get(), SolarPanelEntity::tick);
      case VIII -> Utils.createTickerHelper(type, EntityRegister.SP_TIER_8.get(), SolarPanelEntity::tick);
    };
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pNewState, boolean pIsMoving) {
    if (pState.getBlock() != pNewState.getBlock()) {
      BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
      if (blockEntity instanceof SolarPanelEntity entity) {
        entity.drops();
      }
    }
    super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
  }
}
