package es.degrassi.forge.core.common.storage.energy.block;

import es.degrassi.common.registry.IBlock;
import es.degrassi.common.utils.Utils;
import es.degrassi.forge.EnvHandler;
import es.degrassi.forge.core.common.machines.block.MachineBlock;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.storage.energy.entity.EnergyCellEntity;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.tiers.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnergyCell extends MachineBlock implements IBlock<Storage.Energy, EnergyCell> {
  private final Storage.Energy tier;
  public EnergyCell(Properties properties, Storage.Energy tier) {
    super(properties);
    this.tier = tier;
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos , BlockState state) {
    return EnvHandler.INSTANCE.createEnergyCell(pos, state, tier);
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

  @Override
  public Storage.Energy getVariant() {
    return tier;
  }
}
