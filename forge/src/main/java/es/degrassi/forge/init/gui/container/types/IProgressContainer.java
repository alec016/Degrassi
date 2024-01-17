package es.degrassi.forge.init.gui.container.types;

import es.degrassi.forge.init.entity.type.*;
import es.degrassi.forge.init.gui.element.ProgressGuiElement;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public interface IProgressContainer<T extends IDegrassiEntity & IProgressEntity> extends IContainer<T> {
  Component getId();

  default void elementClicked(ProgressGuiElement element, byte button) {
    if (element == null) throw new IllegalArgumentException("Invalid gui element");
    element.handleClick(button, getEntity(), this, getPlayerInv().player instanceof ServerPlayer player ? player : null);
  }

  default boolean isCrafting() {
    return getEntity().getProgressStorage().getProgress() > 0;
  }

  default int getScaledProgress(int renderSize) {
    int progress = getEntity().getProgressStorage().getProgress();
    int maxProgress = getEntity().getProgressStorage().getMaxProgress();  // Max Progress

    return maxProgress != 0 && progress != 0 ? renderSize * (progress / maxProgress) : 0;
  }
}
