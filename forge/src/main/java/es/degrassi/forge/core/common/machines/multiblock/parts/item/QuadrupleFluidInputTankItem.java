package es.degrassi.forge.core.common.machines.multiblock.parts.item;

import es.degrassi.common.registry.IVariantEntry;
import es.degrassi.common.registry.ItemModelType;
import static es.degrassi.common.utils.Utils.addCommas;
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
    box.set(Component.translatable("info.degrassi.capacity.heat"),
      Component.translatable("info.degrassi.heat", addCommas(getVariant().getCapacity())));
    box.set(Component.translatable("info.degrassi.max.i"),
      Component.translatable("info.degrassi.heat.per.tick", addCommas(getVariant().getCapacity())));
    return box;
  }
}
