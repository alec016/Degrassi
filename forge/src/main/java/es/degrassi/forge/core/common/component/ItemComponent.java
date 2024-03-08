package es.degrassi.forge.core.common.component;

import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.network.component.ItemPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

public class ItemComponent implements IComponent, IItemHandlerModifiable {
  private final String id;
  private ItemStack item = ItemStack.EMPTY;
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
      new ItemPacket(item, id, entity.getBlockPos())
        .sendToChunkListeners(entity.getLevel().getChunkAt(entity.getBlockPos()));
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void serialize(CompoundTag nbt) {
    CompoundTag tag = new CompoundTag();
    tag.put("item", item.serializeNBT());
    nbt.put(id, tag);
  }

  @Override
  public void deserialize(CompoundTag nbt) {
    if (nbt.contains(id)) {
      CompoundTag tag = nbt.getCompound(id);
      this.item.deserializeNBT(tag.getCompound("item"));
    }
  }

  @Override
  public int getSlots() {
    return 1;
  }

  @Override
  public @NotNull ItemStack getStackInSlot(int slot) {
    return item;
  }

  public void setItem(ItemStack item) {
    this.item = item;
    markDirty();
  }

  @Override
  public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack item, boolean simulate) {
    ItemStack toReturn = item.copy();
    if (this.item.isEmpty()) {
      if (!simulate)
        this.item = toReturn.copy();
      toReturn.setCount(0);
    } else if (this.item.is(toReturn.getItem())) {
      int available = this.item.getMaxStackSize() - this.item.getCount();
      if (available >= toReturn.getCount()) {
        if (!simulate)
          this.item.grow(toReturn.getCount());
        toReturn.setCount(0);
      } else {
        if (!simulate)
          this.item.grow(available);
        toReturn.shrink(available);
      }
    }
    markDirty();
    return toReturn;
  }

  @Override
  public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
    ItemStack toReturn = ItemStack.EMPTY;
    if (!this.item.isEmpty()) {
      toReturn = this.item.copy();
      if (toReturn.getCount() >= amount) {
        toReturn.setCount(amount);
        if (!simulate)
          this.item.shrink(amount);
      } else {
        if (!simulate)
          this.item.shrink(toReturn.getCount());
      }
    }
    markDirty();
    return toReturn;
  }

  @Override
  public int getSlotLimit(int slot) {
    return item.getMaxStackSize();
  }

  @Override
  public boolean isItemValid(int slot, @NotNull ItemStack item) {
    return this.item.isEmpty() || this.item.is(item.getItem());
  }

  @Override
  public void setStackInSlot(int slot, @NotNull ItemStack item) {
    setItem(item);
  }

  @Override
  public String toString() {
    return "ItemComponent{" +
      "id='" + id + '\'' +
      ", item=" + item.getHoverName().getString() +
      '}';
  }
}
