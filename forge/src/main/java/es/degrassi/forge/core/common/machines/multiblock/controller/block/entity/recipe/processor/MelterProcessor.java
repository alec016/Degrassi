package es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.recipe.processor;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.common.utils.DegrassiLogger;
import es.degrassi.forge.core.common.RequirementManager;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.MelterControllerEntity;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.recipe.MelterRecipe;
import java.util.ArrayList;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;

public class MelterProcessor extends MultiblockProcessor<MelterRecipe, MelterControllerEntity> {
  public MelterProcessor(MelterControllerEntity entity, boolean reset) {
    super(entity, reset);
  }

  @Override
  protected void init() {
    recipes = new ArrayList<>();
    RequirementManager manager = new RequirementManager();
    manager
      .requireEnergyPerTick(1000, "energy")
      .requireItem(Items.DIAMOND, "input_bus_0_0")
      .produceFluid(Fluids.WATER, 1000, "fluid_output");
    recipes.add(new MelterRecipe(new DegrassiLocation("melter/test"), 100, manager.get()));

    DegrassiLogger.INSTANCE.info("MelterProcessor$recipes: {}", recipes);
  }


}
