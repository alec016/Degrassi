package es.degrassi.forge.core.common.wrapper;

import es.degrassi.common.utils.DegrassiLogger;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.component.ComponentIOMode;
import es.degrassi.forge.core.common.component.ItemComponent;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.entity.InputBusEntity;
import es.degrassi.forge.core.common.requirement.ItemRequirement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

@Getter
public class DegrassiItemStackHandler implements IItemHandlerModifiable, IItemHandler, IComponent {
  private final List<ItemComponent> components;
  private final ComponentManager manager;
  private final AtomicInteger count = new AtomicInteger(0);

  @Override
  public void markDirty() {
    components.forEach(IComponent::markDirty);
  }

  @Override
  public String getId() {
    return "itemHandler";
  }

  @Override
  public void fill(IRequirement<?> requirement) {
    if (!(requirement instanceof ItemRequirement itemReq)) return;
    AtomicBoolean inserted = new AtomicBoolean(false);
    components.forEach(component -> {
      if (inserted.get()) return;
      if (component.getStackInSlot(0).isEmpty()) {
        inserted.set(true);
        component.fill(requirement);
      } else if (component.getStackInSlot(0).is(itemReq.getItem())) {
        inserted.set(true);
        component.fill(requirement);
      }
    });
  }

  @Override
  public ComponentIOMode getMode() {
    return ComponentIOMode.BOTH;
  }

  @Override
  public void setMode(ComponentIOMode mode) {}

  @Override
  public void serialize(CompoundTag nbt) {
  }

  @Override
  public CompoundTag serialize() {
    if (getManager().getEntity() instanceof InputBusEntity && !getManager().getEntity().dummy())
      DegrassiLogger.INSTANCE.info("Serializing ItemStackHandler from {}", this);
    CompoundTag nbt = new CompoundTag();
    ListTag listTag = new ListTag();
    CompoundTag compound;
    for (int i = 0; i < components.size(); i++) {
      compound = components.get(i).serialize();
      compound.putInt("Slot", i);
      listTag.add(i, compound);
    }
    nbt.put("list", listTag);
    nbt.putString("id", getId());
    nbt.putInt("Size", components.size());
    if (getManager().getEntity() instanceof InputBusEntity && !getManager().getEntity().dummy())
      DegrassiLogger.INSTANCE.info("Serialized ItemStackHandler to {} with nbt {}", this, nbt);
    return nbt;
  }

  @Override
  public void deserialize(CompoundTag nbt) {
    setSize(nbt.contains("Size") ? nbt.getInt("Size") : components.size());

    ListTag listTag = nbt.getList("list", Tag.TAG_COMPOUND);

    for (int i = 0; i < listTag.size(); i++) {
      CompoundTag itemNbt = listTag.getCompound(i);
      int slot = itemNbt.getInt("Slot");
      ItemComponent component = components.get(slot);
      if (component == null)
        components.set(slot, new ItemComponent(getManager(), itemNbt.getString("id"), getManager().getEntity(), null));
      component = components.get(slot);
      component.deserialize(itemNbt);
    }
  }

  @Override
  public String getTypeString() {
    return "itemHandler";
  }

  public DegrassiItemStackHandler(ComponentManager manager) {
    components = new LinkedList<>();
    this.manager = manager;
  }

  @Override
  public int getSlots() {
    return components.size();
  }

  @Override
  public void setStackInSlot(int slot, @NotNull ItemStack stack) {
    validateSlotIndex(slot);
    this.components.get(slot).setStackInSlot(0, stack);
    onContentsChanged(slot);
  }

  public ItemStack getStackInSlot(int slot) {
    validateSlotIndex(slot);
    return this.components.get(slot).getStackInSlot(0);
  }

  @Override
  public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
    if (stack.isEmpty())
      return ItemStack.EMPTY;

    if (!isItemValid(slot, stack))
      return stack;

    validateSlotIndex(slot);

    if (components.get(slot).getMode().output())
      return stack;

    ItemStack existing = this.components.get(slot).getStackInSlot(0);

    int limit = getStackLimit(slot, stack);

    if (!existing.isEmpty()) {
      if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
        return stack;

      limit -= existing.getCount();
    }

    if (limit <= 0)
      return stack;

    boolean reachedLimit = stack.getCount() > limit;

    if (!simulate) {
      if (existing.isEmpty()) {
        this.components.get(slot).setStackInSlot(0, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
      }
      else {
        existing.grow(reachedLimit ? limit : stack.getCount());
      }
      onContentsChanged(slot);
    }

    return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount()- limit) : ItemStack.EMPTY;
  }

  @Override
  public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
    if (components.get(slot).getMode().input())
      return ItemStack.EMPTY;

    if (amount == 0)
      return ItemStack.EMPTY;

    validateSlotIndex(slot);

    ItemStack existing = this.components.get(slot).getStackInSlot(0);

    if (existing.isEmpty())
      return ItemStack.EMPTY;

    int toExtract = Math.min(amount, existing.getMaxStackSize());

    if (existing.getCount() <= toExtract) {
      if (!simulate) {
        this.components.get(slot).setStackInSlot(0, ItemStack.EMPTY);
        onContentsChanged(slot);
        return existing;
      }
      else {
        return existing.copy();
      }
    }
    else {
      if (!simulate) {
        this.components.get(slot).setStackInSlot(0, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
        onContentsChanged(slot);
      }

      return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
    }
  }

  @Override
  public int getSlotLimit(int slot) {
    return 64;
  }

  protected int getStackLimit(int slot, @NotNull ItemStack stack) {
    return Math.min(getSlotLimit(slot), stack.getMaxStackSize());
  }

  public void onContentsChanged(int slot) {
    markDirty();
  }

  @Override
  public boolean isItemValid(int slot, @NotNull ItemStack stack) {
    return components.get(slot).isItemValid(0, stack);
  }

  public void validateSlotIndex(int slot) {
    if (slot < 0 ||slot >= getSlots())
      throw new RuntimeException("Slot " + slot + " not in validate range - [0, " + getSlots() + ")");
  }

  public void add(ItemComponent component) {
    count.getAndIncrement();
    this.components.add(component);
  }

  public void addAll(Collection<ItemComponent> components) {
    count.getAndAdd(components.size());
    this.components.addAll(components);
  }

  public void setSize(int size) {
    while (components.size() < size) {
      components.add(null);
    }
  }

  @Override
  public String toString() {
    return "DegrassiItemStackHandler{" +
      "components=" + components +
      ", size=" + components.size() +
      '}';
  }
}
