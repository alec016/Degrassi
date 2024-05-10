package es.degrassi.forge.core.common.machines.multiblock.parts.client.container;

import es.degrassi.forge.core.common.machines.container.MachineContainer;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.entity.EnergyHatchEntity;
import es.degrassi.forge.core.init.ContainerRegistration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class EnergyHatchContainer extends MachineContainer<EnergyHatchEntity> {
  public EnergyHatchContainer(int containerId, Inventory inventory, EnergyHatchEntity entity) {
    super(ContainerRegistration.ENERGY_HATCH.get(), containerId, entity, inventory);
  }

  public EnergyHatchContainer(int id, Inventory inventory, @NotNull FriendlyByteBuf extraData) {
    this(id, inventory, (EnergyHatchEntity) inventory.player.level().getBlockEntity(extraData.readBlockPos()));
  }
}
