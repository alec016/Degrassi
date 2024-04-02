package es.degrassi.forge.core.data;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.common.ForgeLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class DegrassiTags {
  private static TagKey<Block> blockTag(String name, boolean isForge) {
    return BlockTags.create(isForge ? new ForgeLocation(name) : new DegrassiLocation(name));
  }

  private static TagKey<Item> itemTag(String name, boolean isForge) {
    return ItemTags.create(isForge ? new ForgeLocation(name) : new DegrassiLocation(name));
  }

  private static class Tag<T> {
    private final TagKey<T> tag;
    protected Tag(TagKey<T> tag) {
      this.tag = tag;
    }

    public TagKey<T> get() {
      return tag;
    }
  }

  public static class Blocks extends Tag<Block> {
    public static final Blocks FURNACE = new Blocks(false, "furnace");
    public static final Blocks SP = new Blocks(false, "solar_panel");
    public static final Blocks MACHINE = new Blocks(false, "machine");
    public static final Blocks CABLE = new Blocks(false, "cable");
    public static final Blocks ENERGY_CABLE = new Blocks(false, "cable/energy");
    public static final Blocks FLUID_CABLE = new Blocks(false, "cable/fluid");
    public static final Blocks ITEM_CABLE = new Blocks(false, "cable/item");
    private Blocks(boolean isForge, String name) {
      super(blockTag(name, isForge));
    }
  }

  public static class Items extends Tag<Item> {
    public static final Items FURNACE = new Items(false, "furnace");
    public static final Items SP = new Items(false, "panel/solar_panel");
    public static final Items MACHINE = new Items(false, "machine");
    public static final Items PHOTOVOLTAIC_CELL = new Items(false, "panel/photovoltaic_cell");
    public static final Items PANEL = new Items(false, "panel");
    public static final Items CABLE = new Items(false, "cable");
    public static final Items ENERGY_CABLE = new Items(false, "cable/energy");
    public static final Items FLUID_CABLE = new Items(false, "cable/fluid");
    public static final Items ITEM_CABLE = new Items(false, "cable/item");
    public static final Items WRENCH = new Items(true, "tools/wrench");
    public static final Items WRENCHES = new Items(true, "wrenches");
    private Items(boolean isForge, String name) {
      super(itemTag(name, isForge));
    }
  }
}
