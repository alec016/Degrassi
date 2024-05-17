package es.degrassi.forge.core.integration.jei.wrapper;

import es.degrassi.forge.api.core.common.ElementDirection;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.component.ExperienceComponent;
import es.degrassi.forge.core.common.element.ExperienceElement;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import es.degrassi.forge.core.common.requirement.ExperienceRequirement;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;

public class ExperienceWrapper extends ExperienceElement implements IIngredientRenderer<ExperienceComponent> {
  @Getter
  private MachineRecipe<?> recipe;
  private final boolean animated;
  public static final ExperienceWrapper DUMMY = new ExperienceWrapper(0, 0, null, null, ElementDirection.RIGHT, null, null, false, "experience") {
    @Override
    public int getWidth() {
      return 16;
    }

    @Override
    public int getHeight() {
      return 16;
    }
  };

  private final ExperienceRequirement requirement;

  public ExperienceWrapper(int x, int y, ResourceLocation emptyTexture, ResourceLocation filledTexture, ElementDirection direction, ExperienceRequirement requirement, MachineRecipe<?> recipe, boolean animated, String id) {
    super(null, x, y, id, Component.literal("experience"), emptyTexture, filledTexture, direction, true);
    this.recipe = recipe;
    this.animated = animated;
    this.requirement = requirement;
  }

  @Override
  public void render(GuiGraphics guiGraphics, ExperienceComponent ingredient) {}

  @Override
  public void renderInJei(GuiGraphics guiGraphics, IRequirement<?> iRequirement, MachineRecipe<?> recipe, double mouseX, double mouseY, IComponent iComponent) {
    ExperienceComponent component = requirement.getComponent();
    if (component == null) return;
    this.recipe = recipe;
    float total = requirement.getMode().isPerTick() ? requirement.getXp() * recipe.getTime() : requirement.getXp();
    component.setCapacity(total);
    if (animated) {
      float toIncrement = requirement.getMode().isPerTick() ? requirement.getXp() : requirement.getXp() / recipe.getTime();
      component.receiveExperience(toIncrement, false);
      super.renderInJei(guiGraphics, requirement, recipe, mouseX, mouseY, iComponent);
      if (component.getExperienceStored() >= component.getCapacity()) component.setExperience(0);
      return;
    }
    component.setExperience(total);
    super.renderInJei(guiGraphics, requirement, recipe, mouseX, mouseY, iComponent);
  }

  @Override
  public List<Component> getTooltip(ExperienceComponent ingredient, TooltipFlag tooltipFlag) {
    if (requirement == null) return List.of();
    List<Component> tooltips = new ArrayList<>();
    tooltips.add(
      Component.translatable(
        "degrassi.jei.recipe.experience." + (requirement.getMode().isInput() ? "input" : "output")
      ).withStyle(ChatFormatting.ITALIC)
    );
    switch(requirement.getMode()) {
      case INPUT, OUTPUT -> tooltips.add(Component.translatable("degrassi.jei.recipe.experience.total", requirement.getXp()));
      case INPUT_PER_TICK, OUTPUT_PER_TICK -> tooltips.add(
        Component.translatable(
          "degrassi.jei.recipe.experience",
          Component.translatable(
            "degrassi.jei.recipe.experience.total",
            requirement.getXp() * recipe.getTime()
          ),
          Component.literal("@").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC),
          Component.translatable(
            "degrassi.jei.recipe.experience.total",
            requirement.getXp()
          )
        )
      );
    }
    return tooltips;
  }
}
