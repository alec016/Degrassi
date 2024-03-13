package es.degrassi.forge.core.common.requirement;

import com.google.gson.JsonObject;
import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.core.common.CraftingResult;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.api.core.common.RequirementMode;
import es.degrassi.forge.api.core.common.RequirementType;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.init.RequirementRegistration;
import net.minecraft.network.chat.Component;

public class EnergyRequirement implements IRequirement<EnergyComponent> {
  public static final NamedCodec<EnergyRequirement> CODEC = NamedCodec.record(
    requirement -> requirement.group(
      NamedCodec.INT.fieldOf("amount").forGetter(req -> req.amount),
      RequirementMode.CODEC.optionalFieldOf("mode", RequirementMode.INPUT).forGetter(EnergyRequirement::getMode),
      NamedCodec.STRING.fieldOf("id").forGetter(EnergyRequirement::getId)
    ).apply(requirement, EnergyRequirement::new),
    "Energy requirement"
  );
  private int amount;
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

  public JsonObject toJson(JsonObject json) {
    json.addProperty("energy", amount);
    return json;
  }

  @Override
  public CraftingResult processStart(IComponent component) {
    if (component == null || this.mode == null) return CraftingResult.error(Component.literal("Energy Component not found or invalid requirement mode"));
    EnergyComponent comp = (EnergyComponent) component;
    if (this.mode.isInput() && !this.mode.isPerTick()) {
      if(this.amount == comp.extractEnergy(this.amount, true)) {
        comp.extractEnergy(this.amount, false);
        return CraftingResult.success();
      }
      return CraftingResult.error(Component.literal("Can not extract energy, expected " + this.amount + " FE, but only found " + comp.getEnergyStored() + " FE"));
    }
    return CraftingResult.pass();
  }

  @Override
  public CraftingResult processEnd(IComponent component) {
    if (component == null || this.mode == null) return CraftingResult.error(Component.literal("Energy Component not found or invalid requirement mode"));
    EnergyComponent comp = (EnergyComponent) component;
    if(this.mode.isOutput() && !this.mode.isPerTick()) {
      if (this.amount == comp.receiveEnergy(this.amount, true)) {
        comp.receiveEnergy(this.amount, false);
        return CraftingResult.success();
      }
      return CraftingResult.error(Component.literal("Can not receive energy, not enough space to store " + this.amount + " FE"));
    }
    return CraftingResult.pass();
  }

  @Override
  public CraftingResult processTick(IComponent component) {
    if (component == null || this.mode == null) return CraftingResult.error(Component.literal("Energy Component not found or invalid requirement mode"));
    EnergyComponent comp = (EnergyComponent) component;
    if (this.mode.isPerTick()) {
      if (this.mode.isInput()) {
        if (this.amount == comp.extractEnergy(this.amount, true)) {
          comp.extractEnergy(this.amount, false);
          return CraftingResult.success();
        }
        return CraftingResult.error(Component.literal("Can not extract energy, expected " + this.amount + " FE, but only found " + comp.getEnergyStored() + " FE"));
      } else if(this.mode.isOutput()) {
        if (this.amount == comp.receiveEnergy(this.amount, true)) {
          comp.receiveEnergy(this.amount, false);
          return CraftingResult.success();
        }
        return CraftingResult.error(Component.literal("Can not receive energy, not enough space to store " + this.amount + " FE"));
      }
    }
    return CraftingResult.pass();
  }

  @Override
  public boolean componentMatches(IComponent component) {
    return component instanceof EnergyComponent;
  }

  @Override
  public boolean matches(IComponent component, int recipeTime) {
    if (component == null || this.mode == null) return false;
    if (!componentMatches(component)) return false;
    EnergyComponent comp = (EnergyComponent) component;
    return switch(getMode()) {
      case INPUT, INPUT_PER_TICK -> {
        if (getMode().isPerTick()) {
          yield comp.extractEnergy(amount * recipeTime, true) == amount * recipeTime;
        } else {
          yield comp.extractEnergy(amount, true) == amount;
        }
      }
      case OUTPUT, OUTPUT_PER_TICK -> {
        if (getMode().isPerTick()) {
          yield comp.receiveEnergy(amount * recipeTime, true) == amount * recipeTime;
        } else {
          yield comp.receiveEnergy(amount, true) == amount;
        }
      }
    };
  }

  @Override
  public NamedCodec<EnergyRequirement> getCodec() {
    return CODEC;
  }

  @Override
  public IRequirement<?> copy() {
    return new EnergyRequirement(amount, mode, id);
  }

  @Override
  public String toString() {
    return "EnergyRequirement{" +
      "amount=" + amount +
      ", mode=" + mode +
      ", id='" + id + '\'' +
      "}";
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }
}
