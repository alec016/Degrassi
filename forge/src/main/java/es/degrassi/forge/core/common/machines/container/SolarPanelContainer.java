package es.degrassi.forge.core.common.machines.container;

import es.degrassi.forge.core.common.machines.entity.SolarPanelEntity;
import es.degrassi.forge.core.init.ContainerRegistration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class SolarPanelContainer extends MachineContainer<SolarPanelEntity> {
  public SolarPanelContainer(int containerId, Inventory inventory, SolarPanelEntity entity) {
    super(ContainerRegistration.SOLAR_PANEL.get(), containerId, entity, inventory);
  }

  public SolarPanelContainer(int id, Inventory inventory, @NotNull FriendlyByteBuf extraData) {
    this(id, inventory, (SolarPanelEntity) inventory.player.level().getBlockEntity(extraData.readBlockPos()));
  }
}
