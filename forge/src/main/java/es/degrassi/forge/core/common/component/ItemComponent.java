package es.degrassi.forge.core.common.component;

import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.network.component.ItemPacket;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class ItemComponent extends ItemStackHandler implements IComponent {
  private final String id;
  private final ComponentManager manager;
  private final MachineEntity<?> entity;
  private final List<Item> filter;
  private final boolean whitelist;
  private ComponentIOMode mode;
  public ItemComponent(ComponentManager manager, String id, MachineEntity<?> entity, ComponentIOMode mode) {
    this.manager = manager;
    this.id = id;
    this.entity = entity;
    this.filter = new ArrayList<>();
    this.whitelist = false;
    this.mode = mode;
  }

  public ItemComponent(ComponentManager manager, String id, boolean whitelist, MachineEntity<?> entity, ComponentIOMode mode, Item...filter) {
    this.manager = manager;
    this.id = id;
    this.whitelist = whitelist;
    this.entity = entity;
    this.filter = List.of(filter);
    this.mode = mode;
  }

  public ComponentIOMode getMode() {
    return mode;
  }

  public void setMode(ComponentIOMode mode) {
    this.mode = mode;
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
    CompoundTag tag = serializeNBT();
    tag.putString("mode", mode.serialize());
    nbt.put(id, tag);
  }

  @Override
  public void deserialize(CompoundTag nbt) {
    CompoundTag tag = nbt.getCompound(id);
    deserializeNBT(tag);
    mode = ComponentIOMode.deserialize(tag.getString("mode"));
  }

  public void setItem(ItemStack item) {
    setStackInSlot(0, item);
  }

  @Override
  public boolean isItemValid(int slot, @NotNull ItemStack item) {
    return filter.stream().filter(stack -> mode.receiveWillAll() && item.is(stack)).findFirst().map(i -> mode.receiveWillAll() && whitelist).orElse(mode.receiveWillAll() && !whitelist);
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
    if (mode.extract()) return ItemStack.EMPTY;
    return super.insertItem(0, stack, simulate);
  }

  @Override
  public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
    if (mode.receive()) return ItemStack.EMPTY;
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
