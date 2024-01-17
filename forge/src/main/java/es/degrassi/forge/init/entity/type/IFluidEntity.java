package es.degrassi.forge.init.entity.type;

import es.degrassi.forge.init.gui.component.*;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface IFluidEntity extends IDegrassiEntity {
  FluidComponent getFluidStorage();

  void setFluidHandler(FluidTank storage);

  void setFluid(FluidStack fluid);
}
