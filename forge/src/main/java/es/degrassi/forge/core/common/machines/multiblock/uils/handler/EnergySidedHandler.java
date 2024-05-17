package es.degrassi.forge.core.common.machines.multiblock.uils.handler;

import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.component.EnergyComponent;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraftforge.common.util.LazyOptional;

public class EnergySidedHandler extends EnergyComponent {
  public static Map<Direction, LazyOptional<EnergySidedHandler>> DEFAULT_ALL_ENABLED(ComponentManager manager, EnergyComponent component){
    return Map.of(
      Direction.UP, LazyOptional.of(() -> new EnergySidedHandler(manager, component, Direction.UP, Direction.UP, Direction.UP)),
      Direction.DOWN, LazyOptional.of(() -> new EnergySidedHandler(manager, component, Direction.DOWN, Direction.DOWN, Direction.DOWN)),
      Direction.NORTH, LazyOptional.of(() -> new EnergySidedHandler(manager, component, Direction.NORTH, Direction.NORTH, Direction.NORTH)),
      Direction.SOUTH, LazyOptional.of(() -> new EnergySidedHandler(manager, component, Direction.SOUTH, Direction.SOUTH, Direction.SOUTH)),
      Direction.EAST, LazyOptional.of(() -> new EnergySidedHandler(manager, component, Direction.EAST, Direction.EAST, Direction.EAST)),
      Direction.WEST, LazyOptional.of(() -> new EnergySidedHandler(manager, component, Direction.WEST, Direction.WEST, Direction.WEST))
    );
  }
  public static Map<Direction, LazyOptional<EnergySidedHandler>> DEFAULT_ALL_EXTRACT(ComponentManager manager, EnergyComponent component) {
    return Map.of(
      Direction.UP, LazyOptional.of(() -> new EnergySidedHandler(manager, component, Direction.UP, null, Direction.UP)),
      Direction.DOWN, LazyOptional.of(() -> new EnergySidedHandler(manager, component, Direction.DOWN, null, Direction.DOWN)),
      Direction.NORTH, LazyOptional.of(() -> new EnergySidedHandler(manager, component, Direction.NORTH, null, Direction.NORTH)),
      Direction.SOUTH, LazyOptional.of(() -> new EnergySidedHandler(manager, component, Direction.SOUTH, null, Direction.SOUTH)),
      Direction.EAST, LazyOptional.of(() -> new EnergySidedHandler(manager, component, Direction.EAST, null, Direction.EAST)),
      Direction.WEST, LazyOptional.of(() -> new EnergySidedHandler(manager, component, Direction.WEST, null, Direction.WEST))
    );
  }
  public static Map<Direction, LazyOptional<EnergySidedHandler>> DEFAULT_ALL_INSERT(ComponentManager manager, EnergyComponent component) {
    return Map.of(
      Direction.UP, LazyOptional.of(() -> new EnergySidedHandler(manager, component, null, Direction.UP, Direction.UP)),
      Direction.DOWN, LazyOptional.of(() -> new EnergySidedHandler(manager, component, null, Direction.DOWN, Direction.DOWN)),
      Direction.NORTH, LazyOptional.of(() -> new EnergySidedHandler(manager, component, null, Direction.NORTH, Direction.NORTH)),
      Direction.SOUTH, LazyOptional.of(() -> new EnergySidedHandler(manager, component, null, Direction.SOUTH, Direction.SOUTH)),
      Direction.EAST, LazyOptional.of(() -> new EnergySidedHandler(manager, component, null, Direction.EAST, Direction.EAST)),
      Direction.WEST, LazyOptional.of(() -> new EnergySidedHandler(manager, component, null, Direction.WEST, Direction.WEST))
    );
  }


