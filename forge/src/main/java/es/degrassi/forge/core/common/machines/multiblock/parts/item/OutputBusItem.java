package es.degrassi.forge.core.common.machines.multiblock.parts.item;

import es.degrassi.common.registry.IVariantEntry;
import es.degrassi.common.registry.ItemModelType;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.OutputBus;
import es.degrassi.forge.core.tiers.MultiblockPartStorage;
import es.degrassi.forge.lib.client.wiki.page.panel.InfoBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class OutputBusItem extends BlockItem implements IVariantEntry<MultiblockPartStorage.Item.Output, OutputBus>, InfoBox.IInfoBoxHolder {
  public OutputBusItem(OutputBus block, Properties properties) {
    super(block, properties);
  }

  @Override
  public @NotNull Component getName(@NotNull ItemStack stack) {
    return getBlock().getDisplayName(stack);
  }

  @Override
  public OutputBus getBlock() {
    return (OutputBus) super.getBlock();
  }

  public ItemModelType getItemModelType() {
    return ItemModelType.BLOCK;
  }

  @Override
  public MultiblockPartStorage.Item.Output getVariant() {
    return getBlock().getVariant();
  }

  @Override
  public InfoBox getInfoBox(ItemStack stack, InfoBox box) {
    return box;
  }
}
