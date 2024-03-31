package es.degrassi.forge.core.common.component;

import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IManager;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.network.component.FluidPacket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

public class FluidComponent extends FluidTank implements IComponent {
  private final ComponentManager manager;
  private final String id;
  private final boolean whitelist;
  private final List<Fluid> filter;
  private final MachineEntity<?> entity;
  private ComponentIOMode mode;

  public FluidComponent(ComponentManager manager, String id, boolean whitelist, int capacity, MachineEntity<?> entity, ComponentIOMode mode, Fluid...fluids) {
    super(capacity);
    this.manager = manager;
    this.capacity = capacity;
    this.id = id;
    this.whitelist = whitelist;
    this.entity = entity;
    this.filter = new ArrayList<>();
    this.mode = mode;
    filter.addAll(Arrays.asList(fluids));
  }

  public ComponentIOMode getMode() {
    return mode;
  }

  public void setMode(ComponentIOMode mode) {
    this.mode = mode;
  }

  @Override
  public boolean isFluidValid(FluidStack stack) {
    return filter.stream().filter(fluid -> mode.receive() && fluid.isSame(stack.getFluid())).findFirst().map(i -> mode.receive() && whitelist).orElse(mode.receive() && (stack.isFluidEqual(getFluid()) || fluid.isEmpty()));
  }

  @Override
  public IManager<IComponent> getManager() {
    return manager;
  }

  @Override
  protected void onContentsChanged() {
    markDirty();
  }

  @Override
  public void markDirty() {
    entity.setChanged();
    if(entity.getLevel() != null && !entity.getLevel().isClientSide())
      new FluidPacket(this.fluid, this.capacity, id, entity.getBlockPos())
        .sendToChunkListeners(entity.getLevel().getChunkAt(entity.getBlockPos()));
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void serialize(CompoundTag nbt) {
    CompoundTag tag = new CompoundTag();
    super.writeToNBT(tag);
    tag.putString("mode", mode.serialize());
    nbt.put(id, tag);
  }

  @Override
  public void deserialize(CompoundTag nbt) {
    CompoundTag tag = nbt.getCompound(id);
    super.readFromNBT(tag);
    mode = ComponentIOMode.deserialize(tag.getString("mode"));
  }

  public int toComparatorPower() {
    return (int) (subSized() * 15);
  }

  public float subSized() {
    return this.capacity > 0 ? (float) this.getFluidAmount() / this.capacity : 0;
  }

  @Override
  public int fill(FluidStack resource, FluidAction action) {
    if (mode.extract()) return 0;
    return super.fill(resource, action);
  }

  @Override
  public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
    if (mode.receive()) return FluidStack.EMPTY;
    return super.drain(maxDrain, action);
  }

  @Override
  public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
    if (mode.receive()) return FluidStack.EMPTY;
    return super.drain(resource, action);
  }
}
