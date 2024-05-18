package es.degrassi.forge.core.common.machines.container.slot;

import es.degrassi.forge.core.common.component.ItemComponent;
import lombok.Getter;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

@Getter
public class SlotItemComponent extends Slot {
  private static final Container EMPTY = new SimpleContainer(0);

  private final ItemComponent component;

  public SlotItemComponent(ItemComponent component, int slot, int x, int y) {
    super(EMPTY, slot, x, y);
    this.component = component;
  }

  @Override
  public ItemStack getItem() {
    return this.component.getStackInSlot(0);
  }

  @Override
  public void set(ItemStack stack) {
    this.component.setItem(stack);
    setChanged();
  }

  @Override
  public int getMaxStackSize() {
    return this.component.getSlotLimit(0);
  }

  @Override
  public ItemStack remove(int amount) {
    return this.component.extract(amount, false);
  }

  @Override
  public void setChanged() {
    this.component.getManager().markDirty();
  }
}
