package es.degrassi.forge.core.common.storage.fluid.item;

import es.degrassi.common.registry.IVariantEntry;
import es.degrassi.forge.core.common.storage.fluid.block.FluidTank;
import es.degrassi.forge.core.tiers.Storage;
import net.minecraft.world.item.BlockItem;

public class FluidTankItem extends BlockItem implements IVariantEntry<Storage.Fluid, FluidTankItem> {
  public FluidTankItem(FluidTank block, Properties properties) {
    super(block, properties);
  }

  @Override
  public FluidTank getBlock() {
    return (FluidTank) super.getBlock();
  }

  @Override
  public Storage.Fluid getVariant() {
    return getBlock().getVariant();
  }
}
