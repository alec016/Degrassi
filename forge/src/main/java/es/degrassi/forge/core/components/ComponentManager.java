package es.degrassi.forge.core.components;

import es.degrassi.forge.api.core.component.ComponentType;
import es.degrassi.forge.api.core.component.IComparatorInputComponent;
import es.degrassi.forge.api.core.component.IComponent;
import es.degrassi.forge.api.core.component.IComponentManager;
import es.degrassi.forge.api.core.component.IComponentTemplate;
import es.degrassi.forge.api.core.component.IDumpComponent;
import es.degrassi.forge.api.core.component.ISerializableComponent;
import es.degrassi.forge.api.core.component.ISideConfigComponent;
import es.degrassi.forge.api.core.component.ITickableComponent;
import es.degrassi.forge.api.core.component.handler.IComponentHandler;
import es.degrassi.forge.api.core.machine.MachineTile;
import es.degrassi.forge.api.core.network.ISyncable;
import es.degrassi.forge.api.core.network.ISyncableStuff;
import es.degrassi.forge.init.registration.ComponentRegistration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

public class ComponentManager implements IComponentManager {
  private final MachineTile tile;
  private final Map<ComponentType<?>, IComponent> components;
  private final List<ISerializableComponent> serializableComponents;
  private final List<ITickableComponent> tickableComponents;
  private final List<ISyncableStuff> syncableComponents;
  private final List<IComparatorInputComponent> comparatorInputComponents;
  private final List<IDumpComponent> dumpComponents;
  private final Map<String, ISideConfigComponent> configComponents;

  @SuppressWarnings("unchecked, rawtypes")
  public ComponentManager(List<? extends IComponentTemplate<? extends IComponent>> templates, MachineTile tile) {
    this.tile = tile;
    Map<ComponentType<?>, IComponent> components = new LinkedHashMap<>();
    Map<ComponentType<?>, List<IComponent>> handlers = new LinkedHashMap<>();
    templates.forEach(template -> {
      IComponent component = template.build(this);
      if (component.getType().isSingle())
        components.put(component.getType(), component);
      else
        handlers.computeIfAbsent(component.getType(), type -> new ArrayList<>()).add(component);
    });
    handlers.forEach((type, list) -> components.put(type, type.getHandler(this, (List) Collections.unmodifiableList(list))));
    StreamSupport.stream(ComponentRegistration.COMPONENT_TYPE_REGISTRY.spliterator(), false)
      .filter(type ->
        type.isDefaultComponent() &&
          components.values().stream().noneMatch(component -> component.getType() == type)
      )
      .forEach(type -> components.put(type, type.getDefaultComponentBuilder().apply(this)));
    this.components = components;
    this.serializableComponents = this.components.values().stream().filter(component -> component instanceof ISerializableComponent).map(component -> (ISerializableComponent) component).toList();
    this.tickableComponents = this.components.values().stream().filter(component -> component instanceof ITickableComponent).map(component -> (ITickableComponent) component).toList();
    this.syncableComponents = this.components.values().stream().filter(component -> component instanceof ISyncableStuff).map(component -> (ISyncableStuff) component).toList();
    this.comparatorInputComponents = this.components.values().stream().filter(component -> component instanceof IComparatorInputComponent).map(component -> (IComparatorInputComponent) component).toList();
    this.dumpComponents = this.components.values().stream().filter(component -> component instanceof IDumpComponent).map(component -> (IDumpComponent) component).toList();
    this.configComponents = this.components.values().stream()
      .flatMap(component -> {
        if (component instanceof IComponentHandler<?> handler)
          return handler.getComponents().stream();
        return Stream.of(component);
      })
      .filter(component -> component instanceof ISideConfigComponent)
      .map(component -> (ISideConfigComponent) component)
      .collect(Collectors.toUnmodifiableMap(component -> component.getType().getId().toString() + ":" + component.getId(), Function.identity()));
  }

  @Override
  public Map<ComponentType<?>, IComponent> getMap() {
    return components;
  }

  @Override
  public MachineTile getTile() {
    return tile;
  }

  @Override
  public Level getLevel() {
    return tile.getLevel();
  }

  @Override
  public MinecraftServer getServer() {
    return tile.getLevel().getServer();
  }

  @Override
  public void markDirty() {
    this.getTile().setChanged();
//    this.getTile().getProcessor().setMachineInventoryChanged();
  }

  @Override
  public List<ISerializableComponent> getSerializableComponents() {
    return serializableComponents;
  }

  @Override
  public List<ITickableComponent> getTickableComponents() {
    return tickableComponents;
  }

  @Override
  public List<ISyncableStuff> getSyncableComponents() {
    return syncableComponents;
  }

  @Override
  public List<IComparatorInputComponent> getComparatorInputComponents() {
    return comparatorInputComponents;
  }

  @Override
  public List<IDumpComponent> getDumpComponents() {
    return dumpComponents;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends IComponent> Optional<T> getComponent(ComponentType<T> type) {
    return Optional.ofNullable((T)this.components.get(type));
  }

  @SuppressWarnings("unchecked")
  @Override
  public <T extends IComponent> Optional<IComponentHandler<T>> getComponentHandler(ComponentType<T> type) {
    return getComponent(type).filter(component -> component instanceof IComponentHandler).map(component -> (IComponentHandler<T>)component);
  }
  public boolean hasComponent(ComponentType<?> type) {
    return this.components.get(type) != null;
  }
  public void getStuffToSync(Consumer<ISyncable<?, ?>> container) {
    getSyncableComponents().forEach(syncableComponent -> syncableComponent.getStuffToSync(container));
  }
  public Optional<ISideConfigComponent> getConfigComponentById(String id) {
    return Optional.ofNullable(this.configComponents.get(id));
  }

  public Collection<ISideConfigComponent> getConfigComponents() {
    return this.configComponents.values();
  }

  public void serverTick() {
    getTickableComponents().forEach(ITickableComponent::serverTick);
  }

  public void clientTick() {
    getTickableComponents().forEach(ITickableComponent::clientTick);
  }

  public CompoundTag serializeNBT() {
    CompoundTag nbt = new CompoundTag();
    getSerializableComponents().forEach(component -> component.serialize(nbt));
    return nbt;
  }

  public void deserializeNBT(CompoundTag nbt) {
    getSerializableComponents().forEach(component -> component.deserialize(nbt));
  }

  public ComponentManager updateManager() {
    return new ComponentManager(components.values().stream().map(IComponent::template).toList(), tile);
  }
}
