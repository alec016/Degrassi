package es.degrassi.forge.core.common.machines.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import es.degrassi.forge.core.common.machines.container.MachineContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public abstract class MachineScreen<T extends MachineContainer<?>> extends AbstractContainerScreen<T> {
  public MachineScreen(T menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
  }

  @Override
  protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
  }
}
