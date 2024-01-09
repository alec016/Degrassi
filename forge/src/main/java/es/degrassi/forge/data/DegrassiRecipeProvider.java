package es.degrassi.forge.data;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.common.ForgeLocation;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.data.recipes.*;
import es.degrassi.forge.init.registration.BlockRegister;
import es.degrassi.forge.init.registration.FluidRegister;
import es.degrassi.forge.init.registration.ItemRegister;
import java.util.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
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
    addShapelessRecipes(writer);
    addShapedRecipes(writer);
    addFurnaceRecipes(writer);
    addMelterRecipes(writer);
    addUpgradeMakerRecipes(writer);
    addGeneratorsRecipe(writer);
  }

  private void addShapedRecipes(@NotNull Consumer<FinishedRecipe> writer) {
    ShapedRecipeBuilder.shaped(BlockRegister.MACHINE_CASING.get())
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

    ShapedRecipeBuilder.shaped(BlockRegister.IRON_FURNACE_BLOCK.get())
      .pattern("ddd")
      .pattern("dfd")
      .pattern("ddd")
      .define('d', ItemTags.create(new ForgeLocation("ingots/iron")))
      .define('f', Items.FURNACE)
      .unlockedBy(getHasName(Items.FURNACE), has(Items.FURNACE))
      .unlockedBy("iron", has(ItemTags.create(new ForgeLocation("ingots/iron"))))
      .save(writer, new DegrassiLocation("iron_furnace"));

    ShapedRecipeBuilder.shaped(BlockRegister.GOLD_FURNACE_BLOCK.get())
      .pattern("ddd")
      .pattern("dfd")
      .pattern("ddd")
      .define('d', ItemTags.create(new ForgeLocation("ingots/gold")))
      .define('f', BlockRegister.IRON_FURNACE_BLOCK.get())
      .unlockedBy(getHasName(BlockRegister.IRON_FURNACE_BLOCK.get()), has(BlockRegister.IRON_FURNACE_BLOCK.get()))
      .unlockedBy("diamond", has(ItemTags.create(new ForgeLocation("ingots/gold"))))
      .save(writer, new DegrassiLocation("gold_furnace"));

    ShapedRecipeBuilder.shaped(BlockRegister.DIAMOND_FURNACE_BLOCK.get())
      .pattern("ddd")
      .pattern("dfd")
      .pattern("ddd")
      .define('d', ItemTags.create(new ForgeLocation("gems/diamond")))
      .define('f', BlockRegister.GOLD_FURNACE_BLOCK.get())
      .unlockedBy(getHasName(BlockRegister.GOLD_FURNACE_BLOCK.get()), has(BlockRegister.GOLD_FURNACE_BLOCK.get()))
      .unlockedBy("diamond", has(ItemTags.create(new ForgeLocation("gems/diamond"))))
      .save(writer, new DegrassiLocation("diamond_furnace"));

    ShapedRecipeBuilder.shaped(BlockRegister.EMERALD_FURNACE_BLOCK.get())
      .pattern("ddd")
      .pattern("dfd")
      .pattern("ddd")
      .define('d', ItemTags.create(new ForgeLocation("gems/emerald")))
      .define('f', BlockRegister.DIAMOND_FURNACE_BLOCK.get())
      .unlockedBy(getHasName(BlockRegister.DIAMOND_FURNACE_BLOCK.get()), has(BlockRegister.DIAMOND_FURNACE_BLOCK.get()))
      .unlockedBy("emerald", has(ItemTags.create(new ForgeLocation("gems/emerald"))))
      .save(writer, new DegrassiLocation("emerald_furnace"));

    ShapedRecipeBuilder.shaped(BlockRegister.NETHERITE_FURNACE_BLOCK.get())
      .pattern("ddd")
      .pattern("dfd")
      .pattern("ddd")
      .define('d', ItemTags.create(new ForgeLocation("ingots/netherite")))
      .define('f', BlockRegister.EMERALD_FURNACE_BLOCK.get())
      .unlockedBy(getHasName(BlockRegister.EMERALD_FURNACE_BLOCK.get()), has(BlockRegister.EMERALD_FURNACE_BLOCK.get()))
      .unlockedBy("netherite", has(ItemTags.create(new ForgeLocation("ingots/netherite"))))
      .save(writer, new DegrassiLocation("netherite_furnace"));

    ShapedRecipeBuilder.shaped(BlockRegister.MELTER_BLOCK.get())
      .pattern("rbr")
      .pattern("lcl")
      .pattern("rbr")
      .define('r', ItemRegister.RED_MATTER.get())
      .define('b', ItemRegister.BLACK_PEARL.get())
      .define('l', Items.LAVA_BUCKET)
      .define('c', BlockRegister.MACHINE_CASING.get())
      .unlockedBy(getHasName(BlockRegister.MACHINE_CASING.get()), has(BlockRegister.MACHINE_CASING.get()))
      .unlockedBy(getHasName(ItemRegister.RED_MATTER.get()), has(ItemRegister.RED_MATTER.get()))
      .unlockedBy(getHasName(ItemRegister.BLACK_PEARL.get()), has(ItemRegister.BLACK_PEARL.get()))
      .unlockedBy(getHasName(Items.LAVA_BUCKET), has(Items.LAVA_BUCKET))
      .save(writer, new DegrassiLocation("melter"));

    ShapedRecipeBuilder.shaped(ItemRegister.MODIFIER_BASE.get())
      .pattern(" i ")
      .pattern("iii")
      .pattern(" i ")
      .define('i', ItemTags.create(new ForgeLocation("ingots/iron")))
      .unlockedBy("iron_ingot", has(ItemTags.create(new ForgeLocation("ingots/iron"))))
      .save(writer, new DegrassiLocation("modifier_base"));

    ShapedRecipeBuilder.shaped(ItemRegister.UPGRADE_BASE.get())
      .pattern("iri")
      .pattern("rdr")
      .pattern("iri")
      .define('i', ItemTags.create(new ForgeLocation("ingots/iron")))
      .define('r', ItemTags.create(new ForgeLocation("dusts/redstone")))
      .define('d', ItemTags.create(new ForgeLocation("gems/diamond")))
      .unlockedBy("iron_ingot", has(ItemTags.create(new ForgeLocation("ingots/iron"))))
      .unlockedBy("redstone", has(ItemTags.create(new ForgeLocation("dusts/redstone"))))
      .unlockedBy("diamond", has(ItemTags.create(new ForgeLocation("gems/diamond"))))
      .save(writer, new DegrassiLocation("upgrade_base"));

    ShapedRecipeBuilder.shaped(BlockRegister.UPGRADE_MAKER.get())
      .pattern("bmb")
      .pattern("mcm")
      .pattern("bmb")
      .define('b', ItemRegister.UPGRADE_BASE.get())
      .define('m', ItemRegister.MODIFIER_BASE.get())
      .define('c', BlockRegister.MACHINE_CASING.get())
      .unlockedBy(getHasName(BlockRegister.MACHINE_CASING.get()), has(BlockRegister.MACHINE_CASING.get()))
      .unlockedBy(getHasName(ItemRegister.UPGRADE_BASE.get()), has(ItemRegister.UPGRADE_BASE.get()))
      .unlockedBy(getHasName(ItemRegister.MODIFIER_BASE.get()), has(ItemRegister.MODIFIER_BASE.get()))
      .save(writer, new DegrassiLocation("upgrade_maker"));

    ShapedRecipeBuilder.shaped(ItemRegister.PHOTOVOLTAIC_CELL_I.get(), 3)
      .pattern("ggg")
      .pattern(" i ")
      .pattern("   ")
      .define('g', ItemTags.create(new ForgeLocation("glass")))
      .define('i', ItemTags.create(new ForgeLocation("ingots/iron")))
      .unlockedBy("glass", has(ItemTags.create(new ForgeLocation("glass"))))
      .unlockedBy("iron_ingot", has(ItemTags.create(new ForgeLocation("ingots/iron"))))
      .save(writer, new DegrassiLocation("panels/photovoltaic_cell_1"));

    ShapedRecipeBuilder.shaped(ItemRegister.PHOTOVOLTAIC_CELL_II.get())
      .pattern("ggg")
      .pattern("lll")
      .pattern("ppp")
      .define('g', ItemTags.create(new ForgeLocation("glass")))
      .define('l', ItemTags.create(new ForgeLocation("gems/lapis")))
      .define('p', ItemRegister.PHOTOVOLTAIC_CELL_I.get())
      .unlockedBy(getHasName(ItemRegister.PHOTOVOLTAIC_CELL_I.get()), has(ItemRegister.PHOTOVOLTAIC_CELL_I.get()))
      .unlockedBy("glass", has(ItemTags.create(new ForgeLocation("glass"))))
      .unlockedBy("lapis", has(ItemTags.create(new ForgeLocation("gems/lapis"))))
      .save(writer, new DegrassiLocation("panels/photovoltaic_cell_2"));

    ShapedRecipeBuilder.shaped(ItemRegister.PHOTOVOLTAIC_CELL_III.get())
      .pattern("glg")
      .pattern("lgl")
      .pattern("mpm")
      .define('g', Items.CLAY_BALL)
      .define('l', ItemTags.create(new ForgeLocation("gems/lapis")))
      .define('p', ItemRegister.PHOTOVOLTAIC_CELL_II.get())
      .define('m', ItemRegister.PHOTOVOLTAIC_CELL_I.get())
      .unlockedBy(getHasName(ItemRegister.PHOTOVOLTAIC_CELL_II.get()), has(ItemRegister.PHOTOVOLTAIC_CELL_II.get()))
      .unlockedBy("lapis", has(ItemTags.create(new ForgeLocation("gems/lapis"))))
      .unlockedBy(getHasName(Items.CLAY_BALL), has(Items.CLAY_BALL))
      .unlockedBy(getHasName(ItemRegister.PHOTOVOLTAIC_CELL_I.get()), has(ItemRegister.PHOTOVOLTAIC_CELL_I.get()))
      .save(writer, new DegrassiLocation("panels/photovoltaic_cell_3"));

    ShapedRecipeBuilder.shaped(ItemRegister.PHOTOVOLTAIC_CELL_IV.get())
      .pattern("ggg")
      .pattern("lll")
      .pattern("mpm")
      .define('g', ItemTags.create(new ForgeLocation("glass")))
      .define('l', ItemTags.create(new ForgeLocation("dusts/glowstone")))
      .define('p', ItemRegister.PHOTOVOLTAIC_CELL_III.get())
      .define('m', ItemTags.create(new ForgeLocation("obsidian")))
      .unlockedBy(getHasName(ItemRegister.PHOTOVOLTAIC_CELL_III.get()), has(ItemRegister.PHOTOVOLTAIC_CELL_III.get()))
      .unlockedBy("obsidian", has(ItemTags.create(new ForgeLocation("obsidian"))))
      .unlockedBy("glowstone", has(ItemTags.create(new ForgeLocation("dusts/glowstone"))))
      .unlockedBy("glass", has(ItemTags.create(new ForgeLocation("glass"))))
      .save(writer, new DegrassiLocation("panels/photovoltaic_cell_4"));

    ShapedRecipeBuilder.shaped(ItemRegister.PHOTOVOLTAIC_CELL_V.get())
      .pattern("ggg")
      .pattern("ldl")
      .pattern("mpm")
      .define('g', ItemTags.create(new ForgeLocation("glass")))
      .define('l', ItemTags.create(new ForgeLocation("dusts/glowstone")))
      .define('p', ItemRegister.PHOTOVOLTAIC_CELL_IV.get())
      .define('m', ItemTags.create(new ForgeLocation("obsidian")))
      .define('d', ItemTags.create(new ForgeLocation("gems/diamond")))
      .unlockedBy(getHasName(ItemRegister.PHOTOVOLTAIC_CELL_IV.get()), has(ItemRegister.PHOTOVOLTAIC_CELL_IV.get()))
      .unlockedBy("obsidian", has(ItemTags.create(new ForgeLocation("obsidian"))))
      .unlockedBy("glowstone", has(ItemTags.create(new ForgeLocation("dusts/glowstone"))))
      .unlockedBy("glass", has(ItemTags.create(new ForgeLocation("glass"))))
      .unlockedBy("diamond", has(ItemTags.create(new ForgeLocation("gems/diamond"))))
      .save(writer, new DegrassiLocation("panels/photovoltaic_cell_5"));

    ShapedRecipeBuilder.shaped(ItemRegister.PHOTOVOLTAIC_CELL_VI.get())
      .pattern("ggg")
      .pattern("ldl")
      .pattern("mpm")
      .define('g', ItemTags.create(new ForgeLocation("glass")))
      .define('l', ItemTags.create(new ForgeLocation("dusts/glowstone")))
      .define('p', ItemRegister.PHOTOVOLTAIC_CELL_V.get())
      .define('m', ItemTags.create(new ForgeLocation("obsidian")))
      .define('d', ItemTags.create(new ForgeLocation("gems/diamond")))
      .unlockedBy(getHasName(ItemRegister.PHOTOVOLTAIC_CELL_V.get()), has(ItemRegister.PHOTOVOLTAIC_CELL_V.get()))
      .unlockedBy("obsidian", has(ItemTags.create(new ForgeLocation("obsidian"))))
      .unlockedBy("glowstone", has(ItemTags.create(new ForgeLocation("dusts/glowstone"))))
      .unlockedBy("glass", has(ItemTags.create(new ForgeLocation("glass"))))
      .unlockedBy("diamond", has(ItemTags.create(new ForgeLocation("gems/diamond"))))
      .save(writer, new DegrassiLocation("panels/photovoltaic_cell_6"));

    ShapedRecipeBuilder.shaped(ItemRegister.PHOTOVOLTAIC_CELL_VII.get())
      .pattern("ggg")
      .pattern("ldl")
      .pattern("mpm")
      .define('g', ItemTags.create(new ForgeLocation("glass")))
      .define('l', ItemTags.create(new ForgeLocation("dusts/glowstone")))
      .define('p', ItemRegister.PHOTOVOLTAIC_CELL_VI.get())
      .define('m', ItemTags.create(new ForgeLocation("obsidian")))
      .define('d', ItemTags.create(new ForgeLocation("gems/diamond")))
      .unlockedBy(getHasName(ItemRegister.PHOTOVOLTAIC_CELL_VI.get()), has(ItemRegister.PHOTOVOLTAIC_CELL_VI.get()))
      .unlockedBy("obsidian", has(ItemTags.create(new ForgeLocation("obsidian"))))
      .unlockedBy("glowstone", has(ItemTags.create(new ForgeLocation("dusts/glowstone"))))
      .unlockedBy("glass", has(ItemTags.create(new ForgeLocation("glass"))))
      .unlockedBy("diamond", has(ItemTags.create(new ForgeLocation("gems/diamond"))))
      .save(writer, new DegrassiLocation("panels/photovoltaic_cell_7"));

    ShapedRecipeBuilder.shaped(ItemRegister.PHOTOVOLTAIC_CELL_VIII.get())
      .pattern("ggg")
      .pattern("ldl")
      .pattern("mpm")
      .define('g', ItemTags.create(new ForgeLocation("glass")))
      .define('l', ItemTags.create(new ForgeLocation("dusts/glowstone")))
      .define('p', ItemRegister.PHOTOVOLTAIC_CELL_VII.get())
      .define('m', ItemTags.create(new ForgeLocation("obsidian")))
      .define('d', ItemTags.create(new ForgeLocation("gems/diamond")))
      .unlockedBy(getHasName(ItemRegister.PHOTOVOLTAIC_CELL_VII.get()), has(ItemRegister.PHOTOVOLTAIC_CELL_VII.get()))
      .unlockedBy("obsidian", has(ItemTags.create(new ForgeLocation("obsidian"))))
      .unlockedBy("glowstone", has(ItemTags.create(new ForgeLocation("dusts/glowstone"))))
      .unlockedBy("glass", has(ItemTags.create(new ForgeLocation("glass"))))
      .unlockedBy("diamond", has(ItemTags.create(new ForgeLocation("gems/diamond"))))
      .save(writer, new DegrassiLocation("panels/photovoltaic_cell_8"));

    ShapedRecipeBuilder.shaped(BlockRegister.SP1_BLOCK.get())
      .pattern("ppp")
      .pattern("ici")
      .pattern("iii")
      .define('i', ItemTags.create(new ForgeLocation("storage_blocks/iron")))
      .define('c', BlockRegister.MACHINE_CASING.get())
      .define('p', ItemRegister.PHOTOVOLTAIC_CELL_I.get())
      .unlockedBy(getHasName(ItemRegister.PHOTOVOLTAIC_CELL_I.get()), has(ItemRegister.PHOTOVOLTAIC_CELL_I.get()))
      .unlockedBy(getHasName(BlockRegister.MACHINE_CASING.get()), has(BlockRegister.MACHINE_CASING.get()))
      .unlockedBy("iron_block", has(ItemTags.create(new ForgeLocation("storage_blocks/iron"))))
      .save(writer, new DegrassiLocation("panels/sp1"));

    ShapedRecipeBuilder.shaped(BlockRegister.SP2_BLOCK.get())
      .pattern("ppp")
      .pattern("ici")
      .pattern("ifi")
      .define('i', BlockRegister.SP1_BLOCK.get())
      .define('f', ItemTags.create(new ForgeLocation("pistons")))
      .define('c', ItemTags.create(new ForgeLocation("storage_blocks/iron")))
      .define('p', ItemRegister.PHOTOVOLTAIC_CELL_II.get())
      .unlockedBy(getHasName(ItemRegister.PHOTOVOLTAIC_CELL_II.get()), has(ItemRegister.PHOTOVOLTAIC_CELL_II.get()))
      .unlockedBy(getHasName(BlockRegister.SP1_BLOCK.get()), has(BlockRegister.SP1_BLOCK.get()))
      .unlockedBy("iron_block", has(ItemTags.create(new ForgeLocation("storage_blocks/iron"))))
      .unlockedBy("pistons", has(ItemTags.create(new ForgeLocation("pistsons"))))
      .save(writer, new DegrassiLocation("panels/sp2"));

    ShapedRecipeBuilder.shaped(BlockRegister.SP3_BLOCK.get(), 2)
      .pattern("ppp")
      .pattern("srs")
      .pattern("sis")
      .define('s', BlockRegister.SP2_BLOCK.get())
      .define('r', Items.REPEATER)
      .define('i', ItemTags.create(new ForgeLocation("storage_blocks/iron")))
      .define('p', ItemRegister.PHOTOVOLTAIC_CELL_III.get())
      .unlockedBy(getHasName(ItemRegister.PHOTOVOLTAIC_CELL_III.get()), has(ItemRegister.PHOTOVOLTAIC_CELL_III.get()))
      .unlockedBy(getHasName(BlockRegister.SP2_BLOCK.get()), has(BlockRegister.SP2_BLOCK.get()))
      .unlockedBy(getHasName(Items.REPEATER), has(Items.REPEATER))
      .unlockedBy("iron_block", has(ItemTags.create(new ForgeLocation("storage_blocks/iron"))))
      .save(writer, new DegrassiLocation("panels/sp3"));

    ShapedRecipeBuilder.shaped(BlockRegister.SP4_BLOCK.get())
      .pattern("ppp")
      .pattern("srs")
      .pattern("sis")
      .define('s', BlockRegister.SP3_BLOCK.get())
      .define('r', Items.CLOCK)
      .define('i', ItemTags.create(new ForgeLocation("storage_blocks/iron")))
      .define('p', ItemRegister.PHOTOVOLTAIC_CELL_IV.get())
      .unlockedBy(getHasName(ItemRegister.PHOTOVOLTAIC_CELL_IV.get()), has(ItemRegister.PHOTOVOLTAIC_CELL_IV.get()))
      .unlockedBy(getHasName(BlockRegister.SP3_BLOCK.get()), has(BlockRegister.SP3_BLOCK.get()))
      .unlockedBy(getHasName(Items.CLOCK), has(Items.CLOCK))
      .unlockedBy("iron_block", has(ItemTags.create(new ForgeLocation("storage_blocks/iron"))))
      .save(writer, new DegrassiLocation("panels/sp4"));

    ShapedRecipeBuilder.shaped(BlockRegister.SP5_BLOCK.get())
      .pattern("ppp")
      .pattern("srs")
      .pattern("sis")
      .define('s', BlockRegister.SP4_BLOCK.get())
      .define('r', ItemTags.create(new ForgeLocation("dusts/glowstone")))
      .define('i', ItemTags.create(new ForgeLocation("storage_blocks/gold")))
      .define('p', ItemRegister.PHOTOVOLTAIC_CELL_V.get())
      .unlockedBy(getHasName(ItemRegister.PHOTOVOLTAIC_CELL_V.get()), has(ItemRegister.PHOTOVOLTAIC_CELL_V.get()))
      .unlockedBy(getHasName(BlockRegister.SP4_BLOCK.get()), has(BlockRegister.SP4_BLOCK.get()))
      .unlockedBy("gold_block", has(ItemTags.create(new ForgeLocation("storage_blocks/gold"))))
      .unlockedBy("glowstone", has(ItemTags.create(new ForgeLocation("dusts/glowstone"))))
      .save(writer, new DegrassiLocation("panels/sp5"));

    ShapedRecipeBuilder.shaped(BlockRegister.SP6_BLOCK.get())
      .pattern("ppp")
      .pattern("srs")
      .pattern("sis")
      .define('s', BlockRegister.SP5_BLOCK.get())
      .define('r', Items.REDSTONE_LAMP)
      .define('i', ItemTags.create(new ForgeLocation("storage_blocks/diamond")))
      .define('p', ItemRegister.PHOTOVOLTAIC_CELL_VI.get())
      .unlockedBy(getHasName(ItemRegister.PHOTOVOLTAIC_CELL_VI.get()), has(ItemRegister.PHOTOVOLTAIC_CELL_VI.get()))
      .unlockedBy(getHasName(BlockRegister.SP5_BLOCK.get()), has(BlockRegister.SP5_BLOCK.get()))
      .unlockedBy(getHasName(Items.REDSTONE_LAMP), has(Items.REDSTONE_LAMP))
      .unlockedBy("diamond_block", has(ItemTags.create(new ForgeLocation("storage_blocks/diamond"))))
      .save(writer, new DegrassiLocation("panels/sp6"));

    ShapedRecipeBuilder.shaped(BlockRegister.SP7_BLOCK.get())
      .pattern("ppp")
      .pattern("srs")
      .pattern("srs")
      .define('s', BlockRegister.SP6_BLOCK.get())
      .define('r', ItemRegister.BLACK_PEARL.get())
      .define('p', ItemRegister.PHOTOVOLTAIC_CELL_VII.get())
      .unlockedBy(getHasName(ItemRegister.PHOTOVOLTAIC_CELL_VII.get()), has(ItemRegister.PHOTOVOLTAIC_CELL_VII.get()))
      .unlockedBy(getHasName(BlockRegister.SP6_BLOCK.get()), has(BlockRegister.SP6_BLOCK.get()))
      .unlockedBy(getHasName(ItemRegister.BLACK_PEARL.get()), has(ItemRegister.BLACK_PEARL.get()))
      .save(writer, new DegrassiLocation("panels/sp7"));

    ShapedRecipeBuilder.shaped(BlockRegister.SP8_BLOCK.get())
      .pattern("ppp")
      .pattern("srs")
      .pattern("srs")
      .define('s', BlockRegister.SP7_BLOCK.get())
      .define('r', ItemRegister.RED_MATTER.get())
      .define('p', ItemRegister.PHOTOVOLTAIC_CELL_VIII.get())
      .unlockedBy(getHasName(ItemRegister.PHOTOVOLTAIC_CELL_VIII.get()), has(ItemRegister.PHOTOVOLTAIC_CELL_VIII.get()))
      .unlockedBy(getHasName(BlockRegister.SP7_BLOCK.get()), has(BlockRegister.SP7_BLOCK.get()))
      .unlockedBy(getHasName(ItemRegister.RED_MATTER.get()), has(ItemRegister.RED_MATTER.get()))
      .save(writer, new DegrassiLocation("panels/sp8"));
  }

  private void addShapelessRecipes(@NotNull Consumer<FinishedRecipe> writer) {

  }

  private void addGeneratorsRecipe(@NotNull Consumer<FinishedRecipe> writer) {
    addJewelryGeneratorRecipes(writer);
    addCombustionGeneratorRecipes(writer);
  }

  private void addJewelryGeneratorRecipes(@NotNull Consumer<FinishedRecipe> writer) {
    addGeneratorRecipe(Items.DIAMOND, 200, 100, BlockRegister.JEWELRY_GENERATOR.getId().toString())
      .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND))
      .save(writer, new DegrassiLocation("generators/jewelry/diamond"));
    addGeneratorRecipe(Blocks.DIAMOND_BLOCK.asItem(), 200, 1000, BlockRegister.JEWELRY_GENERATOR.getId().toString())
      .unlockedBy(getHasName(Blocks.DIAMOND_BLOCK.asItem()), has(Blocks.DIAMOND_BLOCK.asItem()))
      .save(writer, new DegrassiLocation("generators/jewelry/diamond_block"));
    addGeneratorRecipe(Items.EMERALD, 200, 100, BlockRegister.JEWELRY_GENERATOR.getId().toString())
      .unlockedBy(getHasName(Items.EMERALD), has(Items.EMERALD))
      .save(writer, new DegrassiLocation("generators/jewelry/emerald"));
    addGeneratorRecipe(Blocks.EMERALD_BLOCK.asItem(), 100, 1000, BlockRegister.JEWELRY_GENERATOR.getId().toString())
      .unlockedBy(getHasName(Blocks.EMERALD_BLOCK.asItem()), has(Blocks.EMERALD_BLOCK.asItem()))
      .save(writer, new DegrassiLocation("generators/jewelry/emerald_block"));
    addGeneratorRecipe(Items.LAPIS_LAZULI, 50, 100, BlockRegister.JEWELRY_GENERATOR.getId().toString())
      .unlockedBy(getHasName(Items.LAPIS_LAZULI), has(Items.LAPIS_LAZULI))
      .save(writer, new DegrassiLocation("generators/jewelry/lapis"));
    addGeneratorRecipe(Blocks.LAPIS_BLOCK.asItem(), 50, 1000, BlockRegister.JEWELRY_GENERATOR.getId().toString())
      .unlockedBy(getHasName(Blocks.LAPIS_BLOCK.asItem()), has(Blocks.LAPIS_BLOCK.asItem()))
      .save(writer, new DegrassiLocation("generators/jewelry/lapis_block"));
    addGeneratorRecipe(Items.QUARTZ, 100, 100, BlockRegister.JEWELRY_GENERATOR.getId().toString())
      .unlockedBy(getHasName(Items.QUARTZ), has(Items.QUARTZ))
      .save(writer, new DegrassiLocation("generators/jewelry/quartz"));
    addGeneratorRecipe(Blocks.QUARTZ_BLOCK.asItem(), 100, 1000, BlockRegister.JEWELRY_GENERATOR.getId().toString())
      .unlockedBy(getHasName(Blocks.QUARTZ_BLOCK.asItem()), has(Blocks.QUARTZ_BLOCK.asItem()))
      .save(writer, new DegrassiLocation("generators/jewelry/quartz_block"));
    addGeneratorRecipe(Items.AMETHYST_SHARD, 100, 100, BlockRegister.JEWELRY_GENERATOR.getId().toString())
      .unlockedBy(getHasName(Items.AMETHYST_SHARD), has(Items.AMETHYST_SHARD))
      .save(writer, new DegrassiLocation("generators/jewelry/amethyst"));
    addGeneratorRecipe(Blocks.AMETHYST_BLOCK.asItem(), 100, 1000, BlockRegister.JEWELRY_GENERATOR.getId().toString())
      .unlockedBy(getHasName(Blocks.AMETHYST_BLOCK.asItem()), has(Blocks.AMETHYST_BLOCK.asItem()))
      .save(writer, new DegrassiLocation("generators/jewelry/amethyst_block"));
  }

  private void addCombustionGeneratorRecipes(@NotNull Consumer<FinishedRecipe> writer) {
    // OTHERS
    {
      addGeneratorRecipe(Items.STICK, 3, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
        .save(writer, new DegrassiLocation("generators/combustion/stick"));
      addGeneratorRecipe(Items.DEAD_BUSH, 3, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.DEAD_BUSH), has(Items.DEAD_BUSH))
        .save(writer, new DegrassiLocation("generators/combustion/dead_bush"));
      addGeneratorRecipe(Items.CRAFTING_TABLE, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.CRAFTING_TABLE), has(Items.CRAFTING_TABLE))
        .save(writer, new DegrassiLocation("generators/combustion/crafting_table"));
      addGeneratorRecipe(Items.CHEST, 12, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.CHEST), has(Items.CHEST))
        .save(writer, new DegrassiLocation("generators/combustion/chest"));
      addGeneratorRecipe(Items.BARREL, 12, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.BARREL), has(Items.BARREL))
        .save(writer, new DegrassiLocation("generators/combustion/barrel"));
      addGeneratorRecipe(Items.COMPOSTER, 12, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.COMPOSTER), has(Items.COMPOSTER))
        .save(writer, new DegrassiLocation("generators/combustion/composter"));
      addGeneratorRecipe(Items.CARTOGRAPHY_TABLE, 12, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.CARTOGRAPHY_TABLE), has(Items.CARTOGRAPHY_TABLE))
        .save(writer, new DegrassiLocation("generators/combustion/cartography_table"));
      addGeneratorRecipe(Items.FLETCHING_TABLE, 12, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.FLETCHING_TABLE), has(Items.FLETCHING_TABLE))
        .save(writer, new DegrassiLocation("generators/combustion/fletching_table"));
      addGeneratorRecipe(Items.BEEHIVE, 12, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.BEEHIVE), has(Items.BEEHIVE))
        .save(writer, new DegrassiLocation("generators/combustion/beehive"));
      addGeneratorRecipe(Items.LECTERN, 12, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.LECTERN), has(Items.LECTERN))
        .save(writer, new DegrassiLocation("generators/combustion/lectern"));
      addGeneratorRecipe(Items.BOWL, 4, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.BOWL), has(Items.BOWL))
        .save(writer, new DegrassiLocation("generators/combustion/bowl"));
      addGeneratorRecipe(Items.BOW, 6, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.BOW), has(Items.BOW))
        .save(writer, new DegrassiLocation("generators/combustion/bow"));
      addGeneratorRecipe(Items.WOODEN_SWORD, 12, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.WOODEN_SWORD), has(Items.WOODEN_SWORD))
        .save(writer, new DegrassiLocation("generators/combustion/wooden_sword"));
      addGeneratorRecipe(Items.WOODEN_AXE, 12, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.WOODEN_AXE), has(Items.WOODEN_AXE))
        .save(writer, new DegrassiLocation("generators/combustion/wooden_axe"));
      addGeneratorRecipe(Items.WOODEN_PICKAXE, 12, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.WOODEN_PICKAXE), has(Items.WOODEN_PICKAXE))
        .save(writer, new DegrassiLocation("generators/combustion/wooden_pickaxe"));
      addGeneratorRecipe(Items.WOODEN_HOE, 12, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.WOODEN_HOE), has(Items.WOODEN_HOE))
        .save(writer, new DegrassiLocation("generators/combustion/wooden_hoe"));
      addGeneratorRecipe(Items.WOODEN_SHOVEL, 12, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.WOODEN_SHOVEL), has(Items.WOODEN_SHOVEL))
        .save(writer, new DegrassiLocation("generators/combustion/wooden_shovel"));
      addGeneratorRecipe(Items.BLAZE_ROD, 60, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.BLAZE_ROD), has(Items.BLAZE_ROD))
        .save(writer, new DegrassiLocation("generators/combustion/blaze_rod"));
      addGeneratorRecipe(Items.BLAZE_POWDER, 20, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.BLAZE_POWDER), has(Items.BLAZE_POWDER))
        .save(writer, new DegrassiLocation("generators/combustion/blaze_powder"));
      addGeneratorRecipe(Items.BAMBOO, 5, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.BAMBOO), has(Items.BAMBOO))
        .save(writer, new DegrassiLocation("generators/combustion/bamboo"));
    }
    // COALS
    {
      addGeneratorRecipe(Items.COAL, 10, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.COAL), has(Items.COAL))
        .save(writer, new DegrassiLocation("generators/combustion/coal"));
      addGeneratorRecipe(Blocks.COAL_BLOCK.asItem(), 10, 1000, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Blocks.COAL_BLOCK.asItem()), has(Blocks.COAL_BLOCK.asItem()))
        .save(writer, new DegrassiLocation("generators/combustion/coal_block"));
      addGeneratorRecipe(Items.CHARCOAL, 10, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.CHARCOAL), has(Items.CHARCOAL))
        .save(writer, new DegrassiLocation("generators/combustion/charcoal"));
    }
    // WOOD
    {
      addGeneratorRecipe(Items.ACACIA_WOOD, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.ACACIA_WOOD), has(Items.ACACIA_WOOD))
        .save(writer, new DegrassiLocation("generators/combustion/acacia_wood"));
      addGeneratorRecipe(Items.BIRCH_WOOD, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.BIRCH_WOOD), has(Items.BIRCH_WOOD))
        .save(writer, new DegrassiLocation("generators/combustion/birch_wood"));
      addGeneratorRecipe(Items.OAK_WOOD, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.OAK_WOOD), has(Items.OAK_WOOD))
        .save(writer, new DegrassiLocation("generators/combustion/oak_wood"));
      addGeneratorRecipe(Items.DARK_OAK_WOOD, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.DARK_OAK_WOOD), has(Items.DARK_OAK_WOOD))
        .save(writer, new DegrassiLocation("generators/combustion/dark_oak_wood"));
      addGeneratorRecipe(Items.JUNGLE_WOOD, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.JUNGLE_WOOD), has(Items.JUNGLE_WOOD))
        .save(writer, new DegrassiLocation("generators/combustion/jungle_wood"));
      addGeneratorRecipe(Items.MANGROVE_WOOD, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.MANGROVE_WOOD), has(Items.MANGROVE_WOOD))
        .save(writer, new DegrassiLocation("generators/combustion/mangrove_wood"));
      addGeneratorRecipe(Items.SPRUCE_WOOD, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.SPRUCE_WOOD), has(Items.SPRUCE_WOOD))
        .save(writer, new DegrassiLocation("generators/combustion/spruce_wood"));
    }
    // LOGS
    {
      addGeneratorRecipe(Items.ACACIA_LOG, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.ACACIA_LOG), has(Items.ACACIA_LOG))
        .save(writer, new DegrassiLocation("generators/combustion/acacia_log"));
      addGeneratorRecipe(Items.BIRCH_LOG, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.BIRCH_LOG), has(Items.BIRCH_LOG))
        .save(writer, new DegrassiLocation("generators/combustion/birch_log"));
      addGeneratorRecipe(Items.OAK_LOG, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.OAK_LOG), has(Items.OAK_LOG))
        .save(writer, new DegrassiLocation("generators/combustion/oak_log"));
      addGeneratorRecipe(Items.DARK_OAK_LOG, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.DARK_OAK_LOG), has(Items.DARK_OAK_LOG))
        .save(writer, new DegrassiLocation("generators/combustion/dark_oak_log"));
      addGeneratorRecipe(Items.JUNGLE_LOG, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.JUNGLE_LOG), has(Items.JUNGLE_LOG))
        .save(writer, new DegrassiLocation("generators/combustion/jungle_log"));
      addGeneratorRecipe(Items.MANGROVE_LOG, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.MANGROVE_LOG), has(Items.MANGROVE_LOG))
        .save(writer, new DegrassiLocation("generators/combustion/mangrove_log"));
      addGeneratorRecipe(Items.SPRUCE_LOG, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.SPRUCE_LOG), has(Items.SPRUCE_LOG))
        .save(writer, new DegrassiLocation("generators/combustion/spruce_log"));
    }
    // STRIPPED LOGS
    {
      addGeneratorRecipe(Items.STRIPPED_ACACIA_LOG, 6, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.STRIPPED_ACACIA_LOG), has(Items.STRIPPED_ACACIA_LOG))
        .save(writer, new DegrassiLocation("generators/combustion/stripped_acacia_log"));
      addGeneratorRecipe(Items.STRIPPED_BIRCH_LOG, 6, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.STRIPPED_BIRCH_LOG), has(Items.STRIPPED_BIRCH_LOG))
        .save(writer, new DegrassiLocation("generators/combustion/stripped_birch_log"));
      addGeneratorRecipe(Items.STRIPPED_OAK_LOG, 6, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.STRIPPED_OAK_LOG), has(Items.STRIPPED_OAK_LOG))
        .save(writer, new DegrassiLocation("generators/combustion/stripped_oak_log"));
      addGeneratorRecipe(Items.STRIPPED_DARK_OAK_LOG, 6, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.STRIPPED_DARK_OAK_LOG), has(Items.STRIPPED_DARK_OAK_LOG))
        .save(writer, new DegrassiLocation("generators/combustion/stripped_dark_oak_log"));
      addGeneratorRecipe(Items.STRIPPED_JUNGLE_LOG, 6, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.STRIPPED_JUNGLE_LOG), has(Items.STRIPPED_JUNGLE_LOG))
        .save(writer, new DegrassiLocation("generators/combustion/stripped_jungle_log"));
      addGeneratorRecipe(Items.STRIPPED_MANGROVE_LOG, 6, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.STRIPPED_MANGROVE_LOG), has(Items.STRIPPED_MANGROVE_LOG))
        .save(writer, new DegrassiLocation("generators/combustion/stripped_mangrove_log"));
      addGeneratorRecipe(Items.STRIPPED_SPRUCE_LOG, 6, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.STRIPPED_SPRUCE_LOG), has(Items.STRIPPED_SPRUCE_LOG))
        .save(writer, new DegrassiLocation("generators/combustion/stripped_spruce_log"));
    }
    // PLANKS
    {
      addGeneratorRecipe(Items.ACACIA_PLANKS, 4, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.ACACIA_PLANKS), has(Items.ACACIA_PLANKS))
        .save(writer, new DegrassiLocation("generators/combustion/acacia_planks"));
      addGeneratorRecipe(Items.BIRCH_PLANKS, 4, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.BIRCH_PLANKS), has(Items.BIRCH_PLANKS))
        .save(writer, new DegrassiLocation("generators/combustion/birch_planks"));
      addGeneratorRecipe(Items.OAK_PLANKS, 4, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.OAK_PLANKS), has(Items.OAK_PLANKS))
        .save(writer, new DegrassiLocation("generators/combustion/oak_planks"));
      addGeneratorRecipe(Items.DARK_OAK_PLANKS, 4, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.DARK_OAK_PLANKS), has(Items.DARK_OAK_PLANKS))
        .save(writer, new DegrassiLocation("generators/combustion/dark_oak_planks"));
      addGeneratorRecipe(Items.JUNGLE_PLANKS, 4, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.JUNGLE_PLANKS), has(Items.JUNGLE_PLANKS))
        .save(writer, new DegrassiLocation("generators/combustion/jungle_planks"));
      addGeneratorRecipe(Items.MANGROVE_PLANKS, 4, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.MANGROVE_PLANKS), has(Items.MANGROVE_PLANKS))
        .save(writer, new DegrassiLocation("generators/combustion/mangrove_planks"));
      addGeneratorRecipe(Items.SPRUCE_PLANKS, 4, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.SPRUCE_PLANKS), has(Items.SPRUCE_PLANKS))
        .save(writer, new DegrassiLocation("generators/combustion/spruce_planks"));
    }
    // BUTTONS
    {
      addGeneratorRecipe(Items.ACACIA_BUTTON, 1, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.ACACIA_BUTTON), has(Items.ACACIA_BUTTON))
        .save(writer, new DegrassiLocation("generators/combustion/acacia_button"));
      addGeneratorRecipe(Items.BIRCH_BUTTON, 1, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.BIRCH_BUTTON), has(Items.BIRCH_BUTTON))
        .save(writer, new DegrassiLocation("generators/combustion/birch_button"));
      addGeneratorRecipe(Items.OAK_BUTTON, 1, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.OAK_BUTTON), has(Items.OAK_BUTTON))
        .save(writer, new DegrassiLocation("generators/combustion/oak_button"));
      addGeneratorRecipe(Items.DARK_OAK_BUTTON, 1, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.DARK_OAK_BUTTON), has(Items.DARK_OAK_BUTTON))
        .save(writer, new DegrassiLocation("generators/combustion/dark_oak_button"));
      addGeneratorRecipe(Items.JUNGLE_BUTTON, 1, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.JUNGLE_BUTTON), has(Items.JUNGLE_BUTTON))
        .save(writer, new DegrassiLocation("generators/combustion/jungle_button"));
      addGeneratorRecipe(Items.MANGROVE_BUTTON, 1, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.MANGROVE_BUTTON), has(Items.MANGROVE_BUTTON))
        .save(writer, new DegrassiLocation("generators/combustion/mangrove_button"));
      addGeneratorRecipe(Items.SPRUCE_BUTTON, 1, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.SPRUCE_BUTTON), has(Items.SPRUCE_BUTTON))
        .save(writer, new DegrassiLocation("generators/combustion/spruce_button"));
    }
    // PRESSURE PLATES
    {
      addGeneratorRecipe(Items.ACACIA_PRESSURE_PLATE, 2, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.ACACIA_PRESSURE_PLATE), has(Items.ACACIA_PRESSURE_PLATE))
        .save(writer, new DegrassiLocation("generators/combustion/acacia_pressure_plate"));
      addGeneratorRecipe(Items.BIRCH_PRESSURE_PLATE, 2, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.BIRCH_PRESSURE_PLATE), has(Items.BIRCH_PRESSURE_PLATE))
        .save(writer, new DegrassiLocation("generators/combustion/birch_pressure_plate"));
      addGeneratorRecipe(Items.OAK_PRESSURE_PLATE, 2, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.OAK_PRESSURE_PLATE), has(Items.OAK_PRESSURE_PLATE))
        .save(writer, new DegrassiLocation("generators/combustion/oak_pressure_plate"));
      addGeneratorRecipe(Items.DARK_OAK_PRESSURE_PLATE, 2, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.DARK_OAK_PRESSURE_PLATE), has(Items.DARK_OAK_PRESSURE_PLATE))
        .save(writer, new DegrassiLocation("generators/combustion/dark_oak_pressure_plate"));
      addGeneratorRecipe(Items.JUNGLE_PRESSURE_PLATE, 2, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.JUNGLE_PRESSURE_PLATE), has(Items.JUNGLE_PRESSURE_PLATE))
        .save(writer, new DegrassiLocation("generators/combustion/jungle_pressure_plate"));
      addGeneratorRecipe(Items.MANGROVE_PRESSURE_PLATE, 2, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.MANGROVE_PRESSURE_PLATE), has(Items.MANGROVE_PRESSURE_PLATE))
        .save(writer, new DegrassiLocation("generators/combustion/mangrove_pressure_plate"));
      addGeneratorRecipe(Items.SPRUCE_PRESSURE_PLATE, 2, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.SPRUCE_PRESSURE_PLATE), has(Items.SPRUCE_PRESSURE_PLATE))
        .save(writer, new DegrassiLocation("generators/combustion/spruce_pressure_plate"));
    }
    // DOORS
    {
      addGeneratorRecipe(Items.ACACIA_DOOR, 6, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.ACACIA_DOOR), has(Items.ACACIA_DOOR))
        .save(writer, new DegrassiLocation("generators/combustion/acacia_door"));
      addGeneratorRecipe(Items.BIRCH_DOOR, 6, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.BIRCH_DOOR), has(Items.BIRCH_DOOR))
        .save(writer, new DegrassiLocation("generators/combustion/birch_door"));
      addGeneratorRecipe(Items.OAK_DOOR, 6, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.OAK_DOOR), has(Items.OAK_DOOR))
        .save(writer, new DegrassiLocation("generators/combustion/oak_door"));
      addGeneratorRecipe(Items.DARK_OAK_DOOR, 6, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.DARK_OAK_DOOR), has(Items.DARK_OAK_DOOR))
        .save(writer, new DegrassiLocation("generators/combustion/dark_oak_door"));
      addGeneratorRecipe(Items.JUNGLE_DOOR, 6, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.JUNGLE_DOOR), has(Items.JUNGLE_DOOR))
        .save(writer, new DegrassiLocation("generators/combustion/jungle_door"));
      addGeneratorRecipe(Items.MANGROVE_DOOR, 6, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.MANGROVE_DOOR), has(Items.MANGROVE_DOOR))
        .save(writer, new DegrassiLocation("generators/combustion/mangrove_door"));
      addGeneratorRecipe(Items.SPRUCE_DOOR, 6, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.SPRUCE_DOOR), has(Items.SPRUCE_DOOR))
        .save(writer, new DegrassiLocation("generators/combustion/spruce_door"));
    }
    // TRAP DOORS
    {
      addGeneratorRecipe(Items.ACACIA_TRAPDOOR, 6, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.ACACIA_TRAPDOOR), has(Items.ACACIA_TRAPDOOR))
        .save(writer, new DegrassiLocation("generators/combustion/acacia_trapdoor"));
      addGeneratorRecipe(Items.BIRCH_TRAPDOOR, 6, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.BIRCH_TRAPDOOR), has(Items.BIRCH_TRAPDOOR))
        .save(writer, new DegrassiLocation("generators/combustion/birch_trapdoor"));
      addGeneratorRecipe(Items.OAK_TRAPDOOR, 6, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.OAK_TRAPDOOR), has(Items.OAK_TRAPDOOR))
        .save(writer, new DegrassiLocation("generators/combustion/oak_trapdoor"));
      addGeneratorRecipe(Items.DARK_OAK_TRAPDOOR, 6, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.DARK_OAK_TRAPDOOR), has(Items.DARK_OAK_TRAPDOOR))
        .save(writer, new DegrassiLocation("generators/combustion/dark_oak_trapdoor"));
      addGeneratorRecipe(Items.JUNGLE_TRAPDOOR, 6, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.JUNGLE_TRAPDOOR), has(Items.JUNGLE_TRAPDOOR))
        .save(writer, new DegrassiLocation("generators/combustion/jungle_trapdoor"));
      addGeneratorRecipe(Items.MANGROVE_TRAPDOOR, 6, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.MANGROVE_TRAPDOOR), has(Items.MANGROVE_TRAPDOOR))
        .save(writer, new DegrassiLocation("generators/combustion/mangrove_trapdoor"));
      addGeneratorRecipe(Items.SPRUCE_TRAPDOOR, 6, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.SPRUCE_TRAPDOOR), has(Items.SPRUCE_TRAPDOOR))
        .save(writer, new DegrassiLocation("generators/combustion/spruce_trapdoor"));
    }
    // STAIRS
    {
      addGeneratorRecipe(Items.ACACIA_STAIRS, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.ACACIA_STAIRS), has(Items.ACACIA_STAIRS))
        .save(writer, new DegrassiLocation("generators/combustion/acacia_stairs"));
      addGeneratorRecipe(Items.BIRCH_STAIRS, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.BIRCH_STAIRS), has(Items.BIRCH_STAIRS))
        .save(writer, new DegrassiLocation("generators/combustion/birch_stairs"));
      addGeneratorRecipe(Items.OAK_STAIRS, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.OAK_STAIRS), has(Items.OAK_STAIRS))
        .save(writer, new DegrassiLocation("generators/combustion/oak_stairs"));
      addGeneratorRecipe(Items.DARK_OAK_STAIRS, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.DARK_OAK_STAIRS), has(Items.DARK_OAK_STAIRS))
        .save(writer, new DegrassiLocation("generators/combustion/dark_oak_stairs"));
      addGeneratorRecipe(Items.JUNGLE_STAIRS, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.JUNGLE_STAIRS), has(Items.JUNGLE_STAIRS))
        .save(writer, new DegrassiLocation("generators/combustion/jungle_stairs"));
      addGeneratorRecipe(Items.MANGROVE_STAIRS, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.MANGROVE_STAIRS), has(Items.MANGROVE_STAIRS))
        .save(writer, new DegrassiLocation("generators/combustion/mangrove_stairs"));
      addGeneratorRecipe(Items.SPRUCE_STAIRS, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.SPRUCE_STAIRS), has(Items.SPRUCE_STAIRS))
        .save(writer, new DegrassiLocation("generators/combustion/spruce_stairs"));
    }
    // FENCES
    {
      addGeneratorRecipe(Items.ACACIA_FENCE, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.ACACIA_FENCE), has(Items.ACACIA_FENCE))
        .save(writer, new DegrassiLocation("generators/combustion/acacia_fence"));
      addGeneratorRecipe(Items.BIRCH_FENCE, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.BIRCH_FENCE), has(Items.BIRCH_FENCE))
        .save(writer, new DegrassiLocation("generators/combustion/birch_fence"));
      addGeneratorRecipe(Items.OAK_FENCE, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.OAK_FENCE), has(Items.OAK_FENCE))
        .save(writer, new DegrassiLocation("generators/combustion/oak_fence"));
      addGeneratorRecipe(Items.DARK_OAK_FENCE, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.DARK_OAK_FENCE), has(Items.DARK_OAK_FENCE))
        .save(writer, new DegrassiLocation("generators/combustion/dark_oak_fence"));
      addGeneratorRecipe(Items.JUNGLE_FENCE, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.JUNGLE_FENCE), has(Items.JUNGLE_FENCE))
        .save(writer, new DegrassiLocation("generators/combustion/jungle_fence"));
      addGeneratorRecipe(Items.MANGROVE_FENCE, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.MANGROVE_FENCE), has(Items.MANGROVE_FENCE))
        .save(writer, new DegrassiLocation("generators/combustion/mangrove_fence"));
      addGeneratorRecipe(Items.SPRUCE_FENCE, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.SPRUCE_FENCE), has(Items.SPRUCE_FENCE))
        .save(writer, new DegrassiLocation("generators/combustion/spruce_fence"));
    }
    // FENCE GATES
    {
      addGeneratorRecipe(Items.ACACIA_FENCE_GATE, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.ACACIA_FENCE_GATE), has(Items.ACACIA_FENCE_GATE))
        .save(writer, new DegrassiLocation("generators/combustion/acacia_fence_gate"));
      addGeneratorRecipe(Items.BIRCH_FENCE_GATE, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.BIRCH_FENCE_GATE), has(Items.BIRCH_FENCE_GATE))
        .save(writer, new DegrassiLocation("generators/combustion/birch_fence_gate"));
      addGeneratorRecipe(Items.OAK_FENCE_GATE, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.OAK_FENCE_GATE), has(Items.OAK_FENCE_GATE))
        .save(writer, new DegrassiLocation("generators/combustion/oak_fence_gate"));
      addGeneratorRecipe(Items.DARK_OAK_FENCE_GATE, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.DARK_OAK_FENCE_GATE), has(Items.DARK_OAK_FENCE_GATE))
        .save(writer, new DegrassiLocation("generators/combustion/dark_oak_fence_gate"));
      addGeneratorRecipe(Items.JUNGLE_FENCE_GATE, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.JUNGLE_FENCE_GATE), has(Items.JUNGLE_FENCE_GATE))
        .save(writer, new DegrassiLocation("generators/combustion/jungle_fence_gate"));
      addGeneratorRecipe(Items.MANGROVE_FENCE_GATE, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.MANGROVE_FENCE_GATE), has(Items.MANGROVE_FENCE_GATE))
        .save(writer, new DegrassiLocation("generators/combustion/mangrove_fence_gate"));
      addGeneratorRecipe(Items.SPRUCE_FENCE_GATE, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.SPRUCE_FENCE_GATE), has(Items.SPRUCE_FENCE_GATE))
        .save(writer, new DegrassiLocation("generators/combustion/spruce_fence_gate"));
    }
    // SIGNS
    {
      addGeneratorRecipe(Items.ACACIA_SIGN, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.ACACIA_SIGN), has(Items.ACACIA_SIGN))
        .save(writer, new DegrassiLocation("generators/combustion/acacia_sign"));
      addGeneratorRecipe(Items.BIRCH_SIGN, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.BIRCH_SIGN), has(Items.BIRCH_SIGN))
        .save(writer, new DegrassiLocation("generators/combustion/birch_sign"));
      addGeneratorRecipe(Items.OAK_SIGN, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.OAK_SIGN), has(Items.OAK_SIGN))
        .save(writer, new DegrassiLocation("generators/combustion/oak_sign"));
      addGeneratorRecipe(Items.DARK_OAK_SIGN, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.DARK_OAK_SIGN), has(Items.DARK_OAK_SIGN))
        .save(writer, new DegrassiLocation("generators/combustion/dark_oak_sign"));
      addGeneratorRecipe(Items.JUNGLE_SIGN, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.JUNGLE_SIGN), has(Items.JUNGLE_SIGN))
        .save(writer, new DegrassiLocation("generators/combustion/jungle_sign"));
      addGeneratorRecipe(Items.MANGROVE_SIGN, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.MANGROVE_SIGN), has(Items.MANGROVE_SIGN))
        .save(writer, new DegrassiLocation("generators/combustion/mangrove_sign"));
      addGeneratorRecipe(Items.SPRUCE_SIGN, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.SPRUCE_SIGN), has(Items.SPRUCE_SIGN))
        .save(writer, new DegrassiLocation("generators/combustion/spruce_sign"));
    }
    // BOATS
    {
      addGeneratorRecipe(Items.ACACIA_BOAT, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.ACACIA_BOAT), has(Items.ACACIA_BOAT))
        .save(writer, new DegrassiLocation("generators/combustion/acacia_boat"));
      addGeneratorRecipe(Items.BIRCH_BOAT, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.BIRCH_BOAT), has(Items.BIRCH_BOAT))
        .save(writer, new DegrassiLocation("generators/combustion/birch_boat"));
      addGeneratorRecipe(Items.OAK_BOAT, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.OAK_BOAT), has(Items.OAK_BOAT))
        .save(writer, new DegrassiLocation("generators/combustion/oak_boat"));
      addGeneratorRecipe(Items.DARK_OAK_BOAT, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.DARK_OAK_BOAT), has(Items.DARK_OAK_BOAT))
        .save(writer, new DegrassiLocation("generators/combustion/dark_oak_boat"));
      addGeneratorRecipe(Items.JUNGLE_BOAT, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.JUNGLE_BOAT), has(Items.JUNGLE_BOAT))
        .save(writer, new DegrassiLocation("generators/combustion/jungle_boat"));
      addGeneratorRecipe(Items.MANGROVE_BOAT, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.MANGROVE_BOAT), has(Items.MANGROVE_BOAT))
        .save(writer, new DegrassiLocation("generators/combustion/mangrove_boat"));
      addGeneratorRecipe(Items.SPRUCE_BOAT, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.SPRUCE_BOAT), has(Items.SPRUCE_BOAT))
        .save(writer, new DegrassiLocation("generators/combustion/spruce_boat"));
    }
    // SPAPLINGS
    {
      addGeneratorRecipe(Items.ACACIA_SAPLING, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.ACACIA_SAPLING), has(Items.ACACIA_SAPLING))
        .save(writer, new DegrassiLocation("generators/combustion/acacia_sapling"));
      addGeneratorRecipe(Items.BIRCH_SAPLING, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.BIRCH_SAPLING), has(Items.BIRCH_BOAT))
        .save(writer, new DegrassiLocation("generators/combustion/birch_sapling"));
      addGeneratorRecipe(Items.OAK_SAPLING, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.OAK_SAPLING), has(Items.OAK_BOAT))
        .save(writer, new DegrassiLocation("generators/combustion/oak_sapling"));
      addGeneratorRecipe(Items.DARK_OAK_SAPLING, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.DARK_OAK_SAPLING), has(Items.DARK_OAK_BOAT))
        .save(writer, new DegrassiLocation("generators/combustion/dark_oak_sapling"));
      addGeneratorRecipe(Items.JUNGLE_SAPLING, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.JUNGLE_SAPLING), has(Items.JUNGLE_BOAT))
        .save(writer, new DegrassiLocation("generators/combustion/jungle_sapling"));
      addGeneratorRecipe(Items.MANGROVE_PROPAGULE, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.MANGROVE_PROPAGULE), has(Items.MANGROVE_PROPAGULE))
        .save(writer, new DegrassiLocation("generators/combustion/mangrove_sapling"));
      addGeneratorRecipe(Items.SPRUCE_SAPLING, 8, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.SPRUCE_SAPLING), has(Items.SPRUCE_SAPLING))
        .save(writer, new DegrassiLocation("generators/combustion/spruce_sapling"));
    }
    // LEAVES
    {
      addGeneratorRecipe(Items.ACACIA_LEAVES, 1, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.ACACIA_LEAVES), has(Items.ACACIA_LEAVES))
        .save(writer, new DegrassiLocation("generators/combustion/acacia_leaves"));
      addGeneratorRecipe(Items.BIRCH_LEAVES, 1, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.BIRCH_LEAVES), has(Items.BIRCH_LEAVES))
        .save(writer, new DegrassiLocation("generators/combustion/birch_leaves"));
      addGeneratorRecipe(Items.OAK_LEAVES, 1, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.OAK_LEAVES), has(Items.OAK_LEAVES))
        .save(writer, new DegrassiLocation("generators/combustion/oak_leaves"));
      addGeneratorRecipe(Items.DARK_OAK_LEAVES, 1, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.DARK_OAK_LEAVES), has(Items.DARK_OAK_LEAVES))
        .save(writer, new DegrassiLocation("generators/combustion/dark_oak_leaves"));
      addGeneratorRecipe(Items.JUNGLE_LEAVES, 1, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.JUNGLE_LEAVES), has(Items.JUNGLE_LEAVES))
        .save(writer, new DegrassiLocation("generators/combustion/jungle_leaves"));
      addGeneratorRecipe(Items.MANGROVE_LEAVES, 1, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.MANGROVE_LEAVES), has(Items.MANGROVE_LEAVES))
        .save(writer, new DegrassiLocation("generators/combustion/mangrove_leaves"));
      addGeneratorRecipe(Items.SPRUCE_LEAVES, 1, 100, BlockRegister.COMBUSTION_GENERATOR.getId().toString())
        .unlockedBy(getHasName(Items.SPRUCE_LEAVES), has(Items.SPRUCE_LEAVES))
        .save(writer, new DegrassiLocation("generators/combustion/spruce_leaves"));
    }
  }

  private void addFurnaceRecipes(@NotNull Consumer<FinishedRecipe> writer){
    addFurnaceRecipe(Items.COAL, Items.DIAMOND, 100, 20, 10.0f)
      .unlockedBy(getHasName(Items.COAL), has(Items.COAL))
      .save(writer, new DegrassiLocation("furnace/diamond_from_coal"));
  }

  private void addMelterRecipes(@NotNull Consumer<FinishedRecipe> writer) {
    addMelterRecipe(Fluids.LAVA, 500, 1000, 1000, Items.COBBLESTONE)
      .unlockedBy(getHasName(Items.COBBLESTONE), has(Items.COBBLESTONE))
      .save(writer, new DegrassiLocation("melter/cobblestone_to_lava"));
    addMelterRecipe(Fluids.LAVA, 1000, 1000, Items.STONE)
      .unlockedBy(getHasName(Items.STONE), has(Items.STONE))
      .save(writer, new DegrassiLocation("melter/stone_to_lava"));
    addMelterRecipe(FluidRegister.get("molten_red_matter"), 1000, 500, ItemRegister.RED_MATTER.get())
      .unlockedBy(getHasName(ItemRegister.RED_MATTER.get()), has(ItemRegister.RED_MATTER.get()))
      .save(writer, new DegrassiLocation("melter/red_matter_to_molten_red_matter"));
  }

  private void addUpgradeMakerRecipes(@NotNull Consumer<FinishedRecipe> writer) {
    addUpgradeMakerRecipe(
      ItemRegister.SPEED_UPGRADE.get(),
      2000,
      5000,
      ItemRegister.UPGRADE_BASE.get(),
      ItemRegister.GOLD_COIN.get(),
      Fluids.LAVA
    )
      .unlockedBy(getHasName(ItemRegister.UPGRADE_BASE.get()), has(ItemRegister.UPGRADE_BASE.get()))
      .unlockedBy(getHasName(ItemRegister.GOLD_COIN.get()), has(ItemRegister.GOLD_COIN.get()))
      .save(writer, new DegrassiLocation("upgrade_maker/speed_upgrade"));
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

  private DegrassiGeneratorRecipeBuilder addGeneratorRecipe(Item input, int inputAmount, int energy, int time, List<String> machines) {
    return new DegrassiGeneratorRecipeBuilder(energy)
      .time(time)
      .input(input, inputAmount)
      .machines(machines);
  }

  private DegrassiGeneratorRecipeBuilder addGeneratorRecipe(Item input, int energy, int time, List<String> machines) {
    return addGeneratorRecipe(input, 1, energy, time, machines);
  }

  private DegrassiGeneratorRecipeBuilder addGeneratorRecipe(Item input, int amount, int energy, int time, String machine) {
    return addGeneratorRecipe(input, amount, energy, time, List.of(machine));
  }

  private DegrassiGeneratorRecipeBuilder addGeneratorRecipe(Item input, int energy, int time, String machine) {
    return addGeneratorRecipe(input, 1, energy, time, machine);
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
