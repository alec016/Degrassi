package es.degrassi.forge.core.common.component;

import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.network.component.ItemPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class ItemComponent extends ItemStackHandler implements IComponent {
  private final String id;
  private final ComponentManager manager;
  private final MachineEntity<?> entity;
  public ItemComponent(ComponentManager manager, String id, MachineEntity<?> entity) {
    this.manager = manager;
    this.id = id;
    this.entity = entity;
  }
  @Override
  public ComponentManager getManager() {
    return manager;
  }

  @Override
  public void markDirty() {
    entity.setChanged();
    if(entity.getLevel() != null && !entity.getLevel().isClientSide())
      new ItemPacket(getStackInSlot(0), id, entity.getBlockPos())
        .sendToChunkListeners(entity.getLevel().getChunkAt(entity.getBlockPos()));
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void serialize(CompoundTag nbt) {
    nbt.put(id, serializeNBT());
  }

  @Override
  public void deserialize(CompoundTag nbt) {
    deserializeNBT(nbt.getCompound(id));
  }

  public void setItem(ItemStack item) {
    setStackInSlot(0, item);
  }

  @Override
  public boolean isItemValid(int slot, @NotNull ItemStack item) {
    return this.getStackInSlot(0).isEmpty() || this.getStackInSlot(0).is(item.getItem());
  }

  @Override
  public void setStackInSlot(int slot, @NotNull ItemStack stack) {
    super.setStackInSlot(0, stack);
  }

  @Override
  public @NotNull ItemStack getStackInSlot(int slot) {
    return super.getStackInSlot(0);
  }

  @Override
  public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
    return super.insertItem(0, stack, simulate);
  }

  @Override
  public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
    return super.extractItem(0, amount, simulate);
  }

  @Override
  protected int getStackLimit(int slot, @NotNull ItemStack stack) {
    return super.getStackLimit(0, stack);
  }

  @Override
  protected void onContentsChanged(int slot) {
    markDirty();
  }

  @Override
  public String toString() {
    return "ItemComponent{" +
      "id='" + id + '\'' +
      ", item=" + getStackInSlot(0).getHoverName().getString() +
      '}';
  }
}
