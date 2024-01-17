package es.degrassi.forge.init.entity.type;

import es.degrassi.forge.init.gui.component.EnergyComponent;
import es.degrassi.forge.init.gui.component.GenerationComponent;

public interface IEnergyEntity extends IDegrassiEntity {
  EnergyComponent getEnergyStorage();

  void setEnergyLevel(int energy);

  void setCapacityLevel(int capacity);

  void setTransferRate(int transfer);

  interface IGenerationEntity extends IDegrassiEntity {
    GenerationComponent getGenerationStorage();
  }
}
