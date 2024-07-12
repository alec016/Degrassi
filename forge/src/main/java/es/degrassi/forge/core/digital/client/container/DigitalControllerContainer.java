package es.degrassi.forge.core.digital.client.container;

import es.degrassi.forge.core.common.machines.container.MachineContainer;
import es.degrassi.forge.core.digital.block.entity.DigitalControllerEntity;
import es.degrassi.forge.core.digital.network.Network;
import es.degrassi.forge.core.init.ContainerRegistration;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class DigitalControllerContainer extends MachineContainer<DigitalControllerEntity> {
  @Setter
  @Getter
  public static List<Network> networks;

  public DigitalControllerContainer(int containerId, Inventory inventory, DigitalControllerEntity entity) {
    super(ContainerRegistration.DIGITAL_CONTROLLER.get(), containerId, entity, inventory);
  }

  public DigitalControllerContainer(int id, Inventory inventory, @NotNull FriendlyByteBuf extraData) {
    this(id, inventory, (DigitalControllerEntity) inventory.player.level().getBlockEntity(extraData.readBlockPos()));
  }
}
