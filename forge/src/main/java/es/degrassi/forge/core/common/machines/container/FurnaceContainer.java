package es.degrassi.forge.core.common.machines.container;

import es.degrassi.forge.core.common.machines.entity.FurnaceEntity;
import es.degrassi.forge.core.init.ContainerRegistration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class FurnaceContainer extends MachineContainer<FurnaceEntity> {
  public FurnaceContainer(int containerId, Inventory inventory, FurnaceEntity entity) {
    super(ContainerRegistration.FURNACE.get(), containerId, entity, inventory);
  }

  public FurnaceContainer(int id, Inventory inventory, @NotNull FriendlyByteBuf extraData) {
    this(id, inventory, (FurnaceEntity) inventory.player.level().getBlockEntity(extraData.readBlockPos()));
  }
}
