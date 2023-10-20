package es.degrassi.comon.init.item.upgrade;

public enum UpgradeType {
  ADD("add"),
  MULTIPLY("multiply"),
  EXPONENTIAL("exponential");

  private final String name;

  UpgradeType(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }
}
