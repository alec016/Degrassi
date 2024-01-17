package es.degrassi.forge.init.gui.element;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.*;
import es.degrassi.forge.api.gui.IElementManager;
import es.degrassi.forge.init.entity.BaseEntity;
import java.util.List;
import java.util.concurrent.atomic.*;
import net.minecraft.client.gui.screens.*;
import net.minecraft.nbt.*;
import org.jetbrains.annotations.*;

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

  public void deserializeNBT(CompoundTag nbt) {
    ListTag listTag = nbt.getList("elements", 0);
    AtomicInteger count = new AtomicInteger(0);
    elements.forEach(element -> element.deserializeNBT(listTag.get(count.getAndIncrement())));
    entity.deserializeNBT(nbt.getCompound("entity"));
  }

  public CompoundTag serializeNBT() {
    CompoundTag nbt = new CompoundTag();
    ListTag listTag = new ListTag();
    elements.forEach(element -> listTag.add(element.serializeNBT()));
    nbt.put("elements", listTag);
    nbt.put("entity", entity.serializeNBT());
    return nbt;
  }

  public void renderBg (@NotNull PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
    elements.forEach(element -> element.draw(poseStack, partialTick, mouseX, mouseY));
  }

  public void renderLabels(Screen screen, @NotNull PoseStack poseStack, int mouseX, int mouseY) {
    elements.forEach(element -> element.renderLabels(screen, poseStack, mouseX, mouseY));
  }
}
