package es.degrassi.forge.core.common.storage.energy.block;

import es.degrassi.common.registry.IBlock;
import es.degrassi.common.utils.Utils;
import es.degrassi.forge.EnvHandler;
import es.degrassi.forge.core.common.machines.block.MachineBlock;
import es.degrassi.forge.core.common.storage.energy.entity.EnergyCellEntity;
import es.degrassi.forge.core.common.storage.energy.item.EnergyCellItem;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.tiers.Storage;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class EnergyCell extends MachineBlock implements IBlock<Storage.Energy, EnergyCell> {
  private final Storage.Energy variant;
  public EnergyCell(Properties properties, Storage.Energy tier) {
    super(properties);
    this.variant = tier;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos , BlockState state) {
    return EnvHandler.INSTANCE.createEnergyCell(pos, state, variant);
  }

  @Nullable
  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
    return Utils.createTickerHelper(
      type, EntityRegistration.ENERGY_CELL.get(),
      level.isClientSide()
        ? EnergyCellEntity::clientTick
        : EnergyCellEntity::serverTick
    );
  }

  public BlockItem getBlockItem(Item.Properties properties) {
    return new EnergyCellItem(this, new Item.Properties());
  }
}
