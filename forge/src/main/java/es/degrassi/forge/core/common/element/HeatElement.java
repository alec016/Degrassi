package es.degrassi.forge.core.common.element;

import com.google.gson.JsonObject;
import es.degrassi.common.utils.TextureSizeHelper;
import es.degrassi.forge.api.core.common.ElementDirection;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IElement;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.ElementManager;
import es.degrassi.forge.core.common.component.HeatComponent;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
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
@Setter
public class HeatElement extends AbstractWidget implements IElement<HeatComponent> {
  protected ResourceLocation emptyTexture, filledTexture;
  protected final ElementManager manager;
  protected final String id;
  protected final ElementDirection direction;
  protected final boolean jei;

  private IRequirement<HeatComponent> requirement;

  public HeatElement(ElementManager manager, int x, int y, ResourceLocation emptyTexture, ResourceLocation filledTexture, Component message, String id, ElementDirection direction, boolean jei) {
    super(x, y, TextureSizeHelper.getTextureWidth(emptyTexture), TextureSizeHelper.getTextureHeight(filledTexture), message);
    this.manager = manager;
    this.id = id;
    this.direction = direction;
    this.emptyTexture = emptyTexture;
    this.filledTexture = filledTexture;
    this.jei = jei;
  }

  @Override
  public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    HeatComponent component = (HeatComponent) manager.getEntity().getComponentManager().getComponent(id).orElse(null);
    if (component == null) return;
    renderTexture(guiGraphics, emptyTexture, getX(), getY(), 0, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
    float filledPercentage = (float) component.getFilledPercentage();
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
    HeatComponent component = (HeatComponent) manager.getEntity().getComponentManager().getComponent(id).orElse(null);
    if (component == null) return;

    if (this.isMouseOver(x, y)) {
      renderHighlight(guiGraphics, x, y);
      guiGraphics.renderTooltip(
        Minecraft.getInstance().font,
        List.of(
          getMessage().getVisualOrderText(),
          Component.literal(
            component.getHeat() +
              " / " +
              component.getHeatCapacity()
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
  public void deserialize(CompoundTag nbt) {
    emptyTexture = new ResourceLocation(nbt.getString(EMPTY_TEXTURE_KEY));
    filledTexture = new ResourceLocation(nbt.getString(FILLED_TEXTURE_KEY));
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
  public void renderInJei(GuiGraphics guiGraphics, IRequirement<?> requirement, MachineRecipe<?> recipe, double mouseX, double mouseY, IComponent iComponent) {
    if (!jei) return;
    if (!(iComponent instanceof HeatComponent component)) return;
    renderTexture(guiGraphics, emptyTexture, getX(), getY(), 0, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
    float filledPercentage = (float) component.getFilledPercentage();
    int textureWidth = TextureSizeHelper.getTextureWidth(filledTexture), textureHeight = TextureSizeHelper.getTextureHeight(filledTexture);
    Pair<Integer, Integer> widthHeight = getWidthHeight(textureWidth, textureHeight, filledPercentage);
    Pair<Integer, Integer> xyOffset = getXYOffset(textureWidth, textureHeight, filledPercentage);
    int width = widthHeight.getA(), height = widthHeight.getB();
    int xOffset = xyOffset.getA(), yOffset = xyOffset.getB();
    renderTexture(guiGraphics, filledTexture, getX() + xOffset, getY() + yOffset, xOffset, yOffset, 0, width, height, textureWidth, textureHeight);
  }

  @Override
  public HeatElement copy(ElementManager manager) {
    return new HeatElement(manager, getX(), getY(), emptyTexture, filledTexture, getMessage(), id, direction, jei);
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
    json.addProperty("emptyTexture", emptyTexture.toString());
    json.addProperty("filledTexture", filledTexture.toString());
    return json;
  }
}
