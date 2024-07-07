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
    public static final Blocks CHEST = new Blocks(false, "chest");
    public static final Blocks MACHINE = new Blocks(false, "machine");
    public static final Blocks DIGITAL = new Blocks(false, "digital");
    public static final Blocks DIGITAL_CONTROLLER = new Blocks(false, "digital/controller");
    public static final Blocks MULTIBLOCK = new Blocks(false, "multiblock");
    public static final Blocks FRAME = new Blocks(false, "multiblock/frame");
    public static final Blocks CONTROLLER = new Blocks(false, "multiblock/controller");
    public static final Blocks MBPARTS = new Blocks(false, "multiblock/parts");
    private Blocks(boolean isForge, String name) {
      super(blockTag(name, isForge));
    }
  }

  public static class Items extends Tag<Item> {
    public static final Items FURNACE = new Items(false, "furnace");
    public static final Items SP = new Items(false, "panel/solar_panel");
    public static final Items CHEST = new Items(false, "chest");
    public static final Items MACHINE = new Items(false, "machine");
    public static final Items DIGITAL = new Items(false, "digital");
    public static final Items DIGITAL_CONTROLLER = new Items(false, "digital/controller");
    public static final Items MULTIBLOCK = new Items(false, "multiblock");
    public static final Items FRAME = new Items(false, "multiblock/frame");
    public static final Items CONTROLLER = new Items(false, "multiblock/controller");
    public static final Items MBPARTS = new Items(false, "multiblock/parts");
    public static final Items PHOTOVOLTAIC_CELL = new Items(false, "panel/photovoltaic_cell");
    public static final Items PANEL = new Items(false, "panel");
    public static final Items WRENCH = new Items(true, "tools/wrench");
    public static final Items WRENCHES = new Items(true, "wrenches");
    private Items(boolean isForge, String name) {
      super(itemTag(name, isForge));
    }
  }
}
