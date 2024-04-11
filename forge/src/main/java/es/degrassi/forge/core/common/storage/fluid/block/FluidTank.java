package es.degrassi.forge.core.common.storage.fluid.block;

import es.degrassi.common.registry.IBlock;
import es.degrassi.common.utils.Utils;
import es.degrassi.forge.EnvHandler;
import es.degrassi.forge.core.common.machines.block.MachineBlock;
import es.degrassi.forge.core.common.storage.fluid.entity.FluidTankEntity;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.tiers.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidTank extends MachineBlock implements IBlock<Storage.Fluid, FluidTank> {
  private final Storage.Fluid tier;
  public FluidTank(Properties properties, Storage.Fluid tier) {
    super(properties);
    this.tier = tier;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos , BlockState state) {
    return EnvHandler.INSTANCE.createFluidTank(pos, state, tier);
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

  @Override
  public Storage.Fluid getVariant() {
    return tier;
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
            if (entity.getFluid().isEmpty()) return InteractionResult.PASS;
            Fluid f = entity.removeFluid();
            if (!f.isSame(Fluids.EMPTY)) {
              if (!player.isCreative()) player.getInventory().removeItem(slot, 1);
              player.addItem(new ItemStack(f.getBucket()));
            }
          } else {
            if (!entity.getFluid().isEmpty()) return InteractionResult.PASS;
            boolean inserted = entity.addFluid(fluid);
            if (inserted) {
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
}
