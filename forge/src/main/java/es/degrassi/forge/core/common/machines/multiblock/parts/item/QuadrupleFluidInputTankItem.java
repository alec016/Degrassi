package es.degrassi.forge.core.common.machines.multiblock.parts.item;

import es.degrassi.common.registry.IVariantEntry;
import es.degrassi.common.registry.ItemModelType;
import es.degrassi.common.utils.Utils;
import es.degrassi.forge.core.common.machines.multiblock.parts.block.QuadrupleFluidInputTank;
import es.degrassi.forge.core.tiers.MultiblockPartStorage;
import es.degrassi.forge.lib.client.wiki.page.panel.InfoBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class QuadrupleFluidInputTankItem extends BlockItem implements IVariantEntry<MultiblockPartStorage.Fluid.QuadrupleInput, QuadrupleFluidInputTank>, InfoBox.IInfoBoxHolder {
  public QuadrupleFluidInputTankItem(QuadrupleFluidInputTank block, Properties properties) {
    super(block, properties);
  }

  @Override
  public @NotNull Component getName(@NotNull ItemStack stack) {
    return getBlock().getDisplayName(stack);
  }

  @Override
  public QuadrupleFluidInputTank getBlock() {
    return (QuadrupleFluidInputTank) super.getBlock();
  }

  public ItemModelType getItemModelType() {
    return ItemModelType.BLOCK;
  }

  @Override
  public MultiblockPartStorage.Fluid.QuadrupleInput getVariant() {
    return getBlock().getVariant();
  }


  @Override
  public InfoBox getInfoBox(ItemStack stack, InfoBox box) {
    box.set(Component.translatable("info.degrassi.capacity.fluid.quadruple"),
      Component.translatable("info.degrassi.mb", Utils.format(getVariant().getCapacity())));
    box.set(Component.translatable("info.degrassi.capacity.fluid"),
      Component.translatable("info.degrassi.mb", Utils.format(getVariant().getCapacity() * getVariant().getTankNumber())));
    box.set(Component.translatable("info.degrassi.max.i"),
      Component.translatable("info.degrassi.mb.per.tick", Utils.format(getVariant().getCapacity())));
    return box;
  }
}
