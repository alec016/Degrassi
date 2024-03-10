package es.degrassi.forge.core.common.requirement;

import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.core.common.CraftingResult;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.api.core.common.RequirementMode;
import es.degrassi.forge.api.core.common.RequirementType;
import es.degrassi.forge.api.impl.codec.RegistrarCodec;
import es.degrassi.forge.core.common.component.ItemComponent;
import es.degrassi.forge.core.init.RequirementRegistration;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemRequirement implements IRequirement<ItemComponent> {
  public static final NamedCodec<ItemRequirement> CODEC = NamedCodec.record(
    instance -> instance.group(
      RegistrarCodec.ITEM.fieldOf("item").forGetter(requirement -> requirement.item),
      NamedCodec.INT.optionalFieldOf("amount", 1).forGetter(requirement -> requirement.amount),
      NamedCodec.STRING.fieldOf("id").forGetter(ItemRequirement::getId),
      RequirementMode.CODEC.optionalFieldOf("mode", RequirementMode.INPUT).forGetter(ItemRequirement::getMode)
    ).apply(instance, ItemRequirement::new),
    "Item requirement"
  );
  private final Item item;
  private final int amount;
  private final String id;
  private final RequirementMode mode;

  public ItemRequirement(Item item, int amount, String id, RequirementMode mode) {
    this.item = item;
    this.amount = amount;
    this.id = id;
    this.mode = mode;
  }
  @Override
  public RequirementType<ItemRequirement> getType() {
    return RequirementRegistration.ITEM.get();
  }

  @Override
  public boolean componentMatches(IComponent component) {
    return component instanceof ItemComponent;
  }

  @Override
  public boolean matches(IComponent component, int recipeTime) {
    if (component == null || mode == null) return false;
    if (!componentMatches(component)) return false;
    ItemComponent item = (ItemComponent) component;
    return switch (getMode()) {
      case INPUT -> item.extractItem(0, amount, true).getCount() == amount;
      case OUTPUT -> {
        ItemStack toInsert = new ItemStack(this.item, this.amount);
        ItemStack inserted = item.insertItem(0, toInsert.copy(), true);
        yield inserted.getCount() + this.amount == toInsert.getCount();
      }
      default -> false;
    };
  }

  @Override
  public CraftingResult processTick(IComponent component) {
    if (component == null || mode == null) return CraftingResult.error(Component.literal("No component found or invalid mode"));
    if (!componentMatches(component)) return CraftingResult.error(Component.literal("Component miss match"));
    if (getMode().isPerTick()) return CraftingResult.error(Component.literal("Item requirement can not be per tick"));
    return CraftingResult.pass();
  }

  @Override
  public CraftingResult processStart(IComponent component) {
    if (component == null || mode == null) return CraftingResult.error(Component.literal("No component found or invalid mode"));
    if (!componentMatches(component)) return CraftingResult.error(Component.literal("Component miss match"));
    ItemComponent item = (ItemComponent) component;
    if (getMode().isPerTick()) return CraftingResult.error(Component.literal("Item requirement can not be per tick"));
    else if (getMode().isInput()) {
      item.extractItem(0, amount, false);
      return CraftingResult.success();
    }
    return CraftingResult.pass();
  }

  @Override
  public CraftingResult processEnd(IComponent component) {
    if (component == null || mode == null) return CraftingResult.error(Component.literal("No component found or invalid mode"));
    if (!componentMatches(component)) return CraftingResult.error(Component.literal("Component miss match"));
    ItemComponent item = (ItemComponent) component;
    if (getMode().isPerTick()) return CraftingResult.error(Component.literal("Item requirement can not be per tick"));
    else if (getMode().isOutput()) {
      ItemStack toInsert = new ItemStack(this.item, amount);
      item.insertItem(0, toInsert, false);
      return CraftingResult.success();
    }
    return CraftingResult.pass();
  }

  @Override
  public NamedCodec<ItemRequirement> getCodec() {
    return CODEC;
  }

  @Override
  public RequirementMode getMode() {
    return mode;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public IRequirement<?> copy() {
    return new ItemRequirement(item, amount, id, mode);
  }

  @Override
  public String toString() {
    return "ItemRequirement{" +
      "item=" + item.getDefaultInstance().getHoverName().getString() +
      ", amount=" + amount +
      ", id='" + id + '\'' +
      ", mode=" + mode +
      '}';
  }
}
