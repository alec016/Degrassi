package es.degrassi.forge.init.entity.type;

import es.degrassi.forge.init.gui.component.*;
import net.minecraft.core.BlockPos;

public interface IDegrassiEntity {
  BlockPos getBlockPos();

  void setChanged();

  ComponentManager getManager();
}
