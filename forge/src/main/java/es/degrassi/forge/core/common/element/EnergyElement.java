package es.degrassi.forge.core.common.element;

import es.degrassi.forge.api.core.common.ElementDirection;
import es.degrassi.forge.api.core.common.IElement;
import es.degrassi.forge.api.utils.TextureSizeHelper;
import es.degrassi.forge.core.common.ElementManager;
import es.degrassi.forge.core.common.component.EnergyComponent;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class EnergyElement extends AbstractWidget implements IElement<EnergyComponent> {
  private ResourceLocation emptyTexture, filledTexture;
  private final ElementManager manager;
  private final String id;
  private final ElementDirection direction;
  public EnergyElement(
    ElementManager manager,
    int x,
    int y,
    Component message,
    @NotNull ResourceLocation emptyTexture,
    @NotNull ResourceLocation filledTexture,
    String id,
    ElementDirection direction
  ) {
    super(x, y, TextureSizeHelper.getTextureWidth(emptyTexture), TextureSizeHelper.getTextureHeight(emptyTexture), message);
    this.emptyTexture = emptyTexture;
    this.filledTexture = filledTexture;
    this.manager = manager;
    this.id = id;
    this.direction = direction;
  }

  @Override
  public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    EnergyComponent component = (EnergyComponent) manager.getEntity().getComponentManager().getComponent(id).orElse(null);
    if (component == null) return;
    renderTexture(guiGraphics, emptyTexture, getX(), getY(), 0, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
    float filledPercentage = (component.getEnergyStored()) * 1F / (component.getMaxEnergyStored() * 1F);
    int textureWidth = TextureSizeHelper.getTextureWidth(filledTexture), textureHeight = TextureSizeHelper.getTextureHeight(filledTexture);
    int width = direction.isHorizontal() ? (int) (textureWidth * filledPercentage) : textureWidth;
    int height = direction.isVertical() ? (int) (textureHeight * filledPercentage) : textureHeight;
    renderTexture(guiGraphics, filledTexture, getX(), getY(), 0, 0, 0, width, height, textureWidth, textureHeight);
  }

  @Override
  public void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
    EnergyComponent component = (EnergyComponent) manager.getEntity().getComponentManager().getComponent(id).orElse(null);;
    if (component == null) return;

    if (this.isMouseOver(x, y)) {
      renderHighlight(guiGraphics, x, y);
      guiGraphics.renderTooltip(
        Minecraft.getInstance().font, List.of(
          Component.literal(
            "Energy: " +
              component.getEnergyStored() + "FE / " +
              component.getMaxEnergyStored() + "FE"
          ).getVisualOrderText()
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
  protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    narrationElementOutput.add(NarratedElementType.TITLE, getMessage());
  }

  @Override
  public ElementManager getManager() {
    return manager;
  }

  @Override
  public void markDirty() {
    manager.getEntity().setChanged();
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
    if (tag.contains(EMPTY_TEXTURE_KEY, Tag.TAG_STRING)) emptyTexture = new ResourceLocation(tag.getString(EMPTY_TEXTURE_KEY));
    filledTexture = new ResourceLocation(tag.getString(FILLED_TEXTURE_KEY));
  }

  public String getId() {
    return id;
  }

  @Override
  public String toString() {
    return "EnergyElement{" +
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
