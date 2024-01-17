package es.degrassi.forge.init.gui.element;

import com.google.common.collect.Lists;
import es.degrassi.forge.api.gui.IElementManager;
import es.degrassi.forge.init.entity.BaseEntity;
import java.util.List;

public class ElementManager implements IElementManager {
  private final List<IGuiElement> elements;
  private final BaseEntity entity;

  public ElementManager(BaseEntity entity) {
    this(Lists.newLinkedList(), entity);
  }

  public ElementManager(List<IGuiElement> elements, BaseEntity entity) {
    this.elements = elements;
    this.entity = entity;
  }

  @Override
  public void addElement(IGuiElement element) {
    this.elements.add(element);
  }

  @Override
  public List<IGuiElement> getElements () {
    return elements;
  }

  @Override
  public BaseEntity getEntity () {
    return entity;
  }

  @Override
  public void markDirty () {
    entity.setChanged();
  }
}
