package es.degrassi.forge.core.common.machines.container;

import es.degrassi.forge.core.common.machines.entity.ChestEntity;
import es.degrassi.forge.core.init.ContainerRegistration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class ChestContainer extends MachineContainer<ChestEntity> {
  public ChestContainer(int containerId, Inventory inventory, ChestEntity entity) {
    super(ContainerRegistration.CHEST.get(), containerId, entity, inventory);
  }

  public ChestContainer(int id, Inventory inventory, @NotNull FriendlyByteBuf extraData) {
    this(id, inventory, (ChestEntity) inventory.player.level().getBlockEntity(extraData.readBlockPos()));
  }
}
