package es.degrassi.forge.init.gui.element;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.forge.client.IClientHandler;
import es.degrassi.forge.init.recipe.IDegrassiRecipe;
import es.degrassi.forge.init.registration.ElementRegistry;
import es.degrassi.forge.integration.jei.ingredients.DegrassiTypes;
import es.degrassi.forge.util.TextureSizeHelper;
import es.degrassi.forge.init.gui.component.ProgressComponent;
import java.util.*;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IClickableIngredient;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.*;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class ProgressGuiElement extends GuiElement implements IDrawableAnimated, IGuiElement, IClickableIngredient<ProgressGuiElement> {
  private ProgressComponent component;
  public ResourceLocation texture;
  private Orientation orientation = Orientation.RIGHT;
  private boolean inverted = false, vertical = false;

  public ProgressGuiElement(CompoundTag tag){
    processTag(tag);
  }
  public ProgressGuiElement(int xMin, int yMin, ProgressComponent progress, ResourceLocation texture, boolean inverted, boolean vertical) {
    super(xMin, yMin, TextureSizeHelper.getTextureWidth(texture), TextureSizeHelper.getTextureHeight(texture), Component.literal("Progress"));
    this.component = progress;
    this.texture = texture;
    this.inverted = inverted;
    this.vertical = vertical;
  }

  public ProgressGuiElement inverted() {
    inverted = true;
    return this;
  }

  public ProgressGuiElement vertical() {
    vertical = true;
    return this;
  }

  public boolean isVertical() {
    return vertical;
  }

  public boolean isInverted() {
    return inverted;
  }

  @Override
  public void draw(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
    if (component.getProgress() > 0) {
      draw(poseStack, texture);
    }
  }

  @Override
  public void renderLabels(Screen screen, @NotNull PoseStack poseStack, int mouseX, int mouseY) {
    if(
      isMouseAboveArea(
        mouseX,
        mouseY,
        getX(),
        getY(),
        getWidth(),
        getHeight()
      )
    ) {
      screen.renderTooltip(
        poseStack,
        getTooltips(),
        Optional.empty(),
        mouseX - getX(),
        mouseY - getY()
      );
    }
  }


  @Override
  public void draw(PoseStack transform, ResourceLocation texture) {
    this.texture = texture;
    draw(transform, texture, vertical, inverted);
  }

  public List<Component> getTooltips() {
    return List.of();
  }

  public List<Component> getTooltips(@NotNull IDegrassiRecipe recipe) {
    List<Component> tooltips = new ArrayList<>();
    if(recipe.getTime() > 0)
      tooltips.add(Component.translatable("degrassi.jei.recipe.time", recipe.getTime()));
    else
      tooltips.add(Component.translatable("degrassi.jei.recipe.instant"));
    if(Minecraft.getInstance().options.advancedItemTooltips)
      tooltips.add(Component.translatable("degrassi.jei.recipe.id", recipe.getId().toString()).withStyle(ChatFormatting.DARK_GRAY));
    return tooltips;
  }

  public void draw(PoseStack pose, ResourceLocation texture, boolean vertical, boolean inverted) {
    this.texture = texture;
    this.inverted = inverted;
    this.vertical = vertical;
    IClientHandler.bindTexture(texture);
    int current;
    if (getScaledProgress(getHeight(), inverted) == getHeight() || getScaledProgress(getWidth(), inverted) == getWidth()) {
      blit(
        pose,
        getX(),
        getY(),
        0,
        0,
        getWidth(),
        getHeight(),
        getWidth(),
        getHeight()
      );
      return;
    }
    if (vertical) {
      current = getScaledProgress(getHeight(), inverted);
      blit(
        pose,
        getX(),
        getY() + getHeight() - current,
        0,
        getHeight() - current,
        getWidth(),
        current,
        getWidth(),
        getHeight()
      );
      return;
    }
    current = getScaledProgress(getWidth(), inverted);
    blit(
      pose,
      getX(),
      getY(),
      0,
      0,
      current,
      getHeight(),
      getWidth(),
      getHeight()
    );
  }

  public int getScaledProgress(int renderSize, boolean inverted) {
    this.inverted = inverted;
    int progress = this.component.getProgress();
    int maxProgress = this.component.getMaxProgress();

    return progress != 0 ? (int) Math.floor(Math.abs((inverted ? 1 : 0) - (progress / (float) maxProgress)) * renderSize) : 0;
  }

  @Override
  public GuiElementType<? extends IGuiElement> getType() {
    return ElementRegistry.PROGRESS_GUI_ELEMENT.get();
  }

  @Override
  public void draw(@NotNull PoseStack poseStack, int xOffset, int yOffset) {
    poseStack.pushPose();
    {
      draw(poseStack, texture, vertical, inverted);
    }
    poseStack.popPose();
  }

  public Orientation getDirection() {
    return this.orientation;
  }

  public ProgressGuiElement setDirection(Orientation direction) {
    this.orientation = direction;
    return this;
  }

  public ResourceLocation getFilledTexture() {
    return texture;
  }

  public ProgressComponent getStorage() {
    return component;
  }

  @Override
  public @NotNull ITypedIngredient<ProgressGuiElement> getTypedIngredient() {
    return new ITypedIngredient<>() {
      @Override
      public @NotNull IIngredientType<ProgressGuiElement> getType() {
        return DegrassiTypes.PROGRESS;
      }

      @Override
      public @NotNull ProgressGuiElement getIngredient() {
        return ProgressGuiElement.this;
      }
    };
  }

  @Override
  public @NotNull Rect2i getArea() {
    return new Rect2i(getX(), getY(), getWidth(), getHeight());
  }

  @Override
  public void renderButton(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
    draw(poseStack, texture, vertical, inverted);
  }

  @Override
  protected boolean clicked(double mouseX, double mouseY) {
    return super.clicked(mouseX, mouseY);
  }

  public Tag serializeNBT() {
    CompoundTag nbt = new CompoundTag();
    nbt.put("progress", component.serializeNBT());
    if (texture != null)
      nbt.putString("texture", texture.getNamespace() + ":" + texture.getPath());
    nbt.putInt("x", getX());
    nbt.putInt("y", getY());
    nbt.putInt("width", getWidth());
    nbt.putInt("height", getHeight());
    nbt.putString("orientation", orientation.name());
    nbt.putBoolean("inverted", inverted);
    nbt.putBoolean("vertical", vertical);
    return nbt;
  }

  public void deserializeNBT(Tag tag) {
    if (tag == null) throw new IllegalArgumentException("Tag cant be null");
    if (tag instanceof CompoundTag nbt) {
      component.deserializeNBT(nbt.getCompound("progress"));
      if (nbt.contains("texture"))
        texture = new ResourceLocation(nbt.getString("texture"));
      x = nbt.getInt("x");
      y = nbt.getInt("y");
      width = nbt.getInt("width");
      height = nbt.getInt("height");
      orientation = Orientation.valueOf(nbt.getString("orientation"));
      inverted = nbt.getBoolean("inverted");
      vertical = nbt.getBoolean("vertical");
    }
  }

  protected void processTag(CompoundTag nbt) {
    component.deserializeNBT(nbt.getCompound("progress"));
    if (nbt.contains("texture"))
      texture = new ResourceLocation(nbt.getString("texture"));
    x = nbt.getInt("x");
    y = nbt.getInt("y");
    width = nbt.getInt("width");
    height = nbt.getInt("height");
    orientation = Orientation.valueOf(nbt.getString("orientation"));
    inverted = nbt.getBoolean("inverted");
    vertical = nbt.getBoolean("vertical");
  }

  public enum Orientation {
    RIGHT,
    LEFT,
    TOP,
    BOTTOM
  }
}
