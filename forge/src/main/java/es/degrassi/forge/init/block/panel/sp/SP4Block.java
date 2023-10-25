package es.degrassi.forge.init.block.panel.sp;

import dev.architectury.registry.menu.MenuRegistry;
import es.degrassi.forge.init.entity.panel.sp.SolarPanelEntity;
import es.degrassi.forge.init.entity.panel.sp.SP4Entity;
import es.degrassi.forge.init.gui.container.panel.sp.SP4Container;
import es.degrassi.forge.init.registration.EntityRegister;
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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SP4Block extends SolarPanelBlock {
  public SP4Block() {
    super();
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
    return new SP4Entity(pos, state);
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
    if (tile instanceof SP4Entity entity) {
      if (!level.isClientSide()) {
        MenuRegistry.openExtendedMenu((ServerPlayer) player, new MenuProvider() {
          @Override
          public @NotNull Component getDisplayName() {
            return entity.getName();
          }

          @Override
          public AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player) {
            new EnergyPacket(entity.ENERGY_STORAGE.getEnergyStored(), entity.ENERGY_STORAGE.getMaxEnergyStored(), entity.ENERGY_STORAGE.getMaxEnergyStored(), pos);
            return new SP4Container(id, inv, entity);
          }
        }, buf -> buf.writeBlockPos(pos));
        return InteractionResult.SUCCESS;
      }
      return InteractionResult.SUCCESS;
    }
    return super.use(state, level, pos, player, hand, hit);
  }

  @Override
  public BlockState rotate(@NotNull BlockState state, LevelAccessor level, BlockPos pos, @NotNull Rotation rotation) {
    return state.setValue(BlockStateProperties.HORIZONTAL_FACING, rotation.rotate(state.getValue(BlockStateProperties.HORIZONTAL_FACING)));
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
    return Utils.createTickerHelper(type, EntityRegister.SP_TIER_4.get(), SolarPanelEntity::tick);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pNewState, boolean pIsMoving) {
    if (pState.getBlock() != pNewState.getBlock()) {
      BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
      if (blockEntity instanceof SP4Entity) {
        ((SP4Entity) blockEntity).drops();
      }
    }
    super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
  }
}
