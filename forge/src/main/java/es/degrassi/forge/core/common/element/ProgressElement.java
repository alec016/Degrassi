package es.degrassi.forge.core.common.element;

import es.degrassi.forge.api.core.common.ElementDirection;
import es.degrassi.forge.api.core.common.IElement;
import es.degrassi.forge.api.utils.TextureSizeHelper;
import es.degrassi.forge.core.common.ElementManager;
import es.degrassi.forge.core.common.component.ProgressComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ProgressElement extends AbstractWidget implements IElement<ProgressComponent> {
  private ResourceLocation emptyTexture, filledTexture;
  private final ElementManager manager;
  public static final String id = ProgressComponent.id;
  private final ElementDirection direction;

  public ProgressElement(
    ElementManager manager,
    int x,
    int y,
    Component message,
    @NotNull ResourceLocation emptyTexture,
    @NotNull ResourceLocation filledTexture,
    ElementDirection direction
  ) {
    super(
      x, y,
      TextureSizeHelper.getTextureWidth(emptyTexture),
      TextureSizeHelper.getTextureHeight(emptyTexture),
      message
    );
    this.manager = manager;
    this.emptyTexture = emptyTexture;
    this.filledTexture = filledTexture;
    this.direction = direction;
  }

  @Override
  public ElementManager getManager() {
    return manager;
  }

  @Override
  public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    ProgressComponent component = (ProgressComponent) manager.getEntity().getComponentManager().getComponent(id).orElse(null);
    if (component == null) return;
    renderTexture(guiGraphics, emptyTexture, getX(), getY(), 0, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
    float filledPercentage = component.getProgressPercentage();
    int textureWidth = TextureSizeHelper.getTextureWidth(filledTexture), textureHeight = TextureSizeHelper.getTextureHeight(filledTexture);
    int width = direction.isHorizontal() ? (int) (textureWidth * filledPercentage) : textureWidth;
    int height = direction.isVertical() ? (int) (textureHeight * filledPercentage) : textureHeight;
    renderTexture(guiGraphics, filledTexture, getX(), getY(), 0, 0, 0, width, height, textureWidth, textureHeight);

  }

  @Override
  protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    narrationElementOutput.add(NarratedElementType.TITLE, getMessage());
  }

  @Override
  public void renderTooltip(GuiGraphics guiGraphics, int x, int y) {}

  @Override
  public void renderHighlight(@NotNull GuiGraphics guiGraphics, int x, int y) {}

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
    CompoundTag tag = new CompoundTag();
    tag.putString(EMPTY_TEXTURE_KEY, emptyTexture.toString());
    tag.putString(FILLED_TEXTURE_KEY, filledTexture.toString());
    nbt.put(id, tag);
  }

  @Override
  public void deserialize(CompoundTag nbt) {
    CompoundTag tag = nbt.getCompound(id);
    emptyTexture = new ResourceLocation(tag.getString(EMPTY_TEXTURE_KEY));
    filledTexture = new ResourceLocation(tag.getString(FILLED_TEXTURE_KEY));
  }

  @Override
  public String toString() {
    return "ProgressElement{" +
      "x=" + getX() +
      ", y=" + getY() +
      ", emptyTexture=" + emptyTexture +
      ", filledTexture=" + filledTexture +
      ", id='" + id + '\'' +
      ", direction=" + direction +
      ", width=" + width +
      ", height=" + height +
      '}';
  }
}
