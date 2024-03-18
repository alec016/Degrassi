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
    private Blocks(boolean isForge, String name) {
      super(blockTag(name, isForge));
    }
  }

  public static class Items extends Tag<Item> {
    public static final Items FURNACE = new Items(false, "furnace");
    public static final Items SP = new Items(false, "solar_panel");
    public static final Items MACHINE = new Items(false, "machine");
    private Items(boolean isForge, String name) {
      super(itemTag(name, isForge));
    }
  }
}
