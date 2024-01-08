package es.degrassi.forge.integration.jei;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.init.gui.screen.FurnaceScreen;
import es.degrassi.forge.init.gui.screen.MelterScreen;
import es.degrassi.forge.init.gui.screen.UpgradeMakerScreen;
import es.degrassi.forge.init.gui.screen.generators.JewelryGeneratorScreen;
import es.degrassi.forge.init.recipe.helpers.RecipeHelpers;
import es.degrassi.forge.init.recipe.recipes.*;
import es.degrassi.forge.init.registration.BlockRegister;
import es.degrassi.forge.init.registration.ItemRegister;
import es.degrassi.forge.integration.config.DegrassiConfig;
import es.degrassi.forge.integration.jei.categories.*;
import es.degrassi.forge.integration.jei.ingredients.DegrassiTypes;
import es.degrassi.forge.integration.jei.renderer.EnergyJeiRenderer;
import es.degrassi.forge.integration.jei.renderer.ProgressJeiRenderer;
import es.degrassi.forge.integration.jei.renderer.helpers.EnergyIngredientHelper;
import es.degrassi.forge.integration.jei.renderer.helpers.ProgressIngredientHelper;
import es.degrassi.forge.util.TextureSizeHelper;
import java.util.*;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.*;

import static es.degrassi.forge.integration.jei.DegrassiJEIRecipeTypes.*;

@JeiPlugin
@SuppressWarnings({"unused, unchecked"})
public class JEIDegrassiPlugin implements IModPlugin {

  @Override
  public @NotNull ResourceLocation getPluginUid() {
    return new DegrassiLocation("jei_plugin");
  }

  @Override
  public void registerIngredients(@NotNull IModIngredientRegistration registry) {
    registry.register(DegrassiTypes.ENERGY, new ArrayList<>(), new EnergyIngredientHelper(), new EnergyJeiRenderer(16, 16));
    registry.register(DegrassiTypes.PROGRESS, new ArrayList<>(), new ProgressIngredientHelper(), new ProgressJeiRenderer(16, 16));
  }

