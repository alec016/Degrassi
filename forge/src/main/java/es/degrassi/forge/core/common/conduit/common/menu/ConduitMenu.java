package es.degrassi.forge.core.common.conduit.common.menu;

import es.degrassi.common.conduit.ConduitTypes;
import es.degrassi.common.conduit.IConduitType;
import es.degrassi.forge.core.common.conduit.common.blockentity.ConduitBlockEntity;
import es.degrassi.forge.core.common.conduit.common.blockentity.ConduitBundle;
import es.degrassi.forge.core.common.conduit.common.blockentity.SlotType;
import es.degrassi.forge.core.common.conduit.common.blocks.ConduitBlock;
import es.degrassi.forge.core.common.conduit.common.init.ConduitMenus;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class ConduitMenu extends SyncedMenu<ConduitBlockEntity> {

  //List capped to MaxConduitTypes inner list with 3 (2 filter, 1 upgrade) slots, because that's the maximum. All non visible slots will be made noninteractable
  private final List<ConduitSlot> conduitSlots = new ArrayList<>();
  private Direction direction;
  private IConduitType<?> conduitType;


  public ConduitMenu(@Nullable ConduitBlockEntity blockEntity, Inventory inventory, int pContainerId, Direction direction, IConduitType<?> type) {
    super(blockEntity, inventory, ConduitMenus.CONDUIT_MENU.get(), pContainerId);
    this.direction = direction;
    this.conduitType = type;
    if (blockEntity != null) {
      IItemHandler conduitItemHandler = blockEntity.getConduitItemHandler();
      for (Direction forDirection : Direction.values()) {
        for (int i = 0; i < ConduitBundle.MAX_CONDUIT_TYPES; i++) {
          for (SlotType slotType: SlotType.values()) {
            ConduitSlot slot = new ConduitSlot(blockEntity.getBundle(),conduitItemHandler, () -> this.direction, forDirection, () -> blockEntity.getBundle().getTypes().indexOf(this.conduitType), i, slotType);
            conduitSlots.add(slot);
            slot.updateVisibilityPosition();
            addSlot(slot);
          }
        }
      }
    }
    addInventorySlots(23,113);
  }

  @Override
  public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
    ItemStack itemstack = ItemStack.EMPTY;
    Slot slot = this.slots.get(pIndex);

    if (slot.hasItem()) {
      ItemStack itemstack1 = slot.getItem();
      itemstack = itemstack1.copy();
      if (pIndex < this.slots.size() - 36) {
        if (!this.moveItemStackTo(itemstack1, this.slots.size() - 36, this.slots.size(), true)) {
          return ItemStack.EMPTY;
        }
      } else if (!this.moveItemStackTo(itemstack1, 0, this.slots.size() - 36, false)) {
        return ItemStack.EMPTY;
      }

      if (itemstack1.isEmpty()) {
        slot.set(ItemStack.EMPTY);
      } else {
        slot.setChanged();
      }
    }

    return itemstack;
  }

  @Override
  public boolean stillValid(Player player) {
    return getBlockEntity() != null
      && getBlockEntity().stillValid(player)
      //usually called serverside only, but I want to have a clientcheck for type, to close the screen clientside immediatly
      && (player instanceof ServerPlayer ||clientValid());
  }

  private boolean clientValid() {
    return getBlockEntity().getBundle().getTypes().contains(conduitType)
      && ConduitBlock.canBeOrIsValidConnection(getBlockEntity(), conduitType, direction);
  }

  public static ConduitMenu factory(@Nullable MenuType<ConduitMenu> pMenuType, int pContainerId, Inventory inventory, FriendlyByteBuf buf) {
    BlockEntity entity = inventory.player.level().getBlockEntity(buf.readBlockPos());
    Direction direction = buf.readEnum(Direction.class);
    IConduitType<?> type = ConduitTypes.getRegistry().getValue(buf.readInt());
    if (entity instanceof ConduitBlockEntity castBlockEntity) {
      return new ConduitMenu(castBlockEntity, inventory, pContainerId, direction, type);
    }

    LogManager.getLogger().warn("couldn't find BlockEntity");
    return new ConduitMenu(null, inventory, pContainerId, direction, type);
  }

  @Override
  public void removed(Player player) {
    super.removed(player);
    if (getBlockEntity() != null
      && player instanceof ServerPlayer serverPlayer
      && serverPlayer.serverLevel().players().stream().filter(p -> p != player).noneMatch(p -> p.containerMenu instanceof ConduitMenu)) {
      getBlockEntity().updateEmptyDynConnection();
    }
  }
}
