package es.degrassi.forge.core.common.cables.fluid;

import es.degrassi.forge.core.common.cables.SideConfig;
import es.degrassi.forge.core.common.cables.Transfer;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

public class FluidSideConfig extends SideConfig<FluidCableEntity> {
  public FluidSideConfig(FluidCableEntity storage) {
    super(storage);
  }

  public boolean isAllEquals() {
    boolean flag = true;
    int first = -1;
    for (int i = 1; i < 6; i++) {
      if (this.storage.isFluidPresent(Direction.from3DDataValue(i))) {
        if (first < 0) {
          first = this.transfers[i].ordinal();
        } else if (this.transfers[i].ordinal() != first) {
          flag = false;
        }
      }
    }
    return flag;
  }
  public void setType(@Nullable Direction side, Transfer type) {
    if (side == null || this.storage.getTransferType().equals(Transfer.NONE))
      return;
    if (!this.storage.isFluidPresent(side))
      return;
    this.transfers[side.get3DDataValue()] = type;
  }
}
