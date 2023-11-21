package es.degrassi.forge.init.gui.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class InfoArea extends AbstractWidget {
  protected Rect2i area;
  protected InfoArea(@NotNull Rect2i area, Component component) {
    super(area.getX(), area.getY(), area.getWidth(), area.getHeight(), component);
    this.area = area;
  }

  protected InfoArea() {
    super(0, 0, 1, 1, Component.empty());
  }

  public abstract void draw(PoseStack transform, int x, int y, ResourceLocation texture);
  public abstract void draw(PoseStack transform, int x, int y, ResourceLocation texture, boolean vertical);

  @Override
  public void updateNarration(@NotNull NarrationElementOutput output) {
    output.add(NarratedElementType.HINT, getTooltips().toArray(new Component[0]));
  }

  protected abstract List<Component> getTooltips();
}
