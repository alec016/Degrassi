package es.degrassi.comon.init.gui.screen.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.client.IClientHandler;
import es.degrassi.comon.util.TextureSizeHelper;
import es.degrassi.comon.util.storage.ProgressStorage;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProgressComponent extends InfoArea implements IDrawableAnimated, IGuiElement {
  private final ProgressStorage progress;
  public ResourceLocation texture;
  private final int x, y, width, height;
  private Orientation orientation = Orientation.RIGHT;

  public ProgressComponent (int xMin, int yMin) {
    this(xMin, yMin, null, 8, 64);
  }
  public ProgressComponent(int xMin, int yMin, ProgressStorage progress) {
    this(xMin, yMin, progress, 8, 64);
  }
  public ProgressComponent(int xMin, int yMin, ProgressStorage progress, int width, int height) {
    super(new Rect2i(xMin, yMin, width, height));
    this.x = xMin;
    this.y = yMin;
    this.width = width;
    this.height = height;
    this.progress = progress;
  }
  public ProgressComponent(int xMin, int yMin, ProgressStorage progress, int width, int height, ResourceLocation texture) {
    super(new Rect2i(xMin, yMin, width, height));
    this.progress = progress;
    this.texture = texture;
    this.x = xMin;
    this.y = yMin;
    this.width = width;
    this.height = height;
  }

  @Override
  public void draw(PoseStack transform, int x, int y, ResourceLocation texture) {
    this.texture = texture;
    draw(transform, x, y, texture, false);
  }

  public List<Component> getTooltips() {
    return List.of(
      Component.literal(
        progress.getProgress()
        + "/"
        + progress.getMaxProgress()
      ));
  }

  @Override
  public void draw(PoseStack pose, int x, int y, ResourceLocation texture, boolean vertical) {
    this.texture = texture;
    final int width = TextureSizeHelper.getTextureWidth(texture);
    final int height = TextureSizeHelper.getTextureHeight(texture);
    IClientHandler.bindTexture(texture);
    int current;
    if (getScaledProgress(height) == height || getScaledProgress(width) == width) {
      GuiComponent.blit(
        pose,
        x,
        y,
        0,
        0,
        width,
        height,
        width,
        height
      );
      return;
    }
    if (vertical) {
      current = getScaledProgress(height);
      GuiComponent.blit(
        pose,
        x,
        y + height - current,
        0,
        height - current,
        width,
        current,
        width,
        height
      );
      return;
    }
    current = getScaledProgress(width);
    GuiComponent.blit(
      pose,
      x,
      y,
      0,
      0,
      current,
      height,
      width,
      height
    );
  }

  public int getScaledProgress(int renderSize) {
    int progress = this.progress.getProgress();
    int maxProgress = this.progress.getMaxProgress();  // Max Progress

    return progress != 0 ? (int) Math.floor((progress / (float) maxProgress) * renderSize) : 0;
  }

  @Override
  public GuiElementType<? extends IGuiElement> getType() {
    return null;
  }

  @Override
  public int getX() {
    return x;
  }

  @Override
  public int getY() {
    return y;
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public void draw(@NotNull PoseStack poseStack, int xOffset, int yOffset) {
    poseStack.pushPose();
    {
      draw(poseStack, xOffset, yOffset, texture, false);
    }
    poseStack.popPose();
  }

  public Orientation getDirection() {
    return this.orientation;
  }

  public void setDirection(Orientation direction) {
    this.orientation = direction;
  }

  public ResourceLocation getFilledTexture() {
    return texture;
  }

  public enum Orientation {
    RIGHT,
    LEFT,
    TOP,
    BOTTOM
  }
}
