package es.degrassi.forge.core.common.storage.energy.entity;

import es.degrassi.forge.core.common.component.ComponentIOMode;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.common.storage.StorageEntity;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.tiers.Storage;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

public class EnergyCellEntity extends StorageEntity<Storage.Energy> {

  public EnergyCellEntity(BlockPos pos, BlockState blockState, Storage.Energy tier) {
    super(EntityRegistration.ENERGY_CELL.get(), pos, blockState, tier);

    this.getComponentManager()
      .addEnergy(tier.getCapacity(), tier.getTransfer(), "energy", ComponentIOMode.BOTH);

    if (tier.isCreative()) {
      getComponentManager().getComponent("energy").map(comp -> (EnergyComponent) comp).ifPresent(comp -> comp.setEnergy(Integer.MAX_VALUE));
    }
  }

  @Override
  public Component getName() {
    return null;
  }
}
