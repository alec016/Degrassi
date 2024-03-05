package es.degrassi.forge.core.common.machines.block;

import dev.architectury.registry.menu.MenuRegistry;
import es.degrassi.forge.core.common.machines.container.FurnaceContainer;
import es.degrassi.forge.core.common.machines.entity.FurnaceEntity;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FurnaceBlock extends MachineBlock {
  private Furnace tier;
  public FurnaceBlock(Properties properties, Furnace tier) {
    super(properties);
    this.tier = tier;
  }

  public Furnace getTier() {
    return tier;
  }

  public void setTier(Furnace tier) {
    this.tier = tier;
  }

  public void setTier(String tier) {
    this.tier = Furnace.value(tier);
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new FurnaceEntity(pos, state, tier);
  }

  @Override
  public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
    BlockEntity tile = level.getBlockEntity(pos);
    if (tile instanceof FurnaceEntity entity) {
      if (!level.isClientSide()) {
        MenuRegistry.openExtendedMenu((ServerPlayer) player, new MenuProvider() {
          @Override
          public Component getDisplayName() {
            return entity.getName();
          }

          @Override
          public AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player) {
//            new EnergyPacket(entity.ENERGY_STORAGE.getEnergyStored(), entity.ENERGY_STORAGE.getMaxEnergyStored(), entity.ENERGY_STORAGE.getMaxEnergyStored(), pos);
//            new ProgressPacket(entity.progressComponent.getProgress(), entity.progressComponent.getMaxProgress(), pos);
//            new ExperiencePacket(entity.xp.getXp(), pos);
            return new FurnaceContainer(id, inv, entity);
          }
        }, buf -> buf.writeBlockPos(pos));
        return InteractionResult.SUCCESS;
      }
      return InteractionResult.SUCCESS;
    }
    return super.use(state, level, pos, player, hand, hit);
  }
}
