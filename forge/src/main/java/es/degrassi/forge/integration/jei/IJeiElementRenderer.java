package es.degrassi.forge.integration.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.comon.init.entity.BaseEntity;
import es.degrassi.comon.init.gui.screen.renderer.IGuiElement;
import es.degrassi.comon.init.recipe.IDegrassiRecipe;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.Collections;
import java.util.List;

public interface IJeiElementRenderer<T extends IGuiElement> {
  void renderElementInJEI(PoseStack matrix, T element, IDegrassiRecipe recipe, double mouseX, double mouseY, int x, int y);
  default boolean isHoveredInJei(T element, int posX, int posY, int mouseX, int mouseY) {
    return mouseX >= posX && mouseX <= posX + element.getWidth() && mouseY >= posY && mouseY <= posY + element.getHeight();
  }
  default List<Component> getJEITooltips(T element, IDegrassiRecipe recipe) {
    return Collections.emptyList();
  }
}
