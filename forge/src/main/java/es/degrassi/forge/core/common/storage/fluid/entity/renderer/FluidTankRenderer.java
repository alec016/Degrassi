package es.degrassi.forge.core.common.storage.fluid.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.common.utils.LerpedFloat;
import es.degrassi.forge.core.client.renderer.FluidRenderer;
import es.degrassi.forge.core.client.renderer.SafeBlockEntityRenderer;
import es.degrassi.forge.core.common.storage.fluid.entity.FluidTankEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

public class FluidTankRenderer extends SafeBlockEntityRenderer<FluidTankEntity> {

  public FluidTankRenderer(BlockEntityRendererProvider.Context context) {
    super(context);
  }

  @Override
  protected void renderSafe(FluidTankEntity be , float partialTicks , PoseStack ms , MultiBufferSource buffer , int light , int overlay) {
    renderFluid(be, partialTicks, ms, buffer, light, overlay);
  }

  private void renderFluid(@NotNull FluidTankEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
    LerpedFloat fluidLevel = be.getFluidLevel();
    if (fluidLevel == null) return;

    // height offset(both directions) 1 = full block height on each direction
    float capHeight = 1 / 16f /* 1 block pixel offset(1 pixel of top and 1 pixel of bottom) */; //
    // width offset(both directions) 1 = full block width
    float tankHullWidth = 1 / 8f/* 2 block pixel offset(2 north, 2 south, 2 east, 2 west) */ + 1 / 2048f /* offset to not overlap with model side textures(blink texture overlap) */;
    // height offset(from bottom) 1 = full block height
    float minPuddleHeight = 0; /* offset to not overlap with model bottom texture(blink texture overlap) */
    float totalHeight = 15 / 16f /* block height */ - 2 * capHeight - minPuddleHeight /* render height offset */;

    float level = fluidLevel.getValue(partialTicks);
    /*
     * No render if the level is too low( less than 1 / (512 * renderTotalHeight)
     */
    if (level < 1 / (512f * totalHeight)) return;
    float clampedLevel = Mth.clamp(level * totalHeight, 0, totalHeight);

    FluidStack fluidStack = be.getFluidStack();

    if (fluidStack.isEmpty()) return;

    boolean top = fluidStack.getFluid().getFluidType().isLighterThanAir();
    boolean renderBottom = be.getLevel().getBlockState(be.getBlockPos().below()).is(Blocks.AIR);

    float xMax = tankHullWidth + 1 - 2 * tankHullWidth;
    float yMin = totalHeight + capHeight + minPuddleHeight - clampedLevel;
    float yMax = yMin + clampedLevel;

    if (top) {
      yMin += totalHeight - clampedLevel;
      yMax += totalHeight - clampedLevel;
    }

    float zMax = tankHullWidth + 1 - 2 * tankHullWidth;

    ms.pushPose();
    ms.translate(0, clampedLevel - totalHeight, 0);
    FluidRenderer.renderFluidBox(fluidStack, tankHullWidth, yMin, tankHullWidth, xMax, yMax, zMax, buffer, ms, light, renderBottom);
    ms.popPose();
  }
}
