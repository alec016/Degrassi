package es.degrassi.forge.api.impl.core.elements;

import es.degrassi.forge.api.core.element.IGuiElement;
import es.degrassi.forge.api.core.element.IMachineScreen;
import java.util.List;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractGuiElementWidget<T extends IGuiElement> extends AbstractWidget {

  private final T element;
  private final IMachineScreen screen;

  public AbstractGuiElementWidget(T element, IMachineScreen screen, Component title) {
    super(element.getX() + screen.getX(), element.getY() + screen.getY(), element.getWidth(), element.getHeight(), title);
    this.element = element;
    this.screen = screen;
  }

  public List<Component> getTooltips() {
    return this.element.getTooltips();
  }

  public T getElement() {
    return this.element;
  }

  public IMachineScreen getScreen() {
    return this.screen;
  }

  public boolean isClickable() {
    return false;
  }

  @Nullable
  @Override
  public Tooltip getTooltip() {
    return Tooltip.create(getTooltips().stream().reduce(Component.empty(), (component, acc) -> Component.literal(acc.getString() + "\n" + component.getString())));
  }

  @Override
  public void updateWidgetNarration(NarrationElementOutput output) {
    output.add(NarratedElementType.HINT, getTooltips().toArray(new Component[0]));
  }

  @Override
  protected boolean clicked(double mouseX, double mouseY) {
    return isClickable() && super.clicked(mouseX, mouseY);
  }
}
