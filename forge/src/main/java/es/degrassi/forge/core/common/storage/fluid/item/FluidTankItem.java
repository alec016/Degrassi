package es.degrassi.forge.core.common.storage.fluid.item;

import es.degrassi.common.registry.IVariantEntry;
import es.degrassi.common.registry.ItemModelType;
import static es.degrassi.common.utils.Utils.addCommas;
import es.degrassi.forge.core.common.storage.fluid.block.FluidTank;
import es.degrassi.forge.core.tiers.Storage;
import es.degrassi.forge.lib.client.wiki.page.panel.InfoBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FluidTankItem extends BlockItem implements IVariantEntry<Storage.Fluid, FluidTank>, InfoBox.IInfoBoxHolder {
  public FluidTankItem(FluidTank block, Properties properties) {
    super(block, properties);
  }

  @Override
  public @NotNull Component getName(@NotNull ItemStack stack) {
    return getBlock().getDisplayName(stack);
  }

  @Override
  public FluidTank getBlock() {
    return (FluidTank) super.getBlock();
  }

  public ItemModelType getItemModelType() {
    return ItemModelType.BLOCK;
  }

  @Override
  public Storage.Fluid getVariant() {
    return getBlock().getVariant();
  }

  @Override
  public InfoBox getInfoBox(ItemStack stack, InfoBox box) {
    box.set(Component.translatable("info.degrassi.capacity.fluid"),
      Component.translatable("info.degrassi.mb", addCommas(getVariant().getCapacity())));
    box.set(Component.translatable("info.degrassi.max.io"),
      Component.translatable("info.degrassi.mb.per.tick", addCommas(getVariant().getCapacity())));
    return box;
  }
}
