package es.degrassi.forge.core.common.machines.item;

import es.degrassi.common.registry.IVariantEntry;
import static es.degrassi.common.utils.Utils.addCommas;
import es.degrassi.common.registry.ItemModelType;
import es.degrassi.forge.core.common.machines.block.FurnaceBlock;
import es.degrassi.forge.core.tiers.Furnace;
import es.degrassi.forge.lib.client.wiki.page.panel.InfoBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class FurnaceItem extends BlockItem implements IVariantEntry<Furnace, FurnaceBlock>, InfoBox.IInfoBoxHolder {

  public FurnaceItem(FurnaceBlock block, Properties properties) {
    super(block, properties);
  }

  @Override
  public @NotNull Component getName(@NotNull ItemStack stack) {
    if (this.getBlock() instanceof FurnaceBlock) {
      return this.getBlock().getDisplayName(stack);
    }
    return super.getName(stack);
  }

  public @NotNull FurnaceBlock getBlock() {
    return (FurnaceBlock) super.getBlock();
  }

  public ItemModelType getItemModelType() {
    return ItemModelType.BLOCK;
  }

  public Furnace getVariant() {
    return getBlock().getVariant();
  }
  @Override
  public InfoBox getInfoBox(ItemStack stack, InfoBox box) {
    box.set(Component.translatable("info.degrassi.capacity.energy"),
      Component.translatable("info.degrassi.fe", addCommas(getBlock().getTier().getEnergyCapacity())));
    box.set(Component.translatable("info.degrassi.capacity.xp"),
      Component.translatable("info.degrassi.xp", getBlock().getTier().getExperienceCapacity()));
    return box;
  }
}
