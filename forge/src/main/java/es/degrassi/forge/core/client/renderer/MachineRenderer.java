package es.degrassi.forge.core.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public abstract class MachineRenderer<T extends MachineEntity<?>> implements BlockEntityRenderer<T> {
  protected final BlockEntityRendererProvider.Context context;
  protected MachineRenderer(BlockEntityRendererProvider.Context context) {
    this.context = context;
  }

  @Override
  public final void render(T te, float pt, PoseStack matrix, MultiBufferSource rtb, int light, int ov) {
    Minecraft mc = Minecraft.getInstance();
    LocalPlayer player = mc.player;
    if (player != null) {
      render(te, pt, matrix, rtb, mc, player.clientLevel, player, light, ov);
    }
  }

  public abstract void render(T te, float pt, PoseStack matrix, MultiBufferSource rtb, Minecraft mc, ClientLevel world, LocalPlayer player, int light, int ov);
}
