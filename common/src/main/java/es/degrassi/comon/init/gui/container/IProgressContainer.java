package es.degrassi.comon.init.gui.container;

import net.minecraft.network.chat.Component;

public interface IProgressContainer extends IContainer {
  boolean isCrafting();
  int getScaledProgress(int renderSize);
  Component getId();
}
