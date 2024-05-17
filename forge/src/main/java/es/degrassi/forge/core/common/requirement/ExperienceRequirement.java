package es.degrassi.forge.core.common.requirement;

import com.google.gson.JsonObject;
import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.core.common.CraftingResult;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.api.core.common.RequirementMode;
import es.degrassi.forge.api.core.common.RequirementType;
import es.degrassi.forge.core.common.component.ExperienceComponent;
import es.degrassi.forge.core.init.RequirementRegistration;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.chat.Component;

@Getter
@Setter
public class ExperienceRequirement implements IRequirement<ExperienceComponent> {
  public static final NamedCodec<ExperienceRequirement> CODEC = NamedCodec.record(
    requirementInstance -> requirementInstance.group(
      NamedCodec.FLOAT.fieldOf("amount").forGetter(ExperienceRequirement::getXp),
      RequirementMode.CODEC.optionalFieldOf("mode", RequirementMode.INPUT).forGetter(ExperienceRequirement::getMode),
      NamedCodec.STRING.fieldOf("id").forGetter(ExperienceRequirement::getId)
    ).apply(requirementInstance, ExperienceRequirement::new),
    "Experience requirement"
  );
  private final float xp;
  private final RequirementMode mode;
  private final String id;

  private ExperienceComponent component;

  public ExperienceRequirement(float amount, RequirementMode mode, String id) {
    this.xp = amount;
    this.mode = mode;
    this.id = id;
  }

  @Override
  public JsonObject toJson(JsonObject json) {
    json.addProperty("experience", xp);
    return json;
  }

  @Override
  public RequirementType<ExperienceRequirement> getType() {
    return RequirementRegistration.EXPERIENCE.get();
  }

  @Override
  public boolean componentMatches(ExperienceComponent component) {
    return component instanceof ExperienceComponent;
  }

  @Override
  public boolean matches(ExperienceComponent component, int recipeTime) {
    if (component == null || mode == null) return false;
    if (!componentMatches(component)) return false;
    return switch (getMode()) {
      case INPUT, INPUT_PER_TICK -> {
        if (getMode().isPerTick()) {
          yield component.extractRecipeExperience(xp * recipeTime, true) == xp * recipeTime;
        }
        yield component.extractRecipeExperience(xp, true) == xp;
      }
      case OUTPUT, OUTPUT_PER_TICK -> {
        if (getMode().isPerTick()) {
          yield component.receiveRecipeExperience(xp * recipeTime, true) == xp * recipeTime;
        }
        yield component.receiveRecipeExperience(xp, true) == xp;
      }
    };
  }

  @Override
  public CraftingResult processStart() {
    if (component == null || mode == null) return CraftingResult.error(Component.literal("No component found or invalid mode"));
    if (!componentMatches(component)) return CraftingResult.error(Component.literal("Component miss match"));
    if (getMode().isPerTick()) return CraftingResult.pass();
    else if (getMode().isInput()) {
      component.extractRecipeExperience(xp, false);
      return CraftingResult.success();
    }
    return CraftingResult.pass();
  }

  @Override
  public CraftingResult processEnd() {
    if (component == null || mode == null) return CraftingResult.error(Component.literal("No component found or invalid mode"));
    if (!componentMatches(component)) return CraftingResult.error(Component.literal("Component miss match"));
    if (getMode().isPerTick()) return CraftingResult.pass();
    else if (getMode().isOutput()) {
      component.receiveRecipeExperience(xp, false);
      return CraftingResult.success();
    }
    return CraftingResult.pass();
  }

  @Override
  public CraftingResult processTick() {
    if (component == null || mode == null) return CraftingResult.error(Component.literal("No component found or invalid mode"));
    if (!componentMatches(component)) return CraftingResult.error(Component.literal("Component miss match"));
    if (getMode().isPerTick()) {
      if (getMode().isInput()) {
        component.extractRecipeExperience(xp, false);
      } else {
        component.receiveRecipeExperience(xp, false);
      }
      return CraftingResult.success();
    }
    return CraftingResult.pass();
  }

  @Override
  public NamedCodec<ExperienceRequirement> getCodec() {
    return CODEC;
  }

  @Override
  public ExperienceRequirement copy() {
    return new ExperienceRequirement(xp, mode, id);
  }

  @Override
  public String getTypeString() {
    return "experience";
  }


  @Override
  public String toString() {
    return asJson().toString();
  }

  public JsonObject asJson() {
    JsonObject json = new JsonObject();
    json.addProperty("type", getTypeString());
    json.addProperty("amount", xp);
    json.addProperty("mode", mode.toString());
    json.addProperty("id", id);
    return json;
  }
}
