package es.degrassi.forge.init.gui.screen.furnace;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.comon.init.gui.container.furnace.FurnaceContainer;
import es.degrassi.comon.init.gui.screen.furnace.FurnaceScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class IronFurnaceScreen extends FurnaceScreen {
  public IronFurnaceScreen(FurnaceContainer abstractContainerMenu, Inventory inventory, Component component) {
    super(abstractContainerMenu, inventory, component);
  }

  @Override
  protected void renderBg(@NotNull PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
    super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
  }
}
