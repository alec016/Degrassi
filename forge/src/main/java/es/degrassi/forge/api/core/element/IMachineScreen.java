package es.degrassi.forge.api.core.element;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.forge.api.core.machine.IMachine;
import es.degrassi.forge.api.core.machine.MachineTile;
import java.util.List;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public interface IMachineScreen {
  int getX();
  int getY();
  int getWidth();
  int getHeight();
  MachineTile getTile();
  IMachine getMachine();
  AbstractContainerScreen<? extends AbstractContainerMenu> getScreen();
  void drawTooltips(PoseStack pose, List<Component> tooltips, int mouseX, int mouseY);
  void drawGhostItem(PoseStack pose, ItemStack item, int posX, int posY, int color);
}
