package es.degrassi.forge.core.common.machines.multiblock.uils.handler;

import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.wrapper.DegrassiFluidHandler;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class FluidSidedHandler extends DegrassiFluidHandler {
  public static Map<Direction, LazyOptional<FluidSidedHandler>> DEFAULT_ALL_ENABLED(ComponentManager manager, DegrassiFluidHandler component){
    return Map.of(
      Direction.UP, LazyOptional.of(() -> new FluidSidedHandler(manager, component, Direction.UP, Direction.UP, Direction.UP)),
      Direction.DOWN, LazyOptional.of(() -> new FluidSidedHandler(manager, component, Direction.DOWN, Direction.DOWN, Direction.DOWN)),
      Direction.NORTH, LazyOptional.of(() -> new FluidSidedHandler(manager, component, Direction.NORTH, Direction.NORTH, Direction.NORTH)),
      Direction.SOUTH, LazyOptional.of(() -> new FluidSidedHandler(manager, component, Direction.SOUTH, Direction.SOUTH, Direction.SOUTH)),
      Direction.EAST, LazyOptional.of(() -> new FluidSidedHandler(manager, component, Direction.EAST, Direction.EAST, Direction.EAST)),
      Direction.WEST, LazyOptional.of(() -> new FluidSidedHandler(manager, component, Direction.WEST, Direction.WEST, Direction.WEST))
    );
  }
  public static Map<Direction, LazyOptional<FluidSidedHandler>> DEFAULT_ALL_EXTRACT(ComponentManager manager, DegrassiFluidHandler component) {
    return Map.of(
      Direction.UP, LazyOptional.of(() -> new FluidSidedHandler(manager, component, Direction.UP, null, Direction.UP)),
      Direction.DOWN, LazyOptional.of(() -> new FluidSidedHandler(manager, component, Direction.DOWN, null, Direction.DOWN)),
      Direction.NORTH, LazyOptional.of(() -> new FluidSidedHandler(manager, component, Direction.NORTH, null, Direction.NORTH)),
      Direction.SOUTH, LazyOptional.of(() -> new FluidSidedHandler(manager, component, Direction.SOUTH, null, Direction.SOUTH)),
      Direction.EAST, LazyOptional.of(() -> new FluidSidedHandler(manager, component, Direction.EAST, null, Direction.EAST)),
      Direction.WEST, LazyOptional.of(() -> new FluidSidedHandler(manager, component, Direction.WEST, null, Direction.WEST))
    );
  }
  public static Map<Direction, LazyOptional<FluidSidedHandler>> DEFAULT_ALL_INSERT(ComponentManager manager, DegrassiFluidHandler component) {
    return Map.of(
      Direction.UP, LazyOptional.of(() -> new FluidSidedHandler(manager, component, null, Direction.UP, Direction.UP)),
      Direction.DOWN, LazyOptional.of(() -> new FluidSidedHandler(manager, component, null, Direction.DOWN, Direction.DOWN)),
      Direction.NORTH, LazyOptional.of(() -> new FluidSidedHandler(manager, component, null, Direction.NORTH, Direction.NORTH)),
      Direction.SOUTH, LazyOptional.of(() -> new FluidSidedHandler(manager, component, null, Direction.SOUTH, Direction.SOUTH)),
      Direction.EAST, LazyOptional.of(() -> new FluidSidedHandler(manager, component, null, Direction.EAST, Direction.EAST)),
      Direction.WEST, LazyOptional.of(() -> new FluidSidedHandler(manager, component, null, Direction.WEST, Direction.WEST))
    );
  }

  public static Map<Direction, LazyOptional<FluidSidedHandler>> DEFAULT_ALL_ENABLED(ComponentManager manager, DegrassiFluidHandler component, Direction from){
    return Map.of(
      Direction.UP, LazyOptional.of(() -> new FluidSidedHandler(manager, component, Direction.UP, Direction.UP, from)),
      Direction.DOWN, LazyOptional.of(() -> new FluidSidedHandler(manager, component, Direction.DOWN, Direction.DOWN, from)),
      Direction.NORTH, LazyOptional.of(() -> new FluidSidedHandler(manager, component, Direction.NORTH, Direction.NORTH, from)),
      Direction.SOUTH, LazyOptional.of(() -> new FluidSidedHandler(manager, component, Direction.SOUTH, Direction.SOUTH, from)),
      Direction.EAST, LazyOptional.of(() -> new FluidSidedHandler(manager, component, Direction.EAST, Direction.EAST, from)),
      Direction.WEST, LazyOptional.of(() -> new FluidSidedHandler(manager, component, Direction.WEST, Direction.WEST, from))
    );
  }
  public static Map<Direction, LazyOptional<FluidSidedHandler>> DEFAULT_ALL_EXTRACT(ComponentManager manager, DegrassiFluidHandler component, Direction from) {
    return Map.of(
      Direction.UP, LazyOptional.of(() -> new FluidSidedHandler(manager, component, Direction.UP, null, from)),
      Direction.DOWN, LazyOptional.of(() -> new FluidSidedHandler(manager, component, Direction.DOWN, null, from)),
      Direction.NORTH, LazyOptional.of(() -> new FluidSidedHandler(manager, component, Direction.NORTH, null, from)),
      Direction.SOUTH, LazyOptional.of(() -> new FluidSidedHandler(manager, component, Direction.SOUTH, null, from)),
      Direction.EAST, LazyOptional.of(() -> new FluidSidedHandler(manager, component, Direction.EAST, null, from)),
      Direction.WEST, LazyOptional.of(() -> new FluidSidedHandler(manager, component, Direction.WEST, null, from))
    );
  }
  public static Map<Direction, LazyOptional<FluidSidedHandler>> DEFAULT_ALL_INSERT(ComponentManager manager, DegrassiFluidHandler component, Direction from) {
    return Map.of(
      Direction.UP, LazyOptional.of(() -> new FluidSidedHandler(manager, component, null, Direction.UP, from)),
      Direction.DOWN, LazyOptional.of(() -> new FluidSidedHandler(manager, component, null, Direction.DOWN, from)),
      Direction.NORTH, LazyOptional.of(() -> new FluidSidedHandler(manager, component, null, Direction.NORTH, from)),
      Direction.SOUTH, LazyOptional.of(() -> new FluidSidedHandler(manager, component, null, Direction.SOUTH, from)),
      Direction.EAST, LazyOptional.of(() -> new FluidSidedHandler(manager, component, null, Direction.EAST, from)),
      Direction.WEST, LazyOptional.of(() -> new FluidSidedHandler(manager, component, null, Direction.WEST, from))
    );
  }

  private final DegrassiFluidHandler handler;
  private final Direction extract, insert, from;

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
  public int getTanks() {
    return handler.getTanks();
  }

  @Override
  public @NotNull FluidStack getFluidInTank(int i) {
    return handler.getFluidInTank(i);
  }

  @Override
  public int getTankCapacity(int i) {
    return handler.getTankCapacity(i);
  }

  @Override
  public boolean isFluidValid(int i, @NotNull FluidStack fluidStack) {
    return handler.isFluidValid(i, fluidStack);
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
