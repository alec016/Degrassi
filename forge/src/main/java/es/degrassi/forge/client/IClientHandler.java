package es.degrassi.forge.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.forge.init.entity.BaseEntity;
import es.degrassi.forge.init.gui.container.types.IProgressContainer;
import es.degrassi.forge.util.TextureSizeHelper;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("unused")
public interface IClientHandler {
  static void bindTexture(ResourceLocation texture) {
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderTexture(0, texture);
  }
  static void renderProgressArrow(
    PoseStack pPoseStack,
    int x,
    int y,
    boolean vertical,
    int width,
    int height,
    ResourceLocation EMPTY_ARROW,
    ResourceLocation FILLED_ARROW,
    @NotNull IProgressContainer<? extends BaseEntity> menu
  ) {
    bindTexture(EMPTY_ARROW);
    GuiComponent.blit(pPoseStack, x, y, 0, 0, width, height, width, height);
    renderProgressArrow(
      pPoseStack,
      x,
      y,
      vertical,
      FILLED_ARROW,
      menu
    );
  }

  static void renderProgressArrow(
    @NotNull PoseStack pPoseStack,
    int x,
    int y,
    boolean vertical,
    ResourceLocation FILLED_ARROW,
    @NotNull IProgressContainer<? extends BaseEntity> menu
  ) {
    final int width = TextureSizeHelper.getTextureWidth(FILLED_ARROW);
    final int height = TextureSizeHelper.getTextureHeight(FILLED_ARROW);
    bindTexture(FILLED_ARROW);
    if(menu.isCrafting()) {
      if (menu.getScaledProgress(height) == height || menu.getScaledProgress(width) == width) {
        GuiComponent.blit(
          pPoseStack,
          x,
          y,
          0,
          0,
          width,
          height,
          width,
          height
        );
        return;
      }
      if (vertical) {
        GuiComponent.blit(
          pPoseStack,
          x,
          y + height - menu.getScaledProgress(height),
          0,
          height - menu.getScaledProgress(height),
          width,
          menu.getScaledProgress(height),
          width,
          height
        );
        return;
      }
      GuiComponent.blit(
        pPoseStack,
        x,
        y,
        0,
        0,
        menu.getScaledProgress(width),
        height,
        width,
        height
      );
    }
  }

  static void renderInventory(PoseStack pose, int x, int y, int screenX, int screenY, ResourceLocation BASE_INV, int width, int height) {
    bindTexture(BASE_INV);
    GuiComponent.blit(pose, screenX + x, screenY + y, 0, 0, width, height, width, height);
  }

  static void renderSlot(PoseStack pose, int x, int y, ResourceLocation BASE_SLOT, int size) {
    bindTexture(BASE_SLOT);
    GuiComponent.blit(pose, x, y, 0, 0, size, size, size, size);
  }

  static void renderEnergyStorage(PoseStack pose, int x, int y, ResourceLocation texture) {
    bindTexture(texture);
    int width = TextureSizeHelper.getTextureWidth(texture);
    int height = TextureSizeHelper.getTextureHeight(texture);
    GuiComponent.blit(pose, x, y, 0, 0, width, height, width, height);
  }

  static void renderSlot(PoseStack pose, int x, int y, ResourceLocation BASE_SLOT) {
    bindTexture(BASE_SLOT);
    GuiComponent.blit(pose, x, y, 0, 0, 18, 18, 18, 18);
  }

  static void addPlayerInventory(AbstractContainerMenu menu, Inventory playerInv, int x, int y) {
    AtomicInteger slotIndex = new AtomicInteger(0);
    int i;
    for (i = 0; i < 9; ++i) {
      menu.addSlot(new Slot(playerInv, slotIndex.getAndIncrement(), x + i * 18, y + 58));
    }
    for (i = 0; i < 3; ++i) {
      for (int j = 0; j < 9; ++j) {
        menu.addSlot(new Slot(playerInv, slotIndex.getAndIncrement(), x + j * 18, y + i * 18));
      }
    }
  }

  static void renderEfficiency(PoseStack pose, int x, int y, ResourceLocation texture, double efficiency) {
    final int height = TextureSizeHelper.getTextureHeight(texture);
    final int width = TextureSizeHelper.getTextureWidth(texture);
    int stored = (int) (height * Math.floor(efficiency / 100));
    IClientHandler.bindTexture(texture);
    if (efficiency > 1)
      GuiComponent.blit(
        pose,
        x,
        y,
        0,
        0,
        width,
        height,
        width,
        height
      );
    else
      GuiComponent.blit(
      pose,
      x,
      y + height - stored,
      0,
      height - stored,
      width,
      stored,
      width,
      height
    );
  }
}
