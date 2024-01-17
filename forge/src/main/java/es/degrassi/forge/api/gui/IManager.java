package es.degrassi.forge.api.gui;

import es.degrassi.forge.init.entity.BaseEntity;
import net.minecraft.nbt.*;

public interface IManager {
  void markDirty();
  BaseEntity getEntity();
}
