package es.degrassi.forge.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.menu.MenuRegistry;
import es.degrassi.client.IClientHandler;
import es.degrassi.comon.init.entity.furnace.FurnaceEntity;
import es.degrassi.comon.init.entity.panel.PanelEntity;
import es.degrassi.comon.init.registration.ElementRegistry;
import es.degrassi.forge.init.gui.screen.furnace.IronFurnaceScreen;
import es.degrassi.forge.init.gui.screen.sp.*;
import es.degrassi.forge.init.registration.ContainerRegistry;
import es.degrassi.forge.integration.jei.RegisterGuiElementJEIRendererEvent;
import es.degrassi.forge.integration.jei.renderer.ProgressGuiElementJeiRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
public class ClientHandler {
  public static void init() {
    LifecycleEvent.SETUP.register(ClientHandler::clientSetup);
  }

  private static void clientSetup() {
    MenuRegistry.registerScreenFactory(ContainerRegistry.SP1_CONTAINER.get(), SP1Screen::new);
    MenuRegistry.registerScreenFactory(ContainerRegistry.SP2_CONTAINER.get(), SP2Screen::new);
    MenuRegistry.registerScreenFactory(ContainerRegistry.SP3_CONTAINER.get(), SP3Screen::new);
    MenuRegistry.registerScreenFactory(ContainerRegistry.SP4_CONTAINER.get(), SP4Screen::new);
    MenuRegistry.registerScreenFactory(ContainerRegistry.SP5_CONTAINER.get(), SP5Screen::new);
    MenuRegistry.registerScreenFactory(ContainerRegistry.SP6_CONTAINER.get(), SP6Screen::new);
    MenuRegistry.registerScreenFactory(ContainerRegistry.SP7_CONTAINER.get(), SP7Screen::new);
    MenuRegistry.registerScreenFactory(ContainerRegistry.SP8_CONTAINER.get(), SP8Screen::new);
    MenuRegistry.registerScreenFactory(ContainerRegistry.IRON_FURNACE_CONTAINER.get(), IronFurnaceScreen::new);
  }

  private static void registerGuiElementJEIRenderers(final RegisterGuiElementJEIRendererEvent event) {
    event.register(ElementRegistry.PROGRESS_GUI_ELEMENT.get(), new ProgressGuiElementJeiRenderer());
  }

  @NotNull
  public static PanelEntity getClientSidePanelEntity(BlockPos pos) {
    if(Minecraft.getInstance().level != null) {
      BlockEntity tile = Minecraft.getInstance().level.getBlockEntity(pos);
      if(tile instanceof PanelEntity panel)
        return panel;
    }
    throw new IllegalStateException("Trying to open a Panel container without clicking on a Panel block");
  }

  @NotNull
  public static FurnaceEntity getClientSideFurnaceEntity(BlockPos pos) {
    if(Minecraft.getInstance().level != null) {
      BlockEntity tile = Minecraft.getInstance().level.getBlockEntity(pos);
      if(tile instanceof FurnaceEntity furnace)
        return furnace;
    }
    throw new IllegalStateException("Trying to open a Furnace container without clicking on a Furnace block");
  }

  public static void drawSizedString(@NotNull Font font, @NotNull PoseStack matrix, String string, int x, int y, int size, float maxScale, int color) {
    float stringSize = font.width(string);
    float scale = Math.min(size / stringSize, maxScale);
    matrix.pushPose();
    matrix.scale(scale, scale, 0);
    font.draw(matrix, string, x / scale, y / scale, color);
    matrix.popPose();
  }

  public static void drawCenteredString(@NotNull Font font, @NotNull PoseStack matrix, String string, int x, int y, int color) {
    int width = font.width(string);
    int height = font.lineHeight;
    matrix.pushPose();
    matrix.translate(-width / 2.0D, -height / 2.0D, 0);
    font.draw(matrix, string, x, y, color);
    matrix.popPose();
  }

