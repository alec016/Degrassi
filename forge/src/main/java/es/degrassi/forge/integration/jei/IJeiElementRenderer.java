package es.degrassi.forge.integration.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.forge.init.gui.element.IGuiElement;
import es.degrassi.forge.init.recipe.IDegrassiRecipe;
import net.minecraft.network.chat.Component;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public interface IJeiElementRenderer<T extends IGuiElement> {
  void renderElementInJEI(PoseStack matrix, T element, IDegrassiRecipe recipe, double mouseX, double mouseY, int x, int y);
  default boolean isHoveredInJei(T element, int posX, int posY, int mouseX, int mouseY) {
    return mouseX >= posX && mouseX <= posX + element.getWidth() && mouseY >= posY && mouseY <= posY + element.getHeight();
  }
  default List<Component> getJEITooltips(T element, IDegrassiRecipe recipe) {
    return Collections.emptyList();
  }
}
