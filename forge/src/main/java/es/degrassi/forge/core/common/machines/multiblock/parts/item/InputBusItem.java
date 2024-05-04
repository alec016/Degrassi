package es.degrassi.forge.core.common.machines.multiblock.parts.item;

import es.degrassi.common.registry.IVariantEntry;
import es.degrassi.common.registry.ItemModelType;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.InputBus;
import es.degrassi.forge.core.tiers.MultiblockPartStorage;
import es.degrassi.forge.lib.client.wiki.page.panel.InfoBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InputBusItem extends BlockItem implements IVariantEntry<MultiblockPartStorage.Item.Input, InputBus>, InfoBox.IInfoBoxHolder {
  public InputBusItem(InputBus block, Properties properties) {
    super(block, properties);
  }

  @Override
  public @NotNull Component getName(@NotNull ItemStack stack) {
    return getBlock().getDisplayName(stack);
  }

  @Override
  public InputBus getBlock() {
    return (InputBus) super.getBlock();
  }

  public ItemModelType getItemModelType() {
    return ItemModelType.BLOCK;
  }

  @Override
  public MultiblockPartStorage.Item.Input getVariant() {
    return getBlock().getVariant();
  }

  @Override
  public InfoBox getInfoBox(ItemStack stack, InfoBox box) {
    box.set(Component.translatable("info.degrassi.chest.rows"),
      Component.translatable("info.degrassi.chest.rows.number", getVariant().getRows()));
    box.set(Component.translatable("info.degrassi.chest.cols"),
      Component.translatable("info.degrassi.chest.cols.number", getVariant().getCols()));
    box.set(Component.translatable("info.degrassi.chest.slot"),
      Component.translatable("info.degrassi.chest.slot.number", getVariant().getTotalSlots()));
    return box;
  }
}
