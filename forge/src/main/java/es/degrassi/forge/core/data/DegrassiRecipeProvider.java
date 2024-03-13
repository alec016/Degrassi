package es.degrassi.forge.core.data;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.core.data.recipe.MachineRecipeGeneratorBuilder;
import java.util.function.Consumer;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

public class DegrassiRecipeProvider extends RecipeProvider implements IConditionBuilder {
  public DegrassiRecipeProvider(PackOutput output) {
    super(output);
  }

  @Override
  public void buildRecipes(@NotNull Consumer<FinishedRecipe> writer) {
    MachineRecipeGeneratorBuilder builder = MachineRecipeGeneratorBuilder.furnace(100);
    builder
      .requireEnergy(500, "energy")
      .produceExperience(0.8f, "experience")
      .requireItem(Items.COAL, "input")
      .produceItem(Items.DIAMOND, "output");
    builder.save(writer, new DegrassiLocation("coal_to_diamond"));
  }
}
