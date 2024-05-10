package es.degrassi.forge.core.common.machines.multiblock.uils.handler;

import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.wrapper.DegrassiFluidHandler;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class FluidSidedHandler extends DegrassiFluidHandler {
  private final DegrassiFluidHandler handler;
  private final Direction extract;
  private final Direction insert;
  private final Direction from;
  public FluidSidedHandler(
    ComponentManager manager,
    DegrassiFluidHandler handler,
    Direction extract,
    Direction insert,
    Direction from
  ) {
    super(manager);
    this.handler = handler;
    this.extract = extract;
    this.insert = insert;
    this.from = from;
  }

  @Override
  public void onContentsChanged() {
    handler.onContentsChanged();
  }

  @Override
  public void markDirty() {
    handler.markDirty();
  }

  @Override
  public void fill(IRequirement<?> requirement) {
    handler.fill(requirement);
  }

  @Override
  public void serialize(CompoundTag nbt) {
    handler.serialize(nbt);
  }

  @Override
  public CompoundTag serialize() {
    return handler.serialize();
  }

  @Override
  public void deserialize(CompoundTag nbt) {
    handler.deserialize(nbt);
  }

  public int toComparatorPower() {
    return handler.toComparatorPower();
  }

  public float subSized() {
    return handler.subSized();
  }

  @Override
  public int fill(FluidStack resource, FluidAction action) {
    return from == insert ? handler.fill(resource, action) : 0;
  }

  @Override
  public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
    return from == extract ? handler.drain(maxDrain, action) : FluidStack.EMPTY;
  }

  @Override
  public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
    return from == extract ? handler.drain(resource, action) : FluidStack.EMPTY;
  }

  public float getFillState() {
    return handler.getFillState();
  }

  // recipe stuff
  public int fillRecipe(FluidStack resource, FluidAction action) {
    return handler.fillRecipe(resource, action);
  }

  public @NotNull FluidStack drainRecipe(int maxDrain, FluidAction action) {
    return handler.drainRecipe(maxDrain, action);
  }

  public @NotNull FluidStack drainRecipe(FluidStack resource, FluidAction action) {
    return handler.drainRecipe(resource, action);
  }
}
