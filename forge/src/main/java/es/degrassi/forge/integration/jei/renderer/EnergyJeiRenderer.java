package es.degrassi.forge.integration.jei.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.forge.init.gui.renderer.EnergyInfoArea;
import es.degrassi.forge.init.recipe.IDegrassiRecipe;
import es.degrassi.forge.integration.jei.IJeiElementRenderer;
import es.degrassi.forge.requirements.IRequirement;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class EnergyJeiRenderer implements IJeiElementRenderer<EnergyInfoArea>, IIngredientRenderer<EnergyInfoArea> {
  private final int width, height;
  public EnergyJeiRenderer() {
    this(16, 70);
  }

  public EnergyJeiRenderer(int width, int height) {
    this.width = width;
    this.height = height;
  }
  @Override
  public void renderElementInJEI(PoseStack matrix, EnergyInfoArea ingredient, IDegrassiRecipe recipe, double mouseX, double mouseY, int x, int y) {}

  @Override
  public boolean isHoveredInJei(@NotNull EnergyInfoArea element, int posX, int posY, int mouseX, int mouseY) {
    int width = element.vertical ? this.height : this.width;
    int height = element.vertical ? this.width : this.height;
    return mouseX >= posX && mouseX <= posX + width && mouseY >= posY && mouseY <= posY + height;
  }

  @Override
  public List<Component> getJEITooltips(EnergyInfoArea element, @NotNull IDegrassiRecipe recipe) {
    List<Component> tooltips = new ArrayList<>();
    tooltips.add(
      element.mode == IRequirement.ModeIO.INPUT
        ? Component.translatable("degrassi.gui.element.energy.input")
        : Component.translatable("degrassi.gui.element.energy.output")
    );
    tooltips.add(Component.literal(
      Component.literal(
        (recipe.getEnergyRequired() * recipe.getTime()) + Component.translatable("unit.energy.forge").getString()
      ).withStyle(ChatFormatting.WHITE).getString()
      + Component.literal(" @ ").withStyle(ChatFormatting.BOLD, ChatFormatting.ITALIC, ChatFormatting.GRAY) +
      Component.literal(
        recipe.getEnergyRequired() + Component.translatable("unit.energy.forge").getString() + "/t"
      ).withStyle(ChatFormatting.WHITE)
    ));
    return tooltips;
  }

  @Override
  public void render(@NotNull PoseStack stack, @NotNull EnergyInfoArea ingredient) {
    ingredient.draw(stack, ingredient.getX(), ingredient.getY());
    if (ingredient.isHoveredOrFocused()) {
      ingredient.drawHighlight(stack, ingredient.getX(), ingredient.getY());
    }
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  @Override
  public @NotNull List<Component> getTooltip(@NotNull EnergyInfoArea ingredient, @NotNull TooltipFlag tooltipFlag) {
    return ingredient.getTooltips();
  }
}
