package es.degrassi.forge.api.core.component.variant;

import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.core.component.ComponentType;
import es.degrassi.forge.api.core.component.IComponent;
import java.util.Collections;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ComponentVariantRegistry {

    private static Map<ComponentType<? extends IComponent>, Map<ResourceLocation, NamedCodec<IComponentVariant>>> map = Collections.emptyMap();

    public static void init() {
        RegisterComponentVariantEvent event = new RegisterComponentVariantEvent();
        RegisterComponentVariantEvent.EVENT.invoker().registerComponentVariant(event);
        map = event.getComponentVariants();
    }

    @Nullable
    public static <C extends IComponent> NamedCodec<IComponentVariant> getVariantCodec(ComponentType<C> type, ResourceLocation id) {
        Map<ResourceLocation, NamedCodec<IComponentVariant>> variantsMap = map.get(type);
        if(variantsMap == null)
            return null;
        return variantsMap.get(id);
    }
}
