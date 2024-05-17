package es.degrassi.forge.core.common.requirement;

import com.google.gson.JsonObject;
import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.core.common.CraftingResult;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.api.core.common.RequirementMode;
import es.degrassi.forge.api.core.common.RequirementType;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.init.RequirementRegistration;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.chat.Component;

@Getter
@Setter
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

  private EnergyComponent component;

  public EnergyRequirement(int amount, RequirementMode mode, String id) {
    this.amount = amount;
    this.mode = mode;
    this.id = id;
  }

  @Override
  public RequirementType<EnergyRequirement> getType() {
    return RequirementRegistration.ENERGY.get();
  }

  @Override
  public RequirementMode getMode() {
    return mode;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public JsonObject toJson(JsonObject json) {
    json.addProperty("energy", amount);
    return json;
  }

  @Override
  public String getTypeString() {
    return "energy";
  }

  @Override
  public CraftingResult processStart() {
    if (component == null || this.mode == null) return CraftingResult.error(Component.literal("Energy Component not found or invalid requirement mode"));
    if (this.mode.isInput() && !this.mode.isPerTick()) {
      if(this.amount == component.extractRecipeEnergy(this.amount, true)) {
        component.extractRecipeEnergy(this.amount, false);
        return CraftingResult.success();
      }
      return CraftingResult.error(Component.literal("Can not extract energy, expected " + this.amount + " FE, but only found " + component.getEnergyStored() + " FE"));
    }
    return CraftingResult.pass();
  }

  @Override
  public CraftingResult processEnd() {
    if (component == null || this.mode == null) return CraftingResult.error(Component.literal("Energy Component not found or invalid requirement mode"));
    if(this.mode.isOutput() && !this.mode.isPerTick()) {
      if (this.amount == component.receiveRecipeEnergy(this.amount, true)) {
        component.receiveRecipeEnergy(this.amount, false);
        return CraftingResult.success();
      }
      return CraftingResult.error(Component.literal("Can not receive energy, not enough space to store " + this.amount + " FE"));
    }
    return CraftingResult.pass();
  }

  @Override
  public CraftingResult processTick() {
    if (component == null || this.mode == null) return CraftingResult.error(Component.literal("Energy Component not found or invalid requirement mode"));
    if (this.mode.isPerTick()) {
      if (this.mode.isInput()) {
        if (this.amount == component.extractRecipeEnergy(this.amount, true)) {
          component.extractRecipeEnergy(this.amount, false);
          return CraftingResult.success();
        }
        return CraftingResult.error(Component.literal("Can not extract energy, expected " + this.amount + " FE, but only found " + component.getEnergyStored() + " FE"));
      } else if(this.mode.isOutput()) {
        if (this.amount == component.receiveRecipeEnergy(this.amount, true)) {
          component.receiveRecipeEnergy(this.amount, false);
          return CraftingResult.success();
        }
        return CraftingResult.error(Component.literal("Can not receive energy, not enough space to store " + this.amount + " FE"));
      }
    }
    return CraftingResult.pass();
  }

  @Override
  public boolean componentMatches(EnergyComponent component) {
    return component instanceof EnergyComponent;
  }

  @Override
  public boolean matches(EnergyComponent component, int recipeTime) {
    if (component == null || this.mode == null) return false;
    if (!componentMatches(component)) return false;
    return switch(getMode()) {
      case INPUT, INPUT_PER_TICK -> {
        if (getMode().isPerTick()) {
          yield component.extractRecipeEnergy(amount * recipeTime, true) == amount * recipeTime;
        } else {
          yield component.extractRecipeEnergy(amount, true) == amount;
        }
      }
      case OUTPUT, OUTPUT_PER_TICK -> {
        if (getMode().isPerTick()) {
          yield component.receiveRecipeEnergy(amount * recipeTime, true) == amount * recipeTime;
        } else {
          yield component.receiveRecipeEnergy(amount, true) == amount;
        }
      }
    };
  }

  @Override
  public NamedCodec<EnergyRequirement> getCodec() {
    return CODEC;
  }

  @Override
  public EnergyRequirement copy() {
    return new EnergyRequirement(amount, mode, id);
  }

  @Override
  public String toString() {
    return asJson().toString();
  }

  public JsonObject asJson() {
    JsonObject json = new JsonObject();
    json.addProperty("type", getTypeString());
    json.addProperty("amount", amount);
    json.addProperty("mode", mode.toString());
    json.addProperty("id", id);
    return json;
  }
}
