package es.degrassi.forge.core.common.machines.multiblock.parts.client.container;

import es.degrassi.forge.core.common.machines.container.MachineContainer;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.entity.QuadrupleFluidInputTankEntity;
import es.degrassi.forge.core.init.ContainerRegistration;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class FluidQuadrupleInputTankContainer extends MachineContainer<QuadrupleFluidInputTankEntity> {
  public FluidQuadrupleInputTankContainer(int containerId, Inventory inventory, QuadrupleFluidInputTankEntity entity) {
    super(ContainerRegistration.FLUID_QUADRUPLE_INPUT_TANK.get(), containerId, entity, inventory);
  }

  public FluidQuadrupleInputTankContainer(int id, Inventory inventory, @NotNull FriendlyByteBuf extraData) {
    this(id, inventory, (QuadrupleFluidInputTankEntity) inventory.player.level().getBlockEntity(extraData.readBlockPos()));
  }
}
