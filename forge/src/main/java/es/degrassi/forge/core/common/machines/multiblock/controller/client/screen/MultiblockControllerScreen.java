package es.degrassi.forge.core.common.machines.multiblock.controller.client.screen;

import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.api.core.common.RequirementMode;
import es.degrassi.forge.core.common.machines.MachineStatus;
import es.degrassi.forge.core.common.machines.container.MachineContainer;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.BaseMultiblockControllerEntity;
import es.degrassi.forge.core.common.machines.screen.MachineScreen;
import es.degrassi.forge.core.common.processor.MachineProcessor;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import es.degrassi.forge.core.common.requirement.FluidRequirement;
import es.degrassi.forge.core.common.requirement.ItemRequirement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public abstract class MultiblockControllerScreen<C extends MachineContainer<? extends BaseMultiblockControllerEntity<?, ?, ?>>> extends MachineScreen<C> {
  private static final int pxYOffsetString = 15, initialPX = 30;
  private int currentYPX = 20;
  private final int xPX = 12;
  private final Map<RequirementMode, Integer> colorsMyMode = new LinkedHashMap<>();

  // Base color system for requirement mode
  // Override this to change the color by requirement mode
  {
    colorsMyMode.put(RequirementMode.INPUT, 4210752);
    colorsMyMode.put(RequirementMode.OUTPUT, 4210752);
    colorsMyMode.put(RequirementMode.INPUT_PER_TICK, 4210752);
    colorsMyMode.put(RequirementMode.OUTPUT_PER_TICK, 4210752);
  }

  public MultiblockControllerScreen(C menu, Inventory playerInventory, Component title, ResourceLocation texture) {
    super(menu, playerInventory, title, texture);
  }

  @Override
  protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
    super.renderLabels(guiGraphics, mouseX, mouseY);

    guiGraphics.pose().pushPose();
    guiGraphics.pose().scale(0.7F, 0.7F, 0.7F);
    renderRecipe(guiGraphics, mouseX, mouseY);
    guiGraphics.pose().popPose();
  }

  /**
   *<pre>
   *Renders by {@link MachineStatus}:
   *- {@link MachineStatus#RUNNING}:
   * * Renders the recipe progress.
   * * Renders item input/output.
   * * Renders fluid input/output
   *- {@link MachineStatus#IDLE}:
   * * No valid recipe
   *- {@link MachineStatus#ERROR}:
   * * The error message that has been produced</pre>
   */
  protected void renderRecipe(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    currentYPX = initialPX;
    BaseMultiblockControllerEntity<?, ?, ?> entity = menu.getEntity();
    // Separate rendering by machine status
    switch (entity.getStatus()) {
      case RUNNING -> {
        MachineProcessor<?, ? extends MachineEntity<?>> processor = entity.getProcessor();
        if (entity.getProcessor() == null) return;
        processor.init();
        String progressString = processor.getProgressPercentage();
        int progress = processor.getProgress();
        int recipeTime = processor.getRecipeTime();
        // Renders the processing recipe progress
        guiGraphics.drawString(
          font,
          Component.literal("Progress: " + progress + "/" + recipeTime + " (" + progressString + ")"),
          xPX,
          currentYPX,
          defaultColor,
          false
        );
        currentYPX += pxYOffsetString;
        MachineRecipe<?> recipe = processor.getCurrentRecipe();
        if (recipe == null) return;
        // Gets all requirements
        List<IRequirement<?>> requirements = recipe.getRequirements();
        // Gets item requirements filtering by type
        List<ItemRequirement> itemRequirements = requirements.stream().filter(req -> req instanceof ItemRequirement).map(req -> (ItemRequirement) req).toList();
        // Gets fluid requirements filtering by type
        List<FluidRequirement> fluidRequirements = requirements.stream().filter(req -> req instanceof FluidRequirement).map(req -> (FluidRequirement) req).toList();

        // Renders item requirements
        itemRequirements.forEach(requirement -> {
          int color = colorsMyMode.get(requirement.getMode());
          guiGraphics.drawString(
            font,
            Component.literal(fromMode(requirement.getMode()).replace("{}", requirement.getAmount() + "x " + requirement.getItem().getDefaultInstance().getDisplayName().getString().replaceAll("\\[", "").replaceAll("]", ""))),
            xPX,
            currentYPX,
            color,
            false
          );
          currentYPX += pxYOffsetString;
        });

        // Renders fluid requirements
        fluidRequirements.forEach(requirement -> {
          int color = colorsMyMode.get(requirement.getMode());
          guiGraphics.drawString(
            font,
            Component.literal(fromMode(requirement.getMode()).replace("{}", requirement.getAmount() + "mB " + requirement.getFluid().getFluidType().getDescription().getString())),
            xPX,
            currentYPX,
            color,
            false
          );
          currentYPX += pxYOffsetString;
        });
      }
      case IDLE -> {
        // Renders when there is no valid recipe
        guiGraphics.drawString(
          font,
          Component.literal("No valid recipe"),
          xPX,
          currentYPX,
          defaultColor,
          false
        );
        currentYPX += pxYOffsetString;
      }
      case ERROR -> {
        // Renders the error in case this happens
        guiGraphics.drawString(
          font,
          Component.literal("Error: " + entity.getErrorMessage().getString()),
          xPX,
          currentYPX,
          defaultColor,
          false
        );
        currentYPX += pxYOffsetString;
      }
    }
  }

  protected final String fromMode(RequirementMode mode) {
    return switch(mode) {
      case OUTPUT -> "Produce {}";
      case INPUT -> "Require {}";
      case OUTPUT_PER_TICK -> "Produce {} /t";
      case INPUT_PER_TICK -> "Require {} /t";
    };
  }
}
