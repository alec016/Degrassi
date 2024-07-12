package es.degrassi.forge.core.common.element;

import com.google.gson.JsonObject;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IElement;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.ElementManager;
import es.degrassi.forge.core.common.component.TextComponent;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import javax.swing.text.Element;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class TextElement extends AbstractWidget implements IElement<TextComponent> {
  private final String text;
  private final ElementManager manager;
  private final boolean jei, dropShadow;
  private final Font font;
  private final int color;

  public TextElement(ElementManager manager, int x, int y, Font font, String text, Component message, boolean jei, int color, boolean dropShadow) {
    super(x, y, font.width(text), font.lineHeight, message);
    this.text = text;
    this.manager = manager;
    this.jei = jei;
    this.font = font;
    this.color = color;
    this.dropShadow = dropShadow;
  }

  @Override
  public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    guiGraphics.pose().pushPose();
    guiGraphics.drawString(font, text, getX(), getY(), color, dropShadow);
    guiGraphics.pose().popPose();
  }

  @Override
  protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    narrationElementOutput.add(NarratedElementType.TITLE, text);
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
    return "text";
  }

  @Override
  public void serialize(CompoundTag nbt) {}

  @Override
  public void deserialize(CompoundTag nbt) {}

  @Override
  public void renderInJei(GuiGraphics guiGraphics, IRequirement<?> requirement, MachineRecipe<?> recipe, double mouseX, double mouseY, IComponent component) {}

  @Override
  public void setRequirement(IRequirement<TextComponent> requirement) {}

  @Override
  public TextElement copy(ElementManager manager) {
    return new TextElement(manager, getX(), getY(), font, text, getMessage(), jei, color, dropShadow);
  }

  @Override
  public JsonObject asJson() {
    JsonObject json = new JsonObject();
    asJson(json);
    json.addProperty("text", text);
    json.addProperty("color", color);
    json.addProperty("dropShadow", dropShadow);
    return json;
  }

  @Override
  public String toString() {
    return asJson().toString();
  }
}
