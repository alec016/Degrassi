package es.degrassi.forge.init.gui.container.generators;

import es.degrassi.forge.client.*;
import es.degrassi.forge.init.entity.generators.*;
import es.degrassi.forge.init.registration.*;
import net.minecraft.network.*;
import net.minecraft.network.chat.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.inventory.*;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.items.*;
import org.jetbrains.annotations.*;

public class CombustionGeneratorContainer extends GeneratorContainer<CombustionGeneratorEntity> {
  public CombustionGeneratorContainer(
    int i,
    int invSlots,
    Inventory inv,
    CombustionGeneratorEntity entity,
    ContainerData data
  ) {
    super(ContainerRegistry.COMBUSTION_GENERATOR.get(), i, invSlots , entity, inv);
    this.data = data;

    this.entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
      this.addSlot(new SlotItemHandler(handler, 0, 8, 37)); // generation upgrade
      this.addSlot(new SlotItemHandler(handler, 1, 8, 73)); // transfer upgrade
      this.addSlot(new SlotItemHandler(handler, 2, 53, 55)); // input
    });

    addDataSlots(data);
  }

  public CombustionGeneratorContainer(int id, Inventory inv, CombustionGeneratorEntity entity) {
    this(id, 3, inv, entity, new SimpleContainerData(3));
  }

  public CombustionGeneratorContainer(int id, Inventory playerInv, @NotNull FriendlyByteBuf extraData) {
    this(
      id,
      playerInv,
      ClientHandler.getClientSideCombustionGeneratorEntity(extraData.readBlockPos())
    );
  }

  @Override
  public boolean stillValid(@NotNull Player player) {
    return stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()), player, BlockRegister.COMBUSTION_GENERATOR.get());
  }


  @Override
  public Component getId() {
    return  Component.literal("Combustion Generator Container");
  }
}
