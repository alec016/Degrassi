package es.degrassi.forge.core.common.machines.multiblock.parts.client.container;

import es.degrassi.forge.core.common.machines.container.MachineContainer;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.entity.OutputBusEntity;
import es.degrassi.forge.core.init.ContainerRegistration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class OutputBusContainer extends MachineContainer<OutputBusEntity> {
  public OutputBusContainer(int containerId, Inventory inventory, OutputBusEntity entity) {
    super(ContainerRegistration.OUTPUT_BUS.get(), containerId, entity, inventory);
  }

  public OutputBusContainer(int id, Inventory inventory, @NotNull FriendlyByteBuf extraData) {
    this(id, inventory, (OutputBusEntity) inventory.player.level().getBlockEntity(extraData.readBlockPos()));
  }
}
