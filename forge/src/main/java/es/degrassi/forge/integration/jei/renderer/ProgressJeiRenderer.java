package es.degrassi.forge.integration.jei.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.forge.client.IClientHandler;
import es.degrassi.forge.init.gui.screen.IScreen;
import es.degrassi.forge.init.gui.renderer.ProgressComponent;
import es.degrassi.forge.init.recipe.IDegrassiRecipe;
import es.degrassi.forge.integration.jei.IJeiElementRenderer;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProgressJeiRenderer implements IJeiElementRenderer<ProgressComponent>, IIngredientRenderer<ProgressComponent> {
  private final int width, height;
  public ProgressJeiRenderer(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public ProgressJeiRenderer() {
    this(16, 16);
  }

  @Override
  public void renderElementInJEI(PoseStack stack, @NotNull ProgressComponent ingredient, IDegrassiRecipe recipe, double mouseX, double mouseY, int x, int y) {
//    int width = ingredient.getWidth();
//    int height = ingredient.getHeight();
//
//    if(Minecraft.getInstance().level == null)
//      return;
//
//    int filledWidth = 0;
//    int filledHeight = 0;
    if(recipe.getTime() > 0) {
      ingredient.draw(stack, ingredient.getX(), ingredient.getY());
//      ingredient.getStorage().increment();
//      filledWidth = (int)(Minecraft.getInstance().level.getGameTime() % width);
//      filledHeight = (int)(Minecraft.getInstance().level.getGameTime() % height);
    }

//    IClientHandler.bindTexture(ingredient.getFilledTexture());
//    if (ingredient.isInverted()) {
//      filledWidth = (int)(width % Minecraft.getInstance().level.getGameTime());
//      filledHeight = (int)(height % Minecraft.getInstance().level.getGameTime());
//    }
//
//    switch (ingredient.getDirection()) {
//      case RIGHT -> GuiComponent.blit(
//        stack, x, y,
//        0, 0,
//        filledWidth, height,
//        width, height
//      );
//      case LEFT -> GuiComponent.blit(
//        stack, x + width - filledWidth, y,
//        width - filledWidth, 0,
//        filledWidth, height,
//        width, height
//      );
//      case TOP -> GuiComponent.blit(
//        stack, x, y,
//        0, 0,
//        width, filledHeight,
//        width, height
//      );
//      case BOTTOM -> GuiComponent.blit(
//        stack, x, y + height - filledHeight,
//        0, height - filledHeight,
//        width, filledHeight,
//        width, height
//      );
//    }
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

  @Override
  public void render(@NotNull PoseStack stack, @NotNull ProgressComponent ingredient) {
//    ingredient.draw(stack, ingredient.getX(), ingredient.getY());
//    ingredient.getStorage().increment();
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }
  @Override
  public @NotNull List<Component> getTooltip(@NotNull ProgressComponent ingredient, @NotNull TooltipFlag tooltipFlag) {
    return ingredient.getTooltips();
  }
}
