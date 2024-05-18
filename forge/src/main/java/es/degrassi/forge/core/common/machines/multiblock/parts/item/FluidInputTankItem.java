package es.degrassi.forge.core.common.machines.multiblock.parts.item;

import es.degrassi.common.registry.IVariantEntry;
import es.degrassi.common.registry.ItemModelType;
import es.degrassi.common.utils.Utils;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.FluidInputTank;
import es.degrassi.forge.core.tiers.MultiblockPartStorage;
import es.degrassi.forge.lib.client.wiki.page.panel.InfoBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FluidInputTankItem extends BlockItem implements IVariantEntry<MultiblockPartStorage.Fluid.Input, FluidInputTank>, InfoBox.IInfoBoxHolder {
  public FluidInputTankItem(FluidInputTank block, Properties properties) {
    super(block, properties);
  }

  @Override
  public @NotNull Component getName(@NotNull ItemStack stack) {
    return getBlock().getDisplayName(stack);
  }

  @Override
  public FluidInputTank getBlock() {
    return (FluidInputTank) super.getBlock();
  }

  public ItemModelType getItemModelType() {
    return ItemModelType.BLOCK;
  }

  @Override
  public MultiblockPartStorage.Fluid.Input getVariant() {
    return getBlock().getVariant();
  }

  @Override
  public InfoBox getInfoBox(ItemStack stack, InfoBox box) {
    box.set(Component.translatable("info.degrassi.capacity.fluid"),
      Component.translatable("info.degrassi.mb", Utils.format(getVariant().getCapacity())));
    box.set(Component.translatable("info.degrassi.max.i"),
      Component.translatable("info.degrassi.mb.per.tick", Utils.format(getVariant().getCapacity())));
    return box;
  }
}
