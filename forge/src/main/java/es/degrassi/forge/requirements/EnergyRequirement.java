package es.degrassi.forge.requirements;

import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.init.recipe.CraftingResult;
import es.degrassi.forge.init.registration.RequirementRegistry;
import es.degrassi.forge.util.storage.AbstractEnergyStorage;
import net.minecraft.network.chat.Component;

public class EnergyRequirement implements IRequirement<AbstractEnergyStorage> {
  public static final NamedCodec<EnergyRequirement> CODEC = NamedCodec.record(energyRequirementInstance ->
    energyRequirementInstance.group(
      ModeIO.CODEC.fieldOf("mode").forGetter(EnergyRequirement::getMode),
      NamedCodec.INT.fieldOf("amount").forGetter(requirement -> requirement.amount)
    ).apply(energyRequirementInstance, (EnergyRequirement::new)), "Energy requirement"
  );
  private final ModeIO mode;
  private final int amount;

  public EnergyRequirement(ModeIO mode, int amount) {
    this.mode = mode;
    this.amount = amount;
  }

  @Override
  public RequirementType<EnergyRequirement> getType() {
    return RequirementRegistry.ENERGY_REQUIREMENT.get();
  }

  @Override
  public boolean test(AbstractEnergyStorage energy, ICraftingContext context) {
    int amount = (int)context.getIntegerModifiedValue(this.amount, this, null);
    if(getMode() == ModeIO.INPUT)
      return energy.extractEnergy(amount, false) == amount;
    else
      return energy.receiveEnergy(amount, false) == amount;
  }

  @Override
  public CraftingResult processStart(AbstractEnergyStorage energy, ICraftingContext context) {
    int amount = (int)context.getIntegerModifiedValue(this.amount, this, null);
    if(getMode() == ModeIO.INPUT) {
      int canExtract = energy.extractEnergy(amount, true);
      if(canExtract == amount) {
        energy.extractEnergy(amount, false);
        return CraftingResult.success();
      }
      return CraftingResult.error(Component.translatable("degrassi.requirements.energy.error.input", amount, canExtract));
    }
    return CraftingResult.pass();
  }

  @Override
  public CraftingResult processEnd(AbstractEnergyStorage energy, ICraftingContext context) {
    int amount = (int)context.getIntegerModifiedValue(this.amount, this, null);
    if (getMode() == ModeIO.OUTPUT) {
      int canReceive = energy.receiveEnergy(amount, true);
      if(canReceive == amount) {
        energy.receiveEnergy(amount, false);
        return CraftingResult.success();
      }
      return CraftingResult.error(Component.translatable("degrassi.requirements.energy.error.output", amount));
    }
    return CraftingResult.pass();
  }

  public ModeIO getMode() {
    return this.mode;
  }

  public int getAmount() {
    return this.amount;
  }
}
