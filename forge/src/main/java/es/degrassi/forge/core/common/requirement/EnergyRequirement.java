package es.degrassi.forge.core.common.requirement;

import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.core.common.CraftingResult;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.api.core.common.RequirementMode;
import es.degrassi.forge.api.core.common.RequirementType;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.init.RequirementRegistration;

public class EnergyRequirement implements IRequirement<EnergyComponent> {
  public static final NamedCodec<EnergyRequirement> CODEC = NamedCodec.record(
    requirement -> requirement.group(
      NamedCodec.INT.fieldOf("amount").forGetter(req -> req.amount),
      RequirementMode.CODEC.fieldOf("mode").forGetter(EnergyRequirement::getMode),
      NamedCodec.STRING.fieldOf("id").forGetter(EnergyRequirement::getId)
    ).apply(requirement, EnergyRequirement::new),
    "Energy requirement"
  );
  private final int amount;
  private final RequirementMode mode;
  private final String id;
  public EnergyRequirement(int amount, RequirementMode mode, String id) {
    this.amount = amount;
    this.mode = mode;
    this.id = id;
  }

  @Override
  public RequirementType<EnergyRequirement> getType() {
    return RequirementRegistration.ENERGY.get();
  }

  public RequirementMode getMode() {
    return mode;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public CraftingResult processStart(IComponent component) {
    if (component == null || this.mode == null) return CraftingResult.ERROR;
    EnergyComponent comp = (EnergyComponent) component;
    if (this.mode.isInput() && !this.mode.isPerTick()) {
      if(this.amount == comp.extractEnergy(this.amount, true)) {
        comp.extractEnergy(this.amount, false);
        return CraftingResult.SUCCESS;
      }
      return CraftingResult.ERROR;
    }
    return CraftingResult.PASS;
  }

  @Override
  public CraftingResult processEnd(IComponent component) {
    if (component == null || this.mode == null) return CraftingResult.ERROR;
    EnergyComponent comp = (EnergyComponent) component;
    if(this.mode.isOutput() && !this.mode.isPerTick()) {
      if (this.amount == comp.extractEnergy(this.amount, true)) {
        comp.extractEnergy(this.amount, false);
        return CraftingResult.SUCCESS;
      }
      return CraftingResult.ERROR;
    }
    return CraftingResult.PASS;
  }

  @Override
  public CraftingResult processTick(IComponent component) {
    if (component == null || this.mode == null) return CraftingResult.ERROR;
    EnergyComponent comp = (EnergyComponent) component;
    if (this.mode.isPerTick()) {
      if (this.mode.isInput()) {
        if (this.amount == comp.extractEnergy(this.amount, true)) {
          comp.extractEnergy(this.amount, false);
          return CraftingResult.SUCCESS;
        }
        return CraftingResult.ERROR;
      } else if(this.mode.isOutput()) {
        if (this.amount == comp.receiveEnergy(this.amount, true)) {
          comp.receiveEnergy(this.amount, false);
          return CraftingResult.SUCCESS;
        }
        return CraftingResult.ERROR;
      }
    }
    return CraftingResult.PASS;
  }

  @Override
  public boolean componentMatches(IComponent component) {
    return component instanceof EnergyComponent;
  }

  @Override
  public boolean matches(IComponent component) {
    if (component == null || this.mode == null) return false;
    EnergyComponent comp = (EnergyComponent) component;
    return comp.getEnergyStored() >= amount;
  }

  @Override
  public NamedCodec<? extends IRequirement<EnergyComponent>> getCodec() {
    return CODEC;
  }
}
