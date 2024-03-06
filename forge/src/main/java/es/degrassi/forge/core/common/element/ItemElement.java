package es.degrassi.forge.core.common.element;

import es.degrassi.forge.api.core.common.IElement;
import es.degrassi.forge.api.utils.TextureSizeHelper;
import es.degrassi.forge.core.common.ElementManager;
import es.degrassi.forge.core.common.component.ItemComponent;
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

public class ItemElement extends AbstractWidget implements IElement<ItemComponent> {
  private final ElementManager manager;
  private final String id;
  private final ResourceLocation texture;

  public ItemElement(
    ElementManager manager,
    int x,
    int y,
    Component message,
    String id,
    ResourceLocation texture
  ){
    super(x, y, TextureSizeHelper.getTextureWidth(texture), TextureSizeHelper.getTextureHeight(texture), message);
    this.manager = manager;
    this.id = id;
    this.texture = texture;
  }

  @Override
  public ElementManager getManager() {
    return manager;
  }

  @Override
  public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    ItemComponent component = (ItemComponent) manager.getEntity().getComponentManager().getComponent(id).orElse(null);
    if (component == null) return;
    renderTexture(guiGraphics, texture, getX(), getY(), 0, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
    guiGraphics.renderItem(component.getStackInSlot(0), getX(), getY());
  }

  @Override
  protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    narrationElementOutput.add(NarratedElementType.TITLE, getMessage());
  }

  @Override
  public void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
    ItemComponent component = (ItemComponent) manager.getEntity().getComponentManager().getComponent(id).orElse(null);;
    if (component == null) return;
    if (isMouseOver(x, y)) {
      renderHighlight(guiGraphics);
      if (component.getStackInSlot(0).isEmpty() || component.getStackInSlot(0).is(Items.AIR)) return;
      guiGraphics.renderTooltip(
        Minecraft.getInstance().font, List.of(
          component.getStackInSlot(0).getHoverName().getVisualOrderText()
        ),
        x,
        y
      );
    }
  }

  @Override
  public void renderHighlight(@NotNull GuiGraphics guiGraphics) {
    guiGraphics.fillGradient(RenderType.guiOverlay(), getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getWidth() - 1, -2130706433, -2130706433, 0);
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
