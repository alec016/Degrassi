package es.degrassi.forge.init.entity;

import es.degrassi.forge.util.storage.ProgressStorage;

public interface IProgressEntity {

  ProgressStorage getProgressStorage();
  void setProgress(int progress);

  void setMaxProgress(int maxProgress);
}
