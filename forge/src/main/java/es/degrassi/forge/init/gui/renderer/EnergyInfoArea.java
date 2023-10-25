package es.degrassi.forge.init.gui.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.forge.client.IClientHandler;
import es.degrassi.forge.util.storage.AbstractEnergyStorage;
import es.degrassi.forge.util.TextureSizeHelper;
import es.degrassi.forge.util.Utils;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

@SuppressWarnings("unused")
public class EnergyInfoArea extends InfoArea {
  private final AbstractEnergyStorage energy;

  public EnergyInfoArea (int xMin, int yMin) {
    this(xMin, yMin, null, 8, 64);
  }
  public EnergyInfoArea(int xMin, int yMin, AbstractEnergyStorage energy) {
    this(xMin, yMin, energy, 8, 64);
  }

  public EnergyInfoArea(int xMin, int yMin, AbstractEnergyStorage energy, int width, int height) {
    super(new Rect2i(xMin, yMin, width, height));
    this.energy = energy;
  }
  public List<Component> getTooltips() {
    return List.of(
      Component.translatable("degrassi.gui.element.energy.tooltip", Utils.format(energy.getEnergyStored()), Component.translatable("unit.energy.forge"), Utils.format(energy.getMaxEnergyStored()), Component.translatable("unit.energy.forge"))
    );
  }

  @Override
  public void draw(PoseStack pose, int x, int y, ResourceLocation texture) {
    draw(pose, x, y, texture, true);
  }

  @Override
  public void draw(PoseStack pose, int x, int y, ResourceLocation texture, boolean vertical) {
    final int height = TextureSizeHelper.getTextureHeight(texture);
    final int width = TextureSizeHelper.getTextureWidth(texture);
    final float energyPercent = energy.getEnergyStored() / (float) energy.getMaxEnergyStored();
    int stored;
    IClientHandler.bindTexture(texture);
    if (energyPercent > 1)
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
    else
      if (vertical) {
        stored = (int) (height * energyPercent);
        GuiComponent.blit(
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
      } else {
        stored = (int) (width * energyPercent);
        GuiComponent.blit(
          pose,
          x,
          y,
          0,
          0,
          stored,
          height,
          width,
          height
        );
      }
  }
}
