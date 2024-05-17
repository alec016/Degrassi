package es.degrassi.forge.core.common.machines.multiblock.parts.client.container;

import es.degrassi.forge.core.common.machines.container.MachineContainer;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.entity.FluidOutputTankEntity;
import es.degrassi.forge.core.init.ContainerRegistration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class FluidOutputTankContainer extends MachineContainer<FluidOutputTankEntity> {
  public FluidOutputTankContainer(int containerId, Inventory inventory, FluidOutputTankEntity entity) {
    super(ContainerRegistration.FLUID_OUTPUT_TANK.get(), containerId, entity, inventory);
  }

  public FluidOutputTankContainer(int id, Inventory inventory, @NotNull FriendlyByteBuf extraData) {
    this(id, inventory, (FluidOutputTankEntity) inventory.player.level().getBlockEntity(extraData.readBlockPos()));
  }
}
