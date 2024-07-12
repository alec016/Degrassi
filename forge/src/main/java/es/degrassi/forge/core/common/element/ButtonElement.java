package es.degrassi.forge.core.common.element;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
import es.degrassi.common.utils.TextureSizeHelper;
import es.degrassi.forge.api.core.common.IComponent;
import es.degrassi.forge.api.core.common.IElement;
import es.degrassi.forge.api.core.common.IRequirement;
import es.degrassi.forge.core.common.ElementManager;
import es.degrassi.forge.core.common.component.ButtonComponent;
import es.degrassi.forge.core.common.recipe.MachineRecipe;
import es.degrassi.forge.lib.client.screen.Texture;
import java.util.List;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
public class ButtonElement extends AbstractButton implements IElement<ButtonComponent> {
  protected final ElementManager manager;
  protected ResourceLocation emptyTexture, hoveredTexture;
  protected Item item;
  protected final String id;
  @Nullable
  private SoundEvent sound;
  private final OnPress onPress;
  private final CreateNarration createNarration;
  protected static final CreateNarration DEFAULT_NARRATION = Supplier::get;

  public ButtonElement(
    ElementManager manager,
    int x,
    int y,
    ResourceLocation emptyTexture,
    ResourceLocation hoveredTexture,
    Component message,
    String id,
    OnPress onPress,
    CreateNarration createNarration
  ) {
    super(x, y, TextureSizeHelper.getTextureWidth(emptyTexture), TextureSizeHelper.getTextureHeight(emptyTexture), message);
    this.manager = manager;
    this.id = id;
    this.onPress = onPress;
    this.createNarration = createNarration;
    this.emptyTexture = emptyTexture;
    this.hoveredTexture = hoveredTexture;
    setClickSound();
  }

  @Override
  public void onPress() {
    onPress.onPress(this);
  }

  @Override
  public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();
    RenderSystem.enableDepthTest();
    renderTexture(guiGraphics, emptyTexture, getX(), getY(), 0, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
    if (isMouseOver(mouseX, mouseY)) {
      renderTexture(guiGraphics, hoveredTexture, getX(), getY(), 0, 0, 0, getWidth(), getHeight(), getWidth(), getHeight());
      renderTooltip(guiGraphics, mouseX, mouseY);
    }
    if (item != null) {
      ItemStack stack = new ItemStack(item, 1);
      guiGraphics.renderItem(stack, getX() + getWidth() / 2 - 8, getY() + getHeight() / 2 - 8);
    }
    RenderSystem.disableBlend();
    RenderSystem.disableDepthTest();
  }

  @Override
  public void renderTooltip(GuiGraphics guiGraphics, int x, int y) {
    if (isMouseOver(x, y)) {
      guiGraphics.renderTooltip(
        Minecraft.getInstance().font,
        List.of(getMessage().getVisualOrderText()),
        x,
        y
      );
    }
  }

  @Override
  public void renderHighlight(@NotNull GuiGraphics guiGraphics, int x, int y) {
  }

  @Override
  public void markDirty() {
    manager.getEntity().setChanged();
  }

  @Override
  public void serialize(CompoundTag nbt) {}

  @Override
  public CompoundTag serialize() {
    CompoundTag tag = new CompoundTag();
    tag.putString(EMPTY_TEXTURE_KEY, emptyTexture.toString());
    tag.putString(HOVERED_TEXTURE_KEY, hoveredTexture.toString());
    tag.putString("id", id);
    return tag;
  }

  @Override
  public void deserialize(CompoundTag nbt) {
    emptyTexture = new ResourceLocation(nbt.getString(EMPTY_TEXTURE_KEY));
    hoveredTexture = new ResourceLocation(nbt.getString(HOVERED_TEXTURE_KEY));
  }

  public ButtonElement setTooltip(Component component) {
    super.setTooltip(Tooltip.create(component));
    return this;
  }

  @Override
  public void playDownSound(SoundManager handler) {
    if (this.sound != null) {
      handler.play(SimpleSoundInstance.forUI(this.sound, 1.0F));
    }
  }

  @Override
  protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    this.defaultButtonNarrationText(narrationElementOutput);
  }

  public ButtonElement setClickSound() {
    this.sound = SoundEvents.UI_BUTTON_CLICK.value();
    return this;
  }

  public ButtonElement setSound(@Nullable SoundEvent sound) {
    this.sound = sound;
    return this;
  }

  @Override
  public void renderInJei(GuiGraphics guiGraphics, IRequirement<?> requirement, MachineRecipe<?> recipe, double mouseX, double mouseY, IComponent component) {}

  @Override
  public boolean isJei() {
    return false;
  }

  @Override
  public void setRequirement(IRequirement<ButtonComponent> requirement) {}

  @Override
  public IElement<ButtonComponent> copy(ElementManager manager) {
    return new ButtonElement(manager, getX(), getY(), emptyTexture, hoveredTexture, getMessage(), id, onPress, DEFAULT_NARRATION);
  }

  @Override
  public JsonObject asJson() {
    JsonObject json = new JsonObject();
    asJson(json);
    json.addProperty("width", width);
    json.addProperty("height", height);
    json.addProperty("emptyTexture", emptyTexture.toString());
    json.addProperty("hoveredTexture", hoveredTexture.toString());
    return json;
  }

  @Override
  public String toString() {
    return asJson().toString();
  }

  public static final ButtonElement EMPTY = new ButtonElement(null, 0, 0, Texture.EMPTY.getLocation(), Texture.EMPTY.getLocation(), Component.empty(), "empty", b -> {}, DEFAULT_NARRATION);

  public ButtonElement withItem(Item item) {
    this.item = item;
    return this;
  }

  @Override
  protected boolean isValidClickButton(int button) {
    return true;
  }

  @OnlyIn(Dist.CLIENT)
  public interface OnPress {
    void onPress(AbstractButton button);
  }

  @OnlyIn(Dist.CLIENT)
  public interface CreateNarration {
    MutableComponent createNarrationMessage(Supplier<MutableComponent> supplier);
  }
}
