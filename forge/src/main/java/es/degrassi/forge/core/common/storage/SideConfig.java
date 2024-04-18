package es.degrassi.forge.core.common.storage;

import es.degrassi.forge.core.common.component.ComponentIOMode;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.common.component.FluidComponent;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.storage.energy.entity.EnergyCellEntity;
import es.degrassi.forge.core.common.storage.fluid.entity.FluidTankEntity;
import es.degrassi.forge.core.network.SideConfigPacket;
import java.util.EnumMap;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Contract;

public class SideConfig implements INBTSerializable<CompoundTag> {
  private final Map<Direction, ComponentIOMode> config = new EnumMap<>(Direction.class);
  private final MachineEntity<?> entity;

  public SideConfig(MachineEntity<?> entity) {
    this.entity = entity;
    init();
  }

  public void init() {
    init(ComponentIOMode.BOTH);
  }

  public void init(ComponentIOMode mode) {
    for (Direction side : Direction.values()) {
      config.put(side, mode);
    }
  }

  public ComponentIOMode getMode(Direction side) {
    if (side == null) return null;
    return config.get(side);
  }

  public void setMode(Direction side, ComponentIOMode mode) {
    if (side == null || getMode(side) == mode) return;
    config.put(side, mode);
    markDirty(side, mode);
  }

  public void markDirty(Direction side, ComponentIOMode mode) {
    entity.setChanged();
    if(entity.getLevel() != null && !entity.getLevel().isClientSide())
      new SideConfigPacket(side, mode, entity.getBlockPos())
        .sendToChunkListeners(entity.getLevel().getChunkAt(entity.getBlockPos()));
  }

  public MachineEntity<?> getEntity() {
    return entity;
  }

  public boolean onClick(Direction side, ComponentIOMode newMode) {
    if (side == null) return false;
    ComponentIOMode oldMode = config.get(side);
    if (oldMode == null) {
      config.put(side, newMode);
      return true;
    }
    if (oldMode == newMode) return false;
    config.put(side, newMode);
    return true;
  }

  public boolean onClick(Direction side) {
    if (side == null) return false;
    ComponentIOMode newMode = nextMode(config.get(side));
    config.put(side, newMode);
    return true;
  }

  @Contract(pure = true)
  private ComponentIOMode nextMode(ComponentIOMode mode) {
    if (mode == null) return ComponentIOMode.BOTH;
    return switch (mode) {
      case BOTH -> ComponentIOMode.OUTPUT;
      case OUTPUT -> ComponentIOMode.INPUT;
      case INPUT -> ComponentIOMode.NONE;
      case NONE -> ComponentIOMode.BOTH;
    };
  }

  @Override
  public CompoundTag serializeNBT() {
    CompoundTag nbt = new CompoundTag();
    for (Direction side : Direction.values()) {
      if (config.get(side) != null)
        nbt.putString(side.getSerializedName(), config.get(side).serialize());
      else
        nbt.putString(side.getSerializedName(), "null");
    }
    return nbt;
  }

  @Override
  public void deserializeNBT(CompoundTag nbt) {
    for (Direction side : Direction.values()) {
      config.put(side, ComponentIOMode.deserialize(nbt.getString(side.getSerializedName())));
    }
  }

  public void serverTick(Level level, BlockPos pos) {
//    BlockEntity entity = level.getBlockEntity(pos);
//    if (entity == null) return;
//    for (Direction side : Direction.values()) {
//      BlockPos relative = pos.relative(side);
//      BlockEntity relativeEntity = level.getBlockEntity(relative);
//      if (relativeEntity == null) continue;
//      if (entity instanceof EnergyCellEntity energy)
//        relativeEntity.getCapability(energy.getTier().getCapability(), side).ifPresent(handler ->
//          energy.getComponentManager().getComponent("energy").map(comp -> (EnergyComponent) comp).ifPresent(comp -> {
//            if (config.get(side).outputWithAll() && handler.canReceive()) {
//              int toExtract = comp.extractEnergy(energy.getTier().getTransfer(), true);
//              int toReceive = handler.receiveEnergy(toExtract, true);
//              int amount = Math.min(toExtract, toReceive);
//              if (amount > 0) {
//                comp.extractEnergy(amount, false);
//                handler.receiveEnergy(amount, false);
//              }
//            }
//            if (config.get(side).inputWillAll() && handler.canExtract()) {
//              int toExtract = handler.extractEnergy(energy.getTier().getTransfer(), true);
//              int toReceive = comp.receiveEnergy(toExtract, true);
//              int amount = Math.min(toExtract, toReceive);
//              if (amount > 0) {
//                handler.extractEnergy(amount, false);
//                comp.receiveEnergy(amount, false);
//              }
//            }
//          })
//        );
//      else if (entity instanceof FluidTankEntity fluid) {
//        relativeEntity.getCapability(fluid.getTier().getCapability(), side).ifPresent(handler ->
//          fluid.getComponentManager().getComponent("fluid").map(comp -> (FluidComponent) comp).ifPresent(comp -> {
//            int i;
//            if (config.get(side).outputWithAll()) {
//              for (i = 0; i < handler.getTanks(); ++i) {
//                FluidStack stack = comp.getFluidInTank(0);
//                if (stack.isEmpty() || stack.getAmount() <= 0) break;
//                stack.setAmount(Math.min(stack.getAmount(), fluid.getTier().getTransfer()));
//                FluidStack extracted = comp.drain(stack, IFluidHandler.FluidAction.SIMULATE);
//                int inserted = handler.fill(extracted, IFluidHandler.FluidAction.SIMULATE);
//                extracted.setAmount(Math.min(extracted.getAmount(), inserted));
//                if (extracted.getAmount() == 0) continue;
//                comp.drain(extracted, IFluidHandler.FluidAction.EXECUTE);
//                handler.fill(extracted, IFluidHandler.FluidAction.EXECUTE);
//              }
//            }
//            if (config.get(side).inputWillAll()) {
//              for (i = 0; i < handler.getTanks(); ++i) {
//                FluidStack stack = handler.getFluidInTank(i);
//                if (stack.isEmpty() || stack.getAmount() <= 0) continue;
//                stack.setAmount(Math.min(stack.getAmount(), fluid.getTier().getTransfer()));
//                FluidStack extracted = handler.drain(stack, IFluidHandler.FluidAction.SIMULATE);
//                int inserted = comp.fill(extracted, IFluidHandler.FluidAction.SIMULATE);
//                extracted.setAmount(Math.min(extracted.getAmount(), inserted));
//                if (extracted.getAmount() == 0) continue;
//                comp.fill(extracted, IFluidHandler.FluidAction.EXECUTE);
//                handler.drain(extracted, IFluidHandler.FluidAction.EXECUTE);
//              }
//            }
//          })
//        );
//      }
//    }
  }
}
