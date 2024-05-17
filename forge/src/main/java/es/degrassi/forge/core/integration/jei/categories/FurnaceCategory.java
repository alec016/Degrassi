package es.degrassi.forge.core.integration.jei.categories;

import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.api.core.common.IElement;
import es.degrassi.forge.core.common.ComponentManager;
import es.degrassi.forge.core.common.machines.block.FurnaceBlock;
import es.degrassi.forge.core.common.machines.entity.FurnaceEntity;
import es.degrassi.forge.core.common.machines.screen.FurnaceScreen;
import es.degrassi.forge.core.common.processor.FurnaceProcessor;
import es.degrassi.forge.core.common.recipe.FurnaceRecipe;
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
public class FurnaceCategory extends AbstractCategory<FurnaceRecipe, FurnaceProcessor, FurnaceEntity> {
  public static final FurnaceProcessor defaultProcessor = new FurnaceProcessor(null, false).init();
  private final FurnaceProcessor processor = (FurnaceProcessor) FurnaceEntity.dummyEntity().getProcessor().init();

  public FurnaceCategory(IGuiHelper guiHelper, FurnaceBlock block) {
    super("degrassi.jei.recipe.furnace", DegrassiRecipeTypes.FURNACE);
    this.background = guiHelper.drawableBuilder(FurnaceScreen.getJeiBackground(), 0, 0, 126, 73).setTextureSize(126, 73).build();
    this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(block));

    addTexture(TextureMap.ENERGY_EMPTY, new DegrassiLocation("textures/gui/jei/furnace_energy_empty.png"))
      .addTexture(TextureMap.ENERGY_FILLED, new DegrassiLocation("textures/gui/jei/furnace_energy_filled.png"))
      .addTexture(TextureMap.PROGRESS_EMPTY, new DegrassiLocation("textures/gui/jei/furnace_progress_empty.png"))
      .addTexture(TextureMap.PROGRESS_FILLED, new DegrassiLocation("textures/gui/jei/furnace_progress_filled.png"));

    addXY(PositionMap.PROGRESS, 41, 14)
      .addXY(PositionMap.ENERGY, 7, 50)
      .addXY(PositionMap.EXPERIENCE, 31, 37)
      .addXY(PositionMap.ITEM_INPUT, 13, 14)
      .addXY(PositionMap.ITEM_OUTPUT, 93, 14);
  }

  @Override
  public void setRecipe(IRecipeLayoutBuilder builder, FurnaceRecipe recipe, IFocusGroup focuses) {
    FurnaceProcessor processor = getProcessor();
    FurnaceEntity entity = processor.getEntity().copy(true);
    List<IElement<?>> elements = entity.getJeiElementManager().get().stream().filter(IElement::isJei).toList();
    ComponentManager manager = entity.getJeiComponentManager();
    FurnaceRecipe copy = recipe.copy();

    MachineRecipe.separateRequirements(copy, manager.get());
    setRecipeRequirements(builder, copy, manager, elements, processor, recipe);
  }
}
