package es.degrassi.forge.init.entity.type;

import es.degrassi.forge.util.storage.ProgressStorage;

public interface IProgressEntity extends IDegrassiEntity {

  ProgressStorage getProgressStorage();
  void setProgress(int progress);

  void setMaxProgress(int maxProgress);

  void resetProgress();
}
