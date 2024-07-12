package es.degrassi.forge.core.digital.client.screen.controller;

import com.mojang.blaze3d.systems.RenderSystem;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.common.utils.TextureSizeHelper;
import es.degrassi.forge.api.core.common.IElement;
import es.degrassi.forge.core.common.element.ButtonElement;
import es.degrassi.forge.core.common.element.TextElement;
import es.degrassi.forge.core.common.element.TextureElement;
import es.degrassi.forge.core.digital.block.entity.DigitalControllerEntity;
import es.degrassi.forge.core.digital.client.container.DigitalControllerContainer;
import es.degrassi.forge.core.digital.client.screen.DigitalControllerScreen;
import es.degrassi.forge.core.digital.util.Networks;
import es.degrassi.forge.core.init.BlockRegistration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class SelectFrequencyScreen extends AbstractContainerScreen<DigitalControllerContainer> {
  private static final int defaultColor = 4210752;
  private static final int startY = 10, stepY = 30, x = 253;
  private final DigitalControllerEntity entity;
  private final ResourceLocation background;
  protected final Player player;
  private final Inventory playerInv;
  private final List<IElement<?>> toRender = new ArrayList<>();
  private final List<ButtonElement> buttons = new ArrayList<>();
  private final List<TextElement> texts = new ArrayList<>();

  public SelectFrequencyScreen(DigitalControllerContainer menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, Component.translatable("degrassi.screen.title.frequency_select"));
    this.entity = menu.getEntity();
    this.background = new DegrassiLocation("textures/gui/base_background.png"/*"textures/gui/digital_controller.png"*/);
    this.playerInv = playerInventory;
    this.player = playerInventory.player;
    this.imageWidth = TextureSizeHelper.getTextureWidth(background);
    this.imageHeight = TextureSizeHelper.getTextureHeight(background);
    this.leftPos = (this.width - this.imageWidth) / 2;
    this.topPos = (this.height - this.imageHeight) / 2;

    ButtonElement controller = new ButtonElement(
      entity.getElementManager(),
      0,
      0,
      new DegrassiLocation("textures/gui/base_tab.png"),
      new DegrassiLocation("textures/gui/base_tab_hovered.png"),
      Component.literal("Back"),
      Pages.CONTROLLER.getId(),
      button -> Minecraft.getInstance().setScreen(new DigitalControllerScreen(menu, playerInventory, Component.empty())),
      Supplier::get
    ).withItem(BlockRegistration.DIGITAL_CONTROLLER.get().asItem());
    buttons.add(controller);

    ButtonElement create = new ButtonElement(
      entity.getElementManager(),
      0,
      0,
      new DegrassiLocation("textures/gui/base_tab.png"),
      new DegrassiLocation("textures/gui/base_tab_hovered.png"),
      Component.literal("Create Frequency"),
      Pages.CREATE.getId(),
      button -> {
//        Minecraft.getInstance().setScreen(new CreateScreen(menu, playerInventory, Component.empty()));
      },
      Supplier::get
    );
    buttons.add(create);
  }

  @Override
  protected void init() {
    super.init();
    toRender.stream().map(ele -> (AbstractWidget) ele).forEach(this::removeWidget);
    toRender.clear();
    texts.clear();
    addTabs();
    renderFrequencies();
    toRender.stream().map(element -> (AbstractWidget) element).forEach(this::addRenderableWidget);
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    super.render(guiGraphics, mouseX, mouseY, partialTick);
  }

  private void addTabs() {
    for (int i = 0; i < buttons.size(); i++) {
      toRender.add(new ButtonElement(
        entity.getElementManager(),
        x + getGuiLeft(),
        startY + i * stepY + getGuiTop(),
        buttons.get(i).getEmptyTexture(),
        buttons.get(i).getHoveredTexture(),
        buttons.get(i).getMessage(),
        buttons.get(i).getId(),
        buttons.get(i).getOnPress(),
        buttons.get(i).getCreateNarration()
      ).withItem(buttons.get(i).getItem()));
    }
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
    guiGraphics.pose().pushPose();
    renderBackground(guiGraphics);
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    guiGraphics.blit(this.background, this.leftPos, this.topPos, 0F, 0F, imageWidth, imageHeight, imageWidth, imageHeight);
    renderTooltip(guiGraphics, mouseX, mouseY);
    guiGraphics.pose().popPose();
  }

  private void renderFrequencies() {
    int startX = 10, startY = 20;
    int stepY = 20;
    int posSelect = 48, posEdit = 65, posDelete = 82;
    AtomicInteger currentIndex = new AtomicInteger(0);
    ResourceLocation texture = new DegrassiLocation("textures/gui/base_frequency.png");
    int textureWidth = TextureSizeHelper.getTextureWidth(texture), textureHeight = TextureSizeHelper.getTextureHeight(texture);
    if (DigitalControllerContainer.getNetworks() != null && !DigitalControllerContainer.getNetworks().isEmpty()) DigitalControllerContainer.getNetworks().forEach(network -> {
      if (startY + stepY * currentIndex.get() > getGuiTop() + this.imageWidth) return;
      texts.add(
        new TextElement(
          entity.getElementManager(),
          startX,
          startY + (stepY + 5) * currentIndex.get(),
          font,
          network.getFrequency().replaceAll("_", " "),
          Component.literal("text"),
          false,
          defaultColor,
          false
        )
      );

      toRender.add(new TextureElement(
        entity.getElementManager(),
        startX + getGuiLeft(),
        startY + stepY * currentIndex.get() + getGuiTop(),
        texture,
        Component.literal("Frequency"),
        false
      ));
      toRender.add(new ButtonElement(
        entity.getElementManager(),
        posSelect + getGuiLeft(),
        startY + stepY * currentIndex.get() + getGuiTop(),
        new DegrassiLocation("textures/gui/select_frequency.png"),
        new DegrassiLocation("textures/gui/select_frequency_hovered.png"),
        Component.literal("Select Button"),
        "select_button_" + network.getFrequency(),
        button -> {
          entity.onNetworkSelected(network.getFrequency());
          Minecraft.getInstance().setScreen(new DigitalControllerScreen(menu, playerInv, Component.empty()));
        },
        Supplier::get
      ));
      toRender.add(new ButtonElement(
        entity.getElementManager(),
        posEdit + getGuiLeft(),
        startY + stepY * currentIndex.get() + getGuiTop(),
        new DegrassiLocation("textures/gui/edit_frequency.png"),
        new DegrassiLocation("textures/gui/edit_frequency_hovered.png"),
        Component.literal("Edit Button"),
        "edit_button_" + network.getFrequency(),
        button -> {
//          setCurrentTabId(Pages.EDIT.getId());
        },
        Supplier::get
      ));
      toRender.add(new ButtonElement(
        entity.getElementManager(),
        posDelete + getGuiLeft(),
        startY + stepY * currentIndex.get() + getGuiTop(),
        new DegrassiLocation("textures/gui/delete_frequency.png"),
        new DegrassiLocation("textures/gui/delete_frequency_hovered.png"),
        Component.literal("Delete Button"),
        "delete_button_" + network.getFrequency(),
        button -> {
          Networks.removeNetwork(network.getFrequency(), menu.getLevel().getServer().overworld());
          Minecraft.getInstance().setScreen(new DigitalControllerScreen(menu, playerInv, Component.empty()));
        },
        Supplier::get
      ));
      currentIndex.getAndIncrement();
    });
  }

  @Override
  protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
    guiGraphics.pose().pushPose();
    guiGraphics.drawString(font, title, this.titleLabelX, this.titleLabelY, defaultColor, false);
    guiGraphics.pose().scale(.8f, .8f, 0);
    ResourceLocation texture = new DegrassiLocation("textures/gui/base_frequency.png");
    int textureWidth = TextureSizeHelper.getTextureWidth(texture), textureHeight = TextureSizeHelper.getTextureHeight(texture);
    texts.forEach(text -> guiGraphics.drawString(font, text.getText(), text.getX() + 7, text.getY() + textureHeight / 2 - font.lineHeight / 2 + 7, text.getColor(), text.isDropShadow()));
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
