package es.degrassi.forge.core.integration.jei.wrapper;

import es.degrassi.forge.api.core.common.ElementDirection;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.core.common.component.ProgressComponent;
import es.degrassi.forge.core.common.element.ProgressElement;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import java.util.ArrayList;
import java.util.List;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

public class ProgressWrapper extends ProgressElement implements IIngredientRenderer<ProgressComponent> {
  private final MachineRecipe<?> recipe;
  private final boolean animated;
  public static final ProgressWrapper DUMMY = new ProgressWrapper(0, 0, null, null, ElementDirection.RIGHT, null, false) {
    @Override
    public int getWidth() {
      return 16;
    }

    @Override
    public int getHeight() {
      return 16;
    }
  };

  public ProgressWrapper(int x, int y, @NotNull ResourceLocation emptyTexture, @NotNull ResourceLocation filledTexture, ElementDirection direction, MachineRecipe<?> recipe, boolean animated) {
    super(null, x, y, Component.literal("progress"), emptyTexture, filledTexture, direction);
    this.recipe = recipe;
    this.animated = animated;
  }

  public MachineRecipe<?> getRecipe() {
    return recipe;
  }

  @Override
  public void render(GuiGraphics guiGraphics, ProgressComponent ingredient) {
  }

  @Override
  public void renderInJei(GuiGraphics guiGraphics, MachineRecipe<?> recipe, double mouseX, double mouseY, IComponent iComponent) {
    if (!(iComponent instanceof ProgressComponent component)) return;
    component.setMaxProgress(recipe.getTime());
    if (animated) {
      component.tick();
      super.renderInJei(guiGraphics, recipe, mouseX, mouseY, component);
      if (component.hasEnded()) component.resetProgress();
      return;
    }
    component.setProgress(recipe.getTime());
    super.renderInJei(guiGraphics, recipe, mouseX, mouseY, component);
  }

  @Override
  public List<Component> getTooltip(ProgressComponent ingredient, TooltipFlag tooltipFlag) {
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
