package es.degrassi.forge.integration.jei;

import es.degrassi.forge.Degrassi;
import es.degrassi.forge.init.recipe.helpers.RecipeHelpers;
import es.degrassi.forge.init.recipe.recipes.FurnaceRecipe;
import es.degrassi.forge.init.registration.BlockRegister;
import es.degrassi.forge.init.registration.ItemRegister;
import es.degrassi.forge.integration.config.DegrassiConfig;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@JeiPlugin
public class JEIDegrassiPlugin implements IModPlugin {
  public static RecipeType<FurnaceRecipe> FURNACE_TYPE = new RecipeType<>(
    FurnaceRecipeCategory.UID,
    FurnaceRecipe.class
  );

  @Override
  public @NotNull ResourceLocation getPluginUid() {
    return new ResourceLocation(Degrassi.MODID, "jei_plugin");
  }

  @Override
  public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
    registerFurnaceCatalysts(registration);
  }

  @Override
  public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
    registration.addRecipeCategories(
      new FurnaceRecipeCategory(registration.getJeiHelpers())
    );
  }

  @Override
  public void registerRecipes(@NotNull IRecipeRegistration registration) {
    // RecipeManager manager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

    RecipeHelpers.FURNACE.init();

    registration.addRecipes(FURNACE_TYPE, RecipeHelpers.FURNACE.recipes);

    registerPanelInfo(registration);
    registerUpgradesInfo(registration);
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
          DegrassiConfig.sp1_generation.get(),
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
          DegrassiConfig.sp2_generation.get(),
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
          DegrassiConfig.sp3_generation.get(),
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
          DegrassiConfig.sp4_generation.get(),
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
          DegrassiConfig.sp5_generation.get(),
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
          DegrassiConfig.sp6_generation.get(),
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
          DegrassiConfig.sp7_generation.get(),
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
          DegrassiConfig.sp8_generation.get(),
          Component.translatable("unit.energy.forge")
        ),
        Component.translatable("degrassi.jei.gui.element.sp.solar_position")
      )
    );
  }
}
