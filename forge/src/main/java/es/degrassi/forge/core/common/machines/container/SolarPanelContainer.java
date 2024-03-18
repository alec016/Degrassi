package es.degrassi.forge.core.common.machines.container;

import es.degrassi.forge.core.common.machines.entity.SolarPanelEntity;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.init.ContainerRegistration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import org.jetbrains.annotations.NotNull;

public class SolarPanelContainer extends MachineContainer<SolarPanelEntity> {
  public SolarPanelContainer(int containerId, Inventory inventory, SolarPanelEntity entity) {
    super(ContainerRegistration.SOLAR_PANEL.get(), containerId, entity, inventory);
  }

  public SolarPanelContainer(int id, Inventory inventory, @NotNull FriendlyByteBuf extraData) {
    this(id, inventory, (SolarPanelEntity) inventory.player.level().getBlockEntity(extraData.readBlockPos()));
  }

  @Override
  public boolean stillValid(@NotNull Player player) {
    return switch(getEntity().getTier()) {
      case T1 -> stillValid(ContainerLevelAccess.create(getLevel(), getEntity().getBlockPos()),
        player, BlockRegistration.SP1.get());
      case T2 -> stillValid(ContainerLevelAccess.create(getLevel(), getEntity().getBlockPos()),
        player, BlockRegistration.SP2.get());
      case T3 -> stillValid(ContainerLevelAccess.create(getLevel(), getEntity().getBlockPos()),
        player, BlockRegistration.SP3.get());
      case T4 -> stillValid(ContainerLevelAccess.create(getLevel(), getEntity().getBlockPos()),
        player, BlockRegistration.SP4.get());
      case T5 -> stillValid(ContainerLevelAccess.create(getLevel(), getEntity().getBlockPos()),
        player, BlockRegistration.SP5.get());
      case T6 -> stillValid(ContainerLevelAccess.create(getLevel(), getEntity().getBlockPos()),
        player, BlockRegistration.SP6.get());
      case T7 -> stillValid(ContainerLevelAccess.create(getLevel(), getEntity().getBlockPos()),
        player, BlockRegistration.SP7.get());
      case T8 -> stillValid(ContainerLevelAccess.create(getLevel(), getEntity().getBlockPos()),
        player, BlockRegistration.SP8.get());
    };
  }
}
