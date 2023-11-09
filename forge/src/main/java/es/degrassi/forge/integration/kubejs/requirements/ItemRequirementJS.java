package es.degrassi.forge.integration.kubejs.requirements;

import es.degrassi.forge.api.ingredient.ItemIngredient;
import es.degrassi.forge.api.ingredient.ItemTagIngredient;
import es.degrassi.forge.integration.kubejs.recipes.builder.AbstractRecipeBuilderJS;
import es.degrassi.forge.integration.kubejs.recipes.builder.RecipeBuilderJS;
import es.degrassi.forge.requirements.IRequirement;
import es.degrassi.forge.requirements.ItemRequirement;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public interface ItemRequirementJS<T extends AbstractRecipeBuilderJS<?, ?>> extends RecipeBuilderJS<T> {
  default T requireItem(ItemStack item) {
    return this.requireItem(item, 0);
  }

  default T requireItem(@NotNull ItemStack stack, int slot) {
    return this.addRequirement(new ItemRequirement(new ItemIngredient(stack.getItem()), stack.getCount(), slot, IRequirement.ModeIO.INPUT));
  }

  default T requireItemTag(String tag, int amount) {
    return this.requireItemTag(tag, amount, 0);
  }

  default T requireItemTag(String tag, int amount, int slot) {
    try {
      return this.addRequirement(new ItemRequirement(ItemTagIngredient.create(tag), amount, slot, IRequirement.ModeIO.INPUT));
    } catch (IllegalArgumentException e) {
      return error(e.getMessage());
    }
  }

  default T produceItem(ItemStack stack) {
    return this.produceItem(stack, 0);
  }

  default T produceItem(@NotNull ItemStack stack, int slot) {
    return this.addRequirement(new ItemRequirement(new ItemIngredient(stack.getItem()), stack.getCount(), slot, IRequirement.ModeIO.OUTPUT));
  }
}
