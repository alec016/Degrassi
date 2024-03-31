package es.degrassi.forge.core.common.cables.energy;

import es.degrassi.forge.core.common.cables.ItemModelType;
import es.degrassi.forge.core.common.cables.Transfer;
import es.degrassi.forge.core.tiers.CableTier;
import es.degrassi.common.registry.IVariantEntry;
import es.degrassi.forge.lib.client.wiki.page.panel.InfoBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class EnergyCableItem extends BlockItem implements IVariantEntry<CableTier, EnergyCableBlock>, InfoBox.IInfoBoxHolder, IEnergyContainingItem, IEnergyItemProvider {
  public EnergyCableItem(EnergyCableBlock block, Properties properties) {
    super(block, properties);
  }
  @Override
  public @NotNull Component getName(@NotNull ItemStack stack) {
    if (this.getBlock() instanceof EnergyCableBlock) {
      return this.getBlock().getDisplayName(stack);
    }
    return super.getName(stack);
  }

  public ItemModelType getItemModelType() {
    return ItemModelType.BLOCK;
  }

  public IEnergyContainingItem.Info getEnergyInfo() {
    long transfer = getVariant().getEnergyTransfer();
    return new IEnergyContainingItem.Info(getVariant().getEnergyCapacity(), getTransferType().canReceive() ? transfer : 0,
      getTransferType().canExtract() ? transfer : 0);
  }

  @Override
  public @NotNull EnergyCableBlock getBlock() {
    return (EnergyCableBlock) super.getBlock();
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

  public boolean isChargeable(ItemStack stack) {
    return getBlock().isChargeable(stack);
  }
}
