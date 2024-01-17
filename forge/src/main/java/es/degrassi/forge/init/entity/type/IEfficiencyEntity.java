package es.degrassi.forge.init.entity.type;

import es.degrassi.forge.init.gui.component.EfficiencyComponent;

public interface IEfficiencyEntity extends IDegrassiEntity {
  EfficiencyComponent getCurrentEfficiency();
}
