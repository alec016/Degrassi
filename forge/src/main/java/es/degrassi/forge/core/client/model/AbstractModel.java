package es.degrassi.forge.core.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import es.degrassi.forge.core.client.renderer.MachineRenderer;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import java.util.function.Function;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public abstract class AbstractModel<T extends MachineEntity<?>, R extends MachineRenderer<T>> extends Model {
  public AbstractModel(Function<ResourceLocation, RenderType> function) {
    super(function);
  }

  public abstract void render(T te, R renderer, PoseStack matrix, MultiBufferSource rtb, int light, int ov);

  @Override
  public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
  }
}
