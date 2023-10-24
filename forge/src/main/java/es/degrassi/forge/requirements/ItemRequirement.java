package es.degrassi.forge.requirements;

import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.ingredient.IIngredient;
import es.degrassi.forge.api.ingredient.ItemTagIngredient;
import es.degrassi.forge.init.handlers.ItemWrapperHandler;
import es.degrassi.forge.init.recipe.CraftingResult;
import es.degrassi.forge.init.registration.RequirementRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemRequirement implements IRequirement<ItemWrapperHandler> {

  public static final NamedCodec<ItemRequirement> CODEC = NamedCodec.record(itemRequirementInstance ->
    itemRequirementInstance.group(
      ModeIO.CODEC.fieldOf("mode").forGetter(ItemRequirement::getMode),
      IIngredient.ITEM.fieldOf("item").forGetter(requirement -> requirement.item),
      NamedCodec.INT.fieldOf("amount").forGetter(requirement -> requirement.count),
      NamedCodec.INT.optionalFieldOf("slot", 0).forGetter(requirement -> requirement.slot)
    ).apply(itemRequirementInstance, (mode, item, amount, slot) -> new ItemRequirement(item, amount, slot, mode)), "Item requirement"
  );
  private final IIngredient<Item> item;
  private final int count;
  private final int slot;
  private final ModeIO mode;

  public ItemRequirement(IIngredient<Item> item, int count, int slot, ModeIO mode) {
    if(mode == ModeIO.OUTPUT && item instanceof ItemTagIngredient)
      throw new IllegalArgumentException("You can't use a Tag for an Output Item Requirement");
    this.item = item;
    this.count = count;
    this.slot = slot;
    this.mode = mode;
  }

  @Override
  public RequirementType<ItemRequirement> getType() {
    return RequirementRegistry.ITEM_REQUIREMENT.get();
  }

  @Override
  public boolean test(ItemWrapperHandler handler, ICraftingContext context) {
    int amount = (int) context.getIntegerModifiedValue(this.count, this, null);

    if(getMode() == ModeIO.INPUT) {
      return this.item.getAll().stream().mapToInt(item -> item.getDefaultInstance().getCount()).sum() >= amount;
    } else {
      if(this.item.getAll().get(0) != null)
        return handler.getStackInSlot(this.slot).getMaxStackSize() - handler.getStackInSlot(this.slot).getCount() >= amount;
      else throw new IllegalStateException("Can't use output item requirement with item tag");
    }
  }

  @Override
  public CraftingResult processStart(ItemWrapperHandler handler, ICraftingContext context) {
    int amount = (int)context.getIntegerModifiedValue(this.count, this, null);
    if(getMode() == ModeIO.INPUT) {
      int maxExtract = this.item.getAll().stream().mapToInt(item -> item.asItem().getDefaultInstance().getCount()).sum();
      if(maxExtract >= amount) {
        int toExtract = amount;
        for (Item item : this.item.getAll()) {
          int canExtract = item.asItem().getDefaultInstance().getCount();
          if(canExtract > 0) {
            canExtract = Math.min(canExtract, toExtract);
            handler.extractItem(this.slot, canExtract, false);
            toExtract -= canExtract;
            if(toExtract == 0)
              return CraftingResult.success();
          }
        }
      }
      return CraftingResult.error(Component.translatable("degrassi.requirements.item.error.input", this.item.toString(), amount, maxExtract));
    }
    return CraftingResult.pass();
  }

  @Override
  public CraftingResult processEnd(ItemWrapperHandler component, ICraftingContext context) {
    int amount = (int) context.getIntegerModifiedValue(this.count, this, null);
    if(getMode() == ModeIO.OUTPUT) {
      if(this.item.getAll().get(0) != null) {
        Item item = this.item.getAll().get(0);
        int canInsert = component.getStackInSlot(this.slot).getMaxStackSize() - component.getStackInSlot(this.slot).getCount();
        if(canInsert >= amount) {
          component.insertItem(this.slot, new ItemStack(item, count), false);
          return CraftingResult.success();
        }
        return CraftingResult.error(Component.translatable("degrassi.requirements.item.error.output", amount, Component.translatable(item.getDescriptionId())));
      } else throw new IllegalStateException("Can't use output item requirement with item tag");
    }
    return CraftingResult.pass();
  }

  public ModeIO getMode() {
    return this.mode;
  }

  public ItemStack getItem() {
    return new ItemStack(item.getAll().get(0), count);
  }
}
