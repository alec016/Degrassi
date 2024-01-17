package es.degrassi.forge.requirements;

import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.ingredient.IIngredient;
import es.degrassi.forge.init.recipe.CraftingResult;
import es.degrassi.forge.init.registration.RequirementRegistry;
import es.degrassi.forge.init.gui.component.FluidComponent;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.NotNull;

public class FluidRequirement implements IRequirement<FluidComponent> {

  public static final NamedCodec<FluidRequirement> CODEC = NamedCodec.record(itemRequirementInstance ->
    itemRequirementInstance.group(
      ModeIO.CODEC.fieldOf("mode").forGetter(FluidRequirement::getMode),
      IIngredient.FLUID.fieldOf("fluid").forGetter(requirement -> requirement.fluidIngredient),
      NamedCodec.INT.fieldOf("amount").forGetter(requirement -> requirement.amount)
    ).apply(itemRequirementInstance, (mode, fluid, amount) -> new FluidRequirement(fluid, amount, mode)), "Fluid requirement"
  );
  private final IIngredient<Fluid> fluidIngredient;
  private final int amount;
  private final ModeIO mode;

  public FluidRequirement(@NotNull IIngredient<Fluid> fluid, int amount, ModeIO mode) {
    this.mode = mode;
    this.fluidIngredient = fluid;
    this.amount = amount;
  }

  @Override
  public RequirementType<? extends IRequirement<?>> getType() {
    return RequirementRegistry.FLUID_REQUIREMENT.get();
  }

  @Override
  public boolean test(FluidComponent handler, ICraftingContext context) {
    return false;
  }

  @Override
  public CraftingResult processStart(FluidComponent handler, ICraftingContext context) {
    return CraftingResult.success();
  }

  @Override
  public CraftingResult processEnd(FluidComponent component, ICraftingContext context) {
    return CraftingResult.success();
  }

  public ModeIO getMode() {
    return mode;
  }

  public IIngredient<Fluid> getFluidIngredient() {
    return fluidIngredient;
  }

  public Fluid getFluid() {
    return fluidIngredient.getAll().get(0);
  }

  public int getFluidAmount() {
    return amount;
  }
}
