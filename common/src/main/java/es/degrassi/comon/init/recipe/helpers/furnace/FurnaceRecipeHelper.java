package es.degrassi.comon.init.recipe.helpers.furnace;

import es.degrassi.comon.init.entity.furnace.FurnaceEntity;
import es.degrassi.comon.init.recipe.furnace.FurnaceRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FurnaceRecipeHelper {
  public static final List<FurnaceRecipe> recipes = new LinkedList<>();
  public static final Map<ResourceLocation, FurnaceRecipe> recipesMap = new HashMap<>();

  public static void extractEnergy(@NotNull FurnaceEntity entity) {
    entity.ENERGY_STORAGE.setEnergy(entity.ENERGY_STORAGE.getEnergyStored() - entity.energyReq);
  }

  public static boolean hasEnoughEnergy(@NotNull FurnaceEntity entity) {
    return entity.ENERGY_STORAGE.getEnergyStored() >= entity.energyReq;
  }

  public static boolean canInsertItemIntoOutputSlot(@NotNull SimpleContainer inventory, @NotNull ItemStack stack) {
    return inventory.getItem(3).getItem() == stack.getItem() || inventory.getItem(3).isEmpty();
  }

  public static boolean canInsertAmountIntoOutputSlot(@NotNull SimpleContainer inventory) {
    return inventory.getItem(3).getMaxStackSize() > inventory.getItem(3).getCount();
  }

  public static boolean hasRecipe(@NotNull FurnaceEntity entity) {
    Level level = entity.getLevel();
    if (level == null) return false;

    SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
    for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
      inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
    }

    recipes.forEach(recipe -> {
      if (recipe.getIngredients().get(0).test(inventory.getItem(2))) {
        String name = entity.getName().getString();
        if (!recipe.isModified()) {
          if (name.equals(Component.translatable("block.degrassi.iron_furnace").getString())) {
            recipe.setTime(recipe.getTime() * 95 / 100);
          } else if(name.equals(Component.translatable("block.degrassi.gold_furnace").getString())) {
            recipe.setTime(recipe.getTime() * 80 / 100);
          } else if (name.equals(Component.translatable("block.degrassi.diamond_furnace").getString())) {
            recipe.setTime(recipe.getTime() * 60 / 100);
          } else if (name.equals(Component.translatable("block.degrassi.emerald_furnace").getString())) {
            recipe.setTime(recipe.getTime() * 40 / 100);
          } else if (name.equals(Component.translatable("block.degrassi.netherite_furnace").getString())) {
            recipe.setTime(recipe.getTime() * 10 / 100);
          }
        }
        entity.recipe = recipe;
      }
    });

    if (entity.recipe == null) return false;

    boolean can = canInsertAmountIntoOutputSlot(inventory) && canInsertItemIntoOutputSlot(inventory, entity.recipe.getResultItem()) && hasEnoughEnergy(entity);

    if (can) entity.progressStorage.setMaxProgress(entity.recipe.getTime());

    return can;
  }

  public static void craftItem(@NotNull FurnaceEntity entity) {
    entity.itemHandler.extractItem(2, 1, false);
    entity.itemHandler.setStackInSlot(3, new ItemStack(
      entity.recipe.getResultItem().getItem(),
      entity.itemHandler.getStackInSlot(3).getCount() + 1
    ));

    entity.resetProgress();
  }

  public static void init() {
    Level level = Objects.requireNonNull(Minecraft.getInstance().level);
    List<SmeltingRecipe> smelting = level.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING);
    List<BlastingRecipe> blasting = level.getRecipeManager().getAllRecipesFor(RecipeType.BLASTING);
    List<SmokingRecipe> smoking = level.getRecipeManager().getAllRecipesFor(RecipeType.SMOKING);
    List<FurnaceRecipe> furnaceRecipes = level.getRecipeManager().getAllRecipesFor(FurnaceRecipe.Type.INSTANCE);
    List<AbstractCookingRecipe> cookingRecipes = new LinkedList<>(smelting);
    cookingRecipes.addAll(blasting);
    cookingRecipes.addAll(smoking);
    cookingRecipes.forEach(recipe -> {
      FurnaceRecipe r = new FurnaceRecipe(recipe.getId(), recipe.getResultItem(), recipe.getIngredients(), recipe.getCookingTime());
      recipes.add(r);
      recipesMap.put(recipe.getId(), r);
    });
    furnaceRecipes.forEach(recipe -> {
      recipes.add(recipe);
      recipesMap.put(recipe.getId(), recipe);
    });
  }
}
