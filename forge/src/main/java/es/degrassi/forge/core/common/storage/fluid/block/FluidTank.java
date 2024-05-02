package es.degrassi.forge.core.common.storage.fluid.block;

import es.degrassi.common.registry.IBlock;
import es.degrassi.common.utils.Utils;
import es.degrassi.forge.EnvHandler;
import es.degrassi.forge.core.common.machines.block.MachineBlock;
import es.degrassi.forge.core.common.storage.fluid.entity.FluidTankEntity;
import es.degrassi.forge.core.common.storage.fluid.item.FluidTankItem;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.tiers.Storage;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class FluidTank extends MachineBlock implements IBlock<Storage.Fluid, FluidTank> {
  private final Storage.Fluid variant;
  public FluidTank(Properties properties, Storage.Fluid tier) {
    super(properties);
    this.variant = tier;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos , BlockState state) {
    return EnvHandler.INSTANCE.createFluidTank(pos, state, variant);
  }

  public BlockItem getBlockItem(Item.Properties properties) {
    return new FluidTankItem(this, new Item.Properties());
  }

  @Override
  public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level , @NotNull BlockState state , @NotNull BlockEntityType<T> type) {
    return Utils.createTickerHelper(
      type, EntityRegistration.FLUID_TANK.get(),
      level.isClientSide()
        ? FluidTankEntity::clientTick
        : FluidTankEntity::serverTick
    );
  }

  @SuppressWarnings("deprecation")
  @Override
  public InteractionResult use(BlockState state , Level level , BlockPos pos , Player player , InteractionHand hand , BlockHitResult hit) {
    BlockEntity tile = level.getBlockEntity(pos);
    if (tile instanceof FluidTankEntity entity) {
      if (!level.isClientSide()) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() instanceof BucketItem bucket) {
          Fluid fluid = bucket.getFluid();
          int slot = player.getInventory().findSlotMatchingItem(stack);
          if (fluid.isSame(Fluids.EMPTY)) {
            if (entity.getFluidStack() == null || entity.getFluidStack().isEmpty()) return InteractionResult.SUCCESS;
            Fluid f = entity.removeFluid();
            if (!f.isSame(Fluids.EMPTY)) {
              if (!player.isCreative()) player.getInventory().removeItem(slot, 1);
              player.addItem(new ItemStack(f.getBucket()));
            }
          } else {
            if (entity.addFluid(fluid)) {
              if (!player.isCreative()) {
                player.getInventory().removeItem(slot, 1);
                player.addItem(new ItemStack(Items.BUCKET));
              }
            }
          }
        }
        return InteractionResult.SUCCESS;
      }
      return InteractionResult.SUCCESS;
    }
    return super.use(state, level, pos, player, hand, hit);
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(BlockState state , BlockGetter level , BlockPos pos , CollisionContext context) {
    VoxelShape shape = Shapes.empty();
    shape = Shapes.join(shape, Shapes.create(0.125, 0, 0.125, 0.875, 0.9375, 0.875), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.create(0.875, 0.9375, 0.875, 0.125, 0, 0.125), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.create(0.125, 0, 0.125, 0.875, 0.0625, 0.875), BooleanOp.OR);
    shape = Shapes.join(shape, Shapes.create(0.125, 0.875, 0.125, 0.875, 0.9375, 0.875), BooleanOp.OR);
    return shape;
  }
}
