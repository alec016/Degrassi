package es.degrassi.forge.data;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.data.recipes.DegrassiFurnaceRecipeBuilder;
import es.degrassi.forge.data.recipes.DegrassiMelterRecipeBuilder;
import es.degrassi.forge.data.recipes.DegrassiUpgradeMakerRecipeBuilder;
import es.degrassi.forge.init.registration.ItemRegister;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class DegrassiRecipeProvider extends RecipeProvider implements IConditionBuilder {
  public DegrassiRecipeProvider(DataGenerator arg) {
    super(arg);
  }

  @Override
  protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> writer) {
    addFurnaceRecipes(writer);
    addMelterRecipes(writer);
    addUpgradeMakerRecipes(writer);
  }

  private void addFurnaceRecipes(@NotNull Consumer<FinishedRecipe> writer){
    addFurnaceRecipe(Items.COAL, Items.DIAMOND, 100, 20, 10.0f)
      .save(writer, new DegrassiLocation("furnace/diamond_from_coal"));
  }

  private DegrassiFurnaceRecipeBuilder addFurnaceRecipe(Item input, Item output, int inputC, int outputC, int energy, int time, float xp) {
    return new DegrassiFurnaceRecipeBuilder(output, outputC)
      .energy(energy)
      .xp(xp)
      .time(time)
      .input(input, inputC);
  }

  private DegrassiFurnaceRecipeBuilder addFurnaceRecipe(Item input, Item output, int energy, int time, float xp) {
    return addFurnaceRecipe(input, output, 1, 1, energy, time, xp);
  }

  private DegrassiFurnaceRecipeBuilder addFurnaceRecipe(Item input, int inputC, Item output, int energy, int time, float xp) {
    return addFurnaceRecipe(input, output, inputC, 1, energy, time, xp);
  }

  private DegrassiFurnaceRecipeBuilder addFurnaceRecipe(Item input, Item output, int outputC, int energy, int time, float xp) {
    return addFurnaceRecipe(input, output, 1, outputC, energy, time, xp);
  }

  private void addMelterRecipes(@NotNull Consumer<FinishedRecipe> writer) {
    addMelterRecipe(Fluids.LAVA, 500, 1000, 1000, Items.COBBLESTONE)
      .save(writer, new DegrassiLocation("melter/cobblestone_to_lava"));
    addMelterRecipe(Fluids.LAVA, 1000, 1000, Items.STONE)
      .save(writer, new DegrassiLocation("melter/stone_to_lava"));
  }

  private DegrassiMelterRecipeBuilder addMelterRecipe(Fluid output, int outputAmount, int energy, int time, Item input, int inputAmount) {
    return new DegrassiMelterRecipeBuilder(output, outputAmount)
      .energy(energy)
      .time(time)
      .input(input, inputAmount);
  }

  private DegrassiMelterRecipeBuilder addMelterRecipe(Fluid output, int outputAmount, int energy, int time, Item input) {
    return addMelterRecipe(output, outputAmount, energy, time, input, 1);
  }
  private DegrassiMelterRecipeBuilder addMelterRecipe(Fluid output, int energy, int time, Item input, int inputAmount) {
    return addMelterRecipe(output, 1000, energy, time, input, inputAmount);
  }
  private DegrassiMelterRecipeBuilder addMelterRecipe(Fluid output, int energy, int time, Item input) {
    return addMelterRecipe(output, 1000, energy, time, input, 1);
  }
  private void addUpgradeMakerRecipes(@NotNull Consumer<FinishedRecipe> writer) {
    addUpgradeMakerRecipe(
      ItemRegister.SPEED_UPGRADE.get(),
      2000,
      5000,
      ItemRegister.UPGRADE_BASE.get(),
      ItemRegister.GOLD_COIN.get(),
      Fluids.LAVA
    ).save(writer, new DegrassiLocation("upgrade_maker/speed_upgrade"));
  }

  private DegrassiUpgradeMakerRecipeBuilder addUpgradeMakerRecipe(
    Item result,
    int count,
    int time,
    int energy,
    Item input1,
    int input1Amount,
    Item input2,
    int input2Amount,
    Fluid inputFluid,
    int fluidAmount
  ) {
    return new DegrassiUpgradeMakerRecipeBuilder(result, count)
      .time(time)
      .energy(energy)
      .input1(input1, input1Amount)
      .input2(input2, input2Amount)
      .fluid(inputFluid, fluidAmount);
  }

  private DegrassiUpgradeMakerRecipeBuilder addUpgradeMakerRecipe(
    Item result,
    int time,
    int energy,
    Item input1,
    int input1Amount,
    Item input2,
    int input2Amount,
    Fluid inputFluid,
    int fluidAmount
  ) {
    return addUpgradeMakerRecipe(
      result, 1, time, energy, input1, input1Amount, input2, input2Amount, inputFluid, fluidAmount
    );
  }
  private DegrassiUpgradeMakerRecipeBuilder addUpgradeMakerRecipe(
    Item result,
    int time,
    int energy,
    Item input1,
    Item input2,
    Fluid inputFluid,
    int fluidAmount
  ) {
    return addUpgradeMakerRecipe(
      result, 1, time, energy, input1, 1, input2, 1, inputFluid, fluidAmount
    );
  }

  private DegrassiUpgradeMakerRecipeBuilder addUpgradeMakerRecipe(
    Item result,
    int time,
    int energy,
    Item input1,
    int input1Amount,
    Item input2,
    Fluid inputFluid,
    int fluidAmount
  ) {
    return addUpgradeMakerRecipe(
      result, 1, time, energy, input1, input1Amount, input2, 1, inputFluid, fluidAmount
    );
  }
  private DegrassiUpgradeMakerRecipeBuilder addUpgradeMakerRecipe(
    Item result,
    int time,
    int energy,
    Item input1,
    Item input2,
    int input2Amount,
    Fluid inputFluid,
    int fluidAmount
  ) {
    return addUpgradeMakerRecipe(
      result, 1, time, energy, input1, 1, input2, input2Amount, inputFluid, fluidAmount
    );
  }
  private DegrassiUpgradeMakerRecipeBuilder addUpgradeMakerRecipe(
    Item result,
    int count,
    int time,
    int energy,
    Item input1,
    Item input2,
    Fluid inputFluid,
    int fluidAmount
  ) {
    return addUpgradeMakerRecipe(
      result, count, time, energy, input1, 1, input2, 1, inputFluid, fluidAmount
    );
  }
  private DegrassiUpgradeMakerRecipeBuilder addUpgradeMakerRecipe(
    Item result,
    int count,
    int time,
    int energy,
    Item input1,
    int input1Amount,
    Item input2,
    Fluid inputFluid,
    int fluidAmount
  ) {
    return addUpgradeMakerRecipe(
      result, count, time, energy, input1, input1Amount, input2, 1, inputFluid, fluidAmount
    );
  }

  private DegrassiUpgradeMakerRecipeBuilder addUpgradeMakerRecipe(
    Item result,
    int count,
    int time,
    int energy,
    Item input1,
    Item input2,
    int input2Amount,
    Fluid inputFluid,
    int fluidAmount
  ) {
    return addUpgradeMakerRecipe(
      result, count, time, energy, input1, 1, input2, input2Amount, inputFluid, fluidAmount
    );
  }

  private DegrassiUpgradeMakerRecipeBuilder addUpgradeMakerRecipe(
    Item result,
    int count,
    int time,
    int energy,
    Item input1,
    Item input2,
    Fluid inputFluid
  ) {
    return addUpgradeMakerRecipe(
      result, count, time, energy, input1, 1, input2, 1, inputFluid, 1000
    );
  }
  private DegrassiUpgradeMakerRecipeBuilder addUpgradeMakerRecipe(
    Item result,
    int time,
    int energy,
    Item input1,
    Item input2,
    Fluid inputFluid
  ) {
    return addUpgradeMakerRecipe(
      result, 1, time, energy, input1, 1, input2, 1, inputFluid, 1000
    );
  }

  protected static void nineBlockStorageRecipes(
    @NotNull Consumer<FinishedRecipe> finishedRecipeConsumer,
    ItemLike unpacked,
    ItemLike packed
  ) {
    nineBlockStorageRecipes(
      finishedRecipeConsumer,
      unpacked,
      packed,
      getSimpleRecipeName(packed),
      null,
      getSimpleRecipeName(unpacked),
      null
    );
  }

  protected static void nineBlockStorageRecipes(
    @NotNull Consumer<FinishedRecipe> finishedRecipeConsumer,
    ItemLike unpacked,
    ItemLike packed,
    String packedName,
    @javax.annotation.Nullable String packedGroup,
    String unpackedName,
    @javax.annotation.Nullable String unpackedGroup
  ) {
    ShapelessRecipeBuilder
      .shapeless(unpacked, 9)
      .requires(packed)
      .group(unpackedGroup)
      .unlockedBy(getHasName(packed), has(packed))
      .save(finishedRecipeConsumer, new ResourceLocation(Degrassi.MODID, unpackedName));
    ShapedRecipeBuilder
      .shaped(packed)
      .define('#', unpacked)
      .pattern("###")
      .pattern("###")
      .pattern("###")
      .group(packedGroup)
      .unlockedBy(getHasName(unpacked), has(unpacked))
      .save(finishedRecipeConsumer, new ResourceLocation(Degrassi.MODID, packedName));
  }
}
