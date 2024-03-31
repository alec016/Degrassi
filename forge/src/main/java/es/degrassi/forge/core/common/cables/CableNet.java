package es.degrassi.forge.core.common.cables;

import java.util.List;

public abstract class CableNet<T extends CableEntity<? extends CableNet<T>, ?>> {
  public List<T> cableList;
  protected CableNet(List<T> cableList) {
    this.cableList = cableList;
  }
}
