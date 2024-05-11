package es.degrassi.forge.api.core.capability;

import net.minecraftforge.common.capabilities.AutoRegisterCapability;

@AutoRegisterCapability
public interface IHeatStorage {

  double getHeat();
  double getHeatCapacity();

  double receive(double maxAdd, boolean simulate);
  double extract(double maxSub, boolean simulate);

  boolean canInsert();
  boolean canExtract();
}
