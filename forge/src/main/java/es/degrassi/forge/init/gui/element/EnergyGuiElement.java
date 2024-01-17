package es.degrassi.forge.init.gui.element;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.forge.client.IClientHandler;
import es.degrassi.forge.init.registration.ElementRegistry;
import es.degrassi.forge.requirements.IRequirement.ModeIO;
import es.degrassi.forge.init.gui.component.EnergyComponent;
import es.degrassi.forge.util.TextureSizeHelper;
import es.degrassi.forge.util.Utils;
import java.util.*;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class EnergyGuiElement extends GuiElement implements IDrawableAnimated, IGuiElement {
  private final EnergyComponent component;
  public ResourceLocation texture;
  public boolean vertical;
  public ModeIO mode;

  public EnergyGuiElement(int xMin, int yMin, EnergyComponent energy, ResourceLocation texture, ModeIO mode, boolean vertical) {
    super(xMin, yMin, TextureSizeHelper.getTextureWidth(texture), TextureSizeHelper.getTextureHeight(texture), Component.literal("Energy"));
    this.component = energy;
    this.texture = texture;
    this.mode = mode;
    this.vertical = vertical;
  }

  @Override
  public GuiElementType<? extends IGuiElement> getType() {
    return ElementRegistry.ENERGY_GUI_ELEMENT.get();
  }

  @Override
  public Tag serializeNBT() {
    CompoundTag nbt = new CompoundTag();
    Tag componentTag = component.serializeNBT();
    nbt.put("component", componentTag);
    nbt.putString("texture", texture.toString());
    nbt.putString("mode", mode.name());
    nbt.putBoolean("vertical", vertical);
    nbt.putInt("x", getX());
    nbt.putInt("y", getY());
    nbt.putInt("width", getWidth());
    nbt.putInt("height", getHeight());
    return nbt;
  }

  @Override
  public void deserializeNBT(Tag tag) {
    if(tag == null) throw new IllegalArgumentException("Tag cant be null");
    if (tag instanceof CompoundTag nbt) {
      Tag componentTag = nbt.getCompound("component");
      component.deserializeNBT(componentTag);
      texture = new ResourceLocation(nbt.getString("texture"));
      mode = ModeIO.valueOf(nbt.getString("mode"));
      vertical = nbt.getBoolean("vertical");
      x = nbt.getInt("x");
      y = nbt.getInt("y");
      width = nbt.getInt("width");
      height = nbt.getInt("height");
    }
  }

  public List<Component> getTooltips() {
    return List.of(
      Component.translatable("degrassi.gui.element.energy.tooltip", Utils.format(component.getEnergyStored()), net.minecraft.network.chat.Component.translatable("unit.energy.forge"), Utils.format(component.getMaxEnergyStored()), net.minecraft.network.chat.Component.translatable("unit.energy.forge"))
    );
  }

  @Override
  public void draw(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
    draw(poseStack, texture);
    renderHover(poseStack, mouseX, mouseY);
  }

  @Override
  public void renderLabels(Screen screen, @NotNull PoseStack poseStack, int mouseX, int mouseY) {
    if(
      isMouseAboveArea(
        mouseX,
        mouseY,
        getX(),
        getY(),
        getWidth(),
        getHeight()
      )
    ) {
      screen.renderTooltip(
        poseStack,
        getTooltips(),
        Optional.empty(),
        mouseX - getX(),
        mouseY - getY()
      );
    }
  }

  @Override
  public void draw(PoseStack pose, ResourceLocation texture) {
    if (texture == null) return;
    final float energyPercent = component.getEnergyStored() / (float) component.getMaxEnergyStored();
    int stored;
    IClientHandler.bindTexture(texture);
    pose.pushPose();
    if (energyPercent > 1)
      GuiComponent.blit(
        pose,
        getX(),
        getY(),
        0,
        0,
        getWidth(),
        getHeight(),
        getWidth(),
        getHeight()
      );
    else
      if (vertical) {
        stored = (int) (getHeight() * energyPercent);
        GuiComponent.blit(
          pose,
          getX(),
          getY() + getHeight() - stored,
          0,
          getHeight() - stored,
          getWidth(),
          stored,
          getWidth(),
          getHeight()
        );
      } else {
        stored = (int) (getWidth() * energyPercent);
        GuiComponent.blit(
          pose,
          getX(),
          getY(),
          0,
          0,
          stored,
          getHeight(),
          getWidth(),
          getHeight()
        );
      }
    pose.popPose();
  }

  @Override
  public void draw(@NotNull PoseStack poseStack, int xOffset, int yOffset) {
    poseStack.pushPose();
    {
      draw(poseStack, texture);
    }
    poseStack.popPose();
  }

  public EnergyComponent getStorage() {
    return component;
  }

  public void drawHighlight(PoseStack stack) {
    GuiComponent.fill(stack, getX(), getY(), getX() + getWidth(), getY() + getHeight(), -2130706433);
  }

  public boolean isVertical() {
    return vertical;
  }

  public void vertical() {
    this.vertical = true;
  }

  public void horizontal() {
    this.vertical = false;
  }
}
