package es.degrassi.forge.core.common.element;

import com.google.gson.JsonObject;
import es.degrassi.common.utils.TextureSizeHelper;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IElement;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.ElementManager;
import es.degrassi.forge.core.common.component.TextureComponent;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class TextureElement extends AbstractWidget implements IElement<TextureComponent> {
  private final ElementManager manager;
  private ResourceLocation texture;
  private boolean jei;

  public TextureElement(ElementManager manager, int x, int y, ResourceLocation texture, Component message, boolean jei) {
    super(x, y, TextureSizeHelper.getTextureWidth(texture), TextureSizeHelper.getTextureHeight(texture), message);
    this.manager = manager;
    this.texture = texture;
    this.jei = jei;
  }

  @Override
  public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    renderTexture(guiGraphics, texture, getX(), getY(), 0, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
  }

  @Override
  protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}

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
    return "texture";
  }

  @Override
  public void serialize(CompoundTag nbt) {}

  @Override
  public CompoundTag serialize() {
    CompoundTag tag = new CompoundTag();
    tag.putString(TEXTURE_KEY, texture.toString());
    return tag;
  }

  @Override
  public void deserialize(CompoundTag nbt) {
    texture = new ResourceLocation(nbt.getString(TEXTURE_KEY));
  }

  @Override
  public void renderInJei(GuiGraphics guiGraphics, IRequirement<?> requirement, MachineRecipe<?> recipe, double mouseX, double mouseY, IComponent component) {
    if (jei) {
      renderTexture(guiGraphics, texture, getX(), getY(), 0, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
    }
  }

  @Override
  protected boolean isValidClickButton(int button) {
    return false;
  }

  @Override
  public void setRequirement(IRequirement<TextureComponent> requirement) {}

  @Override
  public TextureElement copy(ElementManager manager) {
    return new TextureElement(manager, getX(), getY(), texture, getMessage(), isJei());
  }

  @Override
  public JsonObject asJson() {
    JsonObject json = new JsonObject();
    asJson(json);
    json.addProperty("texture", texture.toString());
    return json;
  }
}
