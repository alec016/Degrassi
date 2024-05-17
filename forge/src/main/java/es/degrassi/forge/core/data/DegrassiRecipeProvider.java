package es.degrassi.forge.core.data;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.common.ForgeLocation;
import es.degrassi.forge.core.common.conduit.data.recipe.ConduitRecipes;
import es.degrassi.forge.core.data.recipe.MachineRecipeGeneratorBuilder;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.init.ItemRegistration;
import es.degrassi.forge.core.tiers.Chest;
import es.degrassi.forge.core.tiers.Furnace;
import es.degrassi.forge.core.tiers.PhotovoltaicCell;
import es.degrassi.forge.core.tiers.SolarPanel;
import java.util.function.Consumer;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

public class DegrassiRecipeProvider extends RecipeProvider implements IConditionBuilder {
  private final ConduitRecipes conduits;

  public DegrassiRecipeProvider(PackOutput output) {
    super(output);
    conduits = new ConduitRecipes(output);
  }

  @Override
  public void buildRecipes(@NotNull Consumer<FinishedRecipe> writer) {
    addShapelessRecipes(writer);
    addShapedRecipes(writer);
    addMachineRecipes(writer);
    conduits.buildRecipes(writer);
  }

  private void addShapelessRecipes(@NotNull Consumer<FinishedRecipe> writer) {

  }

