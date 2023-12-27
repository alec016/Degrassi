package es.degrassi.forge.init.gui.container.types;

import es.degrassi.forge.init.entity.type.IDegrassiEntity;
import es.degrassi.forge.init.gui.renderer.ProgressComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public interface IProgressContainer<T extends IDegrassiEntity> extends IContainer<T> {
  boolean isCrafting();
  int getScaledProgress(int renderSize);
  Component getId();

  default void elementClicked(ProgressComponent element, byte button) {
    if (element == null) throw new IllegalArgumentException("Invalid gui element");
    element.handleClick(button, getEntity(), this, getPlayerInv().player instanceof ServerPlayer player ? player : null);
  }
}
