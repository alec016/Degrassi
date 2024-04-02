package es.degrassi.forge.core.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.forge.core.client.model.ChestModel;
import es.degrassi.forge.core.client.model.DegrassiLayerDefinition;
import es.degrassi.forge.core.common.machines.entity.ChestEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ChestEntityRenderer extends MachineRenderer<ChestEntity> {
  private final ChestModel model;

  public ChestEntityRenderer(BlockEntityRendererProvider.Context context) {
    super(context);
    model = new ChestModel(context.bakeLayer(DegrassiLayerDefinition.CHEST));
  }

  @Override
  public void render(ChestEntity blockEntity, float partialTick, PoseStack matrix, MultiBufferSource rtb, Minecraft mc, ClientLevel level, LocalPlayer player, int light, int ov) {
    matrix.pushPose();
    model.render(blockEntity, this, partialTick, matrix, rtb, light, ov);
    matrix.popPose();
  }
}
