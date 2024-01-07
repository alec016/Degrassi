package es.degrassi.forge.init.gui.container;

import es.degrassi.forge.client.ClientHandler;
import es.degrassi.forge.client.IClientHandler;
import es.degrassi.forge.init.entity.MelterEntity;
import es.degrassi.forge.init.gui.container.types.IProgressContainer;
import es.degrassi.forge.init.registration.BlockRegister;
import es.degrassi.forge.init.registration.ContainerRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class MelterContainer extends BaseContainer<MelterEntity> implements IProgressContainer<MelterEntity> {

  // THIS YOU HAVE TO DEFINE!
  private static final int TE_INVENTORY_SLOT_COUNT = 3;  // must be the number of slots you have!
  public FluidStack fluid;
  public MelterContainer(int i, Inventory inv, MelterEntity entity, ContainerData data) {
    super(ContainerRegistry.MELTER_CONTAINER.get(), i, TE_INVENTORY_SLOT_COUNT);
    checkContainerSize(inv, TE_INVENTORY_SLOT_COUNT);
    this.entity = entity;
    this.level = inv.player.level;
    this.playerInv = inv;
    this.data = data;
    this.fluid = entity.getFluidStorage().getFluid();

    IClientHandler.addPlayerInventory(this, playerInv, 8, 98);

    this.entity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
      this.addSlot(new SlotItemHandler(handler, 0, 35, 31)); // upgrade 1
      this.addSlot(new SlotItemHandler(handler, 1, 35, 67)); // upgrade 2
      this.addSlot(new SlotItemHandler(handler, 2, 62, 49)); // input slot
    });

    addDataSlots(data);
  }

  public MelterContainer(int id, Inventory inv, MelterEntity entity) {
    this(id, inv, entity, new SimpleContainerData(TE_INVENTORY_SLOT_COUNT));
  }

  public MelterContainer(int id, Inventory playerInv, @NotNull FriendlyByteBuf extraData) {
    this(
      id,
      playerInv,
      ClientHandler.getClientSideMelterEntity(extraData.readBlockPos())
    );
  }

  public MelterEntity getEntity() {
    return this.entity;
  }

  @Override
  public Component getId() {
    return Component.literal("Melter Container");
  }

  @Override
  public boolean stillValid(@NotNull Player player) {
    return stillValid(ContainerLevelAccess.create(level, entity.getBlockPos()),
      player, BlockRegister.MELTER_BLOCK.get());
  }


  public void setFluid(FluidStack fluidStack) {
    this.fluid = fluidStack;
  }

  public FluidStack getFluid() {
    return fluid;
  }
}
