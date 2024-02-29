package es.degrassi.forge.core.components;

import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.core.component.ComponentIOMode;
import es.degrassi.forge.api.core.component.ComponentType;
import es.degrassi.forge.api.core.component.IComponent;
import es.degrassi.forge.api.core.component.IComponentManager;
import es.degrassi.forge.api.core.component.IComponentTemplate;
import es.degrassi.forge.api.core.component.ISerializableComponent;
import es.degrassi.forge.api.core.component.ITickableComponent;
import es.degrassi.forge.api.core.network.ISyncable;
import es.degrassi.forge.api.core.network.ISyncableStuff;
import es.degrassi.forge.api.impl.core.components.AbstractComponent;
import es.degrassi.forge.init.registration.ComponentRegistration;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;

public class ExperienceComponent extends AbstractComponent implements ISerializableComponent, ISyncableStuff, ITickableComponent {
  private float xp, capacity;
  private Template template;
  public ExperienceComponent(IComponentManager manager, float capacity, Template template) {
    super(manager, ComponentIOMode.BOTH);
    this.template = template;
    this.capacity = capacity;
  }

  @Override
  public ComponentType<?> getType() {
    return ComponentRegistration.EXPERIENCE.get();
  }

  @Override
  public Template template() {
    return template;
  }

  @Override
  public void serialize(CompoundTag nbt) {

  }

  @Override
  public void deserialize(CompoundTag nbt) {

  }

  @Override
  public void getStuffToSync(Consumer<ISyncable<?, ?>> container) {

  }

  public static class Template implements IComponentTemplate<ExperienceComponent> {
    public static final NamedCodec<Template> CODEC = NamedCodec.record(
      builder -> builder.group(
        NamedCodec.FLOAT.fieldOf("capacity").forGetter(component -> component.capacity)
      ).apply(
        builder, Template::new
      ), "Experience component"
    );
    private final float capacity;
    public Template(float capacity) {
      this.capacity = capacity;
    }

    @Override
    public ComponentType<ExperienceComponent> getType() {
      return ComponentRegistration.EXPERIENCE.get();
    }

    @Override
    public String getId() {
      return "";
    }

    @Override
    public boolean canAccept(Object ingredient, boolean isInput, IComponentManager manager) {
      return false;
    }

    @Override
    public ExperienceComponent build(IComponentManager manager) {
      return new ExperienceComponent(manager, capacity, this);
    }
  }
}