  private void addShapedRecipes(@NotNull Consumer<FinishedRecipe> writer) {
    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistration.MACHINE_CASING.get())
      .pattern("bib")
      .pattern("idi")
      .pattern("bib")
      .define('b', ItemTags.create(new ForgeLocation("storage_blocks/iron")))
      .define('i', ItemTags.create(new ForgeLocation("glass")))
      .define('d', ItemTags.create(new ForgeLocation("storage_blocks/diamond")))
      .unlockedBy("iron_block", has(ItemTags.create(new ForgeLocation("storage_blocks/iron"))))
      .unlockedBy("glass", has(ItemTags.create(new ForgeLocation("glass"))))
      .unlockedBy("diamond_block", has(ItemTags.create(new ForgeLocation("storage_blocks/diamond"))))
      .save(writer, new DegrassiLocation("machine_casing"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistration.FURNACE.get(Furnace.IRON))
      .pattern("ddd")
      .pattern("dfd")
      .pattern("ddd")
      .define('d', ItemTags.create(new ForgeLocation("ingots/iron")))
      .define('f', Items.FURNACE)
      .unlockedBy(getHasName(Items.FURNACE), has(Items.FURNACE))
      .unlockedBy("iron", has(ItemTags.create(new ForgeLocation("ingots/iron"))))
      .save(writer, new DegrassiLocation("iron_furnace"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistration.FURNACE.get(Furnace.GOLD))
      .pattern("ddd")
      .pattern("dfd")
      .pattern("ddd")
      .define('d', ItemTags.create(new ForgeLocation("ingots/gold")))
      .define('f', BlockRegistration.FURNACE.get(Furnace.IRON))
      .unlockedBy(getHasName(BlockRegistration.FURNACE.get(Furnace.IRON)), has(BlockRegistration.FURNACE.get(Furnace.IRON)))
      .unlockedBy("diamond", has(ItemTags.create(new ForgeLocation("ingots/gold"))))
      .save(writer, new DegrassiLocation("gold_furnace"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistration.FURNACE.get(Furnace.DIAMOND))
      .pattern("ddd")
      .pattern("dfd")
      .pattern("ddd")
      .define('d', ItemTags.create(new ForgeLocation("gems/diamond")))
      .define('f', BlockRegistration.FURNACE.get(Furnace.GOLD))
      .unlockedBy(getHasName(BlockRegistration.FURNACE.get(Furnace.GOLD)), has(BlockRegistration.FURNACE.get(Furnace.GOLD)))
      .unlockedBy("diamond", has(ItemTags.create(new ForgeLocation("gems/diamond"))))
      .save(writer, new DegrassiLocation("diamond_furnace"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistration.FURNACE.get(Furnace.EMERALD))
      .pattern("ddd")
      .pattern("dfd")
      .pattern("ddd")
      .define('d', ItemTags.create(new ForgeLocation("gems/emerald")))
      .define('f', BlockRegistration.FURNACE.get(Furnace.DIAMOND))
      .unlockedBy(getHasName(BlockRegistration.FURNACE.get(Furnace.DIAMOND)), has(BlockRegistration.FURNACE.get(Furnace.DIAMOND)))
      .unlockedBy("emerald", has(ItemTags.create(new ForgeLocation("gems/emerald"))))
      .save(writer, new DegrassiLocation("emerald_furnace"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistration.FURNACE.get(Furnace.NETHERITE))
      .pattern("ddd")
      .pattern("dfd")
      .pattern("ddd")
      .define('d', ItemTags.create(new ForgeLocation("ingots/netherite")))
      .define('f', BlockRegistration.FURNACE.get(Furnace.EMERALD))
      .unlockedBy(getHasName(BlockRegistration.FURNACE.get(Furnace.EMERALD)), has(BlockRegistration.FURNACE.get(Furnace.EMERALD)))
      .unlockedBy("netherite", has(ItemTags.create(new ForgeLocation("ingots/netherite"))))
      .save(writer, new DegrassiLocation("netherite_furnace"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.I), 3)
      .pattern("ggg")
      .pattern(" i ")
      .pattern("   ")
      .define('g', ItemTags.create(new ForgeLocation("glass")))
      .define('i', ItemTags.create(new ForgeLocation("ingots/iron")))
      .unlockedBy("glass", has(ItemTags.create(new ForgeLocation("glass"))))
      .unlockedBy("iron_ingot", has(ItemTags.create(new ForgeLocation("ingots/iron"))))
      .save(writer, new DegrassiLocation("panels/photovoltaic_cell_1"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.II))
      .pattern("ggg")
      .pattern("lll")
      .pattern("ppp")
      .define('g', ItemTags.create(new ForgeLocation("glass")))
      .define('l', ItemTags.create(new ForgeLocation("gems/lapis")))
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.I))
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.I)), has(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.I)))
      .unlockedBy("glass", has(ItemTags.create(new ForgeLocation("glass"))))
      .unlockedBy("lapis", has(ItemTags.create(new ForgeLocation("gems/lapis"))))
      .save(writer, new DegrassiLocation("panels/photovoltaic_cell_2"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.III))
      .pattern("glg")
      .pattern("lgl")
      .pattern("mpm")
      .define('g', Items.CLAY_BALL)
      .define('l', ItemTags.create(new ForgeLocation("gems/lapis")))
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.II))
      .define('m', ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.I))
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.II)), has(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.II)))
      .unlockedBy("lapis", has(ItemTags.create(new ForgeLocation("gems/lapis"))))
      .unlockedBy(getHasName(Items.CLAY_BALL), has(Items.CLAY_BALL))
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.I)), has(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.I)))
      .save(writer, new DegrassiLocation("panels/photovoltaic_cell_3"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.IV))
      .pattern("ggg")
      .pattern("lll")
      .pattern("mpm")
      .define('g', ItemTags.create(new ForgeLocation("glass")))
      .define('l', ItemTags.create(new ForgeLocation("dusts/glowstone")))
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.III))
      .define('m', ItemTags.create(new ForgeLocation("obsidian")))
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.III)), has(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.III)))
      .unlockedBy("obsidian", has(ItemTags.create(new ForgeLocation("obsidian"))))
      .unlockedBy("glowstone", has(ItemTags.create(new ForgeLocation("dusts/glowstone"))))
      .unlockedBy("glass", has(ItemTags.create(new ForgeLocation("glass"))))
      .save(writer, new DegrassiLocation("panels/photovoltaic_cell_4"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.V))
      .pattern("ggg")
      .pattern("ldl")
      .pattern("mpm")
      .define('g', ItemTags.create(new ForgeLocation("glass")))
      .define('l', ItemTags.create(new ForgeLocation("dusts/glowstone")))
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.IV))
      .define('m', ItemTags.create(new ForgeLocation("obsidian")))
      .define('d', ItemTags.create(new ForgeLocation("gems/diamond")))
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.IV)), has(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.IV)))
      .unlockedBy("obsidian", has(ItemTags.create(new ForgeLocation("obsidian"))))
      .unlockedBy("glowstone", has(ItemTags.create(new ForgeLocation("dusts/glowstone"))))
      .unlockedBy("glass", has(ItemTags.create(new ForgeLocation("glass"))))
      .unlockedBy("diamond", has(ItemTags.create(new ForgeLocation("gems/diamond"))))
      .save(writer, new DegrassiLocation("panels/photovoltaic_cell_5"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.VI))
      .pattern("ggg")
      .pattern("ldl")
      .pattern("mpm")
      .define('g', ItemTags.create(new ForgeLocation("glass")))
      .define('l', ItemTags.create(new ForgeLocation("dusts/glowstone")))
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.V))
      .define('m', ItemTags.create(new ForgeLocation("obsidian")))
      .define('d', ItemTags.create(new ForgeLocation("gems/diamond")))
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.V)), has(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.V)))
      .unlockedBy("obsidian", has(ItemTags.create(new ForgeLocation("obsidian"))))
      .unlockedBy("glowstone", has(ItemTags.create(new ForgeLocation("dusts/glowstone"))))
      .unlockedBy("glass", has(ItemTags.create(new ForgeLocation("glass"))))
      .unlockedBy("diamond", has(ItemTags.create(new ForgeLocation("gems/diamond"))))
      .save(writer, new DegrassiLocation("panels/photovoltaic_cell_6"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.VII))
      .pattern("ggg")
      .pattern("ldl")
      .pattern("mpm")
      .define('g', ItemTags.create(new ForgeLocation("glass")))
      .define('l', ItemTags.create(new ForgeLocation("dusts/glowstone")))
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.VI))
      .define('m', ItemTags.create(new ForgeLocation("obsidian")))
      .define('d', ItemTags.create(new ForgeLocation("gems/diamond")))
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.VI)), has(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.VI)))
      .unlockedBy("obsidian", has(ItemTags.create(new ForgeLocation("obsidian"))))
      .unlockedBy("glowstone", has(ItemTags.create(new ForgeLocation("dusts/glowstone"))))
      .unlockedBy("glass", has(ItemTags.create(new ForgeLocation("glass"))))
      .unlockedBy("diamond", has(ItemTags.create(new ForgeLocation("gems/diamond"))))
      .save(writer, new DegrassiLocation("panels/photovoltaic_cell_7"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.VIII))
      .pattern("ggg")
      .pattern("ldl")
      .pattern("mpm")
      .define('g', ItemTags.create(new ForgeLocation("glass")))
      .define('l', ItemTags.create(new ForgeLocation("dusts/glowstone")))
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.VII))
      .define('m', ItemTags.create(new ForgeLocation("obsidian")))
      .define('d', ItemTags.create(new ForgeLocation("gems/diamond")))
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.VII)), has(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.VII)))
      .unlockedBy("obsidian", has(ItemTags.create(new ForgeLocation("obsidian"))))
      .unlockedBy("glowstone", has(ItemTags.create(new ForgeLocation("dusts/glowstone"))))
      .unlockedBy("glass", has(ItemTags.create(new ForgeLocation("glass"))))
      .unlockedBy("diamond", has(ItemTags.create(new ForgeLocation("gems/diamond"))))
      .save(writer, new DegrassiLocation("panels/photovoltaic_cell_8"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistration.SP.get(SolarPanel.T1))
      .pattern("ppp")
      .pattern("ici")
      .pattern("iii")
      .define('i', ItemTags.create(new ForgeLocation("storage_blocks/iron")))
      .define('c', ItemRegistration.MACHINE_CASING.get())
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.I))
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.I)), has(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.I)))
      .unlockedBy(getHasName(ItemRegistration.MACHINE_CASING.get()), has(ItemRegistration.MACHINE_CASING.get()))
      .unlockedBy("iron_block", has(ItemTags.create(new ForgeLocation("storage_blocks/iron"))))
      .save(writer, new DegrassiLocation("panels/sp1"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistration.SP.get(SolarPanel.T2))
      .pattern("ppp")
      .pattern("ici")
      .pattern("ifi")
      .define('i', BlockRegistration.SP.get(SolarPanel.T1))
      .define('f', ItemTags.create(new ForgeLocation("pistons")))
      .define('c', ItemTags.create(new ForgeLocation("storage_blocks/iron")))
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.II))
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.II)), has(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.II)))
      .unlockedBy(getHasName(BlockRegistration.SP.get(SolarPanel.T1)), has(BlockRegistration.SP.get(SolarPanel.T1)))
      .unlockedBy("iron_block", has(ItemTags.create(new ForgeLocation("storage_blocks/iron"))))
      .unlockedBy("pistons", has(ItemTags.create(new ForgeLocation("pistons"))))
      .save(writer, new DegrassiLocation("panels/sp2"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistration.SP.get(SolarPanel.T3), 2)
      .pattern("ppp")
      .pattern("srs")
      .pattern("sis")
      .define('s', BlockRegistration.SP.get(SolarPanel.T2))
      .define('r', Items.REPEATER)
      .define('i', ItemTags.create(new ForgeLocation("storage_blocks/iron")))
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.III))
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.III)), has(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.III)))
      .unlockedBy(getHasName(BlockRegistration.SP.get(SolarPanel.T2)), has(BlockRegistration.SP.get(SolarPanel.T2)))
      .unlockedBy(getHasName(Items.REPEATER), has(Items.REPEATER))
      .unlockedBy("iron_block", has(ItemTags.create(new ForgeLocation("storage_blocks/iron"))))
      .save(writer, new DegrassiLocation("panels/sp3"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistration.SP.get(SolarPanel.T4))
      .pattern("ppp")
      .pattern("srs")
      .pattern("sis")
      .define('s', BlockRegistration.SP.get(SolarPanel.T3))
      .define('r', Items.CLOCK)
      .define('i', ItemTags.create(new ForgeLocation("storage_blocks/iron")))
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.IV))
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.IV)), has(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.IV)))
      .unlockedBy(getHasName(BlockRegistration.SP.get(SolarPanel.T3)), has(BlockRegistration.SP.get(SolarPanel.T3)))
      .unlockedBy(getHasName(Items.CLOCK), has(Items.CLOCK))
      .unlockedBy("iron_block", has(ItemTags.create(new ForgeLocation("storage_blocks/iron"))))
      .save(writer, new DegrassiLocation("panels/sp4"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistration.SP.get(SolarPanel.T5))
      .pattern("ppp")
      .pattern("srs")
      .pattern("sis")
      .define('s', BlockRegistration.SP.get(SolarPanel.T4))
      .define('r', ItemTags.create(new ForgeLocation("dusts/glowstone")))
      .define('i', ItemTags.create(new ForgeLocation("storage_blocks/gold")))
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.V))
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.V)), has(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.V)))
      .unlockedBy(getHasName(BlockRegistration.SP.get(SolarPanel.T4)), has(BlockRegistration.SP.get(SolarPanel.T4)))
      .unlockedBy("gold_block", has(ItemTags.create(new ForgeLocation("storage_blocks/gold"))))
      .unlockedBy("glowstone", has(ItemTags.create(new ForgeLocation("dusts/glowstone"))))
      .save(writer, new DegrassiLocation("panels/sp5"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistration.SP.get(SolarPanel.T6))
      .pattern("ppp")
      .pattern("srs")
      .pattern("sis")
      .define('s', BlockRegistration.SP.get(SolarPanel.T5))
      .define('r', Items.REDSTONE_LAMP)
      .define('i', ItemTags.create(new ForgeLocation("storage_blocks/diamond")))
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.VI))
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.VI)), has(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.VI)))
      .unlockedBy(getHasName(BlockRegistration.SP.get(SolarPanel.T5)), has(BlockRegistration.SP.get(SolarPanel.T5)))
      .unlockedBy(getHasName(Items.REDSTONE_LAMP), has(Items.REDSTONE_LAMP))
      .unlockedBy("diamond_block", has(ItemTags.create(new ForgeLocation("storage_blocks/diamond"))))
      .save(writer, new DegrassiLocation("panels/sp6"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistration.SP.get(SolarPanel.T7))
      .pattern("ppp")
      .pattern("srs")
      .pattern("srs")
      .define('s', BlockRegistration.SP.get(SolarPanel.T6))
      .define('r', ItemRegistration.BLACK_PEARL.get())
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.VII))
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.VII)), has(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.VII)))
      .unlockedBy(getHasName(BlockRegistration.SP.get(SolarPanel.T6)), has(BlockRegistration.SP.get(SolarPanel.T6)))
      .unlockedBy(getHasName(ItemRegistration.BLACK_PEARL.get()), has(ItemRegistration.BLACK_PEARL.get()))
      .save(writer, new DegrassiLocation("panels/sp7"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistration.SP.get(SolarPanel.T8))
      .pattern("ppp")
      .pattern("srs")
      .pattern("srs")
      .define('s', BlockRegistration.SP.get(SolarPanel.T7))
      .define('r', ItemRegistration.RED_MATTER.get())
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.VIII))
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.VIII)), has(ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.VIII)))
      .unlockedBy(getHasName(BlockRegistration.SP.get(SolarPanel.T7)), has(BlockRegistration.SP.get(SolarPanel.T7)))
      .unlockedBy(getHasName(ItemRegistration.RED_MATTER.get()), has(ItemRegistration.RED_MATTER.get()))
      .save(writer, new DegrassiLocation("panels/sp8"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistration.CHEST.get(Chest.IRON))
      .pattern("ddd")
      .pattern("dfd")
      .pattern("ddd")
      .define('d', ItemTags.create(new ForgeLocation("ingots/iron")))
      .define('f', Items.CHEST)
      .unlockedBy(getHasName(Items.CHEST), has(Items.CHEST))
      .unlockedBy("iron", has(ItemTags.create(new ForgeLocation("ingots/iron"))))
      .save(writer, new DegrassiLocation("iron_chest"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistration.CHEST.get(Chest.GOLD))
      .pattern("ddd")
      .pattern("dfd")
      .pattern("ddd")
      .define('d', ItemTags.create(new ForgeLocation("ingots/gold")))
      .define('f', BlockRegistration.CHEST.get(Chest.IRON))
      .unlockedBy(getHasName(BlockRegistration.CHEST.get(Chest.IRON)), has(BlockRegistration.CHEST.get(Chest.IRON)))
      .unlockedBy("gold", has(ItemTags.create(new ForgeLocation("ingots/gold"))))
      .save(writer, new DegrassiLocation("gold_chest"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistration.CHEST.get(Chest.DIAMOND))
      .pattern("ddd")
      .pattern("dfd")
      .pattern("ddd")
      .define('d', ItemTags.create(new ForgeLocation("gems/diamond")))
      .define('f', BlockRegistration.CHEST.get(Chest.GOLD))
      .unlockedBy(getHasName(BlockRegistration.CHEST.get(Chest.GOLD)), has(BlockRegistration.CHEST.get(Chest.GOLD)))
      .unlockedBy("diamond", has(ItemTags.create(new ForgeLocation("gems/diamond"))))
      .save(writer, new DegrassiLocation("diamond_chest"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistration.CHEST.get(Chest.EMERALD))
      .pattern("ddd")
      .pattern("dfd")
      .pattern("ddd")
      .define('d', ItemTags.create(new ForgeLocation("gems/emerald")))
      .define('f', BlockRegistration.CHEST.get(Chest.DIAMOND))
      .unlockedBy(getHasName(BlockRegistration.CHEST.get(Chest.DIAMOND)), has(BlockRegistration.CHEST.get(Chest.DIAMOND)))
      .unlockedBy("emerald", has(ItemTags.create(new ForgeLocation("gems/emerald"))))
      .save(writer, new DegrassiLocation("emerald_chest"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistration.CHEST.get(Chest.NETHERITE))
      .pattern("ddd")
      .pattern("dfd")
      .pattern("ddd")
      .define('d', ItemTags.create(new ForgeLocation("ingots/netherite")))
      .define('f', BlockRegistration.CHEST.get(Chest.EMERALD))
      .unlockedBy(getHasName(BlockRegistration.CHEST.get(Chest.EMERALD)), has(BlockRegistration.CHEST.get(Chest.EMERALD)))
      .unlockedBy("netherite", has(ItemTags.create(new ForgeLocation("ingots/netherite"))))
      .save(writer, new DegrassiLocation("netherite_chest"));
  }

  private void addMachineRecipes(@NotNull Consumer<FinishedRecipe> writer) {
    MachineRecipeGeneratorBuilder furnace = MachineRecipeGeneratorBuilder.furnace(100);
    furnace
      .requireEnergy(500, "energy")
      .produceExperience(0.8f, "experience")
      .requireItem(Items.COAL, "input")
      .produceItem(Items.DIAMOND, "output");
    furnace.save(writer, new DegrassiLocation("coal_to_diamond"));

    MachineRecipeGeneratorBuilder melter = MachineRecipeGeneratorBuilder.melter(100);
    melter
      .requireEnergyPerTick(500, "energy")
      .requireItem(Items.DIAMOND, "")
      .produceFluid(Fluids.LAVA, 1000, "");
    melter.save(writer, new DegrassiLocation("diamond_to_lava"));

    melter = MachineRecipeGeneratorBuilder.melter(300);
    melter
      .requireEnergyPerTick(500, "energy")
      .requireItem(Items.COBBLESTONE, "")
      .produceFluid(Fluids.LAVA, 500, "");
    melter.save(writer, new DegrassiLocation("cobblestone_to_lava"));

    melter = MachineRecipeGeneratorBuilder.melter(200);
    melter
      .requireEnergyPerTick(500, "energy")
      .requireItem(Items.STONE, "")
      .produceFluid(Fluids.LAVA, 1000, "");
    melter.save(writer, new DegrassiLocation("stone_to_lava"));

    melter = MachineRecipeGeneratorBuilder.melter(1000);
    melter
      .requireEnergyPerTick(500, "energy")
      .requireItem(Items.OBSIDIAN, "")
      .produceFluid(Fluids.LAVA, 1000, "");
    melter.save(writer, new DegrassiLocation("obsidian_to_lava"));
  }
}
