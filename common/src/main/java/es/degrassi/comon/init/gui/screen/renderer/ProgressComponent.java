package es.degrassi.comon.init.gui.screen.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.client.IClientHandler;
import es.degrassi.comon.util.TextureSizeHelper;
import es.degrassi.comon.util.storage.ProgressStorage;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.resources.ResourceLocation;

public class ProgressComponent extends InfoArea {
  private final ProgressStorage progress;

  public ProgressComponent (int xMin, int yMin) {
    this(xMin, yMin, null, 8, 64);
  }
  public ProgressComponent(int xMin, int yMin, ProgressStorage progress) {
    this(xMin, yMin, progress, 8, 64);
  }
  public ProgressComponent(int xMin, int yMin, ProgressStorage progress, int width, int height) {
    super(new Rect2i(xMin, yMin, width, height));
    this.progress = progress;
  }

  @Override
  public void draw(PoseStack transform, int x, int y, ResourceLocation texture) {
    draw(transform, x, y, texture, false);
  }

  @Override
  public void draw(PoseStack pose, int x, int y, ResourceLocation texture, boolean vertical) {
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

    return maxProgress != 0 && progress != 0 ? (int) (progress / (float) maxProgress) * renderSize : 0;
  }
}
