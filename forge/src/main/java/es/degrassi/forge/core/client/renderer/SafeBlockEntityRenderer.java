package es.degrassi.forge.core.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.forge.core.common.machines.entity.MachineEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.Blocks;

public abstract class SafeBlockEntityRenderer<T extends MachineEntity<?>> extends MachineRenderer<T> {
  public SafeBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    super(context);
  }

  @Override
  public void render(T te , float pt , PoseStack matrix , MultiBufferSource rtb , Minecraft mc , ClientLevel world , LocalPlayer player , int light , int ov) {
    if (isInvalid(te)) return;
    renderSafe(te, pt, matrix, rtb, light, ov);
  }

  protected abstract void renderSafe(T be, float partialTicks, PoseStack ms, MultiBufferSource bufferSource, int light, int overlay);

  public boolean isInvalid(T be) {
    return !be.hasLevel() || be.getBlockState().getBlock() == Blocks.AIR;
  }
}
