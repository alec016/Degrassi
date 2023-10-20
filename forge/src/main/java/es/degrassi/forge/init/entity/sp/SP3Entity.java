package es.degrassi.forge.init.entity.sp;

import es.degrassi.comon.init.entity.panel.SolarPanelEntity;
import es.degrassi.comon.util.storage.AbstractEnergyStorage;
import es.degrassi.forge.init.registration.EntityRegister;
import es.degrassi.forge.init.registration.ItemRegister;
import es.degrassi.forge.integration.config.DegrassiConfig;
import es.degrassi.network.panel.PanelItemPacket;
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

public class SP3Entity extends SolarPanelEntity {
  private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
  private LazyOptional<AbstractEnergyStorage> lazyEnergyHandler = LazyOptional.empty();
  public SP3Entity(BlockPos pos, BlockState state) {
    super(
      EntityRegister.SP_TIER_3.get(),
      pos,
      state,
      Component.translatable("block.degrassi.solar_panel_tier_3"),
      DegrassiConfig.sp3_capacity.get(),
      DegrassiConfig.sp3_transfer.get()
    );

    this.itemHandler = new ItemStackHandler(4) {
      @Override
      protected void onContentsChanged(int slot) {
        setChanged();
        if (level != null && !level.isClientSide() && level.getServer() != null) {
          new PanelItemPacket(this, worldPosition)
            .sendToAll(level.getServer());
          // .sendToChunkListeners(level.getChunkAt(pos));
        }
      }

      @Override
      public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return switch(slot) {
          case 0 -> stack.is(ItemRegister.EFFICIENCY_UPGRADE.get());
          case 1 -> stack.is(ItemRegister.TRANSFER_UPGRADE.get());
          case 2 -> stack.is(ItemRegister.GENERATION_UPGRADE.get());
          case 3 -> stack.is(ItemRegister.CAPACITY_UPGRADE.get());
          default -> isItemValid(slot, stack);
        };
      }
    };
  }

  @Override
  public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
    if (cap == ForgeCapabilities.ENERGY) return lazyEnergyHandler.cast();
    if(cap == ForgeCapabilities.ITEM_HANDLER) return lazyItemHandler.cast();

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

  public int getGeneration() {
    return DegrassiConfig.sp3_generation.get();
  }

  @Override
  public @NotNull CompoundTag getUpdateTag() {
    CompoundTag nbt = super.getUpdateTag();
    nbt.put("panel.inventory", itemHandler.serializeNBT());
    nbt.putInt("panel.energy", ENERGY_STORAGE.getEnergyStored());
    nbt.putInt("panel.capacity", ENERGY_STORAGE.getMaxEnergyStored());
    return nbt;
  }

  @Override
  public void handleUpdateTag(CompoundTag tag) {
    load(tag);
  }
}
