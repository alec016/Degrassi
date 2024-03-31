package es.degrassi.forge.core.client.renderer.cable;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.forge.core.client.model.cable.CableModel;
import es.degrassi.forge.core.client.renderer.MachineRenderer;
import es.degrassi.forge.core.common.cables.CableEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public abstract class CableRenderer<T extends CableEntity<?, ?>, M extends CableModel<T, R, M>, R extends CableRenderer<T, M, R>> extends MachineRenderer<T> {
  private final M model;
  protected CableRenderer(BlockEntityRendererProvider.Context context, M model) {
    super(context);
    this.model = model;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void render(T te, float pt, PoseStack matrix, MultiBufferSource rtb, Minecraft mc, ClientLevel world, LocalPlayer player, int light, int ov) {
    matrix.pushPose();
    matrix.translate(0.5, 1.5, 0.5);
    matrix.translate(0.0, -0.125, 0.0);
    matrix.scale(1.0f, -1.0f, -1.0f);
    model.render(te, (R) this, matrix, rtb, light, ov);
    matrix.popPose();
  }
}
