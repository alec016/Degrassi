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
import es.degrassi.forge.requirements.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

@SuppressWarnings("unused")
public interface IScreen {
  ResourceLocation BASE_BACKGROUND = new DegrassiLocation("textures/gui/base_background.png");
  ResourceLocation FILLED_ARROW = new DegrassiLocation("textures/gui/furnace_progress_filled.png");

  IScreen getScreen();
  IContainer<?> getMenu();

  void drawTooltips(PoseStack poseStack, List<Component> tooltips, int mouseX, int mouseY);

  int getX();

  int getY();

  void setProgressElement(ProgressGuiElement progress);
  ProgressGuiElement getProgressElement();
  void setEnergyElement(EnergyGuiElement energy);
  EnergyGuiElement getEnergyElement();
  void setFluidElement(FluidGuiElement fluid);
  FluidGuiElement getFluidElement();
  void setEfficiencyElement(EfficiencyGuiElement efficiency);
  EfficiencyGuiElement getEfficiencyElement();

  default void assignEnergyElement(int xOffset, int yOffset, ResourceLocation texture, IRequirement.ModeIO mode, boolean vertical) {
    setEnergyElement(new EnergyGuiElement(getX() + xOffset, getY() + yOffset, ((IEnergyEntity) getMenu().getEntity()).getEnergyStorage(), texture, mode, vertical));
    getMenu().getEntity().getElementManager().addElement(getEnergyElement());
  }

  default void assignProgressElement(int xOffset, int yOffset, boolean inverted, boolean vertical) {
    setProgressElement(new ProgressGuiElement(getX() + xOffset, getY() + yOffset, ((IProgressEntity) getMenu().getEntity()).getProgressStorage(), FILLED_ARROW, inverted, vertical));
    getMenu().getEntity().getElementManager().addElement(getProgressElement());
  }

  default void assignProgressElement(int xOffset, int yOffset, ResourceLocation texture, boolean inverted, boolean vertical) {
    setProgressElement(new ProgressGuiElement(getX() + xOffset, getY() + yOffset, ((IProgressEntity) getMenu().getEntity()).getProgressStorage(), texture, inverted, vertical));
    getMenu().getEntity().getElementManager().addElement(getProgressElement());
  }

  default void assignFluidElement(int xOffset, int yOffset, int width, int height) {
    IFluidEntity entity = (IFluidEntity) getMenu().getEntity();
    setFluidElement(new FluidGuiElement(getX() + xOffset, getY() + yOffset, width, height, entity.getFluidStorage()));
    getMenu().getEntity().getElementManager().addElement(getFluidElement());
  }


  default void assignEfficiencyElement(int xOffset, int yOffset, ResourceLocation texture, boolean vertical) {
    IEfficiencyEntity entity = (IEfficiencyEntity) getMenu().getEntity();
    setEfficiencyElement(new EfficiencyGuiElement(getX() + xOffset, getY() + yOffset, entity.getCurrentEfficiency(), texture, vertical));
    getMenu().getEntity().getElementManager().addElement(getEfficiencyElement());
  }
}
