package es.degrassi.forge.api.core.component.variant;

import com.google.common.collect.ImmutableMap;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.core.component.ComponentType;
import es.degrassi.forge.api.core.component.IComponent;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;

public class RegisterComponentVariantEvent {

    public static final Event<Register> EVENT = EventFactory.createLoop();

    private final Map<ComponentType<? extends IComponent>, Map<ResourceLocation, NamedCodec<IComponentVariant>>> componentVariants = new HashMap<>();

    @SuppressWarnings("unchecked")
    public <C extends IComponent> void register(ComponentType<C> type, ResourceLocation id, NamedCodec<? extends IComponentVariant> codec) {
        if(this.componentVariants.computeIfAbsent(type, t -> new HashMap<>()).containsKey(id))
            throw new IllegalArgumentException("Component variant " + id + " already registered for type: " + type.getId());
        this.componentVariants.get(type).put(id, (NamedCodec<IComponentVariant>)codec);
    }

    public Map<ComponentType<? extends IComponent>, Map<ResourceLocation, NamedCodec<IComponentVariant>>> getComponentVariants() {
        ImmutableMap.Builder<ComponentType<? extends IComponent>, Map<ResourceLocation, NamedCodec<IComponentVariant>>> builder = ImmutableMap.builder();
        this.componentVariants.forEach((type, map) -> builder.put(type, ImmutableMap.copyOf(map)));
        return builder.build();
    }

    public interface Register {

        void registerComponentVariant(RegisterComponentVariantEvent event);
    }
}
