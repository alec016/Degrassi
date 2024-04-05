package es.degrassi.forge.core.common.machines.item;

import es.degrassi.common.registry.IVariantEntry;
import es.degrassi.common.registry.ItemModelType;
import es.degrassi.forge.core.common.machines.block.ChestBlock;
import es.degrassi.forge.core.tiers.Chest;
import es.degrassi.forge.lib.client.wiki.page.panel.InfoBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ChestItem extends BlockItem implements IVariantEntry<Chest, ChestItem>, InfoBox.IInfoBoxHolder {

  public ChestItem(ChestBlock block, Properties properties) {
    super(block, properties);
  }

  @Override
  public @NotNull ChestBlock getBlock() {
    return (ChestBlock) super.getBlock();
  }

  @Override
  public Chest getVariant() {
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
  @Override
  public @NotNull Component getName(@NotNull ItemStack stack) {
    if (this.getBlock() instanceof ChestBlock) {
      return this.getBlock().getDisplayName(stack);
    }
    return super.getName(stack);
  }

  public ItemModelType getItemModelType() {
    return ItemModelType.BLOCK;
  }
}
