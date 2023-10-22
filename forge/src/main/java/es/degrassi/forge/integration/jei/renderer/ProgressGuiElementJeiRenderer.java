package es.degrassi.forge.integration.jei.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.client.IClientHandler;
import es.degrassi.comon.init.gui.screen.IScreen;
import es.degrassi.comon.init.gui.screen.renderer.ProgressComponent;
import es.degrassi.comon.init.recipe.IDegrassiRecipe;
import es.degrassi.forge.integration.jei.IJeiElementRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProgressGuiElementJeiRenderer implements IJeiElementRenderer<ProgressComponent> {
  @Override
  public void renderElementInJEI(PoseStack matrix, @NotNull ProgressComponent element, IDegrassiRecipe recipe, double mouseX, double mouseY, int x, int y) {
    int width = element.getWidth();
    int height = element.getHeight();

    if(Minecraft.getInstance().level == null)
      return;

    int filledWidth = 0;
    int filledHeight = 0;
    if(recipe.getTime() > 0) {
      filledWidth = (int)(Minecraft.getInstance().level.getGameTime() % width);
      filledHeight = (int)(Minecraft.getInstance().level.getGameTime() % height);
    }

    IClientHandler.bindTexture(element.getFilledTexture());
    switch (element.getDirection()) {
      case RIGHT -> GuiComponent.blit(matrix, x, y, 0, 0, filledWidth, height, width, height);
      case LEFT -> GuiComponent.blit(matrix, x + width - filledWidth, y, width - filledWidth, 0, filledWidth, height, width, height);
      case TOP -> GuiComponent.blit(matrix, x, y, 0, 0, width, filledHeight, width, height);
      case BOTTOM -> GuiComponent.blit(matrix, x, y + height - filledHeight, 0, height - filledHeight, width, filledHeight, width, height);
    }
  }

  @Override
  public boolean isHoveredInJei(@NotNull ProgressComponent element, int posX, int posY, int mouseX, int mouseY) {
    boolean invertAxis = element.getFilledTexture().equals(IScreen.FILLED_ARROW) && element.getDirection() != ProgressComponent.Orientation.RIGHT && element.getDirection() != ProgressComponent.Orientation.LEFT;
    int width = invertAxis ? element.getHeight() : element.getWidth();
    int height = invertAxis ? element.getWidth() : element.getHeight();
    return mouseX >= posX && mouseX <= posX + width && mouseY >= posY && mouseY <= posY + height;
  }

  @Override
  public List<Component> getJEITooltips(ProgressComponent element, @NotNull IDegrassiRecipe recipe) {
    List<Component> tooltips = new ArrayList<>();
    if(recipe.getTime() > 0)
      tooltips.add(Component.translatable("degrassi.jei.recipe.time", recipe.getTime()));
    else
      tooltips.add(Component.translatable("degrassi.jei.recipe.instant"));
    if(Minecraft.getInstance().options.advancedItemTooltips)
      tooltips.add(Component.translatable("degrassi.jei.recipe.id", recipe.getId().toString()).withStyle(ChatFormatting.DARK_GRAY));
    return tooltips;
  }
}
