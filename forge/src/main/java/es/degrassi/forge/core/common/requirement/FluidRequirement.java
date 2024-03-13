package es.degrassi.forge.core.common.requirement;

import com.google.gson.JsonObject;
import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.core.common.CraftingResult;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.api.core.common.RequirementMode;
import es.degrassi.forge.api.core.common.RequirementType;
import es.degrassi.forge.api.impl.codec.RegistrarCodec;
import es.degrassi.forge.core.common.component.FluidComponent;
import es.degrassi.forge.core.init.RequirementRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

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

  public FluidRequirement(Fluid fluid, int amount, String id, RequirementMode mode) {
    this.fluid = fluid;
    this.amount = amount;
    this.id = id;
    this.mode = mode;
  }

  @Override
  public JsonObject toJson(JsonObject json) {
    return json;
  }

  @Override
  public RequirementType<? extends IRequirement<?>> getType() {
    return RequirementRegistration.FLUID.get();
  }

  @Override
  public boolean componentMatches(IComponent component) {
    return component instanceof FluidComponent;
  }

  @Override
  public boolean matches(IComponent component, int recipeTime) {
    if (component == null || mode == null) return false;
    if (!componentMatches(component)) return false;
    FluidComponent fluid = (FluidComponent) component;
    return switch (getMode()) {
      case INPUT, INPUT_PER_TICK -> {
        if (getMode().isPerTick()) {
          yield fluid.getFluid().getFluid().isSame(this.fluid) && fluid.drain(new FluidStack(this.fluid, this.amount * recipeTime), IFluidHandler.FluidAction.SIMULATE).getAmount() == amount * recipeTime;
        }
        yield fluid.getFluid().getFluid().isSame(this.fluid) && fluid.drain(new FluidStack(this.fluid, this.amount), IFluidHandler.FluidAction.SIMULATE).getAmount() == amount;
      }
      case OUTPUT, OUTPUT_PER_TICK -> {
        if (getMode().isPerTick()) {
          yield fluid.fill(new FluidStack(this.fluid, this.amount * recipeTime).copy(), IFluidHandler.FluidAction.SIMULATE) == this.amount * recipeTime;
        }
        yield fluid.fill(new FluidStack(this.fluid, this.amount).copy(), IFluidHandler.FluidAction.SIMULATE) == this.amount;
      }
    };
  }

  @Override
  public CraftingResult processTick(IComponent component) {
    if (component == null || mode == null) return CraftingResult.error(Component.literal("No component found or invalid mode"));
    if (!componentMatches(component)) return CraftingResult.error(Component.literal("Component miss match"));
    FluidComponent fluid = (FluidComponent) component;
    if (getMode().isPerTick()) {
      if (getMode().isInput()) {
        fluid.drain(new FluidStack(this.fluid, this.amount).copy(), IFluidHandler.FluidAction.EXECUTE);
      } else if (getMode().isOutput()) {
        fluid.fill(new FluidStack(this.fluid, this.amount).copy(), IFluidHandler.FluidAction.EXECUTE);
      }
      return CraftingResult.success();
    }
    return CraftingResult.pass();
  }

  @Override
  public CraftingResult processStart(IComponent component) {
    if (component == null || mode == null) return CraftingResult.error(Component.literal("No component found or invalid mode"));
    if (!componentMatches(component)) return CraftingResult.error(Component.literal("Component miss match"));
    FluidComponent fluid = (FluidComponent) component;
    if (getMode().isPerTick()) return CraftingResult.pass();
    else if (getMode().isInput()) {
      fluid.drain(new FluidStack(this.fluid, this.amount), IFluidHandler.FluidAction.EXECUTE);
      return CraftingResult.success();
    }
    return CraftingResult.pass();
  }

  @Override
  public CraftingResult processEnd(IComponent component) {
    if (component == null || mode == null) return CraftingResult.error(Component.literal("No component found or invalid mode"));
    if (!componentMatches(component)) return CraftingResult.error(Component.literal("Component miss match"));
    FluidComponent fluid = (FluidComponent) component;
    if (getMode().isPerTick()) return CraftingResult.success();
    else if (getMode().isOutput()) {
      fluid.fill(new FluidStack(this.fluid, this.amount), IFluidHandler.FluidAction.EXECUTE);
      return CraftingResult.success();
    }
    return CraftingResult.pass();
  }

  @Override
  public NamedCodec<? extends IRequirement<FluidComponent>> getCodec() {
    return CODEC;
  }

  @Override
  public RequirementMode getMode() {
    return mode;
  }

  @Override
  public IRequirement<?> copy() {
    return new FluidRequirement(fluid, amount, id, mode);
  }

  @Override
  public String getId() {
    return id;
  }
}
