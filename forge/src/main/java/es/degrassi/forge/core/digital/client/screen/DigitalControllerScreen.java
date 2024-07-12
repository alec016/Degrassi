package es.degrassi.forge.core.digital.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.common.utils.TextureSizeHelper;
import es.degrassi.forge.api.core.common.ElementDirection;
import es.degrassi.forge.api.core.common.IElement;
import es.degrassi.forge.core.common.element.ButtonElement;
import es.degrassi.forge.core.common.element.EnergyElement;
import es.degrassi.forge.core.digital.block.entity.DigitalControllerEntity;
import es.degrassi.forge.core.digital.client.container.DigitalControllerContainer;
import es.degrassi.forge.core.digital.client.screen.controller.Pages;
import es.degrassi.forge.core.digital.client.screen.controller.SelectFrequencyScreen;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class DigitalControllerScreen extends AbstractContainerScreen<DigitalControllerContainer> {
  private static final int defaultColor = 4210752;
  private static final int startY = 10, stepY = 30, x = 253;
  private final Map<String, List<ButtonElement>> validTabs = new LinkedHashMap<>();
  private final List<ButtonElement> buttons = new ArrayList<>();
  private final List<IElement<?>> toRender = new ArrayList<>();
  private final DigitalControllerEntity entity;
  private final ResourceLocation background;
  protected final Player player;
  private final Inventory playerInv;
  private String currentTabId;

  public DigitalControllerScreen(DigitalControllerContainer menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, Component.translatable("degrassi.screen.title.digital_controller"));
    this.player = playerInventory.player;
    this.playerInv = playerInventory;
    this.entity = menu.getEntity();
    this.background = new DegrassiLocation("textures/gui/base_background.png"/*"textures/gui/digital_controller.png"*/);
    this.imageWidth = TextureSizeHelper.getTextureWidth(background);
    this.imageHeight = TextureSizeHelper.getTextureHeight(background);
    this.leftPos = (this.width - this.imageWidth) / 2;
    this.topPos = (this.height - this.imageHeight) / 2;
  }

  private void openCrateTab(AbstractButton button) {
    setCurrentTabId(Pages.CREATE.getId());
  }

  @Override
  protected void init() {
    super.init();
  }

  private void addTabs() {
    toRender.add(
      new ButtonElement(
        entity.getElementManager(),
        x + getGuiLeft(),
        startY + getGuiTop(),
        new DegrassiLocation("textures/gui/base_tab.png"),
        new DegrassiLocation("textures/gui/base_tab_hovered.png"),
        Component.literal("Select Frequency"),
        Pages.FREQUENCY_LIST.getId(),
        button -> Minecraft.getInstance().setScreen(new SelectFrequencyScreen(menu, playerInv, Component.empty())),
        Supplier::get
      )
    );
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    toRender.stream().map(ele -> (AbstractWidget) ele).forEach(this::removeWidget);
    toRender.clear();
    addTabs();
    super.render(guiGraphics, mouseX, mouseY, partialTick);
  }

  @Override
  protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
    guiGraphics.pose().pushPose();
    renderBackground(guiGraphics);
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    guiGraphics.blit(this.background, this.leftPos, this.topPos, 0F, 0F, imageWidth, imageHeight, imageWidth, imageHeight);
    toRender.add(
      new EnergyElement(
        entity.getElementManager(),
        10 + getGuiLeft(),
        20 + getGuiTop(),
        Component.literal("Energy"),
        new DegrassiLocation("textures/gui/base_energy_storage_empty.png"),
        new DegrassiLocation("textures/gui/base_energy_storage_filled.png"),
        "energy",
        ElementDirection.TOP,
        true
      )
    );
    toRender.stream().map(element -> (AbstractWidget) element).forEach(this::addRenderableWidget);
    renderTooltip(guiGraphics, mouseX, mouseY);
    guiGraphics.pose().popPose();
  }

  @Override
  protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
    guiGraphics.pose().pushPose();
    guiGraphics.drawString(font, title, this.titleLabelX, this.titleLabelY, defaultColor, false);
    if (entity.getCurrentNetwork() != null && entity.getCurrentNetwork().getFrequency() != null) {
      guiGraphics.drawString(font, "Current frequency:", 40, 20, defaultColor, false);
      guiGraphics.drawString(font, getEntity().getCurrentNetwork().getFrequency(), 40 + font.width("Current frequency: "), 20, defaultColor, false);
    }
    guiGraphics.pose().popPose();
  }

  @Override
  protected void renderTooltip(@NotNull GuiGraphics guiGraphics, int x, int y) {
    guiGraphics.pose().pushPose();
    toRender.forEach(element -> element.renderTooltip(guiGraphics, x, y));
    guiGraphics.pose().popPose();

    guiGraphics.pose().pushPose();
    super.renderTooltip(guiGraphics, x, y);
    guiGraphics.pose().popPose();
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
    return super.mouseScrolled(mouseX, mouseY, delta);
  }
}
