package es.degrassi.forge.core.data;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.common.ForgeLocation;
import es.degrassi.forge.core.data.recipe.MachineRecipeGeneratorBuilder;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.init.ItemRegistration;
import es.degrassi.forge.core.tiers.SolarPanel;
import java.util.function.Consumer;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

public class DegrassiRecipeProvider extends RecipeProvider implements IConditionBuilder {
  public DegrassiRecipeProvider(PackOutput output) {
    super(output);
  }

  @Override
  public void buildRecipes(@NotNull Consumer<FinishedRecipe> writer) {
    addShapelessRecipes(writer);
    addShapedRecipes(writer);
    addMachineRecipes(writer);
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

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistration.IRON_FURNACE.get())
      .pattern("ddd")
      .pattern("dfd")
      .pattern("ddd")
      .define('d', ItemTags.create(new ForgeLocation("ingots/iron")))
      .define('f', Items.FURNACE)
      .unlockedBy(getHasName(Items.FURNACE), has(Items.FURNACE))
      .unlockedBy("iron", has(ItemTags.create(new ForgeLocation("ingots/iron"))))
      .save(writer, new DegrassiLocation("iron_furnace"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistration.GOLD_FURNACE.get())
      .pattern("ddd")
      .pattern("dfd")
      .pattern("ddd")
      .define('d', ItemTags.create(new ForgeLocation("ingots/gold")))
      .define('f', ItemRegistration.IRON_FURNACE.get())
      .unlockedBy(getHasName(ItemRegistration.IRON_FURNACE.get()), has(ItemRegistration.IRON_FURNACE.get()))
      .unlockedBy("diamond", has(ItemTags.create(new ForgeLocation("ingots/gold"))))
      .save(writer, new DegrassiLocation("gold_furnace"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistration.DIAMOND_FURNACE.get())
      .pattern("ddd")
      .pattern("dfd")
      .pattern("ddd")
      .define('d', ItemTags.create(new ForgeLocation("gems/diamond")))
      .define('f', ItemRegistration.GOLD_FURNACE.get())
      .unlockedBy(getHasName(ItemRegistration.GOLD_FURNACE.get()), has(ItemRegistration.GOLD_FURNACE.get()))
      .unlockedBy("diamond", has(ItemTags.create(new ForgeLocation("gems/diamond"))))
      .save(writer, new DegrassiLocation("diamond_furnace"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistration.EMERALD_FURNACE.get())
      .pattern("ddd")
      .pattern("dfd")
      .pattern("ddd")
      .define('d', ItemTags.create(new ForgeLocation("gems/emerald")))
      .define('f', ItemRegistration.DIAMOND_FURNACE.get())
      .unlockedBy(getHasName(ItemRegistration.DIAMOND_FURNACE.get()), has(ItemRegistration.DIAMOND_FURNACE.get()))
      .unlockedBy("emerald", has(ItemTags.create(new ForgeLocation("gems/emerald"))))
      .save(writer, new DegrassiLocation("emerald_furnace"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistration.NETHERITE_FURNACE.get())
      .pattern("ddd")
      .pattern("dfd")
      .pattern("ddd")
      .define('d', ItemTags.create(new ForgeLocation("ingots/netherite")))
      .define('f', ItemRegistration.EMERALD_FURNACE.get())
      .unlockedBy(getHasName(ItemRegistration.EMERALD_FURNACE.get()), has(ItemRegistration.EMERALD_FURNACE.get()))
      .unlockedBy("netherite", has(ItemTags.create(new ForgeLocation("ingots/netherite"))))
      .save(writer, new DegrassiLocation("netherite_furnace"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistration.PHOTOVOLTAIC_CELL_I.get(), 3)
      .pattern("ggg")
      .pattern(" i ")
      .pattern("   ")
      .define('g', ItemTags.create(new ForgeLocation("glass")))
      .define('i', ItemTags.create(new ForgeLocation("ingots/iron")))
      .unlockedBy("glass", has(ItemTags.create(new ForgeLocation("glass"))))
      .unlockedBy("iron_ingot", has(ItemTags.create(new ForgeLocation("ingots/iron"))))
      .save(writer, new DegrassiLocation("panels/photovoltaic_cell_1"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistration.PHOTOVOLTAIC_CELL_II.get())
      .pattern("ggg")
      .pattern("lll")
      .pattern("ppp")
      .define('g', ItemTags.create(new ForgeLocation("glass")))
      .define('l', ItemTags.create(new ForgeLocation("gems/lapis")))
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL_I.get())
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL_I.get()), has(ItemRegistration.PHOTOVOLTAIC_CELL_I.get()))
      .unlockedBy("glass", has(ItemTags.create(new ForgeLocation("glass"))))
      .unlockedBy("lapis", has(ItemTags.create(new ForgeLocation("gems/lapis"))))
      .save(writer, new DegrassiLocation("panels/photovoltaic_cell_2"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistration.PHOTOVOLTAIC_CELL_III.get())
      .pattern("glg")
      .pattern("lgl")
      .pattern("mpm")
      .define('g', Items.CLAY_BALL)
      .define('l', ItemTags.create(new ForgeLocation("gems/lapis")))
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL_II.get())
      .define('m', ItemRegistration.PHOTOVOLTAIC_CELL_I.get())
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL_II.get()), has(ItemRegistration.PHOTOVOLTAIC_CELL_II.get()))
      .unlockedBy("lapis", has(ItemTags.create(new ForgeLocation("gems/lapis"))))
      .unlockedBy(getHasName(Items.CLAY_BALL), has(Items.CLAY_BALL))
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL_I.get()), has(ItemRegistration.PHOTOVOLTAIC_CELL_I.get()))
      .save(writer, new DegrassiLocation("panels/photovoltaic_cell_3"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistration.PHOTOVOLTAIC_CELL_IV.get())
      .pattern("ggg")
      .pattern("lll")
      .pattern("mpm")
      .define('g', ItemTags.create(new ForgeLocation("glass")))
      .define('l', ItemTags.create(new ForgeLocation("dusts/glowstone")))
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL_III.get())
      .define('m', ItemTags.create(new ForgeLocation("obsidian")))
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL_III.get()), has(ItemRegistration.PHOTOVOLTAIC_CELL_III.get()))
      .unlockedBy("obsidian", has(ItemTags.create(new ForgeLocation("obsidian"))))
      .unlockedBy("glowstone", has(ItemTags.create(new ForgeLocation("dusts/glowstone"))))
      .unlockedBy("glass", has(ItemTags.create(new ForgeLocation("glass"))))
      .save(writer, new DegrassiLocation("panels/photovoltaic_cell_4"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistration.PHOTOVOLTAIC_CELL_V.get())
      .pattern("ggg")
      .pattern("ldl")
      .pattern("mpm")
      .define('g', ItemTags.create(new ForgeLocation("glass")))
      .define('l', ItemTags.create(new ForgeLocation("dusts/glowstone")))
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL_IV.get())
      .define('m', ItemTags.create(new ForgeLocation("obsidian")))
      .define('d', ItemTags.create(new ForgeLocation("gems/diamond")))
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL_IV.get()), has(ItemRegistration.PHOTOVOLTAIC_CELL_IV.get()))
      .unlockedBy("obsidian", has(ItemTags.create(new ForgeLocation("obsidian"))))
      .unlockedBy("glowstone", has(ItemTags.create(new ForgeLocation("dusts/glowstone"))))
      .unlockedBy("glass", has(ItemTags.create(new ForgeLocation("glass"))))
      .unlockedBy("diamond", has(ItemTags.create(new ForgeLocation("gems/diamond"))))
      .save(writer, new DegrassiLocation("panels/photovoltaic_cell_5"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistration.PHOTOVOLTAIC_CELL_VI.get())
      .pattern("ggg")
      .pattern("ldl")
      .pattern("mpm")
      .define('g', ItemTags.create(new ForgeLocation("glass")))
      .define('l', ItemTags.create(new ForgeLocation("dusts/glowstone")))
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL_V.get())
      .define('m', ItemTags.create(new ForgeLocation("obsidian")))
      .define('d', ItemTags.create(new ForgeLocation("gems/diamond")))
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL_V.get()), has(ItemRegistration.PHOTOVOLTAIC_CELL_V.get()))
      .unlockedBy("obsidian", has(ItemTags.create(new ForgeLocation("obsidian"))))
      .unlockedBy("glowstone", has(ItemTags.create(new ForgeLocation("dusts/glowstone"))))
      .unlockedBy("glass", has(ItemTags.create(new ForgeLocation("glass"))))
      .unlockedBy("diamond", has(ItemTags.create(new ForgeLocation("gems/diamond"))))
      .save(writer, new DegrassiLocation("panels/photovoltaic_cell_6"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistration.PHOTOVOLTAIC_CELL_VII.get())
      .pattern("ggg")
      .pattern("ldl")
      .pattern("mpm")
      .define('g', ItemTags.create(new ForgeLocation("glass")))
      .define('l', ItemTags.create(new ForgeLocation("dusts/glowstone")))
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL_VI.get())
      .define('m', ItemTags.create(new ForgeLocation("obsidian")))
      .define('d', ItemTags.create(new ForgeLocation("gems/diamond")))
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL_VI.get()), has(ItemRegistration.PHOTOVOLTAIC_CELL_VI.get()))
      .unlockedBy("obsidian", has(ItemTags.create(new ForgeLocation("obsidian"))))
      .unlockedBy("glowstone", has(ItemTags.create(new ForgeLocation("dusts/glowstone"))))
      .unlockedBy("glass", has(ItemTags.create(new ForgeLocation("glass"))))
      .unlockedBy("diamond", has(ItemTags.create(new ForgeLocation("gems/diamond"))))
      .save(writer, new DegrassiLocation("panels/photovoltaic_cell_7"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ItemRegistration.PHOTOVOLTAIC_CELL_VIII.get())
      .pattern("ggg")
      .pattern("ldl")
      .pattern("mpm")
      .define('g', ItemTags.create(new ForgeLocation("glass")))
      .define('l', ItemTags.create(new ForgeLocation("dusts/glowstone")))
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL_VII.get())
      .define('m', ItemTags.create(new ForgeLocation("obsidian")))
      .define('d', ItemTags.create(new ForgeLocation("gems/diamond")))
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL_VII.get()), has(ItemRegistration.PHOTOVOLTAIC_CELL_VII.get()))
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
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL_I.get())
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL_I.get()), has(ItemRegistration.PHOTOVOLTAIC_CELL_I.get()))
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
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL_II.get())
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL_II.get()), has(ItemRegistration.PHOTOVOLTAIC_CELL_II.get()))
      .unlockedBy(getHasName(BlockRegistration.SP.get(SolarPanel.T1)), has(BlockRegistration.SP.get(SolarPanel.T1)))
      .unlockedBy("iron_block", has(ItemTags.create(new ForgeLocation("storage_blocks/iron"))))
      .unlockedBy("pistons", has(ItemTags.create(new ForgeLocation("pistsons"))))
      .save(writer, new DegrassiLocation("panels/sp2"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistration.SP.get(SolarPanel.T3), 2)
      .pattern("ppp")
      .pattern("srs")
      .pattern("sis")
      .define('s', BlockRegistration.SP.get(SolarPanel.T2))
      .define('r', Items.REPEATER)
      .define('i', ItemTags.create(new ForgeLocation("storage_blocks/iron")))
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL_III.get())
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL_III.get()), has(ItemRegistration.PHOTOVOLTAIC_CELL_III.get()))
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
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL_IV.get())
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL_IV.get()), has(ItemRegistration.PHOTOVOLTAIC_CELL_IV.get()))
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
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL_V.get())
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL_V.get()), has(ItemRegistration.PHOTOVOLTAIC_CELL_V.get()))
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
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL_VI.get())
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL_VI.get()), has(ItemRegistration.PHOTOVOLTAIC_CELL_VI.get()))
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
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL_VII.get())
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL_VII.get()), has(ItemRegistration.PHOTOVOLTAIC_CELL_VII.get()))
      .unlockedBy(getHasName(BlockRegistration.SP.get(SolarPanel.T6)), has(BlockRegistration.SP.get(SolarPanel.T6)))
      .unlockedBy(getHasName(ItemRegistration.BLACK_PEARL.get()), has(ItemRegistration.BLACK_PEARL.get()))
      .save(writer, new DegrassiLocation("panels/sp7"));

    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, BlockRegistration.SP.get(SolarPanel.T8))
      .pattern("ppp")
      .pattern("srs")
      .pattern("srs")
      .define('s', BlockRegistration.SP.get(SolarPanel.T7))
      .define('r', ItemRegistration.RED_MATTER.get())
      .define('p', ItemRegistration.PHOTOVOLTAIC_CELL_VIII.get())
      .unlockedBy(getHasName(ItemRegistration.PHOTOVOLTAIC_CELL_VIII.get()), has(ItemRegistration.PHOTOVOLTAIC_CELL_VIII.get()))
      .unlockedBy(getHasName(BlockRegistration.SP.get(SolarPanel.T7)), has(BlockRegistration.SP.get(SolarPanel.T7)))
      .unlockedBy(getHasName(ItemRegistration.RED_MATTER.get()), has(ItemRegistration.RED_MATTER.get()))
      .save(writer, new DegrassiLocation("panels/sp8"));
  }

  private void addMachineRecipes(@NotNull Consumer<FinishedRecipe> writer) {
    MachineRecipeGeneratorBuilder builder = MachineRecipeGeneratorBuilder.furnace(100);
    builder
      .requireEnergy(500, "energy")
      .produceExperience(0.8f, "experience")
      .requireItem(Items.COAL, "input")
      .produceItem(Items.DIAMOND, "output");
    builder.save(writer, new DegrassiLocation("coal_to_diamond"));
  }
}
