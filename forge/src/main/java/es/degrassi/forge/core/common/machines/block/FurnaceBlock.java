package es.degrassi.forge.core.common.machines.block;

import dev.architectury.registry.menu.MenuRegistry;
import es.degrassi.forge.EnvHandler;
import es.degrassi.forge.api.utils.Utils;
import es.degrassi.forge.core.common.cables.IBlock;
import es.degrassi.forge.core.common.machines.container.FurnaceContainer;
import es.degrassi.forge.core.common.machines.entity.FurnaceEntity;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.machines.item.FurnaceItem;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.tiers.Furnace;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class FurnaceBlock extends MachineBlock implements IBlock<Furnace, FurnaceBlock> {
  private final Furnace tier;
  public FurnaceBlock(Properties properties, Furnace tier) {
    super(properties);
    this.tier = tier;
  }

  public BlockItem getBlockItem(Item.Properties properties) {
    return new FurnaceItem(this, new Item.Properties());
  }

  @Override
  protected Facing getFacing() {
    return Facing.HORIZONTAL;
  }

  public Furnace getTier() {
    return tier;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
    return EnvHandler.INSTANCE.createFurnace(pos, state, tier);
  }

  @SuppressWarnings("deprecation")
  @Override
  public @NotNull InteractionResult use(
    @NotNull BlockState state,
    Level level,
    @NotNull BlockPos pos,
    @NotNull Player player,
    @NotNull InteractionHand hand,
    @NotNull BlockHitResult hit
  ) {
    BlockEntity tile = level.getBlockEntity(pos);
    if (tile instanceof FurnaceEntity entity) {
      if (!level.isClientSide()) {
        MenuRegistry.openExtendedMenu((ServerPlayer) player, new MenuProvider() {
          @Override
          public @NotNull Component getDisplayName() {
            return entity.getName();
          }

          @Override
          public AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player) {
            entity.getComponentManager().markDirty();
            entity.getElementManager().markDirty();
            return new FurnaceContainer(id, inv, entity);
          }
        }, buf -> buf.writeBlockPos(pos));
        return InteractionResult.SUCCESS;
      }
      return InteractionResult.SUCCESS;
    }
    return super.use(state, level, pos, player, hand, hit);
  }


  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
    return Utils.createTickerHelper(
      type, EntityRegistration.FURNACE.get(),
      level.isClientSide()
        ? MachineEntity::clientTick
        : MachineEntity::serverTick
    );
  }

  @Override
  public Furnace getVariant() {
    return getTier();
  }
}
