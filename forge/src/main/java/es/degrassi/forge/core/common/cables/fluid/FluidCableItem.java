package es.degrassi.forge.core.common.cables.fluid;

import es.degrassi.forge.core.common.cables.ItemModelType;
import es.degrassi.forge.core.common.cables.Transfer;
import es.degrassi.forge.core.tiers.CableTier;
import es.degrassi.common.registry.IVariantEntry;
import es.degrassi.forge.lib.client.wiki.page.panel.InfoBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FluidCableItem extends BlockItem implements IVariantEntry<CableTier, FluidCableBlock>, InfoBox.IInfoBoxHolder, IFluidContainingItem, IFluidItemProvider {
  public FluidCableItem(FluidCableBlock block, Properties properties) {
    super(block, properties);
  }
  @Override
  public @NotNull Component getName(@NotNull ItemStack stack) {
    if (this.getBlock() instanceof FluidCableBlock) {
      return this.getBlock().getDisplayName(stack);
    }
    return super.getName(stack);
  }

  public ItemModelType getItemModelType() {
    return ItemModelType.BLOCK;
  }

  @Override
  public @NotNull FluidCableBlock getBlock() {
    return (FluidCableBlock) super.getBlock();
  }

  public Transfer getTransferType() {
    return getBlock().getTransferType();
  }

  public CableTier getVariant() {
    return getBlock().getVariant();
  }

  @Override
  public InfoBox getInfoBox(ItemStack stack, InfoBox box) {
    return getBlock().getInfoBox(stack, box);
  }

  @Nullable
  @Override
  public IFluidContainingItem.Info getFluidInfo() {
    long transfer = getVariant().getFluidTransfer();
    return new IFluidContainingItem.Info(getVariant().getFluidCapacity(), getTransferType().canReceive ? transfer : 0,
      getTransferType().canExtract ? transfer : 0);
  }

  @Override
  public boolean isTransferable(ItemStack stack) {
    return getBlock().isTransferable(stack);
  }
}
