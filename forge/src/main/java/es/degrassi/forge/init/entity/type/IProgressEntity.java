package es.degrassi.forge.init.entity.type;

import es.degrassi.forge.init.gui.component.ProgressComponent;

public interface IProgressEntity extends IDegrassiEntity {

  ProgressComponent getProgressStorage();
  void setProgress(int progress);

  void setMaxProgress(int maxProgress);

  void resetProgress();
}
