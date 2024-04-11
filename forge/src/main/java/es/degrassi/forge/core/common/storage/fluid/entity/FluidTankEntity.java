package es.degrassi.forge.core.common.storage.fluid.entity;

import es.degrassi.forge.core.common.component.ComponentIOMode;
import es.degrassi.forge.core.common.component.FluidComponent;
import es.degrassi.forge.core.common.storage.StorageEntity;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.tiers.Storage;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidTankEntity extends StorageEntity<Storage.Fluid> {

  public FluidTankEntity(BlockPos pos, BlockState blockState, Storage.Fluid tier) {
    super(EntityRegistration.FLUID_TANK.get(), pos, blockState, tier);

    this.getComponentManager()
      .addFluid(tier.getCapacity(), "fluid", ComponentIOMode.BOTH);
  }

  @Override
  public Component getName() {
    return null;
  }

  public boolean addFluid(Fluid fluid) {
    AtomicBoolean executed = new AtomicBoolean(false);
    getComponentManager().getComponent("fluid").map(comp -> (FluidComponent) comp).ifPresent(comp -> {
      if (tier.isCreative()) {
        comp.setFluid(new FluidStack(fluid, Integer.MAX_VALUE));
        executed.set(true);
      } else {
        int insert = comp.fill(new FluidStack(fluid, 1000), IFluidHandler.FluidAction.SIMULATE);
        if (insert == 1000) executed.set(true);
      }
    });
    return executed.get();
  }

  public Fluid removeFluid() {
    AtomicReference<Fluid> fluid = new AtomicReference<>(Fluids.EMPTY);
    getComponentManager().getComponent("fluid").map(comp -> (FluidComponent) comp).ifPresent(comp -> {
      FluidStack f = comp.drain(1000, IFluidHandler.FluidAction.SIMULATE);
      if (f.getAmount() == 1000 && !f.getFluid().isSame(Fluids.EMPTY)) {
        fluid.set(f.getFluid());
        if (!tier.isCreative()) comp.drain(1000, IFluidHandler.FluidAction.EXECUTE);
      }
    });
    return fluid.get();
  }

  public FluidStack getFluid() {
    AtomicReference<FluidStack> fluid = new AtomicReference<>(FluidStack.EMPTY);
    getComponentManager().getComponent("fluid").map(comp -> (FluidComponent) comp).ifPresent(comp -> fluid.set(comp.getFluid()));
    return fluid.get();
  }
}