  @SuppressWarnings("deprecation")
  public static void renderItemAndEffectsIntoGUI(@NotNull PoseStack matrix, ItemStack stack, int x, int y) {
    matrix.pushPose();
    IClientHandler.bindTexture(TextureAtlas.LOCATION_BLOCKS);
    Minecraft.getInstance().getTextureManager().getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
    RenderSystem.enableBlend();
    RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    matrix.translate((float)x, (float)y, 100.0F + Minecraft.getInstance().getItemRenderer().blitOffset);
    matrix.translate(8.0F, 8.0F, 0.0F);
    matrix.scale(1.0F, -1.0F, 1.0F);
    matrix.scale(16.0F, 16.0F, 16.0F);
    MultiBufferSource.BufferSource irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
    BakedModel model = Minecraft.getInstance().getItemRenderer().getModel(stack, null, null, 0);
    boolean flag = !model.usesBlockLight();
    if (flag) {
      Lighting.setupForFlatItems();
    }
    RenderSystem.disableDepthTest();
    Minecraft.getInstance().getItemRenderer().render(stack, ItemTransforms.TransformType.GUI, false, matrix, irendertypebuffer$impl, 15728880, OverlayTexture.NO_OVERLAY, model);
    irendertypebuffer$impl.endBatch();
    RenderSystem.enableDepthTest();
    if (flag) {
      Lighting.setupFor3DItems();
    }
    RenderSystem.disableBlend();
    matrix.popPose();
  }

  private static void draw(@NotNull BufferBuilder renderer, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
    renderer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
    renderer.vertex(x, y, 0.0D).color(red, green, blue, alpha).endVertex();
    renderer.vertex(x, y + height, 0.0D).color(red, green, blue, alpha).endVertex();
    renderer.vertex(x + width, y + height, 0.0D).color(red, green, blue, alpha).endVertex();
    renderer.vertex(x + width, y, 0.0D).color(red, green, blue, alpha).endVertex();
    Tesselator.getInstance().end();
  }

  public static void drawHoveringText(PoseStack poseStack, List<Component> textLines, int x, int y) {
    Minecraft minecraft = Minecraft.getInstance();
    Font font = minecraft.font;
    drawHoveringText(poseStack, textLines, x, y, font);
  }

  public static void drawHoveringText(PoseStack poseStack, List<Component> textLines, int x, int y, Font font) {
    Minecraft minecraft = Minecraft.getInstance();
    Screen screen = minecraft.screen;
    if (screen == null) {
      return;
    }

    screen.renderTooltip(poseStack, textLines, Optional.empty(), x, y);
  }

  public static void renderSlotHighlight(PoseStack pose, int x, int y, int width, int height) {
    RenderSystem.disableDepthTest();
    RenderSystem.colorMask(true, true, true, false);
    GuiComponent.fill(pose, x, y, x + width, y + height, -2130706433);
    RenderSystem.colorMask(true, true, true, true);
    RenderSystem.enableDepthTest();
  }

  public static void renderButtonHover(PoseStack pose, int x, int y, int width, int height) {
    RenderSystem.disableDepthTest();
    RenderSystem.colorMask(true, true, true, false);
    GuiComponent.fill(pose, x, y, x + width, y + 1, FastColor.ARGB32.color(255, 255, 255, 255));
    GuiComponent.fill(pose, x + width - 1, y, x + width, y + height, FastColor.ARGB32.color(255, 255, 255, 255));
    GuiComponent.fill(pose, x, y + height - 1, x + width, y + height, FastColor.ARGB32.color(255, 255, 255, 255));
    GuiComponent.fill(pose, x, y, x + 1, y + height, FastColor.ARGB32.color(255, 255, 255, 255));
    RenderSystem.colorMask(true, true, true, true);
    RenderSystem.enableDepthTest();
  }

  public static boolean isShiftKeyDown() {
    return Screen.hasShiftDown();
  }
}
