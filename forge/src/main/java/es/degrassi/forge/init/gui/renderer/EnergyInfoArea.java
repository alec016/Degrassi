package es.degrassi.forge.init.gui.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.client.IClientHandler;
import es.degrassi.forge.init.registration.ElementRegistry;
import es.degrassi.forge.requirements.IRequirement.ModeIO;
import es.degrassi.forge.util.storage.AbstractEnergyStorage;
import es.degrassi.forge.util.TextureSizeHelper;
import es.degrassi.forge.util.Utils;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("unused")
public class EnergyInfoArea extends InfoArea implements IDrawableAnimated, IGuiElement {
  private final AbstractEnergyStorage energy;
  private final int x, y, width, height;
  public ResourceLocation texture;
  public boolean vertical = true;
  public ModeIO mode;

  public EnergyInfoArea (int xMin, int yMin, ModeIO mode) {
    this(xMin, yMin, null, 8, 64, null, mode);
  }
  public EnergyInfoArea (int xMin, int yMin) {
    this(xMin, yMin, null, 8, 64, null, ModeIO.INPUT);
  }
  public EnergyInfoArea(int xMin, int yMin, AbstractEnergyStorage energy, ModeIO mode) {
    this(xMin, yMin, energy, 8, 64, null, mode);
  }
  public EnergyInfoArea(int xMin, int yMin, AbstractEnergyStorage energy) {
    this(xMin, yMin, energy, 8, 64, null, ModeIO.INPUT);
  }

  public EnergyInfoArea(int xMin, int yMin, AbstractEnergyStorage energy, int width, int height, ModeIO mode) {
    this(xMin, yMin, energy, width, height, new DegrassiLocation(""), mode);
  }
  public EnergyInfoArea(int xMin, int yMin, AbstractEnergyStorage energy, int width, int height) {
    this(xMin, yMin, energy, width, height, new DegrassiLocation(""));
  }
  public EnergyInfoArea(int xMin, int yMin, AbstractEnergyStorage energy, int width, int height, ResourceLocation texture, ModeIO mode) {
    super(new Rect2i(xMin, yMin, width, height), Component.literal("Energy"));
    this.energy = energy;
    this.x = xMin;
    this.y = yMin;
    this.width = width;
    this.height = height;
    this.texture = texture;
    this.mode = mode;
  }
  public EnergyInfoArea(int xMin, int yMin, AbstractEnergyStorage energy, int width, int height, ResourceLocation texture) {
    this(xMin, yMin, energy, width, height, texture, ModeIO.INPUT);
  }

  @Override
  public GuiElementType<? extends IGuiElement> getType() {
    return ElementRegistry.ENERGY_GUI_ELEMENT.get();
  }

  @Override
  public int getX() {
    return x;
  }

  @Override
  public int getY() {
    return y;
  }

  public List<Component> getTooltips() {
    return List.of(
      Component.translatable("degrassi.gui.element.energy.tooltip", Utils.format(energy.getEnergyStored()), Component.translatable("unit.energy.forge"), Utils.format(energy.getMaxEnergyStored()), Component.translatable("unit.energy.forge"))
    );
  }

  @Override
  public void draw(PoseStack pose, int x, int y, ResourceLocation texture) {
    this.texture = texture;
    draw(pose, x, y, texture, true);
  }

  @Override
  public void draw(PoseStack pose, int x, int y, ResourceLocation texture, boolean vertical) {
    if (texture == null) return;
    this.texture = texture;
    this.vertical = vertical;
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
      draw(poseStack, xOffset, yOffset, texture, true);
    }
    poseStack.popPose();
  }

  public AbstractEnergyStorage getStorage() {
    return energy;
  }

  public void drawHighlight(PoseStack stack, int x, int y) {
    GuiComponent.fill(stack, x, y, x + width, y + height, -2130706433);
  }
}
