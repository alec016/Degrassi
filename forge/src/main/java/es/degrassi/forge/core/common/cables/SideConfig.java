package es.degrassi.forge.core.common.cables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;

public abstract class SideConfig<T extends CableEntity<?, ?>> {
  protected final Transfer[] transfers = new Transfer[6];
  protected final T storage;
  private boolean isSetFromNBT;

  public SideConfig(T storage) {
    this.storage = storage;
    Arrays.fill(this.transfers, Transfer.NONE);
  }

  public void init() {
    if (!this.isSetFromNBT) {
      for (Direction side : Direction.values()) {
        setType(side, this.storage.getTransferType());
      }
    }
  }

  public void read(CompoundTag nbt) {
    if (nbt.contains("side_transfer_type", Tag.TAG_INT_ARRAY)) {
      int[] arr = nbt.getIntArray("side_transfer_type");
      for (int i = 0; i < arr.length; i++) {
        this.transfers[i] = Transfer.values()[arr[i]];
      }
      this.isSetFromNBT = true;
    }
  }

  public CompoundTag write(CompoundTag nbt) {
    List<Integer> list = new ArrayList<>();
    for (int i = 0, valuesLength = this.transfers.length; i < valuesLength; i++) {
      list.add(i, this.transfers[i].ordinal());
    }
    nbt.putIntArray("side_transfer_type", list);
    return nbt;
  }

  public void nextTypeAll() {
    if (isAllEquals()) {
      for (Direction side : Direction.values()) {
        nextType(side);
      }
    } else {
      for (Direction side : Direction.values()) {
        setType(side, Transfer.ALL);
      }
    }
  }

  public void nextType(@Nullable Direction side) {
    setType(side, getType(side).next(this.storage.getTransferType()));
  }

  public Transfer getType(@Nullable Direction side) {
    if (side != null) {
      return this.transfers[side.get3DDataValue()];
    }
    return Transfer.NONE;
  }

  public abstract boolean isAllEquals();

  public abstract void setType(@Nullable Direction side, Transfer type);
}
