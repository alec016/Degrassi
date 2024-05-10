package es.degrassi.forge.core.common.machines.multiblock.controller.client.container;

import es.degrassi.forge.core.common.machines.container.MachineContainer;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.MelterControllerEntity;
import es.degrassi.forge.core.init.ContainerRegistration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class MelterContainer extends MachineContainer<MelterControllerEntity> {
  public MelterContainer(int containerId, Inventory inventory, MelterControllerEntity entity) {
    super(ContainerRegistration.MELTER.get(), containerId, entity, inventory);
  }

  public MelterContainer(int id, Inventory inventory, @NotNull FriendlyByteBuf extraData) {
    this(id, inventory, (MelterControllerEntity) inventory.player.level().getBlockEntity(extraData.readBlockPos()));
  }
}
