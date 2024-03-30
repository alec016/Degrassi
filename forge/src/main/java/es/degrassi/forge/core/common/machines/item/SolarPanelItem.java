package es.degrassi.forge.core.common.machines.item;

import es.degrassi.common.registry.IVariantEntry;
import static es.degrassi.forge.api.utils.Utils.addCommas;
import es.degrassi.forge.core.common.cables.ItemModelType;
import es.degrassi.forge.core.common.cables.fluid.FluidCableBlock;
import es.degrassi.forge.core.common.machines.block.SolarPanelBlock;
import es.degrassi.forge.core.tiers.CableTier;
import es.degrassi.forge.core.tiers.SolarPanel;
import es.degrassi.forge.lib.client.wiki.page.panel.InfoBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SolarPanelItem extends BlockItem implements IVariantEntry<SolarPanel, SolarPanelBlock>, InfoBox.IInfoBoxHolder  {
  public SolarPanelItem(SolarPanelBlock block, Properties properties) {
    super(block, properties);
  }

  @Override
  public @NotNull Component getName(@NotNull ItemStack stack) {
    if (this.getBlock() instanceof SolarPanelBlock) {
      return this.getBlock().getDisplayName(stack);
    }
    return super.getName(stack);
  }

  public @NotNull SolarPanelBlock getBlock() {
    return (SolarPanelBlock) super.getBlock();
  }

  public ItemModelType getItemModelType() {
    return ItemModelType.BLOCK;
  }

  public SolarPanel getVariant() {
    return getBlock().getVariant();
  }
  @Override
  public InfoBox getInfoBox(ItemStack stack, InfoBox box) {
    box.set(Component.translatable("info.degrassi.capacity"),
      Component.translatable("info.degrassi.fe", addCommas(getBlock().getTier().getEnergyCapacity())));
    box.set(Component.translatable("info.degrassi.max.io"),
      Component.translatable("info.degrassi.fe.per.tick", addCommas(getBlock().getTier().getEnergyCapacity())));
    box.set(Component.translatable("info.degrassi.generation"),
      Component.translatable("info.degrassi.sp.generation", addCommas(getBlock().getTier().getMaxGeneration())));
    return box;
  }
}
