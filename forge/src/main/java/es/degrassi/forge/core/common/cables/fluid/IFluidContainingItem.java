package es.degrassi.forge.core.common.cables.fluid;

import org.jetbrains.annotations.Nullable;

public interface IFluidContainingItem {
  @Nullable
  Info getFluidInfo();

  record Info(long capacity, long maxInsert, long maxExtract) {
  }
}
