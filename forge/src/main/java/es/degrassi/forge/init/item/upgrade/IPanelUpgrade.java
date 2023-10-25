package es.degrassi.forge.init.item.upgrade;

public interface IPanelUpgrade extends IUpgrade {
  void setType(UpgradeType type);
  UpgradeType getType();
  default String getTypeString() {
    if (getType() == null) return "null";
    return getType().getName();
  }
}
