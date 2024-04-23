package es.degrassi.forge.core.integration.jei.wrapper;

import es.degrassi.forge.api.core.common.ElementDirection;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.core.common.component.EnergyComponent;
import es.degrassi.forge.core.common.element.EnergyElement;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import es.degrassi.forge.core.common.requirement.EnergyRequirement;
import java.util.ArrayList;
import java.util.List;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.NotNull;

public class EnergyWrapper extends EnergyElement implements IIngredientRenderer<EnergyComponent> {
  public static final EnergyWrapper DUMMY = new EnergyWrapper(0, 0, null, null, ElementDirection.TOP, null, false) {
    @Override
    public int getWidth() {
      return 16;
    }

    @Override
    public int getHeight() {
      return 16;
    }
  };
  private final MachineRecipe<?> recipe;
  private final boolean animated;

  public EnergyWrapper(int x, int y, @NotNull ResourceLocation emptyTexture, @NotNull ResourceLocation filledTexture, ElementDirection direction, MachineRecipe<?> recipe, boolean animated) {
    super(null, x, y, Component.literal("energy"), emptyTexture, filledTexture, "energy", direction);
    this.recipe = recipe;
    this.animated = animated;
  }

  public MachineRecipe<?> getRecipe() {
    return recipe;
  }

  @Override
  public void render(GuiGraphics guiGraphics, EnergyComponent ingredient) {}

  @Override
  public void renderInJei(GuiGraphics guiGraphics, MachineRecipe<?> recipe, double mouseX, double mouseY, IComponent iComponent) {
    if (!(iComponent instanceof EnergyComponent component)) return;
    EnergyRequirement requirement = recipe.getRequirements().stream().filter(req -> req.getId().equals(component.getId())).map(req -> (EnergyRequirement) req).findFirst().orElse(null);
    if (requirement == null) return;
    int total = requirement.getMode().isPerTick() ? requirement.getAmount() : requirement.getAmount() * recipe.getTime();
    component.setCapacity(total);
    if (animated) {
      int toIncrement = requirement.getMode().isPerTick() ? requirement.getAmount() : requirement.getAmount() / recipe.getTime();
      component.receiveEnergy(toIncrement, false);
      super.renderInJei(guiGraphics, recipe, mouseX, mouseY, iComponent);
      if (component.getEnergyStored() >= component.getMaxEnergyStored()) component.setEnergy(0);
      return;
    }
    component.setEnergy(total);
    super.renderInJei(guiGraphics, recipe, mouseX, mouseY, iComponent);
  }

  @Override
  public List<Component> getTooltip(EnergyComponent ingredient, TooltipFlag tooltipFlag) {
    EnergyRequirement requirement = recipe.getRequirements().stream().filter(req -> req.getId().equals(ingredient.getId())).map(req -> (EnergyRequirement) req).findFirst().orElse(null);
    if (requirement == null) return List.of();
    List<Component> tooltips = new ArrayList<>();
    tooltips.add(
      Component.translatable(
        "degrassi.jei.recipe.energy." + (requirement.getMode().isInput() ? "input" : "output")
      ).withStyle(ChatFormatting.ITALIC)
    );
    switch(requirement.getMode()) {
      case INPUT, OUTPUT -> tooltips.add(Component.translatable("degrassi.jei.recipe.energy.total", requirement.getAmount()));
      case INPUT_PER_TICK, OUTPUT_PER_TICK -> tooltips.add(
        Component.translatable(
          "degrassi.jei.recipe.energy",
          Component.translatable(
            "degrassi.jei.recipe.energy.total",
            requirement.getAmount() * recipe.getTime()
          ),
          Component.literal("@").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC),
          Component.translatable(
            "degrassi.jei.recipe.energy.total",
            requirement.getAmount()
          )
        )
      );
    }
    return tooltips;
  }
}
