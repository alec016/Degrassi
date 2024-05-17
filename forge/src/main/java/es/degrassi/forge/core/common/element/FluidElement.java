package es.degrassi.forge.core.common.element;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IElement;
import es.degrassi.common.utils.TextureSizeHelper;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.ElementManager;
import es.degrassi.forge.core.common.component.FluidComponent;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

@Getter
@Setter
public class FluidElement extends AbstractWidget implements IElement<FluidComponent> {
  private static final int MIN_FLUID_HEIGHT = 1;
  private static final int TEXTURE_SIZE = 16;
  private final ElementManager manager;
  private final String id;
  private ResourceLocation texture;
  private final boolean jei;

  private IRequirement<FluidComponent> requirement;

  public FluidElement(ElementManager manager, int x, int y, ResourceLocation texture, Component message, String id, boolean jei) {
    super(x, y, TextureSizeHelper.getTextureWidth(texture), TextureSizeHelper.getTextureHeight(texture), message);
    this.manager = manager;
    this.texture = texture;
    this.id = id;
    this.jei = jei;
  }

  @Override
  public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    renderTexture(guiGraphics, texture, getX(), getY(), 0, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
    renderFluid(guiGraphics.pose(), getX() + 1, getY() + 1);
  }

  @Override
  protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    narrationElementOutput.add(NarratedElementType.TITLE, getMessage());
  }

  @Override
  public void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
    FluidComponent component = (FluidComponent) manager.getEntity().getComponentManager().getComponent(id).orElse(null);
    if (component == null) return;

    if (this.isMouseOver(x, y)) {
      renderHighlight(guiGraphics, x, y);
      StringBuilder builder = new StringBuilder();
      FluidStack fluid = component.getFluid();
      if (fluid.getFluid().isSame(Fluids.EMPTY)) {
        builder.append("Empty");
      } else {
        builder.append(fluid.getDisplayName().getString())
          .append(": ")
          .append(fluid.getAmount())
          .append("mB / ")
          .append(component.getCapacity())
          .append("mB");
      }
      guiGraphics.renderTooltip(
        Minecraft.getInstance().font, List.of(
          Component.literal(builder.toString()).getVisualOrderText()
        ),
        x,
        y
      );
    }
  }

  @Override
  public void renderHighlight(@NotNull GuiGraphics guiGraphics, int x, int y) {
    guiGraphics.fillGradient(RenderType.guiOverlay(), getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, -2130706433, -2130706433, 0);
  }

  @Override
  public void markDirty() {
    manager.getEntity().setChanged();
  }

  @Override
  public void serialize(CompoundTag nbt) {
  }

  @Override
  public CompoundTag serialize() {
    CompoundTag tag = new CompoundTag();
    tag.putString("texture", texture.toString());
    tag.putString("id", id);
    return tag;
  }

  @Override
  public void deserialize(CompoundTag nbt) {
    texture = new ResourceLocation(nbt.getString("texture"));
  }

  @Override
  public void renderInJei(GuiGraphics guiGraphics, IRequirement<?> requirement, MachineRecipe<?> recipe, double mouseX, double mouseY, IComponent iComponent) {
    if (!jei) return;
    if (!(iComponent instanceof FluidComponent component)) return;
    renderTexture(guiGraphics, texture, getX(), getY(), 0, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
    renderFluid(guiGraphics.pose(), getX() + 1, getY() + 1, component);
  }

  public void renderFluid(PoseStack poseStack, int x, int y, FluidComponent component) {
    Fluid fluid = component.getFluid().getFluid();
    if (fluid == null || fluid == Fluids.EMPTY)
      return;

    RenderSystem.enableBlend();

    poseStack.pushPose();
    poseStack.translate(x, y, 0);

    TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(IClientFluidTypeExtensions.of(component.getFluid().getFluid()).getStillTexture(component.getFluid()));
    int fluidColor = IClientFluidTypeExtensions.of(component.getFluid().getFluid()).getTintColor(component.getFluid());
    int height = getHeight() - 2;
    long amount = component.getFluid().getAmount();
    int scaledAmount = (int) ((amount * height) / component.getCapacity());
    if (amount > 0 && scaledAmount < MIN_FLUID_HEIGHT) {
      scaledAmount = MIN_FLUID_HEIGHT;
    }
    if (scaledAmount > height) {
      scaledAmount = height;
    }

    drawTiledSprite(poseStack, getWidth() - 2, height, fluidColor, scaledAmount, sprite);

    poseStack.popPose();

    RenderSystem.disableBlend();
  }

  public void renderFluid(PoseStack poseStack, int x, int y) {
    FluidComponent component = (FluidComponent) manager.getEntity().getComponentManager().getComponent(id).orElse(null);
    if (component == null) return;
    renderFluid(poseStack, x, y, component);
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

  @Override
  public String toString() {
    return asJson().toString();
  }

  public JsonObject asJson() {
    JsonObject json = new JsonObject();
    asJson(json);
    json.addProperty("width", width);
    json.addProperty("height", height);
    json.addProperty("texture", texture.toString());
    return json;
  }

  @Override
  public FluidElement copy(ElementManager manager) {
    return new FluidElement(manager, getX(), getY(), texture, getMessage(), id, jei);
  }
}
