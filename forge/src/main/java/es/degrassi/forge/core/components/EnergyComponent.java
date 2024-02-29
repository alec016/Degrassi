package es.degrassi.forge.core.components;

import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.core.component.ComponentIOMode;
import es.degrassi.forge.api.core.component.ComponentType;
import es.degrassi.forge.api.core.component.IComponent;
import es.degrassi.forge.api.core.component.IComponentManager;
import es.degrassi.forge.api.core.component.IComponentTemplate;
import es.degrassi.forge.api.core.component.IDumpComponent;
import es.degrassi.forge.api.core.component.ISerializableComponent;
import es.degrassi.forge.api.core.component.ISideConfigComponent;
import es.degrassi.forge.api.core.component.ITickableComponent;
import es.degrassi.forge.api.core.network.ISyncable;
import es.degrassi.forge.api.core.network.ISyncableStuff;
import es.degrassi.forge.api.impl.core.components.AbstractComponent;
import es.degrassi.forge.api.impl.core.components.config.SideConfig;
import es.degrassi.forge.init.registration.ComponentRegistration;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.nbt.CompoundTag;

public class EnergyComponent extends AbstractComponent implements ISerializableComponent, IDumpComponent, ISyncableStuff, ITickableComponent, ISideConfigComponent {
  private final long capacity, maxInput, maxOutput;
  private long energy;
  private final Template template;
  private SideConfig config;
  private final String id;

  public EnergyComponent(IComponentManager manager, long capacity, long maxInput, long maxOutput, SideConfig.Template config, String id, Template template) {
    super(manager, ComponentIOMode.BOTH);
    this.capacity = capacity;
    this.maxInput = Math.min(maxInput, capacity);
    this.maxOutput = Math.min(maxOutput, capacity);
    this.template = template;
    this.config = config.build(this);
    this.id = id;
  }
  @Override
  public ComponentType<EnergyComponent> getType() {
    return ComponentRegistration.ENERGY.get();
  }

  @Override
  public Template template() {
    return template;
  }

  @Override
  public void dump(List<String> ids) {

  }

  @Override
  public void serialize(CompoundTag nbt) {

  }

  @Override
  public void deserialize(CompoundTag nbt) {

  }

  @Override
  public SideConfig getConfig() {
    return config;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public void getStuffToSync(Consumer<ISyncable<?, ?>> container) {

  }

  public static class Template implements IComponentTemplate<EnergyComponent> {
    public static final NamedCodec<Template> CODEC = NamedCodec.record(builder -> builder.group(
        NamedCodec.LONG.fieldOf("capacity").forGetter(component -> component.capacity),
        NamedCodec.LONG.optionalFieldOf("maxInput").forGetter(component -> Optional.of(component.maxInput)),
        NamedCodec.LONG.optionalFieldOf("maxOutput").forGetter(component -> Optional.of(component.maxOutput)),
        SideConfig.Template.CODEC.optionalFieldOf("config", SideConfig.Template.DEFAULT_ALL_BOTH).forGetter(component -> component.config),
        NamedCodec.STRING.fieldOf("id").forGetter(component -> component.id)
      ).apply(builder, (capacity, maxInput, maxOutput, config, id) -> new Template(capacity, maxInput.orElse(capacity), maxOutput.orElse(capacity), config, id))
    , "Energy Component template");
    private final long capacity, maxInput, maxOutput;
    private final String id;
    private final SideConfig.Template config;
    public Template(long capacity, long maxInput, long maxOutput, SideConfig.Template config, String id) {
      this.capacity = capacity;
      this.maxInput = Math.min(maxInput, capacity);
      this.maxOutput = Math.min(maxOutput, capacity);
      this.config = config;
      this.id = id;
    }
    public Template(long capacity, long transfer, SideConfig.Template config, String id) {
      this(capacity, transfer, transfer, config, id);
    }

    public Template(long capacity, SideConfig.Template config, String id) {
      this(capacity, capacity, config, id);
    }

    public Template(long capacity, long maxInput, long maxOutput) {
      this(capacity, maxInput, maxOutput, null, "");
    }

    public Template(long capacity, long maxInput, long maxOutput, SideConfig.Template config) {
      this(capacity, maxInput, maxOutput, config, "");
    }


    public Template(long capacity, long maxInput, long maxOutput, String id) {
      this(capacity, maxInput, maxOutput, null, id);
    }
    @Override
    public ComponentType<EnergyComponent> getType() {
      return ComponentRegistration.ENERGY.get();
    }

    @Override
    public String getId() {
      return id;
    }

    @Override
    public boolean canAccept(Object ingredient, boolean isInput, IComponentManager manager) {
      return false;
    }

    @Override
    public EnergyComponent build(IComponentManager manager) {
      return new EnergyComponent(manager, capacity, maxInput, maxOutput, config, id, this);
    }
  }
}
