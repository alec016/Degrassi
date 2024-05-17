package es.degrassi.forge.core.common.machines.multiblock.parts.block;

import dev.architectury.registry.menu.MenuRegistry;
import es.degrassi.forge.EnvHandler;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.entity.FluidInputTankEntity;
import es.degrassi.forge.core.common.machines.multiblock.parts.client.container.FluidInputTankContainer;
import es.degrassi.forge.core.common.machines.multiblock.parts.item.FluidInputTankItem;
import es.degrassi.forge.core.tiers.MultiblockPartStorage;
import lombok.Getter;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class FluidInputTank extends BaseMultiblockPartBlock<MultiblockPartStorage.Fluid.Input, FluidInputTank> {
  public FluidInputTank(Properties properties, MultiblockPartStorage.Fluid.Input variant) {
    super(properties, variant);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return EnvHandler.INSTANCE.createFluidInputTank(pos, state, getVariant());
  }

  public BlockItem getBlockItem(Item.Properties properties) {
    return new FluidInputTankItem(this, new Item.Properties());
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
    if (tile instanceof FluidInputTankEntity entity) {
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
            return new FluidInputTankContainer(id, inv, entity);
          }
        }, buf -> buf.writeBlockPos(pos));
        return InteractionResult.SUCCESS;
      }
      return InteractionResult.SUCCESS;
    }
    return super.use(state, level, pos, player, hand, hit);
  }
}
