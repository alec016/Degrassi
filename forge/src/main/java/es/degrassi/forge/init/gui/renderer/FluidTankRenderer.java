package es.degrassi.forge.init.gui.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import es.degrassi.forge.util.DegrassiLogger;
import es.degrassi.forge.util.Utils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FluidTankRenderer extends InfoArea {
  private static final int MIN_FLUID_HEIGHT = 1;
  private static final int TEXTURE_SIZE = 16;

  protected FluidTankRenderer(Rect2i area) {
    super(area);
  }

  @Override
  public void draw(PoseStack transform, int x, int y, ResourceLocation texture) {}

  @Override
  public void draw(PoseStack transform, int x, int y, ResourceLocation texture, boolean vertical) {}

  public enum TooltipMode {
    SHOW_AMOUNT,
    SHOW_AMOUNT_AND_CAPACITY
  }

  public static void renderFluid(PoseStack poseStack, int posX, int posY, int width, int height, @NotNull FluidStack fluidStack, long capacity) {
    Fluid fluid = fluidStack.getFluid();
    if (fluid == null || fluid == Fluids.EMPTY)
      return;

    RenderSystem.enableBlend();

    poseStack.pushPose();
    poseStack.translate(posX, posY, 0);

    TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(IClientFluidTypeExtensions.of(fluidStack.getFluid()).getStillTexture(fluidStack));
    int fluidColor = IClientFluidTypeExtensions.of(fluidStack.getFluid()).getTintColor(fluidStack);

    long amount = fluidStack.getAmount();
    int scaledAmount = (int) ((amount * height) / capacity);
    if (amount > 0 && scaledAmount < MIN_FLUID_HEIGHT) {
      scaledAmount = MIN_FLUID_HEIGHT;
    }
    if (scaledAmount > height) {
      scaledAmount = height;
    }

    drawTiledSprite(poseStack, width, height, fluidColor, scaledAmount, sprite);

    poseStack.popPose();

    RenderSystem.disableBlend();
  }

  private static void drawTiledSprite(@NotNull PoseStack poseStack, final int tiledWidth, final int tiledHeight, int color, int scaledAmount, TextureAtlasSprite sprite) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
    Matrix4f matrix = poseStack.last().pose();
    setGLColorFromInt(color);

    final int xTileCount = tiledWidth / TEXTURE_SIZE;
    final int xRemainder = tiledWidth - (xTileCount * TEXTURE_SIZE);
    final int yTileCount = scaledAmount / TEXTURE_SIZE;
    final int yRemainder = scaledAmount - (yTileCount * TEXTURE_SIZE);

    for (int xTile = 0; xTile <= xTileCount; xTile++) {
      for (int yTile = 0; yTile <= yTileCount; yTile++) {
        int width = (xTile == xTileCount) ? xRemainder : TEXTURE_SIZE;
        int height = (yTile == yTileCount) ? yRemainder : TEXTURE_SIZE;
        int x = (xTile * TEXTURE_SIZE);
        int y = tiledHeight - ((yTile + 1) * TEXTURE_SIZE);
        if (width > 0 && height > 0) {
          int maskTop = TEXTURE_SIZE - height;
          int maskRight = TEXTURE_SIZE - width;

          drawTextureWithMasking(matrix, x, y, sprite, maskTop, maskRight);
        }
      }
    }

    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
  }

  private static void setGLColorFromInt(int color) {
    float red = (color >> 16 & 0xFF) / 255.0F;
    float green = (color >> 8 & 0xFF) / 255.0F;
    float blue = (color & 0xFF) / 255.0F;
    float alpha = ((color >> 24) & 0xFF) / 255F;
    if(alpha == 0)
      alpha = 1;
    RenderSystem.setShaderColor(red, green, blue, alpha);
  }

  private static void drawTextureWithMasking(Matrix4f matrix, float xCoord, float yCoord, @NotNull TextureAtlasSprite textureSprite, int maskTop, int maskRight) {
    float uMin = textureSprite.getU0();
    float uMax = textureSprite.getU1();
    float vMin = textureSprite.getV0();
    float vMax = textureSprite.getV1();
    uMax = uMax - (maskRight / 16F * (uMax - uMin));
    vMax = vMax - (maskTop / 16F * (vMax - vMin));

    Tesselator tesselator = Tesselator.getInstance();
    BufferBuilder bufferBuilder = tesselator.getBuilder();
    bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
    bufferBuilder.vertex(matrix, xCoord, yCoord + 16, 100F).uv(uMin, vMax).endVertex();
    bufferBuilder.vertex(matrix, xCoord + 16 - maskRight, yCoord + 16, 100F).uv(uMax, vMax).endVertex();
    bufferBuilder.vertex(matrix, xCoord + 16 - maskRight, yCoord + maskTop, 100F).uv(uMax, vMin).endVertex();
    bufferBuilder.vertex(matrix, xCoord, yCoord + maskTop, 100F).uv(uMin, vMin).endVertex();
    tesselator.end();
  }

  public static List<Component> getTooltip(@NotNull FluidStack fluidStack, TooltipMode mode, int capacity) {
    List<Component> tooltip = new ArrayList<>();

    Fluid fluidType = fluidStack.getFluid();
    try {
      if (fluidType.isSame(Fluids.EMPTY)) {
        return tooltip;
      }

      Component displayName = fluidStack.getDisplayName();
      tooltip.add(displayName);

      long amount = fluidStack.getAmount();
      long milliBuckets = (amount * 1000) / FluidType.BUCKET_VOLUME;

      if (mode == TooltipMode.SHOW_AMOUNT_AND_CAPACITY) {
        MutableComponent amountString = Component.translatable("degrassi.gui.element.fluid.tooltip", Utils.format(milliBuckets), Utils.format(capacity));
        tooltip.add(amountString.withStyle(ChatFormatting.GRAY));
      } else if (mode == TooltipMode.SHOW_AMOUNT) {
        MutableComponent amountString = Component.translatable("degrassi.gui.element.fluid.tooltip.amount", Utils.format(milliBuckets));
        tooltip.add(amountString.withStyle(ChatFormatting.GRAY));
      }
    } catch (RuntimeException e) {
      DegrassiLogger.INSTANCE.error("Failed to get tooltip for fluid: " + e);
    }

    return tooltip;
  }
}
