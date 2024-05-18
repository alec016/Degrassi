package es.degrassi.forge.core.common.machines.container;

import es.degrassi.forge.core.common.element.ItemElement;
import es.degrassi.forge.core.common.element.PlayerInventoryElement;
import es.degrassi.forge.core.common.machines.container.slot.SlotItemComponent;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.Getter;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public abstract class MachineContainer<T extends MachineEntity<?>> extends AbstractContainerMenu {
  private final int firstComponentSlotIndex;
  private boolean hasPlayerInventory = false;

  @Getter
  private final T entity;
  @Getter
  private final Level level;
  @Getter
  private final Inventory playerInv;

  private final List<SlotItemComponent> inputSlots = new ArrayList<>();

  protected MachineContainer(@Nullable MenuType<?> menuType, int containerId, T entity, Inventory inventory) {
    super(menuType, containerId);
    this.entity = entity;
    this.playerInv = inventory;
    this.level = inventory.player.level();
    AtomicInteger index = new AtomicInteger(0);
    entity.getElementManager().getElement("player_inventory").map(element -> (PlayerInventoryElement) element).ifPresent(element -> {
      this.hasPlayerInventory = true;
      int x = element.getX() + 1;
      int y = element.getY() + 1;
      int i;
      for (i = 0; i < 9; ++i) {
        addSlot(new Slot(playerInv, index.getAndIncrement(), x + i * 18, y + 58));
      }
      for (i = 0; i < 3; ++i) {
        for (int j = 0; j < 9; ++j) {
          addSlot(new Slot(playerInv, index.getAndIncrement(), x + j * 18, y + i * 18));
        }
      }
    });
    firstComponentSlotIndex = index.get();
    entity.getComponentManager().getItemHandler().getComponents()
      .forEach(
        component -> entity
          .getElementManager()
          .getElement(component.getId())
          .map(element -> (ItemElement) element)
          .ifPresent(
            element -> {
              SlotItemComponent slot = new SlotItemComponent(
                component,
                index.getAndIncrement(),
                element.getX() + 1,
                element.getY() + 1
              );
              addSlot(slot);
              if (component.getMode().inputWillAll())
                inputSlots.add(slot);
            }
          )
      );
  }

  @Override
  public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
    if(!this.hasPlayerInventory)
      return ItemStack.EMPTY;

    Slot clickedSlot = this.slots.get(index);
    if(clickedSlot.getItem().isEmpty())
      return ItemStack.EMPTY;

    if (clickedSlot.container == this.playerInv) {
      ItemStack stack = clickedSlot.getItem().copy();
      for (SlotItemComponent slotComponent : this.inputSlots) {
        int maxInput = slotComponent.getComponent().insert(stack.getItem(), stack.getCount(), true);
        if (maxInput > 0) {
          int toInsert = Math.min(maxInput, stack.getCount());
          slotComponent.getComponent().insert(stack.getItem(), toInsert, false);
          stack.shrink(toInsert);
        }
        if (stack.isEmpty())
          break;
      }
      if (stack.isEmpty())
        clickedSlot.remove(clickedSlot.getItem().getCount());
      else
        clickedSlot.remove(clickedSlot.getItem().getCount() - stack.getCount());
    } else {
      if (!(clickedSlot instanceof SlotItemComponent slotComponent))
        return ItemStack.EMPTY;

      ItemStack removed = slotComponent.getItem();
      if (!moveItemStackTo(removed, 0, this.firstComponentSlotIndex - 1, false))
        return ItemStack.EMPTY;
      slotComponent.setChanged();
    }

    return ItemStack.EMPTY;
  }

  @Override
  public boolean stillValid(@NotNull Player player) {
    return player.level.getBlockState(this.entity.getBlockPos()) == this.entity.getBlockState() &&
      player.level.getBlockEntity(this.entity.getBlockPos()) == this.entity &&
      player.position().distanceToSqr(Vec3.atCenterOf(this.entity.getBlockPos())) <= 64;
  }
}
