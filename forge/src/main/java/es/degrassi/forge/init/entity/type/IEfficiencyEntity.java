package es.degrassi.forge.init.entity.type;

import es.degrassi.forge.util.storage.EfficiencyStorage;

public interface IEfficiencyEntity extends IDegrassiEntity {
  EfficiencyStorage getCurrentEfficiency();
}
