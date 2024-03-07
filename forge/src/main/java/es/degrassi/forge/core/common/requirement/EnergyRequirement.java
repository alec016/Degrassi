package es.degrassi.forge.core.common.requirement;

import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.core.common.CraftingResult;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.api.core.common.RequirementMode;
import es.degrassi.forge.api.core.common.RequirementType;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.init.RequirementRegistration;

public class EnergyRequirement implements IRequirement<EnergyComponent> {
  public static final NamedCodec<EnergyRequirement> CODEC = NamedCodec.record(
    requirement -> requirement.group(
      NamedCodec.INT.fieldOf("amount").forGetter(req -> req.amount),
      RequirementMode.CODEC.fieldOf("mode").forGetter(EnergyRequirement::getMode)
    ).apply(requirement, EnergyRequirement::new),
    "Energy requirement"
  );
  private final int amount;
  private final RequirementMode mode;
  public EnergyRequirement(int amount, RequirementMode mode) {
    this.amount = amount;
    this.mode = mode;
  }

  @Override
  public RequirementType<EnergyRequirement> getType() {
    return RequirementRegistration.ENERGY.get();
  }

  public RequirementMode getMode() {
    return mode;
  }

  @Override
  public CraftingResult processStart(EnergyComponent component) {
    if (component == null || this.mode == null) return CraftingResult.ERROR;
    if (this.mode.isInput() && !this.mode.isPerTick()) {
      if(this.amount == component.extractEnergy(this.amount, true)) {
        component.extractEnergy(this.amount, false);
        return CraftingResult.SUCCESS;
      }
      return CraftingResult.ERROR;
    }
    return CraftingResult.PASS;
  }

  @Override
  public CraftingResult processEnd(EnergyComponent component) {
    if (component == null || this.mode == null) return CraftingResult.ERROR;
    if(this.mode.isOutput() && !this.mode.isPerTick()) {
      if (this.amount == component.extractEnergy(this.amount, true)) {
        component.extractEnergy(this.amount, false);
        return CraftingResult.SUCCESS;
      }
      return CraftingResult.ERROR;
    }
    return CraftingResult.PASS;
  }

  @Override
  public CraftingResult processTick(EnergyComponent component) {
    if (component == null || this.mode == null) return CraftingResult.ERROR;
    if (this.mode.isPerTick()) {
      if (this.mode.isInput()) {
        if (this.amount == component.receiveEnergy(this.amount, true)) {
          component.receiveEnergy(this.amount, false);
          return CraftingResult.SUCCESS;
        }
        return CraftingResult.ERROR;
      } else if(this.mode.isOutput()) {
        if (this.amount == component.extractEnergy(this.amount, true)) {
          component.extractEnergy(this.amount, false);
          return CraftingResult.SUCCESS;
        }
        return CraftingResult.ERROR;
      }
    }
    return CraftingResult.PASS;
  }

  @Override
  public boolean matches(EnergyComponent component) {
    if (component == null || this.mode == null) return false;
    return component.getEnergyStored() >= amount;
  }

  @Override
  public NamedCodec<? extends IRequirement<EnergyComponent>> getCodec() {
    return CODEC;
  }
}
