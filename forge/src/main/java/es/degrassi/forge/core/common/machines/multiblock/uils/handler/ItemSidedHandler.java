package es.degrassi.forge.core.common.machines.multiblock.uils.handler;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.wrapper.DegrassiItemStackHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ItemSidedHandler extends DegrassiItemStackHandler {
  private final DegrassiItemStackHandler handler;
  private final Direction extract, insert, from;
  public ItemSidedHandler(
    ComponentManager manager,
    DegrassiItemStackHandler handler,
    Direction extract,
    Direction insert,
    Direction from
  ) {
    super(manager);
    this.handler = handler;
    this.from = from;
    this.extract = extract;
    this.insert = insert;
  }

  @Override
  public CompoundTag serialize() {
    return handler.serialize();
  }

  @Override
  public void deserialize(CompoundTag nbt) {
    handler.deserialize(nbt);
  }

  @Override
  public int getSlots() {
    return handler.getSlots();
  }

  @Override
  public void setStackInSlot(int slot, @NotNull ItemStack stack) {
    handler.setStackInSlot(slot, stack);
  }

  public ItemStack getStackInSlot(int slot) {
    return handler.getStackInSlot(slot);
  }

  @Override
  public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
    return from == insert ? handler.insertItem(slot, stack, simulate) : ItemStack.EMPTY;
  }

  @Override
  public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
    return from == extract ? handler.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
  }

  @Override
  public boolean isItemValid(int slot, @NotNull ItemStack stack) {
    return handler.isItemValid(slot, stack);
  }

  @Override
  public void validateSlotIndex(int slot) {
    handler.validateSlotIndex(slot);
  }
}
