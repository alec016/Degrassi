package es.degrassi.forge.init.entity.type;

import es.degrassi.forge.util.storage.AbstractEnergyStorage;
import es.degrassi.forge.util.storage.GenerationStorage;

public interface IEnergyEntity {
  AbstractEnergyStorage getEnergyStorage();

  void setEnergyLevel(int energy);

  void setCapacityLevel(int capacity);

  void setTransferRate(int transfer);

  interface IGenerationEntity {
    GenerationStorage getGenerationStorage();
  }
}
