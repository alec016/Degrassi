package es.degrassi.forge.init.gui.container.types;

import es.degrassi.forge.init.entity.BaseEntity;
import net.minecraft.network.chat.Component;

public interface IProgressContainer<T extends BaseEntity> extends IContainer<T> {
  boolean isCrafting();
  int getScaledProgress(int renderSize);
  Component getId();
}
