package es.degrassi.forge.core.common.machines.container;

import es.degrassi.forge.core.common.machines.entity.FurnaceEntity;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.init.ContainerRegistration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import org.jetbrains.annotations.NotNull;

public class FurnaceContainer extends MachineContainer<FurnaceEntity> {
  public FurnaceContainer(int containerId, Inventory inventory, FurnaceEntity entity) {
    super(ContainerRegistration.FURNACE.get(), containerId, entity, inventory);
  }

  public FurnaceContainer(int id, Inventory inventory, @NotNull FriendlyByteBuf extraData) {
    this(id, inventory, (FurnaceEntity) inventory.player.level().getBlockEntity(extraData.readBlockPos()));
  }

  @Override
  public boolean stillValid(Player player) {
    return switch(getEntity().getTier()) {
      case IRON -> stillValid(ContainerLevelAccess.create(getLevel(), getEntity().getBlockPos()),
        player, BlockRegistration.IRON_FURNACE.get());
      case GOLD -> stillValid(ContainerLevelAccess.create(getLevel(), getEntity().getBlockPos()),
        player, BlockRegistration.GOLD_FURNACE.get());
      case DIAMOND -> stillValid(ContainerLevelAccess.create(getLevel(), getEntity().getBlockPos()),
        player, BlockRegistration.DIAMOND_FURNACE.get());
      case EMERALD -> stillValid(ContainerLevelAccess.create(getLevel(), getEntity().getBlockPos()),
        player, BlockRegistration.EMERALD_FURNACE.get());
      case NETHERITE -> stillValid(ContainerLevelAccess.create(getLevel(), getEntity().getBlockPos()),
        player, BlockRegistration.NETHERITE_FURNACE.get());
    };
  }
}
