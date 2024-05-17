package es.degrassi.forge.core.integration.jei.categories;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.api.core.common.IElement;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.MelterController;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.MelterControllerEntity;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.recipe.MelterRecipe;
import es.degrassi.forge.core.common.machines.multiblock.controller.block.entity.recipe.processor.MelterProcessor;
import es.degrassi.forge.core.common.machines.multiblock.controller.client.screen.MelterScreen;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import es.degrassi.forge.core.integration.jei.DegrassiRecipeTypes;
import java.util.List;
import lombok.Getter;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.world.item.ItemStack;

@Getter
public class MelterCategory extends AbstractCategory<MelterRecipe, MelterProcessor, MelterControllerEntity> {
  public static final MelterProcessor defaultProcessor = new MelterProcessor(null, false).init();
  private final MelterProcessor processor = (MelterProcessor) MelterControllerEntity.dummyController().getProcessor().init();

  public MelterCategory(IGuiHelper guiHelper, MelterController block) {
    super("degrassi.jei.recipe.melter", DegrassiRecipeTypes.MELTER);
    this.background = guiHelper.drawableBuilder(MelterScreen.getJeiBackground(), 0, 0, 140, 84).setTextureSize(140, 84).build();
    this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(block));

    addTexture(TextureMap.ENERGY_EMPTY, new DegrassiLocation("textures/gui/jei/melter_energy_empty.png"))
      .addTexture(TextureMap.ENERGY_FILLED, new DegrassiLocation("textures/gui/jei/melter_energy_filled.png"))
      .addTexture(TextureMap.FLUID, new DegrassiLocation("textures/gui/jei/melter_fluid.png"))
      .addTexture(TextureMap.PROGRESS_EMPTY, new DegrassiLocation("textures/gui/jei/melter_progress_empty.png"))
      .addTexture(TextureMap.PROGRESS_FILLED, new DegrassiLocation("textures/gui/jei/melter_progress_filled.png"));

    addXY(PositionMap.ENERGY, 7, 6)
      .addXY(PositionMap.PROGRESS, 62, 33)
      .addXY(PositionMap.FLUID_OUTPUT, 115, 6)
      .addXY(PositionMap.ITEM_INPUT, 35, 33);
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, MelterRecipe recipe, IFocusGroup focuses) {
    MelterProcessor processor = getProcessor();
    MelterControllerEntity entity = processor.getEntity().copy(true);
    List<IElement<?>> elements = entity.getJeiElementManager().get().stream().filter(IElement::isJei).toList();
    ComponentManager manager = entity.getJeiComponentManager();
    MelterRecipe copy = recipe.copy();

    MachineRecipe.separateRequirements(copy, manager.get());
    setRecipeRequirements(builder, copy, manager, elements, processor, recipe);
  }
}
