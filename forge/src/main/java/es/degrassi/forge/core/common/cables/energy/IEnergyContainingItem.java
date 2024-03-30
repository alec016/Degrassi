package es.degrassi.forge.core.common.cables.energy;

import org.jetbrains.annotations.Nullable;

public interface IEnergyContainingItem {
  @Nullable
  Info getEnergyInfo();

  record Info(long capacity, long maxInsert, long maxExtract) {
  }
}
