package es.degrassi.forge.init.gui.container.generators;

import es.degrassi.forge.client.ClientHandler;
import es.degrassi.forge.init.entity.generators.JewelryGeneratorEntity;
import es.degrassi.forge.init.registration.ContainerRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
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
  }

  public JewelryGeneratorContainer(int id, Inventory inv, JewelryGeneratorEntity entity) {
    this(id, 5, inv, entity, new SimpleContainerData(5));
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
    return stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()), player, entity.getDelegate());
  }


  @Override
  public Component getId() {
    return  Component.literal("Jewelry Generator Container");
  }
}
