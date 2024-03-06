package es.degrassi.forge.core.common.element;

import es.degrassi.forge.api.core.common.IElement;
import es.degrassi.forge.api.utils.TextureSizeHelper;
import es.degrassi.forge.core.common.ElementManager;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class PlayerInventoryElement extends AbstractWidget implements IElement {
  private static final String id = "player_inventory";
  private final ElementManager manager;
  private final ResourceLocation texture;
  private int currentXIndex;
  private int currentYIndex;
  public PlayerInventoryElement(ElementManager manager, int x, int y, ResourceLocation texture, Component message) {
    super(x, y, TextureSizeHelper.getTextureWidth(texture), TextureSizeHelper.getTextureHeight(texture), message);
    this.texture = texture;
    this.manager = manager;
  }

  @Override
  public ElementManager getManager() {
    return manager;
  }

  @Override
  public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    renderTexture(guiGraphics, texture, getX(), getY(), 0, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
//    int index = 0;
//    for (int i = 0; i < 4; i++) {
//      for (int j = 0; j < 10; j++) {
//        guiGraphics.renderItem(component.getStackInSlot(index), getX() + 18*j + 1, getY() + 18*i + 1);
//        index++;
//      }
//    }
  }

  @Override
  protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    narrationElementOutput.add(NarratedElementType.TITLE, getMessage());
  }

  @Override
  public void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
//    PlayerInventoryComponent component = (PlayerInventoryComponent) manager.getEntity().getComponentManager().getComponent(id).orElse(null);;
//    if (component == null) return;
//    if (isMouseOver(x, y)) {
//      currentYIndex = getCurrentYIndex(y - getY());
//      currentXIndex = getCurrentXIndex(x - getX());
//      if (currentXIndex == -1 || currentYIndex == -1) return;
//      renderHighlight(guiGraphics);
//      if (component.getStackInSlot(currentXIndex + currentYIndex).isEmpty() || component.getStackInSlot(currentXIndex + currentYIndex).is(Items.AIR)) return;
//      guiGraphics.renderTooltip(
//        Minecraft.getInstance().font, List.of(
//          component.getStackInSlot(currentXIndex + currentYIndex).getHoverName().getVisualOrderText()
//        ),
//        x,
//        y
//      );
//    }
  }

  private int getCurrentYIndex(int y) {
    if (y > 0 && y <= 18) return 0;
    if (y > 18 && y <= 36) return 1;
    if (y > 36 && y <= 54) return 2;
    if (y > 58 && y <= 76) return 3;
    return -1;
  }

  private int getCurrentXIndex(int x) {
    for (int i = 0; i < 10; i++) {
      if (x > i*18 && x <= (i+1)*18) return i;
    }
    return -1;
  }

  @Override
  public void renderHighlight(@NotNull GuiGraphics guiGraphics) {
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 10; j++) {
        if (currentXIndex == j && currentYIndex == i) {
          guiGraphics.fillGradient(
            RenderType.guiOverlay(),
            getX() + 18*j + 1,
            getY() + (i == 3 ? 4 : 0) + 18*i + 1,
            getX() + 18*(j + 1) - 1,
            getY() + (i == 3 ? 4 : 0) + 18*(i + 1) - 1,
            -2130706433,
            -2130706433, 0
          );
        }
      }
    }

  }

  @Override
  public void markDirty() {
    manager.getEntity().setChanged();
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void serialize(CompoundTag nbt) {
  }

  @Override
  public void deserialize(CompoundTag nbt) {
  }
}
