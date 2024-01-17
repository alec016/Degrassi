package es.degrassi.forge.integration.jei.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.forge.init.gui.screen.IScreen;
import es.degrassi.forge.init.gui.element.ProgressGuiElement;
import es.degrassi.forge.init.recipe.IDegrassiRecipe;
import es.degrassi.forge.integration.jei.IJeiElementRenderer;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ProgressJeiRenderer implements IJeiElementRenderer<ProgressGuiElement>, IIngredientRenderer<ProgressGuiElement> {
  private final int width, height;
  public ProgressJeiRenderer(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public ProgressJeiRenderer() {
    this(16, 16);
  }

  @Override
  public void renderElementInJEI(PoseStack stack, @NotNull ProgressGuiElement ingredient, IDegrassiRecipe recipe, double mouseX, double mouseY, int x, int y) {
    if(recipe.getTime() > 0) {
      ingredient.draw(stack, ingredient.getX(), ingredient.getY());
    }
  }

  @Override
  public boolean isHoveredInJei(@NotNull ProgressGuiElement element, int posX, int posY, int mouseX, int mouseY) {
    boolean invertAxis = element.getFilledTexture().equals(IScreen.FILLED_ARROW) && element.getDirection() != ProgressGuiElement.Orientation.RIGHT && element.getDirection() != ProgressGuiElement.Orientation.LEFT;
    int width = invertAxis ? element.getHeight() : element.getWidth();
    int height = invertAxis ? element.getWidth() : element.getHeight();
    return mouseX >= posX && mouseX <= posX + width && mouseY >= posY && mouseY <= posY + height;
  }

  @Override
  public List<Component> getJEITooltips(ProgressGuiElement element, @NotNull IDegrassiRecipe recipe) {
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
  public void render(@NotNull PoseStack stack, @NotNull ProgressGuiElement ingredient) {
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }
  @Override
  public @NotNull List<Component> getTooltip(@NotNull ProgressGuiElement ingredient, @NotNull TooltipFlag tooltipFlag) {
    return ingredient.getTooltips();
  }
}
