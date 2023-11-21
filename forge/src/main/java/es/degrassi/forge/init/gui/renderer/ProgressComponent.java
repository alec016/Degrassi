package es.degrassi.forge.init.gui.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import es.degrassi.forge.client.IClientHandler;
import es.degrassi.forge.init.recipe.IDegrassiRecipe;
import es.degrassi.forge.init.registration.ElementRegistry;
import es.degrassi.forge.integration.jei.ingredients.DegrassiTypes;
import es.degrassi.forge.util.TextureSizeHelper;
import es.degrassi.forge.util.storage.ProgressStorage;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.runtime.IClickableIngredient;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ProgressComponent extends InfoArea implements IDrawableAnimated, IGuiElement, IClickableIngredient<ProgressComponent> {
  private ProgressStorage progress;
  public ResourceLocation texture;
  private int x, y, width, height;
  private Orientation orientation = Orientation.RIGHT;

  public ProgressComponent(CompoundTag tag){
    processTag(tag);
  }
  public ProgressComponent (int xMin, int yMin) {
    this(xMin, yMin, null, 8, 64);
  }
  public ProgressComponent(int xMin, int yMin, ProgressStorage progress) {
    this(xMin, yMin, progress, 8, 64);
  }
  public ProgressComponent(int xMin, int yMin, ProgressStorage progress, int width, int height) {
    super(new Rect2i(xMin, yMin, width, height), Component.literal("Progress"));
    this.x = xMin;
    this.y = yMin;
    this.width = width;
    this.height = height;
    this.progress = progress;
  }
  public ProgressComponent(int xMin, int yMin, ProgressStorage progress, int width, int height, ResourceLocation texture) {
    super(new Rect2i(xMin, yMin, width, height), Component.literal("Progress"));
    this.progress = progress;
    this.texture = texture;
    this.x = xMin;
    this.y = yMin;
    this.width = width;
    this.height = height;
  }

  @Override
  public void draw(PoseStack transform, int x, int y, ResourceLocation texture) {
    this.texture = texture;
    draw(transform, x, y, texture, false);
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

  @Override
  public void draw(PoseStack pose, int x, int y, ResourceLocation texture, boolean vertical) {
    this.texture = texture;
    final int width = TextureSizeHelper.getTextureWidth(texture);
    final int height = TextureSizeHelper.getTextureHeight(texture);
    IClientHandler.bindTexture(texture);
    int current;
    if (getScaledProgress(height) == height || getScaledProgress(width) == width) {
      blit(
        pose,
        x,
        y,
        0,
        0,
        width,
        height,
        width,
        height
      );
      return;
    }
    if (vertical) {
      current = getScaledProgress(height);
      blit(
        pose,
        x,
        y + height - current,
        0,
        height - current,
        width,
        current,
        width,
        height
      );
      return;
    }
    current = getScaledProgress(width);
    blit(
      pose,
      x,
      y,
      0,
      0,
      current,
      height,
      width,
      height
    );
  }

  public int getScaledProgress(int renderSize) {
    int progress = this.progress.getProgress();
    int maxProgress = this.progress.getMaxProgress();  // Max Progress

    return progress != 0 ? (int) Math.floor((progress / (float) maxProgress) * renderSize) : 0;
  }

  @Override
  public GuiElementType<? extends IGuiElement> getType() {
    return ElementRegistry.PROGRESS_GUI_ELEMENT.get();
  }

  @Override
  public int getX() {
    return x;
  }

  @Override
  public int getY() {
    return y;
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public void draw(@NotNull PoseStack poseStack, int xOffset, int yOffset) {
    poseStack.pushPose();
    {
      draw(poseStack, xOffset, yOffset, texture, false);
    }
    poseStack.popPose();
  }

  public Orientation getDirection() {
    return this.orientation;
  }

  public void setDirection(Orientation direction) {
    this.orientation = direction;
  }

  public ResourceLocation getFilledTexture() {
    return texture;
  }

  public ProgressStorage getStorage() {
    return progress;
  }

  @Override
  public @NotNull ITypedIngredient<ProgressComponent> getTypedIngredient() {
    return new ITypedIngredient<>() {
      @Override
      public @NotNull IIngredientType<ProgressComponent> getType() {
        return DegrassiTypes.PROGRESS;
      }

      @Override
      public @NotNull ProgressComponent getIngredient() {
        return ProgressComponent.this;
      }
    };
  }

  @Override
  public @NotNull Rect2i getArea() {
    return area;
  }

  @Override
  public void renderButton(@NotNull PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
    draw(poseStack, mouseX, mouseY, texture, false);
  }

  @Override
  protected boolean clicked(double mouseX, double mouseY) {
    return super.clicked(mouseX, mouseY);
  }

  public CompoundTag toNBT() {
    CompoundTag nbt = new CompoundTag();
    nbt.put("progress", progress.serializeNBT());
    if (texture != null)
      nbt.putString("texture", texture.getNamespace() + ":" + texture.getPath());
    nbt.putInt("x", x);
    nbt.putInt("y", y);
    nbt.putInt("width", width);
    nbt.putInt("height", height);
    nbt.putString("orientation", orientation.name());
    return nbt;
  }

  public void fromNBT(CompoundTag nbt) {
    progress.deserializeNBT(nbt);
    if (nbt.contains("texture"))
      texture = new ResourceLocation(nbt.getString("texture"));
    x = nbt.getInt("x");
    y = nbt.getInt("y");
    width = nbt.getInt("width");
    height = nbt.getInt("height");
    orientation = Orientation.valueOf(nbt.getString("orientation"));
  }

  protected void processTag(CompoundTag nbt) {
    progress = ProgressStorage.fromNBT(nbt);
    if (nbt.contains("texture"))
      texture = new ResourceLocation(nbt.getString("texture"));
    x = nbt.getInt("x");
    y = nbt.getInt("y");
    width = nbt.getInt("width");
    height = nbt.getInt("height");
    orientation = Orientation.valueOf(nbt.getString("orientation"));
  }

  public enum Orientation {
    RIGHT,
    LEFT,
    TOP,
    BOTTOM
  }
}
