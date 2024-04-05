package es.degrassi.forge.core.common.conduit.common.types.item;

import es.degrassi.common.conduit.IConduitMenuData;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.common.conduit.common.types.SimpleConduitType;

public class ItemConduitType extends SimpleConduitType<ItemExtendedData> {
  public ItemConduitType() {
    super(Degrassi.rl("block/conduit/item"), new ItemConduitTicker(),
      ItemExtendedData::new, new ItemClientConduitData(), IConduitMenuData.ITEM);
  }
}
