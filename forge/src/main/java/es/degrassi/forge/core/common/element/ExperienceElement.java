package es.degrassi.forge.core.common.element;

import es.degrassi.common.utils.DegrassiLogger;
import es.degrassi.forge.api.core.common.ElementDirection;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IElement;
import es.degrassi.common.utils.TextureSizeHelper;
import es.degrassi.forge.core.common.ElementManager;
import es.degrassi.forge.core.common.component.ExperienceComponent;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import java.util.List;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

@Getter
public class ExperienceElement extends AbstractWidget implements IElement<ExperienceComponent> {
  private final ElementManager manager;
  private ResourceLocation emptyTexture, filledTexture;
  private final String id;
  private final ElementDirection direction;
  private final boolean jei;

  public ExperienceElement(
    ElementManager manager,
    int x,
    int y,
    String id,
    Component message,
    ResourceLocation emptyTexture,
    ResourceLocation filledTexture,
    ElementDirection direction,
    boolean jei
  ) {
    super(x, y, TextureSizeHelper.getTextureWidth(emptyTexture), TextureSizeHelper.getTextureHeight(emptyTexture), message);
    this.manager = manager;
    this.emptyTexture = emptyTexture;
    this.filledTexture = filledTexture;
    this.id = id;
    this.direction = direction;
    this.jei = jei;
  }

  @Override
  public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    ExperienceComponent component = (ExperienceComponent) manager.getEntity().getComponentManager().getComponent(id).orElse(null);
    if (component == null) return;
    renderTexture(guiGraphics, emptyTexture, getX(), getY(), 0, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
    float filledPercentage = component.getExperienceStored() / component.getCapacity();
    int textureWidth = TextureSizeHelper.getTextureWidth(filledTexture), textureHeight = TextureSizeHelper.getTextureHeight(filledTexture);
    Pair<Integer, Integer> widthHeight = getWidthHeight(textureWidth, textureHeight, filledPercentage);
    Pair<Integer, Integer> xyOffset = getXYOffset(textureWidth, textureHeight, filledPercentage);
    int width = widthHeight.getA(), height = widthHeight.getB();
    int xOffset = xyOffset.getA(), yOffset = xyOffset.getB();
    renderTexture(guiGraphics, filledTexture, getX() + xOffset, getY() + yOffset, xOffset, yOffset, 0, width, height, textureWidth, textureHeight);
  }

  @Override
  protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    narrationElementOutput.add(NarratedElementType.TITLE, getMessage());
  }

  @Override
  public void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
    ExperienceComponent component = (ExperienceComponent) manager.getEntity().getComponentManager().getComponent(id).orElse(null);
    if (component == null) return;

    if (this.isMouseOver(x, y)) {
      renderHighlight(guiGraphics, x, y);
      guiGraphics.renderTooltip(
        Minecraft.getInstance().font, List.of(
          Component.literal(
            "Experience: " +
              component.getExperienceStored() + "XP / " +
              component.getCapacity() + "XP"
          ).getVisualOrderText()
        ),
        x,
        y
      );
    }
  }

  @Override
  public void renderHighlight(@NotNull GuiGraphics guiGraphics, int x, int y) {
    guiGraphics.fillGradient(RenderType.gui(), getX() + 1, getY() + 1, getX() + getWidth() - 1, getY() + getHeight() - 1, -2130706433, -2130706433, 0);
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
    tag.putString(EMPTY_TEXTURE_KEY, emptyTexture.toString());
    tag.putString(FILLED_TEXTURE_KEY, filledTexture.toString());
    tag.putString("id", id);
    return tag;
  }

  @Override
  public void deserialize(CompoundTag nbt) {
    emptyTexture = new ResourceLocation(nbt.getString(EMPTY_TEXTURE_KEY));
    filledTexture = new ResourceLocation(nbt.getString(FILLED_TEXTURE_KEY));
  }

  @Override
  public void renderInJei(GuiGraphics guiGraphics, MachineRecipe<?> recipe, double mouseX, double mouseY, IComponent iComponent) {
    if (!jei) return;
    if (!(iComponent instanceof ExperienceComponent component)) return;
    renderTexture(guiGraphics, emptyTexture, getX(), getY(), 0, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
    float filledPercentage = component.getExperienceStored() / component.getCapacity();
    int textureWidth = TextureSizeHelper.getTextureWidth(filledTexture), textureHeight = TextureSizeHelper.getTextureHeight(filledTexture);
    Pair<Integer, Integer> widthHeight = getWidthHeight(textureWidth, textureHeight, filledPercentage);
    Pair<Integer, Integer> xyOffset = getXYOffset(textureWidth, textureHeight, filledPercentage);
    int width = widthHeight.getA(), height = widthHeight.getB();
    int xOffset = xyOffset.getA(), yOffset = xyOffset.getB();
    renderTexture(guiGraphics, filledTexture, getX() + xOffset, getY() + yOffset, xOffset, yOffset, 0, width, height, textureWidth, textureHeight);
  }

  public boolean jei() {
    return jei;
  }
}
