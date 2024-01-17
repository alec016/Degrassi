package es.degrassi.forge.init.gui.element;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import es.degrassi.forge.init.gui.component.*;
import es.degrassi.forge.init.registration.*;
import es.degrassi.forge.util.*;
import java.util.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;

public class FluidGuiElement extends GuiElement {
  private static final int MIN_FLUID_HEIGHT = 1;
  private static final int TEXTURE_SIZE = 16;

  private final FluidComponent component;

  public FluidGuiElement(int x, int y, int width, int height, FluidComponent component) {
    super(x, y, width, height, Component.literal("Fluid Tank"));
    this.component = component;
  }


  @Override
  public void draw(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
    renderFluid(poseStack, getX(), getY());
    renderHover(poseStack, mouseX, mouseY);
  }

  @Override
  public void renderLabels(Screen screen, @NotNull PoseStack poseStack, int mouseX, int mouseY) {
    if(
      isMouseAboveArea(
        mouseX,
        mouseY,
        getX(),
        getY(),
        getWidth(),
        getHeight()
      )
    ) {
      screen.renderTooltip(
        poseStack,
        getTooltips(),
        Optional.empty(),
        mouseX - getX(),
        mouseY - getY()
      );
    }
  }

  @Override
  public void draw(PoseStack transform, ResourceLocation texture) {}

  @Override
  public Tag serializeNBT() {
    CompoundTag nbt = new CompoundTag();
    Tag componentTag = component.serializeNBT();
    nbt.put("component", componentTag);
    nbt.putInt("x", getX());
    nbt.putInt("y", getY());
    nbt.putInt("width", getWidth());
    nbt.putInt("height", getHeight());
    return nbt;
  }

  @Override
  public void deserializeNBT(Tag tag) {
    if(tag == null) throw new IllegalArgumentException("Tag cant be null");
    if (tag instanceof CompoundTag nbt) {
      Tag componentTag = nbt.getCompound("component");
      component.deserializeNBT(componentTag);
      x = nbt.getInt("x");
      y = nbt.getInt("y");
      width = nbt.getInt("width");
      height = nbt.getInt("height");
    }
  }

  @Override
  public GuiElementType<? extends IGuiElement> getType() {
    return ElementRegistry.FLUID_GUI_ELEMENT.get();
  }

  @Override
  public List<Component> getTooltips() {
    return getTooltip(TooltipMode.SHOW_AMOUNT_AND_CAPACITY);
  }

  public enum TooltipMode {
    SHOW_AMOUNT,
    SHOW_AMOUNT_AND_CAPACITY
  }

  public void renderFluid(PoseStack poseStack, int x, int y) {
    Fluid fluid = this.component.getFluid().getFluid();
    if (fluid == null || fluid == Fluids.EMPTY)
      return;

    RenderSystem.enableBlend();

    poseStack.pushPose();
    poseStack.translate(x, y, 0);

    TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(IClientFluidTypeExtensions.of(this.component.getFluid().getFluid()).getStillTexture(this.component.getFluid()));
    int fluidColor = IClientFluidTypeExtensions.of(this.component.getFluid().getFluid()).getTintColor(this.component.getFluid());

    long amount = this.component.getFluid().getAmount();
    int scaledAmount = (int) ((amount * getHeight()) / this.component.getCapacity());
    if (amount > 0 && scaledAmount < MIN_FLUID_HEIGHT) {
      scaledAmount = MIN_FLUID_HEIGHT;
    }
    if (scaledAmount > getHeight()) {
      scaledAmount = getHeight();
    }

    drawTiledSprite(poseStack, getWidth(), getHeight(), fluidColor, scaledAmount, sprite);

    poseStack.popPose();

    RenderSystem.disableBlend();
  }

  private void drawTiledSprite(@NotNull PoseStack poseStack, final int tiledWidth, final int tiledHeight, int color, int scaledAmount, TextureAtlasSprite sprite) {
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

  private void setGLColorFromInt(int color) {
    float red = (color >> 16 & 0xFF) / 255.0F;
    float green = (color >> 8 & 0xFF) / 255.0F;
    float blue = (color & 0xFF) / 255.0F;
    float alpha = ((color >> 24) & 0xFF) / 255F;
    if(alpha == 0)
      alpha = 1;
    RenderSystem.setShaderColor(red, green, blue, alpha);
  }

  private void drawTextureWithMasking(Matrix4f matrix, float xCoord, float yCoord, @NotNull TextureAtlasSprite textureSprite, int maskTop, int maskRight) {
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

  public List<Component> getTooltip(TooltipMode mode) {
    List<Component> tooltip = new ArrayList<>();

    Fluid fluidType = component.getFluid().getFluid();
    try {
      if (fluidType.isSame(Fluids.EMPTY)) {
        tooltip.add(Component.translatable("degrassi.gui.element.fluid.empty", 0, this.component.getCapacity()));
        return tooltip;
      }

      Component displayName = this.component.getFluid().getDisplayName();
      tooltip.add(displayName);

      long amount = component.getFluid().getAmount();
      long milliBuckets = (amount * 1000) / FluidType.BUCKET_VOLUME;

      if (mode == TooltipMode.SHOW_AMOUNT_AND_CAPACITY) {
        MutableComponent amountString = Component.translatable("degrassi.gui.element.fluid.tooltip", Utils.format(milliBuckets), Utils.format(this.component.getCapacity()));
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
