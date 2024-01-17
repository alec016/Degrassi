package es.degrassi.forge.init.gui.element;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.forge.client.IClientHandler;
import es.degrassi.forge.init.gui.component.EfficiencyComponent;
import es.degrassi.forge.init.registration.*;
import es.degrassi.forge.util.TextureSizeHelper;
import es.degrassi.forge.util.Utils;
import java.util.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;

import org.jetbrains.annotations.*;

@SuppressWarnings("unused")
public class EfficiencyGuiElement extends GuiElement {
  private final EfficiencyComponent component;
  private ResourceLocation texture;
  private boolean vertical;

  public EfficiencyGuiElement(int xMin, int yMin, EfficiencyComponent efficiency, ResourceLocation texture, boolean vertical) {
    super(xMin, yMin, TextureSizeHelper.getTextureWidth(texture), TextureSizeHelper.getTextureHeight(texture), Component.literal("Efficiency"));
    this.component = efficiency;
    this.texture = texture;
    this.vertical = vertical;
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

  @Override
  public Tag serializeNBT() {
    CompoundTag nbt = new CompoundTag();
    Tag componentTag = component.serializeNBT();
    nbt.put("component", componentTag);
    nbt.putString("texture", texture.toString());
    nbt.putBoolean("vertical", vertical);
    nbt.putInt("x", x);
    nbt.putInt("y", y);
    nbt.putInt("width", width);
    nbt.putInt("height", height);
    return nbt;
  }

  @Override
  public void deserializeNBT(Tag tag) {
    if(tag == null) throw new IllegalArgumentException("Tag cant be null");
    if (tag instanceof CompoundTag nbt) {
      Tag componentTag = nbt.getCompound("component");
      component.deserializeNBT(componentTag);
      texture = new ResourceLocation(nbt.getString("texture"));
      vertical = nbt.getBoolean("vertical");
      x = nbt.getInt("x");
      y = nbt.getInt("y");
      width = nbt.getInt("width");
      height = nbt.getInt("height");
    }
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
        TextureSizeHelper.getTextureWidth(texture),
        TextureSizeHelper.getTextureHeight(texture)
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
  public GuiElementType<? extends IGuiElement> getType() {
    return ElementRegistry.EFFICIENCY_GUI_ELEMENT.get();
  }

  public List<Component> getTooltips() {
    String eff = (component.getEfficiency() + "              ").substring(0, (component.getEfficiency() + "").indexOf(".") + 3).trim();
    return List.of(
      Component.literal(
        net.minecraft.network.chat.Component.translatable(
          "degrassi.gui.element.efficiency",
          Utils.format(eff)
        ).getString() + "%"
      ));
  }

  @Override
  public void draw(PoseStack pose, ResourceLocation texture) {
    int stored = (int) Math.floor(getHeight() * component.getEfficiency() / 100);
    IClientHandler.bindTexture(texture);
    pose.pushPose();
    if (component.getEfficiency() / 100 > 1)
      blit(
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
      blit(
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
    pose.popPose();
  }
}
