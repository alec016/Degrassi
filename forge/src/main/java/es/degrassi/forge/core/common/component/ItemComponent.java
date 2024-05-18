package es.degrassi.forge.core.common.component;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.requirement.ItemRequirement;
import es.degrassi.forge.core.network.component.ItemPacket;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class ItemComponent extends ItemStackHandler implements IComponent {
  private final String id;
  private final ComponentManager manager;
  private final MachineEntity<?> entity;
  private final List<Item> filter;
  private boolean whitelist;
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
    this.filter = new ArrayList<>();
    this.filter.addAll(List.of(filter));
    this.mode = mode;
  }

  public ItemStack getItem() {
    return this.stacks.get(0);
  }

  public int getCapacity() {
    return 64;
  }

  public int getRemainingSpace() {
    if(!this.getItem().isEmpty())
      return this.getCapacity() - this.getItem().getCount();
    return this.getCapacity();
  }

  @Override
  public void markDirty() {
    entity.setChanged();
    if(entity.getLevel() != null && !entity.getLevel().isClientSide())
      new ItemPacket(getStackInSlot(0), id, entity.getBlockPos())
        .sendToChunkListeners(entity.getLevel().getChunkAt(entity.getBlockPos()));
  }

  @Override
  public void fill(IRequirement<?> requirement) {
    if (requirement instanceof ItemRequirement req) {
      if (!this.filter.isEmpty())
        this.filter.clear();
      this.filter.add(req.getItem());
      this.whitelist = true;
      if (req.getMode().isInput())
        setStackInSlot(0, new ItemStack(req.getItem(), req.getAmount()));
      markDirty();
    }
  }

  @Override
  public CompoundTag serialize() {
    CompoundTag tag = serializeNBT();
    tag.putString("mode", mode.serialize());
    tag.putString("id", id);
    return tag;
  }

  @Override
  public void deserialize(CompoundTag nbt) {
    deserializeNBT(nbt);
    mode = ComponentIOMode.deserialize(nbt.getString("mode"));
  }

  public void setItem(ItemStack item) {
    setStackInSlot(0, item);
  }

  @Override
  public boolean isItemValid(int slot, @NotNull ItemStack item) {
    return filter.stream().filter(item::is).findFirst().map(i -> whitelist).orElse(!whitelist);
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
    if (mode.output()) return ItemStack.EMPTY;
    return super.insertItem(0, stack, simulate);
  }


  public int insert(Item item, int amount, boolean simulate) {
    if (amount <= 0 || mode.output() || item == Items.AIR) return 0;

    //Check the inserted stack max size, in case a mod like AE2 try to insert a stack of non-stackable items
    amount = Math.min(amount, new ItemStack(item, amount).getMaxStackSize());

    //Check the current stack limit
    amount = Math.min(amount, this.getItem().getMaxStackSize() - this.getItem().getCount());

    //Check the slot capacity
    amount = Math.min(amount, this.getCapacity() - this.getItem().getCount());

    if (getItem().isEmpty()) {
      if (!simulate) {
        setItem(new ItemStack(item, amount));
        markDirty();
      }
      return amount;
    } else if (this.getItem().is(item)) {
      amount = Math.min(getRemainingSpace(), amount);
      if (!simulate) {
        this.getItem().grow(amount);
        markDirty();
      }
      return amount;
    }
    return 0;
  }

  @Override
  public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
    if (mode.input()) return ItemStack.EMPTY;
    return super.extractItem(0, amount, simulate);
  }

  public ItemStack extract(int amount, boolean simulate) {
    if (amount <= 0 || this.getItem().isEmpty() || mode.input())
      return ItemStack.EMPTY;
    amount = Math.min(amount, getItem().getCount());
    ItemStack removed = new ItemStack(getItem().getItem(), amount);
    if (!simulate) {
      this.getItem().shrink(amount);
      markDirty();
    }
    return removed;
  }

  @Override
  public int getStackLimit(int slot, @NotNull ItemStack stack) {
    return super.getStackLimit(0, stack);
  }

  @Override
  public void onContentsChanged(int slot) {
    markDirty();
  }

  // recipe stuff
  public @NotNull ItemStack insertRecipeItem(int slot, @NotNull ItemStack stack, boolean simulate) {
    return super.insertItem(0, stack, simulate);
  }

  public @NotNull ItemStack extractRecipeItem(int slot, int amount, boolean simulate) {
    return super.extractItem(0, amount, simulate);
  }

  @Override
  public String getTypeString() {
    return "item";
  }

  @Override
  public String toString() {
    return asJson().toString();
  }

  public JsonObject asJson() {
    JsonObject json = new JsonObject();
    json.addProperty("id", id);
    json.addProperty("whitelist", whitelist);
    json.addProperty("mode", mode.serialize());
    json.addProperty("item", getStackInSlot(0).getHoverName().getString());
    json.addProperty("amount", getStackInSlot(0).getCount());
    JsonArray filter = new JsonArray();
    this.filter.forEach(item -> filter.add(item.getDefaultInstance().getHoverName().getString()));
    json.add("filter", filter);
    json.addProperty("type", "item");
    return json;
  }

  public ItemComponent copy(MachineEntity<?> entity, ComponentManager manager) {
    return new ItemComponent(manager, id, whitelist, entity, mode, filter.toArray(Item[]::new));
  }
}
