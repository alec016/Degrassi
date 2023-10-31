package es.degrassi.forge.init.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import es.degrassi.forge.init.block.melter.Melter;
import es.degrassi.forge.init.entity.melter.MelterEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@SuppressWarnings("unused")
public class MelterRenderer extends SafeBlockEntityRenderer<MelterEntity> {
  public MelterRenderer(BlockEntityRendererProvider.Context context) {}

  @Override
  protected void renderSafe(@NotNull MelterEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
    renderItem(be, partialTicks, ms, buffer, light, overlay);
    renderFluid(be, partialTicks, ms, buffer, light, overlay);
  }

  private void renderItem(@NotNull MelterEntity blockEntity, float partialTicks, @NotNull PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
    ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
    ItemStack itemStack = blockEntity.getRenderStack();

    float fluidPercentage = (float) blockEntity.getFluidStorage().getFluidAmount() / blockEntity.getFluidStorage().getCapacity();
    float minY = 0.14f;

    if (fluidPercentage > 0.7) {
      minY = fluidPercentage + 0.01f;
    }

    poseStack.pushPose();
    poseStack.translate(0.5f, Math.max((fluidPercentage + 0.05f) > 1 ? 1.01f : fluidPercentage + 0.05f, minY), 0.5f);
    poseStack.scale(0.25f, 0.25f, 0.25f);
    poseStack.mulPose((Vector3f.XP.rotationDegrees(90)));

    switch (blockEntity.getBlockState().getValue(Melter.FACING)) {
      case NORTH -> poseStack.mulPose(Vector3f.ZP.rotationDegrees(0));
      case EAST -> poseStack.mulPose(Vector3f.ZP.rotationDegrees(90));
      case SOUTH -> poseStack.mulPose(Vector3f.ZP.rotationDegrees(180));
      case WEST -> poseStack.mulPose(Vector3f.ZP.rotationDegrees(270));
    }

    itemRenderer.renderStatic(
      itemStack,
      ItemTransforms.TransformType.GUI,
      getLightLevel(Objects.requireNonNull(blockEntity.getLevel()), blockEntity.getBlockPos()),
      OverlayTexture.NO_OVERLAY,
      poseStack,
      buffer,
      1
    );
    poseStack.popPose();
  }

  private int getLightLevel(@NotNull Level level, BlockPos pos) {
    int bLight = level.getBrightness(LightLayer.BLOCK, pos);
    int sLight = level.getBrightness(LightLayer.SKY, pos);
    return LightTexture.pack(bLight, sLight);
  }

  private void renderFluid(@NotNull MelterEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
    LerpedFloat fluidLevel = be.getFluidLevel();
    if (fluidLevel == null)
      return;

    float capHeight = 0f;
    float tankHullWidth = 1 / 3f + 1 / 24f; // 0,375
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
