package es.degrassi.forge.integration.jei;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.init.gui.screen.FurnaceScreen;
import es.degrassi.forge.init.gui.screen.MelterScreen;
import es.degrassi.forge.init.gui.screen.UpgradeMakerScreen;
import es.degrassi.forge.init.gui.screen.generators.JewelryGeneratorScreen;
import es.degrassi.forge.init.recipe.helpers.RecipeHelpers;
import es.degrassi.forge.init.registration.BlockRegister;
import es.degrassi.forge.init.registration.ItemRegister;
import es.degrassi.forge.integration.config.DegrassiConfig;
import es.degrassi.forge.integration.jei.categories.FurnaceRecipeCategory;
import es.degrassi.forge.integration.jei.categories.MelterRecipeCategory;
import es.degrassi.forge.integration.jei.categories.UpgradeMakerRecipeCategory;
import es.degrassi.forge.integration.jei.categories.generators.JewelryGeneratorRecipeCategory;
import es.degrassi.forge.integration.jei.ingredients.DegrassiTypes;
import es.degrassi.forge.integration.jei.renderer.EnergyJeiRenderer;
import es.degrassi.forge.integration.jei.renderer.ProgressJeiRenderer;
import es.degrassi.forge.integration.jei.renderer.helpers.EnergyIngredientHelper;
import es.degrassi.forge.integration.jei.renderer.helpers.ProgressIngredientHelper;
import es.degrassi.forge.util.TextureSizeHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.registration.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import static es.degrassi.forge.integration.jei.DegrassiJEIRecipeTypes.*;

@JeiPlugin
@SuppressWarnings("unused")
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
  }

  @Override
  public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
    registration.addRecipeCategories(
      new FurnaceRecipeCategory(registration.getJeiHelpers()),
      new MelterRecipeCategory(registration.getJeiHelpers()),
      new UpgradeMakerRecipeCategory(registration.getJeiHelpers()),
      new JewelryGeneratorRecipeCategory(registration.getJeiHelpers())
    );
  }

  @Override
  public void registerRecipes(@NotNull IRecipeRegistration registration) {

    RecipeHelpers.init();

    registration.addRecipes(FURNACE_TYPE, RecipeHelpers.FURNACE.recipes);
    registration.addRecipes(MELTER_TYPE, RecipeHelpers.MELTER.recipes);
    registration.addRecipes(UPGRADE_MAKER_TYPE, RecipeHelpers.UPGRADE_MAKER.recipes);
    registration.addRecipes(JEWELRY_GENERATOR_TYPE, RecipeHelpers.JEWELRY_GENERATOR.recipes);

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
      77,
      49,
      TextureSizeHelper.getTextureWidth(UpgradeMakerScreen.FILLED_ARROW),
      TextureSizeHelper.getTextureHeight(UpgradeMakerScreen.FILLED_ARROW),
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
      FURNACE_TYPE
    );
  }

  private void registerFurnaceCatalysts(@NotNull IRecipeCatalystRegistration registration) {
    registration.addRecipeCatalyst(
      new ItemStack(BlockRegister.IRON_FURNACE_BLOCK.get()),
      FURNACE_TYPE,
      RecipeTypes.SMELTING,
      RecipeTypes.BLASTING,
      RecipeTypes.SMOKING
    );
    registration.addRecipeCatalyst(
      new ItemStack(BlockRegister.GOLD_FURNACE_BLOCK.get()),
      FURNACE_TYPE,
      RecipeTypes.SMELTING,
      RecipeTypes.BLASTING,
      RecipeTypes.SMOKING
    );
    registration.addRecipeCatalyst(
      new ItemStack(BlockRegister.DIAMOND_FURNACE_BLOCK.get()),
      FURNACE_TYPE,
      RecipeTypes.SMELTING,
      RecipeTypes.BLASTING,
      RecipeTypes.SMOKING
    );
    registration.addRecipeCatalyst(
      new ItemStack(BlockRegister.EMERALD_FURNACE_BLOCK.get()),
      FURNACE_TYPE,
      RecipeTypes.SMELTING,
      RecipeTypes.BLASTING,
      RecipeTypes.SMOKING
    );
    registration.addRecipeCatalyst(
      new ItemStack(BlockRegister.NETHERITE_FURNACE_BLOCK.get()),
      FURNACE_TYPE,
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
