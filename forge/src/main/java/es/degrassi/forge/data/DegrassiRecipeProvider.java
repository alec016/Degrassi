package es.degrassi.forge.data;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.common.ForgeLocation;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.data.recipes.DegrassiFurnaceRecipeBuilder;
import es.degrassi.forge.data.recipes.DegrassiMelterRecipeBuilder;
import es.degrassi.forge.data.recipes.DegrassiUpgradeMakerRecipeBuilder;
import es.degrassi.forge.init.registration.BlockRegister;
import es.degrassi.forge.init.registration.FluidRegister;
import es.degrassi.forge.init.registration.ItemRegister;
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
