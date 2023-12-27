package es.degrassi.forge.init.block;

import dev.architectury.registry.menu.MenuRegistry;
import es.degrassi.forge.init.entity.FurnaceEntity;
import es.degrassi.forge.init.gui.container.FurnaceContainer;
import es.degrassi.forge.init.registration.EntityRegister;
import es.degrassi.forge.init.tiers.FurnaceTier;
import es.degrassi.forge.integration.config.DegrassiConfig;
import es.degrassi.forge.network.EnergyPacket;
import es.degrassi.forge.network.ProgressPacket;
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
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FurnaceBlock extends BaseBlock {
  private final FurnaceTier tier;

  public FurnaceBlock(FurnaceTier tier) {
    super(
      BlockBehaviour.Properties
        .of(Material.STONE)
        .strength(6f)
        .requiresCorrectToolForDrops()
        .noOcclusion()
    );
    this.tier = tier;
  }
  private static final VoxelShape SHAPE = createVoxelShape();

  @SuppressWarnings("deprecation")
  @Override
  public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter getter, @NotNull BlockPos pos, @NotNull CollisionContext collisionContext) {
    return SHAPE;
  }

  @SuppressWarnings("deprecation")
  @Override
  public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
    return RenderShape.MODEL;
  }


  private static @NotNull VoxelShape createVoxelShape() {
    return Shapes.block();
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
    if (tile instanceof FurnaceEntity entity) {
      if (!level.isClientSide()) {
        MenuRegistry.openExtendedMenu((ServerPlayer) player, new MenuProvider() {
          @Override
          public @NotNull Component getDisplayName() {
            return entity.getName();
          }

          @Override
          public AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player) {
            new EnergyPacket(entity.ENERGY_STORAGE.getEnergyStored(), entity.ENERGY_STORAGE.getMaxEnergyStored(), entity.ENERGY_STORAGE.getMaxEnergyStored(), pos);
            new ProgressPacket(entity.progressStorage.getProgress(), entity.progressStorage.getMaxProgress(), pos);
            return new FurnaceContainer(id, inv, entity, entity.data);
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
      case IRON -> Utils.createTickerHelper(type, EntityRegister.IRON_FURNACE.get(), FurnaceEntity::tick);
      case GOLD -> Utils.createTickerHelper(type, EntityRegister.GOLD_FURNACE.get(), FurnaceEntity::tick);
      case DIAMOND -> Utils.createTickerHelper(type, EntityRegister.DIAMOND_FURNACE.get(), FurnaceEntity::tick);
      case EMERALD -> Utils.createTickerHelper(type, EntityRegister.EMERALD_FURNACE.get(), FurnaceEntity::tick);
      case NETHERITE -> Utils.createTickerHelper(type, EntityRegister.NETHERITE_FURNACE.get(), FurnaceEntity::tick);
    };
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onRemove(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pNewState, boolean pIsMoving) {
    if (pState.getBlock() != pNewState.getBlock()) {
      BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
      if (blockEntity instanceof FurnaceEntity entity) {
        entity.drops();
      }
    }
    super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
    FurnaceEntity entity = null;
    switch(tier) {
      case IRON -> entity = new FurnaceEntity(
        EntityRegister.IRON_FURNACE.get(),
        pos,
        state,
        Component.translatable(
          "block.degrassi.iron_furnace"
        ),
        DegrassiConfig.get().furnaceConfig.iron_furnace_capacity,
        DegrassiConfig.get().furnaceConfig.iron_furnace_transfer,
        tier
      );
      case GOLD -> entity = new FurnaceEntity(
        EntityRegister.GOLD_FURNACE.get(),
        pos,
        state,
        Component.translatable(
          "block.degrassi.gold_furnace"
        ),
        DegrassiConfig.get().furnaceConfig.gold_furnace_capacity,
        DegrassiConfig.get().furnaceConfig.gold_furnace_transfer,
        tier
      );
      case DIAMOND -> entity = new FurnaceEntity(
        EntityRegister.DIAMOND_FURNACE.get(),
        pos,
        state,
        Component.translatable(
          "block.degrassi.diamond_furnace"
        ),
        DegrassiConfig.get().furnaceConfig.diamond_furnace_capacity,
        DegrassiConfig.get().furnaceConfig.diamond_furnace_transfer,
        tier
      );
      case EMERALD -> entity = new FurnaceEntity(
        EntityRegister.EMERALD_FURNACE.get(),
        pos,
        state,
        Component.translatable(
          "block.degrassi.emerald_furnace"
        ),
        DegrassiConfig.get().furnaceConfig.emerald_furnace_capacity,
        DegrassiConfig.get().furnaceConfig.emerald_furnace_transfer,
        tier
      );
      case NETHERITE -> entity = new FurnaceEntity(
        EntityRegister.NETHERITE_FURNACE.get(),
        pos,
        state,
        Component.translatable(
          "block.degrassi.netherite_furnace"
        ),
        DegrassiConfig.get().furnaceConfig.netherite_furnace_capacity,
        DegrassiConfig.get().furnaceConfig.netherite_furnace_transfer,
        tier
      );
    }
    if (entity != null) entity.delegate = this;
    return entity;
  }
}
