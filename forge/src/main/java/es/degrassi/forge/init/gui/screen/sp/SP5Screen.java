package es.degrassi.forge.init.gui.screen.sp;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.comon.init.gui.container.panel.PanelContainer;
import es.degrassi.comon.init.gui.screen.panel.SolarPanelScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class SP5Screen extends SolarPanelScreen {
  public SP5Screen(PanelContainer container, Inventory inventory, Component name) {
    super(container, inventory, name);
  }

  @Override
  protected void renderBg(@NotNull PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
    super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
  }
}
