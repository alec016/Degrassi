package es.degrassi.forge.core.common.component;

import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.requirement.FluidRequirement;
import es.degrassi.forge.core.network.component.FluidPacket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
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

  @Override
  public boolean isFluidValid(FluidStack stack) {
    return filter.stream().filter(fluid -> fluid.isSame(stack.getFluid())).findFirst().map(i -> whitelist).orElse(stack.isFluidEqual(getFluid()) || getFluid().isEmpty());
  }

  @Override
  public void onContentsChanged() {
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
  public void fill(IRequirement<?> requirement) {
    if (requirement instanceof FluidRequirement req) {
      this.fluid = new FluidStack(req.getFluid(), req.getAmount());
      markDirty();
    }
  }

  @Override
  public void serialize(CompoundTag nbt) {
  }

  public CompoundTag serialize() {
    CompoundTag tag = new CompoundTag();
    super.writeToNBT(tag);
    tag.putInt("capacity", capacity);
    tag.putString("mode", mode.serialize());
    tag.putString("id", id);
    return tag;
  }

  @Override
  public void deserialize(CompoundTag nbt) {
    super.readFromNBT(nbt);
    capacity = nbt.getInt("capacity");
    mode = ComponentIOMode.deserialize(nbt.getString("mode"));
  }

  public int toComparatorPower() {
    return (int) (subSized() * 15);
  }

  public float subSized() {
    return this.capacity > 0 ? (float) this.getFluidAmount() / this.capacity : 0;
  }

  @Override
  public int fill(FluidStack resource, FluidAction action) {
    if (mode.output()) return 0;
    return super.fill(resource, action);
  }

  @Override
  public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
    if (mode.input()) return FluidStack.EMPTY;
    return super.drain(maxDrain, action);
  }

  @Override
  public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
    if (mode.input()) return FluidStack.EMPTY;
    return super.drain(resource, action);
  }

  public float getFillState() {
    return this.getFluidAmount() / (float) this.capacity;
  }

  // recipe stuff
  public int fillRecipe(FluidStack resource, FluidAction action) {
    return super.fill(resource, action);
  }

  public @NotNull FluidStack drainRecipe(int maxDrain, FluidAction action) {
    return super.drain(maxDrain, action);
  }

  public @NotNull FluidStack drainRecipe(FluidStack resource, FluidAction action) {
    return super.drain(resource, action);
  }

  @Override
  public String getTypeString() {
    return "fluid";
  }
}
