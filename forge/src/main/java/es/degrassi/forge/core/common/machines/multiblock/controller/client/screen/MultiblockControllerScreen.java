package es.degrassi.forge.core.common.machines.multiblock.controller.client.screen;

import es.degrassi.common.utils.DegrassiLogger;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.api.core.common.RequirementMode;
import es.degrassi.forge.core.common.machines.MachineStatus;
import es.degrassi.forge.core.common.machines.container.MachineContainer;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.BaseMultiblockControllerEntity;
import es.degrassi.forge.core.common.machines.screen.MachineScreen;
import es.degrassi.forge.core.common.processor.MachineProcessor;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import es.degrassi.forge.core.common.requirement.EnergyRequirement;
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
  private static final int pxYOffsetString = 15, initialYPX = 30, initialXPX = 12;
  private int currentYPX = 20;
  private int currentXPX = 12;
  protected final Map<RequirementMode, Integer> colorsMyMode = new LinkedHashMap<>();

  public MultiblockControllerScreen(C menu, Inventory playerInventory, Component title, ResourceLocation texture) {
    super(menu, playerInventory, title, texture);

    // Base color system for requirement mode
    // Override this to change the color by requirement mode
    colorsMyMode.put(RequirementMode.INPUT, 0x00FFE8);
    colorsMyMode.put(RequirementMode.OUTPUT, 0xFF0051);
    colorsMyMode.put(RequirementMode.INPUT_PER_TICK, 0x00FFE8);
    colorsMyMode.put(RequirementMode.OUTPUT_PER_TICK, 0xFF0051);
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
    currentYPX = initialYPX;
    currentXPX = initialXPX;
    BaseMultiblockControllerEntity<?, ?, ?> entity = menu.getEntity();
    // Separate rendering by machine status
    switch (entity.getStatus()) {
      case RUNNING -> {
        MachineProcessor<?, ? extends MachineEntity<?>> processor = entity.getProcessor();
        if (processor == null) return;
        processor.init();
        if (processor.getCurrentRecipe() == null) return;
        String progressString = processor.getProgressPercentage();
        int progress = processor.getProgress();
        int recipeTime = processor.getRecipeTime();
        // Renders the processing recipe progress
        guiGraphics.drawString(
          font,
          Component.literal("Progress: " + progress + "/" + recipeTime + " (" + progressString + ")"),
          currentXPX,
          currentYPX,
          defaultColor,
          false
        );
        currentYPX += pxYOffsetString;
        MachineRecipe<?> recipe = processor.getCurrentRecipe().copy();
        // Gets all requirements
        List<IRequirement<?>> requirements = recipe.getRequirements();
        // Gets item requirements filtering by type
        List<ItemRequirement> itemRequirements = requirements.stream().filter(req -> req instanceof ItemRequirement).map(req -> (ItemRequirement) req).toList();
        // Gets fluid requirements filtering by type
        List<FluidRequirement> fluidRequirements = requirements.stream().filter(req -> req instanceof FluidRequirement).map(req -> (FluidRequirement) req).toList();
        EnergyRequirement energyRequirement = requirements.stream().filter(req -> req instanceof EnergyRequirement).map(req -> (EnergyRequirement) req).findFirst().orElse(null);

        if (energyRequirement != null) {
          currentXPX = initialXPX;
          int color = colorsMyMode.get(energyRequirement.getMode());
          String text = fromMode(energyRequirement.getMode());
          guiGraphics.drawString(
            font,
            Component.literal(text),
            currentXPX,
            currentYPX,
            defaultColor,
            false
          );
          currentXPX += font.width(text);
          text = energyRequirement.getAmount() + " RF";
          guiGraphics.drawString(
            font,
            Component.literal(text),
            currentXPX,
            currentYPX,
            color,
            false
          );
          currentXPX += font.width(text);
          if (energyRequirement.getMode().isPerTick()) {
            guiGraphics.drawString(
              font,
              Component.literal(" /t"),
              currentXPX,
              currentYPX,
              defaultColor,
              false
            );
          }
          currentYPX += pxYOffsetString;
        }

        // Renders item requirements
        itemRequirements.forEach(requirement -> {
          currentXPX = initialXPX;
          int color = colorsMyMode.get(requirement.getMode());
          String text = fromMode(requirement.getMode());
          guiGraphics.drawString(
            font,
            Component.literal(text),
            currentXPX,
            currentYPX,
            defaultColor,
            false
          );
          currentXPX += font.width(text);
          text = requirement.getAmount() +
            "x " +
            requirement.getItem().getDefaultInstance().getDisplayName().getString().replaceAll("\\[", "").replaceAll("]", "");
          guiGraphics.drawString(
            font,
            Component.literal(text),
            currentXPX,
            currentYPX,
            color,
            false
          );
          currentXPX += font.width(text);
          if (requirement.getMode().isPerTick()) {
            guiGraphics.drawString(
              font,
              Component.literal(" /t"),
              currentXPX,
              currentYPX,
              defaultColor,
              false
            );
          }
          currentYPX += pxYOffsetString;
        });

        // Renders fluid requirements
        fluidRequirements.forEach(requirement -> {
          currentXPX = initialXPX;
          int color = colorsMyMode.get(requirement.getMode());
          String text = fromMode(requirement.getMode());
          guiGraphics.drawString(
            font,
            Component.literal(text),
            currentXPX,
            currentYPX,
            defaultColor,
            false
          );
          currentXPX += font.width(text);
          text = requirement.getAmount() +
            "mB " +
            requirement.getFluid().getFluidType().getDescription().getString();
          guiGraphics.drawString(
            font,
            Component.literal(text),
            currentXPX,
            currentYPX,
            color,
            false
          );
          currentXPX += font.width(text);
          if (requirement.getMode().isPerTick()) {
            guiGraphics.drawString(
              font,
              Component.literal(" /t"),
              currentXPX,
              currentYPX,
              defaultColor,
              false
            );
          }
          currentYPX += pxYOffsetString;
        });
      }
      case IDLE -> {
        // Renders when there is no valid recipe
        guiGraphics.drawString(
          font,
          Component.literal("No valid recipe"),
          currentXPX,
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
          currentXPX,
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
      case OUTPUT, OUTPUT_PER_TICK -> "Produce: ";
      case INPUT, INPUT_PER_TICK -> "Require: ";
    };
  }
}
