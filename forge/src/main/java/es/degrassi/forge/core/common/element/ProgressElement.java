package es.degrassi.forge.core.common.element;

import com.google.gson.JsonObject;
import es.degrassi.forge.api.core.common.ElementDirection;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IElement;
import es.degrassi.common.utils.TextureSizeHelper;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.ElementManager;
import es.degrassi.forge.core.common.component.ProgressComponent;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

@Getter
@Setter
public class ProgressElement extends AbstractWidget implements IElement<ProgressComponent> {
  private ResourceLocation emptyTexture, filledTexture;
  private final ElementManager manager;
  public final String id = "progress";
  private final ElementDirection direction;
  private final boolean jei;

  private IRequirement<ProgressComponent> requirement;

  public ProgressElement(
    ElementManager manager,
    int x,
    int y,
    Component message,
    @NotNull ResourceLocation emptyTexture,
    @NotNull ResourceLocation filledTexture,
    ElementDirection direction,
    boolean jei
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
    this.jei = jei;
  }

  @Override
  public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    ProgressComponent component = (ProgressComponent) manager.getEntity().getComponentManager().getComponent(id).orElse(null);
    if (component == null) return;
    renderTexture(guiGraphics, emptyTexture, getX(), getY(), 0, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
    float filledPercentage = component.getProgressPercentage();
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
  public void renderTooltip(GuiGraphics guiGraphics, int x, int y) {}

  @Override
  public void renderHighlight(@NotNull GuiGraphics guiGraphics, int x, int y) {}

  @Override
  public void markDirty() {
    manager.getEntity().setChanged();
  }

  @Override
  public ElementDirection getDirection() {
    return direction;
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
  public void renderInJei(GuiGraphics guiGraphics, IRequirement<?> requirement, MachineRecipe<?> recipe, double mouseX, double mouseY, IComponent iComponent) {
    if (!jei) return;
    if (!(iComponent instanceof ProgressComponent component)) return;
    renderTexture(guiGraphics, emptyTexture, getX(), getY(), 0, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
    float filledPercentage = component.getProgressPercentage();
    int textureWidth = TextureSizeHelper.getTextureWidth(filledTexture), textureHeight = TextureSizeHelper.getTextureHeight(filledTexture);
    Pair<Integer, Integer> widthHeight = getWidthHeight(textureWidth, textureHeight, filledPercentage);
    Pair<Integer, Integer> xyOffset = getXYOffset(textureWidth, textureHeight, filledPercentage);
    int width = widthHeight.getA(), height = widthHeight.getB();
    int xOffset = xyOffset.getA(), yOffset = xyOffset.getB();
    renderTexture(guiGraphics, filledTexture, getX() + xOffset, getY() + yOffset, xOffset, yOffset, 0, width, height, textureWidth, textureHeight);
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

  @Override
  public ProgressElement copy(ElementManager manager) {
    return new ProgressElement(manager, getX(), getY(), getMessage(), emptyTexture, filledTexture, direction, jei);
  }
}
