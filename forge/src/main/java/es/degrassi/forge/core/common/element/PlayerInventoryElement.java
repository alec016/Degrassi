package es.degrassi.forge.core.common.element;

import com.google.gson.JsonObject;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IElement;
import es.degrassi.common.utils.TextureSizeHelper;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.ElementManager;
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

@Getter
@Setter
public class PlayerInventoryElement extends AbstractWidget implements IElement<IComponent> {
  private final String id = "player_inventory";
  private final ElementManager manager;
  private ResourceLocation texture;
  private final boolean jei = false;

  private IRequirement<IComponent> requirement;

  public PlayerInventoryElement(ElementManager manager, int x, int y, ResourceLocation texture, Component message) {
    super(x, y, TextureSizeHelper.getTextureWidth(texture), TextureSizeHelper.getTextureHeight(texture), message);
    this.texture = texture;
    this.manager = manager;
  }

  @Override
  public void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    renderTexture(guiGraphics, texture, getX(), getY(), 0, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
  }

  @Override
  protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    narrationElementOutput.add(NarratedElementType.TITLE, getMessage());
  }

  @Override
  public void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
  }

  @Override
  public void renderHighlight(@NotNull GuiGraphics guiGraphics, int x, int y) {
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
  public void renderInJei(GuiGraphics guiGraphics, IRequirement<?> requirement, MachineRecipe<?> recipe, double mouseX, double mouseY, IComponent component) {}

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
  public PlayerInventoryElement copy(ElementManager manager) {
    return new PlayerInventoryElement(manager, getX(), getY(), texture, getMessage());
  }
}
