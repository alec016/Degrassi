package es.degrassi.forge.init.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("unused")
public abstract class PanelItem extends BlockItem {
  public PanelItem(Block block, Properties properties) {
    super(block, properties);
  }
}
