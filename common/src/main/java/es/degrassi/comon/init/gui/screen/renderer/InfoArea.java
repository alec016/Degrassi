package es.degrassi.comon.init.gui.screen.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.comon.util.MouseUtil;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;

public abstract class InfoArea extends GuiComponent {
  protected final Rect2i area;
  protected InfoArea(Rect2i area) {
    this.area = area;
  }

  public abstract void draw(PoseStack transform, int x, int y, ResourceLocation texture);
  public abstract void draw(PoseStack transform, int x, int y, ResourceLocation texture, boolean vertical);
}
