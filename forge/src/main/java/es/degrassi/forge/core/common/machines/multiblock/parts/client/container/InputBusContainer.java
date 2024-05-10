package es.degrassi.forge.core.common.machines.multiblock.parts.client.container;

import es.degrassi.forge.core.common.machines.container.MachineContainer;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.entity.InputBusEntity;
import es.degrassi.forge.core.init.ContainerRegistration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class InputBusContainer extends MachineContainer<InputBusEntity> {
  public InputBusContainer(int containerId, Inventory inventory, InputBusEntity entity) {
    super(ContainerRegistration.INPUT_BUS.get(), containerId, entity, inventory);
  }

  public InputBusContainer(int id, Inventory inventory, @NotNull FriendlyByteBuf extraData) {
    this(id, inventory, (InputBusEntity) inventory.player.level().getBlockEntity(extraData.readBlockPos()));
  }
}
