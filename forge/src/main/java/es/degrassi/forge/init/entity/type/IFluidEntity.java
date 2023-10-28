package es.degrassi.forge.init.entity.type;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface IFluidEntity {
  FluidTank getFluidStorage();

  void setFluidHandler(FluidTank storage);

  void setFluid(FluidStack fluid);
}
