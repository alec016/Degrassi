package es.degrassi.forge.init.gui.element;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class GuiElement extends AbstractWidget {
  protected Rect2i area;
  protected GuiElement(@NotNull Rect2i area, net.minecraft.network.chat.Component component) {
    super(area.getX(), area.getY(), area.getWidth(), area.getHeight(), component);
    this.area = area;
  }

  protected GuiElement() {
    super(0, 0, 1, 1, net.minecraft.network.chat.Component.empty());
  }

  public abstract void draw(PoseStack transform, int x, int y, ResourceLocation texture);
  public abstract void draw(PoseStack transform, int x, int y, ResourceLocation texture, boolean vertical);

  @Override
  public void updateNarration(@NotNull NarrationElementOutput output) {
    output.add(NarratedElementType.HINT, getTooltips().toArray(new net.minecraft.network.chat.Component[0]));
  }

  protected abstract List<net.minecraft.network.chat.Component> getTooltips();
}
