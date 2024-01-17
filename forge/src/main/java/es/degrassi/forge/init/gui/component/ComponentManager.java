package es.degrassi.forge.init.gui.component;

import com.google.common.collect.Lists;
import es.degrassi.forge.api.gui.*;
import es.degrassi.forge.init.entity.BaseEntity;
import java.util.List;
import java.util.concurrent.atomic.*;
import net.minecraft.nbt.*;

public class ComponentManager implements IComponentManager {
  private List<IComponent> components;
  private BaseEntity entity;

  public ComponentManager(BaseEntity entity) {
    this(Lists.newLinkedList(), entity);
  }

  public ComponentManager(List<IComponent> components, BaseEntity entity) {
    this.components = components;
    this.entity = entity;
  }

  @Override
  public void addComponent(IComponent component) {
    this.components.add(component);
  }

  @Override
  public List<IComponent> getComponents() {
    return components;
  }

  @Override
  public BaseEntity getEntity() {
    return entity;
  }

  @Override
  public void markDirty() {
    entity.setChanged();
  }

  public void deserializeNBT(CompoundTag nbt) {
    ListTag listTag = nbt.getList("components", 0);
    AtomicInteger count = new AtomicInteger(0);
    components.forEach(component -> component.deserializeNBT(listTag.get(count.getAndIncrement())));
    entity.deserializeNBT(nbt.getCompound("entity"));
  }

  public CompoundTag serializeNBT() {
    CompoundTag nbt = new CompoundTag();
    ListTag listTag = new ListTag();
    components.forEach(component -> listTag.add(component.serializeNBT()));
    nbt.put("components", listTag);
    nbt.put("entity", entity.serializeNBT());
    return nbt;
  }
}