  @Override
  public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
    registerFurnaceCatalysts(registration);
    registerMelterCatalysts(registration);
    registerUpgradeMakerCatalysts(registration);
    registerGeneratorsCatalysts(registration);
  }

  @Override
  public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
    registration.addRecipeCategories(
      new FurnaceRecipeCategory(registration.getJeiHelpers(), BlockRegister.IRON_FURNACE_BLOCK.get()),
      new FurnaceRecipeCategory(registration.getJeiHelpers(), BlockRegister.GOLD_FURNACE_BLOCK.get()),
      new FurnaceRecipeCategory(registration.getJeiHelpers(), BlockRegister.DIAMOND_FURNACE_BLOCK.get()),
      new FurnaceRecipeCategory(registration.getJeiHelpers(), BlockRegister.EMERALD_FURNACE_BLOCK.get()),
      new FurnaceRecipeCategory(registration.getJeiHelpers(), BlockRegister.NETHERITE_FURNACE_BLOCK.get())
    );
    registration.addRecipeCategories(
      new MelterRecipeCategory(registration.getJeiHelpers())
    );
    registration.addRecipeCategories(
      new UpgradeMakerRecipeCategory(registration.getJeiHelpers())
    );
    registration.addRecipeCategories(
      new GeneratorRecipeCategory(
        registration.getJeiHelpers(),
        BlockRegister.JEWELRY_GENERATOR.get(),
        new DegrassiLocation("textures/gui/jei/jewelry_generator_gui.png"),
        new DegrassiLocation("textures/gui/jei/jewelry_generator_progress_filled.png")
      )
    );
  }

  @Override
  public void registerRecipes(@NotNull IRecipeRegistration registration) {

    RecipeHelpers.init();

    registration.addRecipes(IRON_FURNACE_TYPE, RecipeHelpers.FURNACE.recipes);
    registration.addRecipes(GOLD_FURNACE_TYPE, RecipeHelpers.FURNACE.recipes);
    registration.addRecipes(DIAMOND_FURNACE_TYPE, RecipeHelpers.FURNACE.recipes);
    registration.addRecipes(EMERALD_FURNACE_TYPE, RecipeHelpers.FURNACE.recipes);
    registration.addRecipes(NETHERITE_FURNACE_TYPE, RecipeHelpers.FURNACE.recipes);
    registration.addRecipes(MELTER_TYPE, RecipeHelpers.MELTER.recipes);
    registration.addRecipes(UPGRADE_MAKER_TYPE, RecipeHelpers.UPGRADE_MAKER.recipes);

    List<GeneratorRecipe> jewelryRecipes = RecipeHelpers.GENERATORS.getRecipesForMachine(BlockRegister.JEWELRY_GENERATOR.get());
    registration.addRecipes(JEWELRY_GENERATOR_TYPE, jewelryRecipes);

    registerPanelInfo(registration);
    registerUpgradesInfo(registration);
  }

  @Override
  public void registerGuiHandlers(@NotNull IGuiHandlerRegistration registration) {
    registerFurnaceHandler(registration);
    registerMelterHandler(registration);
    registerUpgradeMakerHandler(registration);
    registerJewelryHandler(registration);
  }

  private void registerJewelryHandler(@NotNull IGuiHandlerRegistration registration) {
    registration.addRecipeClickArea(
      JewelryGeneratorScreen.class,
      86,
      46,
      TextureSizeHelper.getTextureWidth(JewelryGeneratorScreen.FILLED_ARROW),
      TextureSizeHelper.getTextureHeight(JewelryGeneratorScreen.FILLED_ARROW),
      JEWELRY_GENERATOR_TYPE
    );
  }

  private void registerUpgradeMakerHandler(@NotNull IGuiHandlerRegistration registration) {
    registration.addRecipeClickArea(
      UpgradeMakerScreen.class,
      77,
      49,
      TextureSizeHelper.getTextureWidth(UpgradeMakerScreen.FILLED_ARROW),
      TextureSizeHelper.getTextureHeight(UpgradeMakerScreen.FILLED_ARROW),
      UPGRADE_MAKER_TYPE
    );
  }

  private void registerMelterHandler(@NotNull IGuiHandlerRegistration registration) {
    registration.addRecipeClickArea(
      MelterScreen.class,
      84,
      48,
      TextureSizeHelper.getTextureWidth(MelterScreen.FILLED_ARROW),
      TextureSizeHelper.getTextureHeight(MelterScreen.FILLED_ARROW),
      MELTER_TYPE
    );
  }

  private void registerFurnaceHandler(@NotNull IGuiHandlerRegistration registration) {
    registration.addRecipeClickArea(
      FurnaceScreen.class,
      66,
      33,
      TextureSizeHelper.getTextureWidth(FurnaceScreen.FILLED_ARROW),
      TextureSizeHelper.getTextureHeight(FurnaceScreen.FILLED_ARROW),
      IRON_FURNACE_TYPE,
      GOLD_FURNACE_TYPE,
      DIAMOND_FURNACE_TYPE,
      EMERALD_FURNACE_TYPE,
      NETHERITE_FURNACE_TYPE
    );
  }

  private void registerGeneratorsCatalysts(@NotNull IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalyst(
      new ItemStack(BlockRegister.JEWELRY_GENERATOR.get()),
      JEWELRY_GENERATOR_TYPE
    );
  }

  private void registerFurnaceCatalysts(@NotNull IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalyst(
      new ItemStack(BlockRegister.IRON_FURNACE_BLOCK.get()),
      IRON_FURNACE_TYPE,
      GOLD_FURNACE_TYPE,
      DIAMOND_FURNACE_TYPE,
      EMERALD_FURNACE_TYPE,
      NETHERITE_FURNACE_TYPE,
      RecipeTypes.SMELTING,
      RecipeTypes.BLASTING,
      RecipeTypes.SMOKING
    );
    registration.addRecipeCatalyst(
      new ItemStack(BlockRegister.GOLD_FURNACE_BLOCK.get()),
      IRON_FURNACE_TYPE,
      GOLD_FURNACE_TYPE,
      DIAMOND_FURNACE_TYPE,
      EMERALD_FURNACE_TYPE,
      NETHERITE_FURNACE_TYPE,
      RecipeTypes.SMELTING,
      RecipeTypes.BLASTING,
      RecipeTypes.SMOKING
    );
    registration.addRecipeCatalyst(
      new ItemStack(BlockRegister.DIAMOND_FURNACE_BLOCK.get()),
      IRON_FURNACE_TYPE,
      GOLD_FURNACE_TYPE,
      DIAMOND_FURNACE_TYPE,
      EMERALD_FURNACE_TYPE,
      NETHERITE_FURNACE_TYPE,
      RecipeTypes.SMELTING,
      RecipeTypes.BLASTING,
      RecipeTypes.SMOKING
    );
    registration.addRecipeCatalyst(
      new ItemStack(BlockRegister.EMERALD_FURNACE_BLOCK.get()),
      IRON_FURNACE_TYPE,
      GOLD_FURNACE_TYPE,
      DIAMOND_FURNACE_TYPE,
      EMERALD_FURNACE_TYPE,
      NETHERITE_FURNACE_TYPE,
      RecipeTypes.SMELTING,
      RecipeTypes.BLASTING,
      RecipeTypes.SMOKING
    );
    registration.addRecipeCatalyst(
      new ItemStack(BlockRegister.NETHERITE_FURNACE_BLOCK.get()),
      IRON_FURNACE_TYPE,
      GOLD_FURNACE_TYPE,
      DIAMOND_FURNACE_TYPE,
      EMERALD_FURNACE_TYPE,
      NETHERITE_FURNACE_TYPE,
      RecipeTypes.SMELTING,
      RecipeTypes.BLASTING,
      RecipeTypes.SMOKING
    );
  }

  private void registerMelterCatalysts(@NotNull IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalyst(
      new ItemStack(BlockRegister.MELTER_BLOCK.get()),
      MELTER_TYPE
    );
  }

  private void registerUpgradeMakerCatalysts(@NotNull IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalyst(
      new ItemStack(BlockRegister.UPGRADE_MAKER.get()),
      UPGRADE_MAKER_TYPE
    );
  }

  private void registerUpgradesInfo(@NotNull IRecipeRegistration registration) {
    registerPanelUpgradesInfo(registration);
  }

  private void registerPanelUpgradesInfo(@NotNull IRecipeRegistration registration) {
    registration.addIngredientInfo(
      ItemRegister.TRANSFER_UPGRADE.get().getDefaultInstance(),
      VanillaTypes.ITEM_STACK,
      Component.translatable(
        "degrassi.jei.gui.element.upgrades.transfer",
        ItemRegister.TRANSFER_UPGRADE.get().getModifier()
      )
    );
    registration.addIngredientInfo(
      ItemRegister.GENERATION_UPGRADE.get().getDefaultInstance(),
      VanillaTypes.ITEM_STACK,
      Component.translatable(
        "degrassi.jei.gui.element.upgrades.generation",
        ItemRegister.GENERATION_UPGRADE.get().getModifier()
      )
    );
    registration.addIngredientInfo(
      ItemRegister.EFFICIENCY_UPGRADE.get().getDefaultInstance(),
      VanillaTypes.ITEM_STACK,
      Component.translatable(
        "degrassi.jei.gui.element.upgrades.efficiency",
        ItemRegister.EFFICIENCY_UPGRADE.get().getModifier()
      )
    );
    registration.addIngredientInfo(
      ItemRegister.CAPACITY_UPGRADE.get().getDefaultInstance(),
      VanillaTypes.ITEM_STACK,
      Component.translatable(
        "degrassi.jei.gui.element.upgrades.capacity",
        ItemRegister.CAPACITY_UPGRADE.get().getModifier()
      )
    );
  }

  private void registerPanelInfo(@NotNull IRecipeRegistration registration) {
    registerSolarPanelInfo(registration);
  }

  private void registerSolarPanelInfo(@NotNull IRecipeRegistration registration) {
    registration.addIngredientInfo(
      BlockRegister.getDefaultInstance("solar_panel_tier_1"),
      VanillaTypes.ITEM_STACK,
      Component.translatable(
        "degrassi.jei.gui.element.sp",
        Component.translatable(
          "degrassi.jei.gui.element.energy.generation",
          DegrassiConfig.get().solarPanelConfig.sp1_generation,
          Component.translatable("unit.energy.forge")
        ),
        Component.translatable("degrassi.jei.gui.element.sp.solar_position")
      )
    );
    registration.addIngredientInfo(
      BlockRegister.getDefaultInstance("solar_panel_tier_2"),
      VanillaTypes.ITEM_STACK,
      Component.translatable(
        "degrassi.jei.gui.element.sp",
        Component.translatable(
          "degrassi.jei.gui.element.energy.generation",
          DegrassiConfig.get().solarPanelConfig.sp2_generation,
          Component.translatable("unit.energy.forge")
        ),
        Component.translatable("degrassi.jei.gui.element.sp.solar_position")
      )
    );
    registration.addIngredientInfo(
      BlockRegister.getDefaultInstance("solar_panel_tier_3"),
      VanillaTypes.ITEM_STACK,
      Component.translatable(
        "degrassi.jei.gui.element.sp",
        Component.translatable(
          "degrassi.jei.gui.element.energy.generation",
          DegrassiConfig.get().solarPanelConfig.sp3_generation,
          Component.translatable("unit.energy.forge")
        ),
        Component.translatable("degrassi.jei.gui.element.sp.solar_position")
      )
    );
    registration.addIngredientInfo(
      BlockRegister.getDefaultInstance("solar_panel_tier_4"),
      VanillaTypes.ITEM_STACK,
      Component.translatable(
        "degrassi.jei.gui.element.sp",
        Component.translatable(
          "degrassi.jei.gui.element.energy.generation",
          DegrassiConfig.get().solarPanelConfig.sp4_generation,
          Component.translatable("unit.energy.forge")
        ),
        Component.translatable("degrassi.jei.gui.element.sp.solar_position")
      )
    );
    registration.addIngredientInfo(
      BlockRegister.getDefaultInstance("solar_panel_tier_5"),
      VanillaTypes.ITEM_STACK,
      Component.translatable(
        "degrassi.jei.gui.element.sp",
        Component.translatable(
          "degrassi.jei.gui.element.energy.generation",
          DegrassiConfig.get().solarPanelConfig.sp5_generation,
          Component.translatable("unit.energy.forge")
        ),
        Component.translatable("degrassi.jei.gui.element.sp.solar_position")
      )
    );
    registration.addIngredientInfo(
      BlockRegister.getDefaultInstance("solar_panel_tier_6"),
      VanillaTypes.ITEM_STACK,
      Component.translatable(
        "degrassi.jei.gui.element.sp",
        Component.translatable(
          "degrassi.jei.gui.element.energy.generation",
          DegrassiConfig.get().solarPanelConfig.sp6_generation,
          Component.translatable("unit.energy.forge")
        ),
        Component.translatable("degrassi.jei.gui.element.sp.solar_position")
      )
    );
    registration.addIngredientInfo(
      BlockRegister.getDefaultInstance("solar_panel_tier_7"),
      VanillaTypes.ITEM_STACK,
      Component.translatable(
        "degrassi.jei.gui.element.sp",
        Component.translatable(
          "degrassi.jei.gui.element.energy.generation",
          DegrassiConfig.get().solarPanelConfig.sp7_generation,
          Component.translatable("unit.energy.forge")
        ),
        Component.translatable("degrassi.jei.gui.element.sp.solar_position")
      )
    );
    registration.addIngredientInfo(
      BlockRegister.getDefaultInstance("solar_panel_tier_8"),
      VanillaTypes.ITEM_STACK,
      Component.translatable(
        "degrassi.jei.gui.element.sp",
        Component.translatable(
          "degrassi.jei.gui.element.energy.generation",
          DegrassiConfig.get().solarPanelConfig.sp8_generation,
          Component.translatable("unit.energy.forge")
        ),
        Component.translatable("degrassi.jei.gui.element.sp.solar_position")
      )
    );
  }
}
