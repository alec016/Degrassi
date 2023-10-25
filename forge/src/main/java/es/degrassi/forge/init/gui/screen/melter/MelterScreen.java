package es.degrassi.forge.init.gui.screen.melter;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.forge.init.gui.container.melter.MelterContainer;
import es.degrassi.forge.init.gui.screen.IScreen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class MelterScreen extends AbstractContainerScreen<MelterContainer> implements IScreen {
  public MelterScreen(MelterContainer container, Inventory inv, Component name) {
    super(container, inv, name);
  }

  @Override
  public IScreen getScreen() {
    return this;
  }

  @Override
  public void renderBg(@NotNull PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
  }
}
