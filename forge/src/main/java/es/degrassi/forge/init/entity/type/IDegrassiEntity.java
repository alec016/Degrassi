package es.degrassi.forge.init.entity.type;

import es.degrassi.forge.init.gui.component.*;
import es.degrassi.forge.init.gui.element.*;
import net.minecraft.core.BlockPos;

public interface IDegrassiEntity {
  BlockPos getBlockPos();

  void setChanged();

  ComponentManager getComponentManager();
  ElementManager getElementManager();
}
