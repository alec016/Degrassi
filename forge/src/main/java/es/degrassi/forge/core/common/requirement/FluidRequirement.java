package es.degrassi.forge.core.common.requirement;

import com.google.gson.JsonObject;
import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.core.common.CraftingResult;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.api.core.common.RequirementMode;
import es.degrassi.forge.api.core.common.RequirementType;
import es.degrassi.forge.api.impl.codec.RegistrarCodec;
import es.degrassi.forge.core.common.component.FluidComponent;
import es.degrassi.forge.core.init.RequirementRegistration;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.ForgeRegistries;

@Getter
@Setter
public class FluidRequirement implements IRequirement<FluidComponent> {
  public static final NamedCodec<FluidRequirement> CODEC = NamedCodec.record(
    requirement -> requirement.group(
      RegistrarCodec.FLUID.fieldOf("fluid").forGetter(req -> req.fluid),
      NamedCodec.INT.fieldOf("amount").forGetter(req -> req.amount),
      NamedCodec.STRING.fieldOf("id").forGetter(FluidRequirement::getId),
      RequirementMode.CODEC.optionalFieldOf("mode", RequirementMode.INPUT).forGetter(FluidRequirement::getMode)
    ).apply(requirement, FluidRequirement::new),
    "Fluid requirement"
  );

  private final RequirementMode mode;
  private final String id;
  private final Fluid fluid;
  private final int amount;

  private FluidComponent component;

  public FluidRequirement(Fluid fluid, int amount, String id, RequirementMode mode) {
    this.fluid = fluid;
    this.amount = amount;
    this.id = id;
    this.mode = mode;
  }

  @Override
  public JsonObject toJson(JsonObject json) {
    switch (getMode()) {
      case INPUT -> {
        json.addProperty("input", Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fluid)).toString());
        json.addProperty("inputAmount", amount);
      }
      case OUTPUT -> {
        json.addProperty("output", Objects.requireNonNull(ForgeRegistries.FLUIDS.getKey(fluid)).toString());
        json.addProperty("outputAmount", amount);
      }
    }
    return json;
  }

  @Override
  public RequirementType<? extends IRequirement<?>> getType() {
    return RequirementRegistration.FLUID.get();
  }

  @Override
  public boolean componentMatches(FluidComponent component) {
    return component instanceof FluidComponent;
  }

  @Override
  public boolean matches(FluidComponent component, int recipeTime) {
    if (component == null || mode == null) return false;
    if (!componentMatches(component)) return false;
    return switch (getMode()) {
      case INPUT, INPUT_PER_TICK -> {
        if (getMode().isPerTick()) {
          yield component.getFluid().getFluid().isSame(this.fluid) && component.drainRecipe(new FluidStack(this.fluid, this.amount * recipeTime), IFluidHandler.FluidAction.SIMULATE).getAmount() == amount * recipeTime;
        }
        yield component.getFluid().getFluid().isSame(this.fluid) && component.drainRecipe(new FluidStack(this.fluid, this.amount), IFluidHandler.FluidAction.SIMULATE).getAmount() == amount;
      }
      case OUTPUT, OUTPUT_PER_TICK -> {
        if (getMode().isPerTick()) {
          yield component.fillRecipe(new FluidStack(this.fluid, this.amount * recipeTime).copy(), IFluidHandler.FluidAction.SIMULATE) == this.amount * recipeTime;
        }
        yield component.fillRecipe(new FluidStack(this.fluid, this.amount).copy(), IFluidHandler.FluidAction.SIMULATE) == this.amount;
      }
    };
  }

  @Override
  public CraftingResult processTick() {
    if (component == null || mode == null) return CraftingResult.error(Component.literal("No component found or invalid mode"));
    if (!componentMatches(component)) return CraftingResult.error(Component.literal("Component miss match"));
    if (getMode().isPerTick()) {
      if (getMode().isInput()) {
        component.drainRecipe(new FluidStack(this.fluid, this.amount).copy(), IFluidHandler.FluidAction.EXECUTE);
      } else if (getMode().isOutput()) {
        component.fillRecipe(new FluidStack(this.fluid, this.amount).copy(), IFluidHandler.FluidAction.EXECUTE);
      }
      return CraftingResult.success();
    }
    return CraftingResult.pass();
  }

  @Override
  public CraftingResult processStart() {
    if (component == null || mode == null) return CraftingResult.error(Component.literal("No component found or invalid mode"));
    if (!componentMatches(component)) return CraftingResult.error(Component.literal("Component miss match"));
    if (getMode().isPerTick()) return CraftingResult.pass();
    else if (getMode().isInput()) {
      component.drainRecipe(new FluidStack(this.fluid, this.amount), IFluidHandler.FluidAction.EXECUTE);
      return CraftingResult.success();
    }
    return CraftingResult.pass();
  }

  @Override
  public CraftingResult processEnd() {
    if (component == null || mode == null) return CraftingResult.error(Component.literal("No component found or invalid mode"));
    if (!componentMatches(component)) return CraftingResult.error(Component.literal("Component miss match"));
    if (getMode().isPerTick()) return CraftingResult.success();
    else if (getMode().isOutput()) {
      component.fillRecipe(new FluidStack(this.fluid, this.amount), IFluidHandler.FluidAction.EXECUTE);
      return CraftingResult.success();
    }
    return CraftingResult.pass();
  }

  @Override
  public NamedCodec<? extends IRequirement<FluidComponent>> getCodec() {
    return CODEC;
  }

  @Override
  public IRequirement<?> copy() {
    return new FluidRequirement(fluid, amount, id, mode);
  }

  @Override
  public String getTypeString() {
    return "fluid";
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
    json.addProperty("fluid", fluid.getFluidType().getDescription().getString());
    return json;
  }
}
