package es.degrassi.forge.init.gui.container.generators;

import es.degrassi.forge.client.ClientHandler;
import es.degrassi.forge.init.entity.generators.JewelryGeneratorEntity;
import es.degrassi.forge.init.registration.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.items.*;
import org.jetbrains.annotations.NotNull;

public class JewelryGeneratorContainer extends GeneratorContainer<JewelryGeneratorEntity> {
  public JewelryGeneratorContainer(
    int i,
    int invSlots,
    Inventory inv,
    JewelryGeneratorEntity entity,
    ContainerData data
  ) {
    super(ContainerRegistry.JEWELRY_GENERATOR.get(), i, invSlots , entity, inv);
    this.data = data;

    this.entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
      this.addSlot(new SlotItemHandler(handler, 0, 8, 37)); // generation upgrade
      this.addSlot(new SlotItemHandler(handler, 1, 8, 73)); // transfer upgrade
      this.addSlot(new SlotItemHandler(handler, 2, 53, 55)); // input
    });

    addDataSlots(data);
  }

  public JewelryGeneratorContainer(int id, Inventory inv, JewelryGeneratorEntity entity) {
    this(id, 3, inv, entity, new SimpleContainerData(3));
  }

  public JewelryGeneratorContainer(int id, Inventory playerInv, @NotNull FriendlyByteBuf extraData) {
    this(
      id,
      playerInv,
      ClientHandler.getClientSideJewelryGeneratorEntity(extraData.readBlockPos())
    );
  }

  @Override
  public boolean stillValid(@NotNull Player player) {
    return stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()), player, BlockRegister.JEWELRY_GENERATOR.get());
  }


  @Override
  public Component getId() {
    return  Component.literal("Jewelry Generator Container");
  }
}
