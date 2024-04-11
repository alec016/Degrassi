package es.degrassi.forge.core.common.storage.energy.item;

import es.degrassi.common.registry.IVariantEntry;
import es.degrassi.forge.core.common.storage.energy.block.EnergyCell;
import es.degrassi.forge.core.tiers.Storage;
import net.minecraft.world.item.BlockItem;

public class EnergyCellItem extends BlockItem implements IVariantEntry<Storage.Energy, EnergyCellItem> {
  public EnergyCellItem(EnergyCell block , Properties properties) {
    super(block, properties);
  }

  @Override
  public EnergyCell getBlock() {
    return (EnergyCell) super.getBlock();
  }

  @Override
  public Storage.Energy getVariant() {
    return getBlock().getVariant();
  }
}
