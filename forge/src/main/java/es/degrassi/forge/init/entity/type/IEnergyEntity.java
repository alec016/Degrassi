package es.degrassi.forge.init.entity.type;

import es.degrassi.forge.util.storage.AbstractEnergyStorage;
import es.degrassi.forge.util.storage.GenerationStorage;

public interface IEnergyEntity extends IDegrassiEntity {
  AbstractEnergyStorage getEnergyStorage();

  void setEnergyLevel(int energy);

  void setCapacityLevel(int capacity);

  void setTransferRate(int transfer);

  interface IGenerationEntity extends IDegrassiEntity {
    GenerationStorage getGenerationStorage();
  }
}
