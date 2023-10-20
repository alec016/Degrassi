package es.degrassi.comon.init.gui.screen;

import es.degrassi.Degrassi;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("unused")
public interface IScreen {
  ResourceLocation BASE_BACKGROUND = new ResourceLocation(Degrassi.MODID,"textures/gui/base_background.png");

  IScreen getScreen();
}
