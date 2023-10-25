package es.degrassi.forge.init.entity;

import es.degrassi.forge.util.storage.AbstractEnergyStorage;

public interface IEnergyEntity {
  AbstractEnergyStorage getEnergyStorage();

  void setEnergyLevel(int energy);

  void setCapacityLevel(int capacity);

  void setTransferRate(int transfer);
}
