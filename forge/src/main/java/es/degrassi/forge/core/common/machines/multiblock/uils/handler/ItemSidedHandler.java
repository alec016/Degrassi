package es.degrassi.forge.core.common.machines.multiblock.uils.handler;

import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.wrapper.DegrassiItemStackHandler;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class ItemSidedHandler extends DegrassiItemStackHandler {
  public static Map<Direction, LazyOptional<ItemSidedHandler>> DEFAULT_ALL_ENABLED(ComponentManager manager, DegrassiItemStackHandler component){
    return Map.of(
      Direction.UP, LazyOptional.of(() -> new ItemSidedHandler(manager, component, Direction.UP, Direction.UP, Direction.UP)),
      Direction.DOWN, LazyOptional.of(() -> new ItemSidedHandler(manager, component, Direction.DOWN, Direction.DOWN, Direction.DOWN)),
      Direction.NORTH, LazyOptional.of(() -> new ItemSidedHandler(manager, component, Direction.NORTH, Direction.NORTH, Direction.NORTH)),
      Direction.SOUTH, LazyOptional.of(() -> new ItemSidedHandler(manager, component, Direction.SOUTH, Direction.SOUTH, Direction.SOUTH)),
      Direction.EAST, LazyOptional.of(() -> new ItemSidedHandler(manager, component, Direction.EAST, Direction.EAST, Direction.EAST)),
      Direction.WEST, LazyOptional.of(() -> new ItemSidedHandler(manager, component, Direction.WEST, Direction.WEST, Direction.WEST))
    );
  }
  public static Map<Direction, LazyOptional<ItemSidedHandler>> DEFAULT_ALL_EXTRACT(ComponentManager manager, DegrassiItemStackHandler component) {
    return Map.of(
      Direction.UP, LazyOptional.of(() -> new ItemSidedHandler(manager, component, Direction.UP, null, Direction.UP)),
      Direction.DOWN, LazyOptional.of(() -> new ItemSidedHandler(manager, component, Direction.DOWN, null, Direction.DOWN)),
      Direction.NORTH, LazyOptional.of(() -> new ItemSidedHandler(manager, component, Direction.NORTH, null, Direction.NORTH)),
      Direction.SOUTH, LazyOptional.of(() -> new ItemSidedHandler(manager, component, Direction.SOUTH, null, Direction.SOUTH)),
      Direction.EAST, LazyOptional.of(() -> new ItemSidedHandler(manager, component, Direction.EAST, null, Direction.EAST)),
      Direction.WEST, LazyOptional.of(() -> new ItemSidedHandler(manager, component, Direction.WEST, null, Direction.WEST))
    );
  }
  public static Map<Direction, LazyOptional<ItemSidedHandler>> DEFAULT_ALL_INSERT(ComponentManager manager, DegrassiItemStackHandler component) {
    return Map.of(
      Direction.UP, LazyOptional.of(() -> new ItemSidedHandler(manager, component, null, Direction.UP, Direction.UP)),
      Direction.DOWN, LazyOptional.of(() -> new ItemSidedHandler(manager, component, null, Direction.DOWN, Direction.DOWN)),
      Direction.NORTH, LazyOptional.of(() -> new ItemSidedHandler(manager, component, null, Direction.NORTH, Direction.NORTH)),
      Direction.SOUTH, LazyOptional.of(() -> new ItemSidedHandler(manager, component, null, Direction.SOUTH, Direction.SOUTH)),
      Direction.EAST, LazyOptional.of(() -> new ItemSidedHandler(manager, component, null, Direction.EAST, Direction.EAST)),
      Direction.WEST, LazyOptional.of(() -> new ItemSidedHandler(manager, component, null, Direction.WEST, Direction.WEST))
    );
  }


  public static Map<Direction, LazyOptional<ItemSidedHandler>> DEFAULT_ALL_ENABLED(ComponentManager manager, DegrassiItemStackHandler component, Direction from){
    return Map.of(
      Direction.UP, LazyOptional.of(() -> new ItemSidedHandler(manager, component, Direction.UP, Direction.UP, from)),
      Direction.DOWN, LazyOptional.of(() -> new ItemSidedHandler(manager, component, Direction.DOWN, Direction.DOWN, from)),
      Direction.NORTH, LazyOptional.of(() -> new ItemSidedHandler(manager, component, Direction.NORTH, Direction.NORTH, from)),
      Direction.SOUTH, LazyOptional.of(() -> new ItemSidedHandler(manager, component, Direction.SOUTH, Direction.SOUTH, from)),
      Direction.EAST, LazyOptional.of(() -> new ItemSidedHandler(manager, component, Direction.EAST, Direction.EAST, from)),
      Direction.WEST, LazyOptional.of(() -> new ItemSidedHandler(manager, component, Direction.WEST, Direction.WEST, from))
    );
  }
  public static Map<Direction, LazyOptional<ItemSidedHandler>> DEFAULT_ALL_EXTRACT(ComponentManager manager, DegrassiItemStackHandler component, Direction from) {
    return Map.of(
      Direction.UP, LazyOptional.of(() -> new ItemSidedHandler(manager, component, Direction.UP, null, from)),
      Direction.DOWN, LazyOptional.of(() -> new ItemSidedHandler(manager, component, Direction.DOWN, null, from)),
      Direction.NORTH, LazyOptional.of(() -> new ItemSidedHandler(manager, component, Direction.NORTH, null, from)),
      Direction.SOUTH, LazyOptional.of(() -> new ItemSidedHandler(manager, component, Direction.SOUTH, null, from)),
      Direction.EAST, LazyOptional.of(() -> new ItemSidedHandler(manager, component, Direction.EAST, null, from)),
      Direction.WEST, LazyOptional.of(() -> new ItemSidedHandler(manager, component, Direction.WEST, null, from))
    );
  }
  public static Map<Direction, LazyOptional<ItemSidedHandler>> DEFAULT_ALL_INSERT(ComponentManager manager, DegrassiItemStackHandler component, Direction from) {
    return Map.of(
      Direction.UP, LazyOptional.of(() -> new ItemSidedHandler(manager, component, null, Direction.UP, from)),
      Direction.DOWN, LazyOptional.of(() -> new ItemSidedHandler(manager, component, null, Direction.DOWN, from)),
      Direction.NORTH, LazyOptional.of(() -> new ItemSidedHandler(manager, component, null, Direction.NORTH, from)),
      Direction.SOUTH, LazyOptional.of(() -> new ItemSidedHandler(manager, component, null, Direction.SOUTH, from)),
      Direction.EAST, LazyOptional.of(() -> new ItemSidedHandler(manager, component, null, Direction.EAST, from)),
      Direction.WEST, LazyOptional.of(() -> new ItemSidedHandler(manager, component, null, Direction.WEST, from))
    );
  }

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
