package es.degrassi.forge.init.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.init.entity.type.IEfficiencyEntity;
import es.degrassi.forge.init.entity.type.IEnergyEntity;
import es.degrassi.forge.init.entity.type.IFluidEntity;
import es.degrassi.forge.init.entity.type.IProgressEntity;
import es.degrassi.forge.init.gui.container.types.IContainer;
import es.degrassi.forge.init.gui.element.EfficiencyGuiElement;
import es.degrassi.forge.init.gui.element.EnergyGuiElement;
import es.degrassi.forge.init.gui.element.FluidGuiElement;
import es.degrassi.forge.init.gui.element.ProgressGuiElement;
import es.degrassi.forge.util.MouseUtil;
import es.degrassi.forge.util.TextureSizeHelper;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

@SuppressWarnings("unused")
public interface IScreen {
  ResourceLocation BASE_BACKGROUND = new DegrassiLocation("textures/gui/base_background.png");
  ResourceLocation FILLED_ARROW = new DegrassiLocation("textures/gui/furnace_progress_filled.png");

  IScreen getScreen();
  IContainer<?> getMenu();

  default void renderHover(PoseStack pPoseStack, int x, int y, int xOffset, int yOffset, int mouseX, int mouseY, int width, int height) {
    if(isMouseAboveArea(
      mouseX,
      mouseY,
      x,
      y,
      xOffset,
      yOffset,
      width,
      height
    )) {
      GuiComponent.fill(pPoseStack, x + xOffset, y + yOffset, x + xOffset + width, y + yOffset + height, -2130706433/*0x45FFFFFF*/);
    }
  }

  default boolean isMouseAboveArea(int mouseX, int mouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
    return MouseUtil.isMouseOver(mouseX, mouseY, x + offsetX, y + offsetY, width, height);
  }

  void drawTooltips(PoseStack poseStack, List<Component> tooltips, int mouseX, int mouseY);

  int getX();

  int getY();

  void setProgressComponent(ProgressGuiElement progress);
  void setEnergyComponent(EnergyGuiElement energy);
  void setFluidComponent(FluidGuiElement fluid);
  void setEfficiencyComponent(EfficiencyGuiElement efficiency);

  default void assignEnergyInfoArea(int xOffset, int yOffset) {
    setEnergyComponent(new EnergyGuiElement(getX() + xOffset, getY() + yOffset, ((IEnergyEntity) getMenu().getEntity()).getEnergyStorage()));
  }

  default void assignProgressComponent(int xOffset, int yOffset) {
    setProgressComponent(new ProgressGuiElement(getX() + xOffset, getY() + yOffset, ((IProgressEntity) getMenu().getEntity()).getProgressStorage(), TextureSizeHelper.getTextureWidth(FILLED_ARROW), TextureSizeHelper.getTextureHeight(FILLED_ARROW)));
  }

  default void assignProgressComponent(int xOffset, int yOffset, ResourceLocation texture) {
    setProgressComponent(new ProgressGuiElement(getX() + xOffset, getY() + yOffset, ((IProgressEntity) getMenu().getEntity()).getProgressStorage(), TextureSizeHelper.getTextureWidth(texture), TextureSizeHelper.getTextureHeight(texture)));
  }

  default void assignFluidComponent(int xOffset, int yOffset, int width, int height) {
    setFluidComponent(new FluidGuiElement(new Rect2i(getX() + xOffset, getY() + yOffset, width, height), ((IFluidEntity) getMenu().getEntity()).getFluidStorage().getFluid(), ((IFluidEntity) getMenu().getEntity()).getFluidStorage().getCapacity()));
  }

  default void assignEfficiencyInfoArea(int xOffset, int yOffset) {
    int x = getX();
    int y = getY();
    IEfficiencyEntity entity = (IEfficiencyEntity) getMenu().getEntity();
    setEfficiencyComponent(new EfficiencyGuiElement(x + xOffset, y + yOffset, entity.getCurrentEfficiency(), 18, 72));
  }
}
