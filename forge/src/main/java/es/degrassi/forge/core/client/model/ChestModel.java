package es.degrassi.forge.core.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.core.client.renderer.ChestEntityRenderer;
import es.degrassi.forge.core.common.machines.block.ChestBlock;
import es.degrassi.forge.core.common.machines.entity.ChestEntity;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;

public class ChestModel extends AbstractModel<ChestEntity, ChestEntityRenderer> {
  private static final String BOTTOM = "bottom";
  private static final String LID = "lid";
  private static final String LOCK = "lock";
  private final ModelPart lid;
  private final ModelPart bottom;
  private final ModelPart lock;

  public ChestModel() {
    this(createDefinition().bakeRoot());
  }

  public ChestModel(ModelPart root) {
    super(RenderType::entityCutout);
    this.bottom = root.getChild(BOTTOM);
    this.lid = root.getChild(LID);
    this.lock = root.getChild(LOCK);
  }

  public static LayerDefinition createDefinition() {
    MeshDefinition meshdefinition = new MeshDefinition();
    PartDefinition partdefinition = meshdefinition.getRoot();
    partdefinition.addOrReplaceChild(BOTTOM, CubeListBuilder.create().texOffs(0, 19).addBox(1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F), PartPose.ZERO);
    partdefinition.addOrReplaceChild(LID, CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F), PartPose.offset(0.0F, 9.0F, 1.0F));
    partdefinition.addOrReplaceChild(LOCK, CubeListBuilder.create().texOffs(0, 0).addBox(7.0F, -2.0F, 14.0F, 2.0F, 4.0F, 1.0F), PartPose.offset(0.0F, 9.0F, 1.0F));
    return LayerDefinition.create(meshdefinition, 64, 64);
  }

  @Override
  public void render(ChestEntity te, ChestEntityRenderer renderer, PoseStack poseStack, MultiBufferSource rtb, int light, int ov) {
    VertexConsumer buffer = rtb.getBuffer(renderType(new DegrassiLocation("textures/block/chest/chest_" + te.getTier().nameL() + ".png")));
    this.lid.render(poseStack, buffer, light, ov);
    this.lock.render(poseStack, buffer, light, ov);
    this.bottom.render(poseStack, buffer, light, ov);
  }


  public void render(ChestEntity blockEntity, ChestEntityRenderer renderer, float partialTick, PoseStack poseStack, MultiBufferSource rtb, int light, int ov) {
    poseStack.pushPose();

    float f = (blockEntity.getBlockState().getValue(ChestBlock.FACING)).toYRot();
    poseStack.translate(0.5F, 0.5F, 0.5F);
    poseStack.mulPose(Axis.YP.rotationDegrees(-f));
    poseStack.translate(-0.5F, -0.5F, -0.5F);

    float lidAngle = blockEntity.getOpenNess(partialTick);
    lidAngle = 1.0F - lidAngle;
    lidAngle = 1.0F - lidAngle * lidAngle * lidAngle;

    this.lid.xRot = -(lidAngle * 1.5707964F);
    this.lock.xRot = this.lid.xRot;

    render(blockEntity, renderer, poseStack, rtb, light, ov);

    poseStack.popPose();
  }
}
