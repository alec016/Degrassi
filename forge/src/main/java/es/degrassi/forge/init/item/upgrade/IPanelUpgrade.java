package es.degrassi.forge.init.item.upgrade;

public interface IPanelUpgrade {
  void setValue(Integer value);
  void setType(UpgradeType type);
  Integer getValue();
  UpgradeType getType();
  double getModifier();
  default String getTypeString() {
    if (getType() == null) return "null";
    return getType().getName();
  }
}