  public static Map<Direction, LazyOptional<EnergySidedHandler>> DEFAULT_ALL_ENABLED(ComponentManager manager, EnergyComponent component, Direction from){
    return Map.of(
      Direction.UP, LazyOptional.of(() -> new EnergySidedHandler(manager, component, Direction.UP, Direction.UP, from)),
      Direction.DOWN, LazyOptional.of(() -> new EnergySidedHandler(manager, component, Direction.DOWN, Direction.DOWN, from)),
      Direction.NORTH, LazyOptional.of(() -> new EnergySidedHandler(manager, component, Direction.NORTH, Direction.NORTH, from)),
      Direction.SOUTH, LazyOptional.of(() -> new EnergySidedHandler(manager, component, Direction.SOUTH, Direction.SOUTH, from)),
      Direction.EAST, LazyOptional.of(() -> new EnergySidedHandler(manager, component, Direction.EAST, Direction.EAST, from)),
      Direction.WEST, LazyOptional.of(() -> new EnergySidedHandler(manager, component, Direction.WEST, Direction.WEST, from))
    );
  }
  public static Map<Direction, LazyOptional<EnergySidedHandler>> DEFAULT_ALL_EXTRACT(ComponentManager manager, EnergyComponent component, Direction from) {
    return Map.of(
      Direction.UP, LazyOptional.of(() -> new EnergySidedHandler(manager, component, Direction.UP, null, from)),
      Direction.DOWN, LazyOptional.of(() -> new EnergySidedHandler(manager, component, Direction.DOWN, null, from)),
      Direction.NORTH, LazyOptional.of(() -> new EnergySidedHandler(manager, component, Direction.NORTH, null, from)),
      Direction.SOUTH, LazyOptional.of(() -> new EnergySidedHandler(manager, component, Direction.SOUTH, null, from)),
      Direction.EAST, LazyOptional.of(() -> new EnergySidedHandler(manager, component, Direction.EAST, null, from)),
      Direction.WEST, LazyOptional.of(() -> new EnergySidedHandler(manager, component, Direction.WEST, null, from))
    );
  }
  public static Map<Direction, LazyOptional<EnergySidedHandler>> DEFAULT_ALL_INSERT(ComponentManager manager, EnergyComponent component, Direction from) {
    return Map.of(
      Direction.UP, LazyOptional.of(() -> new EnergySidedHandler(manager, component, null, Direction.UP, from)),
      Direction.DOWN, LazyOptional.of(() -> new EnergySidedHandler(manager, component, null, Direction.DOWN, from)),
      Direction.NORTH, LazyOptional.of(() -> new EnergySidedHandler(manager, component, null, Direction.NORTH, from)),
      Direction.SOUTH, LazyOptional.of(() -> new EnergySidedHandler(manager, component, null, Direction.SOUTH, from)),
      Direction.EAST, LazyOptional.of(() -> new EnergySidedHandler(manager, component, null, Direction.EAST, from)),
      Direction.WEST, LazyOptional.of(() -> new EnergySidedHandler(manager, component, null, Direction.WEST, from))
    );
  }

  private final EnergyComponent handler;
  private final Direction extract;
  private final Direction insert;
  private final Direction from;

  public EnergySidedHandler(
    ComponentManager manager,
    EnergyComponent handler,
    Direction extract,
    Direction insert,
    Direction from
  ) {
    super(manager, handler.getMaxEnergyStored(), handler.getMaxInput(), handler.getMaxOutput(), handler.getEntity(), handler.getId(), handler.getMode());
    this.handler = handler;
    this.extract = extract;
    this.insert = insert;
    this.from = from;
  }

  @Override
  public int receiveEnergy(int energy, boolean simulate) {
    return handler.receiveEnergy(energy, simulate);
  }

  @Override
  public int extractEnergy(int energy, boolean simulate) {
    return handler.extractEnergy(energy, simulate);
  }

  @Override
  public boolean canExtract() {
    return from == extract && getMode().outputWithAll() && getMaxOutput() > 0;
  }

  @Override
  public boolean canReceive() {
    return from == insert && getMode().inputWillAll() && getMaxInput() > 0;
  }

  @Override
  public void fill(IRequirement<?> requirement) {
    handler.fill(requirement);
  }

  public int toComparatorPower() {
    return handler.toComparatorPower();
  }

  public float subSized() {
    return handler.subSized();
  }

  public EnergyComponent setCapacity(int capacity) {
    handler.setCapacity(capacity);
    return this;
  }

  // recipe stuff
  public int receiveRecipeEnergy(int energy, boolean simulate) {
    return handler.receiveRecipeEnergy(energy, simulate);
  }

  public int extractRecipeEnergy(int energy, boolean simulate) {
    return handler.extractRecipeEnergy(energy, simulate);
  }
}
