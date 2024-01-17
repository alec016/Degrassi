package es.degrassi.forge.init.gui.screen.generators;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.forge.init.entity.generators.GeneratorEntity;
import es.degrassi.forge.init.gui.container.generators.GeneratorContainer;
import es.degrassi.forge.init.gui.element.EnergyGuiElement;
import es.degrassi.forge.init.gui.element.ProgressGuiElement;
import es.degrassi.forge.init.gui.screen.IScreen;
import es.degrassi.forge.util.*;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public abstract class GeneratorScreen<T extends GeneratorEntity<?, ?>> extends AbstractContainerScreen<GeneratorContainer<T>> implements IScreen {
  protected EnergyGuiElement energyComponent;
  protected ProgressGuiElement progressComponent;

  public GeneratorScreen(GeneratorContainer<T> container, Inventory inventory, Component name) {
    super(container, inventory, name);
  }

  @Override
  public void init() {
    super.init();
    getMenu().getEntity().getElementManager().getElements().clear();
  }

  @Override
  public void render(@NotNull PoseStack pPoseStack, int mouseX, int mouseY, float delta) {
    renderBackground(pPoseStack);
    super.render(pPoseStack, mouseX, mouseY, delta);
    renderTooltip(pPoseStack, mouseX, mouseY);
  }

  @Override
  protected void renderLabels(@NotNull PoseStack poseStack, int mouseX, int mouseY) {
    this.font.draw(poseStack, this.title, (float) this.titleLabelX, (float) this.titleLabelY, 4210752);

    getMenu().getEntity().getElementManager().renderLabels(
      this, poseStack, mouseX, mouseY
    );
  }

  @Override
  public IScreen getScreen() {
    return this;
  }

  @Override
  public void drawTooltips(PoseStack poseStack, @NotNull List<Component> tooltips, int mouseX, int mouseY) {
    tooltips.forEach(tooltip -> renderTooltip(poseStack, mouseX, mouseY));
  }

  @Override
  public int getX() {
    return this.leftPos;
  }

  @Override
  public int getY() {
    return this.topPos;
  }
}
