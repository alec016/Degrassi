package es.degrassi.forge.init.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.forge.init.entity.melter.MelterEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.Mth;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class MelterRenderer extends SafeBlockEntityRenderer<MelterEntity> {
  public MelterRenderer(BlockEntityRendererProvider.Context context) {}

  @Override
  protected void renderSafe(MelterEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
    LerpedFloat fluidLevel = be.getFluidLevel();
    if (fluidLevel == null)
      return;

    float capHeight = 1 / 4f;
    float tankHullWidth = 1 / 16f + 1 / 128f;
    float minPuddleHeight = 1 / 16f;
    float totalHeight = 1 - 2 * capHeight - minPuddleHeight;

    float level = fluidLevel.getValue(partialTicks);
    if (level < 1 / (512f * totalHeight))
      return;
    float clampedLevel = Mth.clamp(level * totalHeight, 0, totalHeight);

    FluidTank tank = be.getFluidStorage();
    FluidStack fluidStack = tank.getFluid();

    if (fluidStack.isEmpty())
      return;

    boolean top = fluidStack.getFluid()
      .getFluidType()
      .isLighterThanAir();

    float xMin = tankHullWidth;
    float xMax = xMin + 1 - 2 * tankHullWidth;
    float yMin = totalHeight + capHeight + minPuddleHeight - clampedLevel;
    float yMax = yMin + clampedLevel;

    if (top) {
      yMin += totalHeight - clampedLevel;
      yMax += totalHeight - clampedLevel;
    }

    float zMin = tankHullWidth;
    float zMax = zMin + 1 - 2 * tankHullWidth;

    ms.pushPose();
    ms.translate(0, clampedLevel - totalHeight, 0);
    FluidRenderer.renderFluidBox(fluidStack, xMin, yMin, zMin, xMax, yMax, zMax, buffer, ms, light, false);
    ms.popPose();
  }

}
