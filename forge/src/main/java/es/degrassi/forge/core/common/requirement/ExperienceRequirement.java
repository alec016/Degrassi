package es.degrassi.forge.core.common.requirement;

import com.google.gson.JsonObject;
import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.core.common.CraftingResult;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.api.core.common.RequirementMode;
import es.degrassi.forge.api.core.common.RequirementType;
import es.degrassi.forge.core.common.component.ExperienceComponent;
import es.degrassi.forge.core.init.RequirementRegistration;
import net.minecraft.network.chat.Component;

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
  public boolean componentMatches(IComponent component) {
    return component instanceof ExperienceComponent;
  }

  @Override
  public boolean matches(IComponent component, int recipeTime) {
    if (component == null || mode == null) return false;
    if (!componentMatches(component)) return false;
    ExperienceComponent experience = (ExperienceComponent) component;
    return switch (getMode()) {
      case INPUT, INPUT_PER_TICK -> {
        if (getMode().isPerTick()) {
          yield experience.extractExperience(xp * recipeTime, true) == xp * recipeTime;
        }
        yield experience.extractExperience(xp, true) == xp;
      }
      case OUTPUT, OUTPUT_PER_TICK -> {
        if (getMode().isPerTick()) {
          yield experience.receiveExperience(xp * recipeTime, true) == xp * recipeTime;
        }
        yield experience.receiveExperience(xp, true) == xp;
      }
    };
  }

  @Override
  public CraftingResult processStart(IComponent component) {
    if (component == null || mode == null) return CraftingResult.error(Component.literal("No component found or invalid mode"));
    if (!componentMatches(component)) return CraftingResult.error(Component.literal("Component miss match"));
    ExperienceComponent experience = (ExperienceComponent) component;
    if (getMode().isPerTick()) return CraftingResult.pass();
    else if (getMode().isInput()) {
      experience.extractExperience(xp, false);
      return CraftingResult.success();
    }
    return CraftingResult.pass();
  }

  @Override
  public CraftingResult processEnd(IComponent component) {
    if (component == null || mode == null) return CraftingResult.error(Component.literal("No component found or invalid mode"));
    if (!componentMatches(component)) return CraftingResult.error(Component.literal("Component miss match"));
    ExperienceComponent experience = (ExperienceComponent) component;
    if (getMode().isPerTick()) return CraftingResult.pass();
    else if (getMode().isOutput()) {
      experience.receiveExperience(xp, false);
      return CraftingResult.success();
    }
    return CraftingResult.pass();
  }

  @Override
  public CraftingResult processTick(IComponent component) {
    if (component == null || mode == null) return CraftingResult.error(Component.literal("No component found or invalid mode"));
    if (!componentMatches(component)) return CraftingResult.error(Component.literal("Component miss match"));
    ExperienceComponent experience = (ExperienceComponent) component;
    if (getMode().isPerTick()) {
      if (getMode().isInput()) {
        experience.extractExperience(xp, false);
      } else {
        experience.receiveExperience(xp, false);
      }
      return CraftingResult.success();
    }
    return CraftingResult.pass();
  }

  @Override
  public NamedCodec<ExperienceRequirement> getCodec() {
    return CODEC;
  }

  public float getXp() {
    return xp;
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
  public IRequirement<?> copy() {
    return new ExperienceRequirement(xp, mode, id);
  }

  @Override
  public String toString() {
    return "ExperienceRequirement{" +
      "xp=" + xp +
      ", mode=" + mode +
      ", id='" + id + '\'' +
      "}";
  }
}
