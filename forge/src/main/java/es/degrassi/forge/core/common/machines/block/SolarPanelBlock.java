package es.degrassi.forge.core.common.machines.block;

import dev.architectury.registry.menu.MenuRegistry;
import es.degrassi.forge.api.utils.Utils;
import es.degrassi.forge.core.common.machines.container.SolarPanelContainer;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.machines.entity.SolarPanelEntity;
import es.degrassi.forge.core.tiers.SolarPanel;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class SolarPanelBlock extends MachineBlock {
  private SolarPanel tier;
  public SolarPanelBlock(Properties properties, SolarPanel tier) {
    super(properties);
    this.tier = tier;
  }

  @SuppressWarnings("deprecation")
  @Override
  public @NotNull VoxelShape getShape(@NotNull BlockState state, BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
    if(world.getBlockEntity(pos) instanceof SolarPanelEntity spt) return spt.getShape(this);
    return Shapes.create(0, 0, 0, 1, 6 / 16f, 1);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void neighborChanged(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull Block blockIn, @NotNull BlockPos fromPos, boolean isMoving) {
    super.neighborChanged(state, worldIn, pos, blockIn, fromPos, isMoving);
    if (worldIn.getBlockEntity(pos) instanceof SolarPanelEntity spt)
      spt.resetVoxelShape();
  }

  public VoxelShape recalcShape(BlockGetter world, BlockPos pos) {
    float ph = 6 / 16f;

    VoxelShape baseShape = Shapes.create(0, 0, 0, 1, ph, 1);
    Stream.Builder<VoxelShape> shapes = Stream.builder();

    boolean west = false, east = false, north = false, south = false;

    float h2 = ph + 0.25F / 16F;

    if(west = world.getBlockState(pos.west()).getBlock() != this)
      shapes.add(Shapes.create(0, ph, 1 / 16F, 1 / 16F, h2, 15 / 16F));

    if(east = world.getBlockState(pos.east()).getBlock() != this)
      shapes.add(Shapes.create(15 / 16F, ph, 1 / 16F, 1, h2, 15 / 16F));

    if(north = world.getBlockState(pos.north()).getBlock() != this)
      shapes.add(Shapes.create(1 / 16F, ph, 0, 15 / 16F, h2, 1 / 16F));

    if(south = world.getBlockState(pos.south()).getBlock() != this)
      shapes.add(Shapes.create(1 / 16F, ph, 15 / 16F, 15 / 16F, h2, 1));

    if(west || north || world.getBlockState(pos.west().north()).getBlock() != this)
      shapes.add(Shapes.create(0, ph, 0, 1 / 16F, h2, 1 / 16F));

    if(east || north || world.getBlockState(pos.east().north()).getBlock() != this)
      shapes.add(Shapes.create(15 / 16F, ph, 0, 1, h2, 1 / 16F));

    if(south || east || world.getBlockState(pos.south().east()).getBlock() != this)
      shapes.add(Shapes.create(15 / 16F, ph, 15 / 16F, 1, h2, 1));

    if(west || south || world.getBlockState(pos.west().south()).getBlock() != this)
      shapes.add(Shapes.create(0, ph, 15 / 16F, 1 / 16F, h2, 1));

    return Shapes.or(baseShape, shapes.build().toArray(VoxelShape[]::new));
  }

  public SolarPanel getTier() {
    return tier;
  }

  public void setTier(SolarPanel tier) {
    this.tier = tier;
  }
  public void setTier(String tier) {
    this.tier = SolarPanel.value(tier);
  }

  @Nullable
  @Override
  public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
    return super.getStateForPlacement(context);
  }

  @Override
  protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
    super.createBlockStateDefinition(builder);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
    return new SolarPanelEntity(pos, state, tier);
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
    if (tile instanceof SolarPanelEntity entity) {
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
  public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
    return Utils.createTickerHelper(
      type, getTier().getType().get(),
      level.isClientSide()
        ? MachineEntity::clientTick
        : SolarPanelEntity::serverTick
    );
  }
}
