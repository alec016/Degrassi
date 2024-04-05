package es.degrassi.common.NBT;

import net.minecraft.world.item.BlockItem;

public class ConduitNBTKeys {
  // region Standard Keys

  public static final String LEVEL = "Level";
  public static final String BLOCK_POS = "BlockPos";
  public static final String ITEM = "Item";
  public static final String ITEMS = "Items";
  public static final String FLUID = "Fluid";
  public static final String ENERGY = "Energy";
  public static final String BLOCK_ENTITY_TAG = BlockItem.BLOCK_ENTITY_TAG;

  // endregion

  // region Energy Storage

  public static final String ENERGY_STORED = "EnergyStored";
  public static final String ENERGY_MAX_STORED = "MaxEnergyStored";
  public static final String ENERGY_MAX_USE = "MaxEnergyUse";
  public static final String ENERGY_MAX_RECEIVE = "MaxEnergyUse";
  public static final String ENERGY_MAX_EXTRACT = "MaxEnergyUse";

  // endregion

  // region Sync

  public static final String SYNC_DATA_SLOT_INDEX = "DataSlotIndex";
  public static final String SYNC_DATA = "Data";

  // endregion

  //region Travel Target

  public static final String ANCHOR_NAME = "AnchorName";
  public static final String ANCHOR_ICON = "AnchorIcon";
  public static final String ANCHOR_VISIBILITY = "AnchorVisibility";

  //endregion

  // region Capability Serialized Names

  public static final String CAPACITOR_DATA = "CapacitorData";
  public static final String OWNER = "Owner";
  public static final String ENTITY_STORAGE = "EntityStorage";
  public static final String TOGGLE_STATE = "ToggleState";
  public static final String COORDINATE_SELECTION = "CoordinateSelection";
  public static final String DARK_STEEL_UPGRADEABLE = "DarkSteelUpgradable";

  public static final String PAINT = "Paint";
  public static final String PAINT_2 = "Paint2";

  // endregion

  // region Misc task

  public static final String ACTIVE = "Active";

  // endregion
  public static final String CONDUIT_BUNDLE = "ConduitBundle";
  public static final String CONDUIT_EXTRA_DATA = "ConduitExtraData";
}
