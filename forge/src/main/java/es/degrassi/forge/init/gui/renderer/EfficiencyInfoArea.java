package es.degrassi.forge.init.gui.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.forge.client.IClientHandler;
import es.degrassi.forge.util.storage.EfficiencyStorage;
import es.degrassi.forge.util.TextureSizeHelper;
import es.degrassi.forge.util.Utils;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

@SuppressWarnings("unused")
public class EfficiencyInfoArea extends InfoArea {
  private final EfficiencyStorage efficiency;

  public EfficiencyInfoArea (int xMin, int yMin) {
    this(xMin, yMin, null, 8, 64);
  }
  public EfficiencyInfoArea(int xMin, int yMin, EfficiencyStorage efficiency) {
    this(xMin, yMin, efficiency, 8, 64);
  }

  public EfficiencyInfoArea(int xMin, int yMin, EfficiencyStorage efficiency, int width, int height) {
    super(new Rect2i(xMin, yMin, width, height));
    this.efficiency = efficiency;
  }
  public List<Component> getTooltips() {
    String eff = (efficiency.getEfficiency() + "              ").substring(0, (efficiency.getEfficiency() + "").indexOf(".") + 3).trim();
    return List.of(
      Component.literal(
        Component.translatable(
          "degrassi.gui.element.efficiency",
          Utils.format(eff)
        ).getString() + "%"
      ));
  }

  @Override
  public void draw(PoseStack pose, int x, int y, ResourceLocation texture) {
    final int height = TextureSizeHelper.getTextureHeight(texture);
    final int width = TextureSizeHelper.getTextureWidth(texture);
    int stored = (int) Math.floor(height * efficiency.getEfficiency() / 100);
    IClientHandler.bindTexture(texture);
    if (efficiency.getEfficiency() / 100 > 1)
      blit(
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
    else
      blit(
        pose,
        x,
        y + height - stored,
        0,
        height - stored,
        width,
        stored,
        width,
        height
      );
  }

  @Override
  public void draw(PoseStack pose, int x, int y, ResourceLocation texture, boolean vertical) {
    final int height = TextureSizeHelper.getTextureHeight(texture);
    final int width = TextureSizeHelper.getTextureWidth(texture);
    int stored = (int) Math.floor(height * efficiency.getEfficiency() / 100);
    IClientHandler.bindTexture(texture);
    if (efficiency.getEfficiency() / 100 > 1)
      blit(
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
    else
      blit(
        pose,
        x,
        y + height - stored,
        0,
        height - stored,
        width,
        stored,
        width,
        height
      );
  }
}
