package es.degrassi.forge.core.common.machines.block;

import dev.architectury.registry.menu.MenuRegistry;
import es.degrassi.forge.EnvHandler;
import es.degrassi.common.utils.Utils;
import es.degrassi.common.registry.IBlock;
import es.degrassi.forge.core.common.machines.container.ChestContainer;
import es.degrassi.forge.core.common.machines.entity.ChestEntity;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.machines.item.ChestItem;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.tiers.Chest;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class ChestBlock extends MachineBlock implements IBlock<Chest, ChestBlock> {
  private final Chest tier;
  public ChestBlock(Properties properties, Chest tier) {
    super(properties);
    this.tier = tier;
  }

  public BlockItem getBlockItem(Item.Properties properties) {
    return new ChestItem(this, new Item.Properties());
  }

  @Override
  protected Facing getFacing() {
    return Facing.HORIZONTAL;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
    return EnvHandler.INSTANCE.createChest(pos, state, tier);
  }

  @Override
  public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
    return Utils.createTickerHelper(
      type, EntityRegistration.CHEST.get(),
      level.isClientSide()
        ? ChestEntity::lidAnimateTick
        : MachineEntity::serverTick
    );
  }

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
    if (tile instanceof ChestEntity entity) {
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
//            player.awardStat(getOpenChestStat());
            entity.startOpen(player);
            return new ChestContainer(id, inv, entity);
          }
        }, buf -> buf.writeBlockPos(pos));
        return InteractionResult.CONSUME;
      }
      return InteractionResult.SUCCESS;
    }
    return super.use(state, level, pos, player, hand, hit);
  }

  protected Stat<ResourceLocation> getOpenChestStat() {
    return Stats.CUSTOM.get(Stats.OPEN_CHEST);
  }

  @SuppressWarnings("deprecation")
  public void tick(@NotNull BlockState state, ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
    BlockEntity blockentity = level.getBlockEntity(pos);
    if (blockentity instanceof ChestEntity) {
      ((ChestEntity) blockentity).recheckOpen();
    }
  }

  public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int id, int param) {
    super.triggerEvent(state, level, pos, id, param);
    BlockEntity blockEntity = level.getBlockEntity(pos);
    return blockEntity != null && blockEntity.triggerEvent(id, param);
  }

  public RenderShape getRenderShape(BlockState state) {
    return RenderShape.ENTITYBLOCK_ANIMATED;
  }

  public boolean hasAnalogOutputSignal(BlockState state) {
    return true;
  }

  @Override
  public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
    return Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);
  }

  @Override
  public Chest getVariant() {
    return tier;
  }
}
