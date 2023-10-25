package es.degrassi.forge.init.item.upgrade.types;

import es.degrassi.forge.init.item.upgrade.UpgradeUpgradeType;

public interface IUpgrade {
  Integer getValue();

  void setValue(Integer value);

  double getModifier();
  UpgradeUpgradeType getUpgradeType();
}
