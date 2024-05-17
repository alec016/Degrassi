package es.degrassi.forge.core.common.machines.multiblock.parts.client.container;

import es.degrassi.forge.core.common.machines.container.MachineContainer;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.entity.FluidInputTankEntity;
import es.degrassi.forge.core.init.ContainerRegistration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class FluidInputTankContainer extends MachineContainer<FluidInputTankEntity> {
  public FluidInputTankContainer(int containerId, Inventory inventory, FluidInputTankEntity entity) {
    super(ContainerRegistration.FLUID_INPUT_TANK.get(), containerId, entity, inventory);
  }

  public FluidInputTankContainer(int id, Inventory inventory, @NotNull FriendlyByteBuf extraData) {
    this(id, inventory, (FluidInputTankEntity) inventory.player.level().getBlockEntity(extraData.readBlockPos()));
  }
}
