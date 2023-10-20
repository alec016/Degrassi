package es.degrassi.forge.init.block.furnace;

import dev.architectury.registry.menu.MenuRegistry;
import es.degrassi.comon.init.block.furnace.FurnaceBlock;
import es.degrassi.comon.init.entity.furnace.FurnaceEntity;
import es.degrassi.comon.util.Utils;
import es.degrassi.forge.init.entity.furnace.IronFurnaceEntity;
import es.degrassi.forge.init.gui.container.furnace.IronFurnaceContainer;
import es.degrassi.forge.init.registration.EntityRegister;
import es.degrassi.network.furnace.FurnaceEnergyPacket;
import es.degrassi.network.furnace.FurnaceProgressPacket;
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

public class IronFurnace extends FurnaceBlock {
  public IronFurnace() {
    super();
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
    if(this.entity == null) this.entity = new IronFurnaceEntity(pos, state);
    return this.entity;
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
    if (tile instanceof IronFurnaceEntity) {
      this.entity = (FurnaceEntity) tile;
      if (!level.isClientSide()) {
        MenuRegistry.openExtendedMenu((ServerPlayer) player, new MenuProvider() {
          @Override
          public @NotNull Component getDisplayName() {
            return entity.getName();
          }

          @Override
          public AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player) {
            new FurnaceEnergyPacket(entity.ENERGY_STORAGE.getEnergyStored(), entity.ENERGY_STORAGE.getMaxEnergyStored(), entity.ENERGY_STORAGE.getMaxEnergyStored(), pos);
            new FurnaceProgressPacket(entity.progressStorage.getProgress(), entity.progressStorage.getMaxProgress(), pos);
            return new IronFurnaceContainer(id, inv, entity);
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
    return Utils.createTickerHelper(type, EntityRegister.IRON_FURNACE.get(), FurnaceEntity::tick);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pNewState, boolean pIsMoving) {
    if (pState.getBlock() != pNewState.getBlock()) {
      BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
      if (blockEntity instanceof IronFurnaceEntity) {
        ((IronFurnaceEntity) blockEntity).drops();
      }
    }
    super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
  }
}
