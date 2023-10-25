package es.degrassi.forge.init.item.upgrade;

public interface IUpgrade {
  Integer getValue();

  void setValue(Integer value);

  double getModifier();
  UpgradeUpgradeType getUpgradeType();
}
