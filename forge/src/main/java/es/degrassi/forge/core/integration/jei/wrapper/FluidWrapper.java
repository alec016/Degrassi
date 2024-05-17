package es.degrassi.forge.core.integration.jei.wrapper;

import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.component.FluidComponent;
import es.degrassi.forge.core.common.element.FluidElement;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import es.degrassi.forge.core.common.requirement.FluidRequirement;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

@Getter
public class FluidWrapper extends FluidElement implements IIngredientRenderer<FluidComponent> {
  public static final FluidWrapper DUMMY = new FluidWrapper(0, 0, null, null, null, "fluid", false) {
    @Override
    public int getWidth() {
      return 16;
    }

    @Override
    public int getHeight() {
      return 16;
    }
  };
  private MachineRecipe<?> recipe;
  private final FluidRequirement requirement;
  private final boolean animated;

  public FluidWrapper(int x, int y, @NotNull ResourceLocation emptyTexture, FluidRequirement requirement, MachineRecipe<?> recipe, String id, boolean animated) {
    super(null, x, y, emptyTexture, Component.literal("fluid"), id, true);
    this.animated = animated;
    this.recipe = recipe;
    this.requirement = requirement;
  }

  @Override
  public void render(GuiGraphics guiGraphics, FluidComponent ingredient) {}

  @Override
  public void renderInJei(GuiGraphics guiGraphics, IRequirement<?> iRequirement, MachineRecipe<?> recipe, double mouseX, double mouseY, IComponent iComponent) {
    FluidComponent component = requirement.getComponent();
    if (component == null) return;
    this.recipe = recipe;
    int total = requirement.getMode().isPerTick() ? requirement.getAmount() * recipe.getTime() : requirement.getAmount();
    component.setCapacity(total);
    if (animated) {
      int toIncrement = requirement.getMode().isPerTick() ? requirement.getAmount() : requirement.getAmount() / recipe.getTime();
      component.fill(new FluidStack(requirement.getFluid(), toIncrement), IFluidHandler.FluidAction.EXECUTE);
      super.renderInJei(guiGraphics, requirement, recipe, mouseX, mouseY, iComponent);
      if (component.getFluid().getAmount() >= component.getCapacity()) component.setFluid(FluidStack.EMPTY);
      return;
    }
    component.setFluid(new FluidStack(requirement.getFluid(), requirement.getAmount()));
    super.renderInJei(guiGraphics, requirement, recipe, mouseX, mouseY, iComponent);
  }

  @Override
  public List<Component> getTooltip(FluidComponent ingredient, TooltipFlag tooltipFlag) {
    if (requirement == null) return List.of();
    List<Component> tooltips = new ArrayList<>();
    tooltips.add(
      Component.translatable(
        "degrassi.jei.recipe.fluid." + (requirement.getMode().isInput() ? "input" : "output")
      ).withStyle(ChatFormatting.ITALIC)
    );
    tooltips.add(new FluidStack(requirement.getFluid(), requirement.getAmount()).getDisplayName().copy().withStyle(ChatFormatting.BOLD));
    switch(requirement.getMode()) {
      case INPUT, OUTPUT -> tooltips.add(Component.translatable("degrassi.jei.recipe.fluid.total", requirement.getAmount()));
      case INPUT_PER_TICK, OUTPUT_PER_TICK -> tooltips.add(
        Component.translatable(
          "degrassi.jei.recipe.fluid",
          Component.translatable(
            "degrassi.jei.recipe.fluid.total",
            requirement.getAmount() * recipe.getTime()
          ),
          Component.literal("@").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC),
          Component.translatable(
            "degrassi.jei.recipe.fluid.total",
            requirement.getAmount()
          )
        )
      );
    }
    return tooltips;
  }

  @Override
  public String toString() {
    return "FluidWrapper{" +
      "id='" + getId() + '\'' +
      ", texture=" + getTexture().toString() +
      ", jei=" + isJei() +
      ", animated=" + animated +
      '}';
  }
}
