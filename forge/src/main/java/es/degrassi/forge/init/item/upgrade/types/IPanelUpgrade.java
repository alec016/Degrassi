package es.degrassi.forge.init.item.upgrade.types;

import es.degrassi.forge.init.item.upgrade.UpgradeType;

public interface IPanelUpgrade extends IUpgrade {
  void setType(UpgradeType type);
  UpgradeType getType();
  default String getTypeString() {
    if (getType() == null) return "null";
    return getType().getName();
  }
}
