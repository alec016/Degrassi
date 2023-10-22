package es.degrassi.forge.init.entity.furnace;

import es.degrassi.comon.init.entity.furnace.FurnaceEntity;
import es.degrassi.comon.util.storage.AbstractEnergyStorage;
import es.degrassi.forge.init.registration.EntityRegister;
import es.degrassi.forge.integration.config.DegrassiConfig;
import es.degrassi.network.furnace.FurnaceItemPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NetheriteFurnaceEntity extends FurnaceEntity {
  private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
  private LazyOptional<AbstractEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
  public NetheriteFurnaceEntity(BlockPos pos, BlockState state) {
    super(
      EntityRegister.NETHERITE_FURNACE.get(),
      pos,
      state,
      Component.translatable(
        "block.degrassi.netherite_furnace"
      ),
      DegrassiConfig.netherite_furnace_capacity.get(),
      DegrassiConfig.netherite_furnace_transfer.get()
    );
    this.itemHandler = new ItemStackHandler(4) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
        if (level != null && !level.isClientSide() && level.getServer() != null) {
          new FurnaceItemPacket(this, worldPosition)
            .sendToAll(level.getServer());
          //.sendToChunkListeners(level.getChunkAt(pos));
        }
      }

      @Override
      public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return switch(slot) {
          case 0, 1, 2 -> true;
          case 3 -> false;
          default -> isItemValid(slot, stack);
        };
      }
    };
  }
  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ENERGY) return lazyEnergyHandler.cast();
    if (cap == ForgeCapabilities.ITEM_HANDLER) return lazyItemHandler.cast();

    return super.getCapability(cap, side);
  }

  @Override
  public void onLoad() {
    super.onLoad();
    lazyItemHandler = LazyOptional.of(() -> itemHandler);
    lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
  }

  @Override
  public void invalidateCaps() {
    super.invalidateCaps();
    lazyItemHandler.invalidate();
    lazyEnergyHandler.invalidate();
  }

  @Override
  public @NotNull CompoundTag getUpdateTag() {
    CompoundTag nbt = super.getUpdateTag();
    nbt.put("furnace.inventory", itemHandler.serializeNBT());
    nbt.putInt("furnace.energy", ENERGY_STORAGE.getEnergyStored());
    nbt.putInt("furnace.capacity", ENERGY_STORAGE.getMaxEnergyStored());
    nbt.putInt("furnace.progress", progressStorage.getProgress());
    nbt.putInt("furnace.maxProgress", progressStorage.getMaxProgress());
    return nbt;
  }

  @Override
  public void handleUpdateTag(CompoundTag tag) {
    load(tag);
  }
}
