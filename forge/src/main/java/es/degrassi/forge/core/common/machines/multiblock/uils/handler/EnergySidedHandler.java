package es.degrassi.forge.core.common.machines.multiblock.uils.handler;

import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.component.EnergyComponent;
import net.minecraft.core.Direction;

public class EnergySidedHandler extends EnergyComponent {
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
