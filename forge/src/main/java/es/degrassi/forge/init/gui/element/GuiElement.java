package es.degrassi.forge.init.gui.element;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class GuiElement extends AbstractWidget implements IGuiElement {
  protected int x, y, width, height;
  protected GuiElement(int x, int y, int width, int height, Component component) {
    super(x, y, width, height, component);
    this.x = x;
    this. y = y;
    this.width = width;
    this.height = height;
  }

  protected GuiElement() {
    super(0, 0, 1, 1, Component.empty());
  }

  public abstract void draw(PoseStack transform, ResourceLocation texture);

  @Override
  public void updateNarration(@NotNull NarrationElementOutput output) {
    output.add(NarratedElementType.HINT, getTooltips().toArray(new Component[0]));
  }

  public abstract List<Component> getTooltips();

  @Override
  public int getY() {
    return y;
  }

  @Override
  public int getX() {
    return x;
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }
}
