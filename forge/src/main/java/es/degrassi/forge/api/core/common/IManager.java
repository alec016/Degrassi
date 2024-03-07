package es.degrassi.forge.api.core.common;

import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IManager<T extends IType> {
  List<T> get();
  void add(T value);

  MachineEntity getEntity();
}
