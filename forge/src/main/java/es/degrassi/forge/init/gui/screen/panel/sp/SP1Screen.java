package es.degrassi.forge.init.gui.screen.panel.sp;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.forge.init.gui.container.panel.PanelContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class SP1Screen extends SolarPanelScreen {
  public SP1Screen(PanelContainer container, Inventory inventory, Component name) {
    super(container, inventory, name);
  }

  @Override
  protected void renderBg(@NotNull PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
    super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
  }
}
