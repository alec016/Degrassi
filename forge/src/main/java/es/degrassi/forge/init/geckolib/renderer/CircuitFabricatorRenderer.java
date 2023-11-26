package es.degrassi.forge.init.geckolib.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import es.degrassi.forge.init.geckolib.entity.CircuitFabricatorEntity;
import es.degrassi.forge.init.geckolib.models.CircuitFabricatorModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class CircuitFabricatorRenderer extends GeoBlockRenderer<CircuitFabricatorEntity> {
  public CircuitFabricatorRenderer(BlockEntityRendererProvider.Context context) {
    super(context, new CircuitFabricatorModel());
  }

  @Override
  public RenderType getRenderType(CircuitFabricatorEntity animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
    return RenderType.entityTranslucent(getTextureLocation(animatable));
  }
}
