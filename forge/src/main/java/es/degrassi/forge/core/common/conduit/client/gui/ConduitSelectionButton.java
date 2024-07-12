package es.degrassi.forge.core.common.conduit.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import es.degrassi.common.conduit.IConduitType;
import es.degrassi.common.misc.Vector2i;
import java.util.function.Consumer;
import java.util.function.Supplier;
import lombok.Getter;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class ConduitSelectionButton extends AbstractButton {
  @Getter
  private final IConduitType type;
  private final Supplier<IConduitType> getter;
  private final Consumer<IConduitType> setter;
  public ConduitSelectionButton(int pX, int pY, IConduitType type, Supplier<IConduitType> getter, Consumer<IConduitType> setter) {
    super(pX, pY, 21, 24, Component.empty());
    this.type = type;
    this.getter = getter;
    this.setter = setter;
  }

  @Override
  protected boolean isValidClickButton(int pButton) {
    return super.isValidClickButton(pButton) && getter.get() != type;
  }

  @Override
  public void onPress() {
    setter.accept(type);
  }

  @Override
  public void renderWidget(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {

    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();
    RenderSystem.enableDepthTest();
    guiGraphics.blit(ConduitScreen.TEXTURE, getX(), getY(), 227, 0, this.width, this.height);
    if (getter.get() == type) {
      guiGraphics.blit(ConduitScreen.TEXTURE, getX() - 3, getY(), 224, 0, 3, this.height);
    }
    RenderSystem.disableBlend();
    RenderSystem.disableDepthTest();
    IScreen.renderIcon(guiGraphics, new Vector2i(getX(), getY()).add(3, 6), type.getClientData());
  }

  @Override
  protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
  }
}
