package es.degrassi.forge.core.common.machines.item;

import es.degrassi.common.registry.IVariantEntry;
import es.degrassi.common.registry.ItemModelType;
import es.degrassi.forge.core.tiers.PhotovoltaicCell;
import es.degrassi.forge.lib.client.wiki.page.panel.InfoBox;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PhotovoltaicCellItem extends Item implements IVariantEntry<PhotovoltaicCell, PhotovoltaicCellItem>, InfoBox.IInfoBoxHolder {
  private final PhotovoltaicCell tier;
  public PhotovoltaicCellItem(PhotovoltaicCell tier) {
    super(new Properties());
    this.tier = tier;
  }
  public ItemModelType getItemModelType() {
    return ItemModelType.GENERATED;
  }

  @Override
  public PhotovoltaicCell getVariant() {
    return tier;
  }

  @Override
  public InfoBox getInfoBox(ItemStack stack, InfoBox box) {
    return box;
  }
}
