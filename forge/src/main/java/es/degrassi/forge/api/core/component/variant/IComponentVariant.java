package es.degrassi.forge.api.core.component.variant;

import es.degrassi.forge.Degrassi;
import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.api.impl.codec.NamedMapCodec;
import es.degrassi.forge.api.impl.codec.RegistrarCodec;
import es.degrassi.forge.api.core.component.ComponentType;
import es.degrassi.forge.api.core.component.IComponent;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;

public interface IComponentVariant {

    static <C extends IComponent> NamedMapCodec<IComponentVariant> codec(Supplier<ComponentType<C>> type){
        return RegistrarCodec.Degrassi_LOC_CODEC.dispatch("variant", IComponentVariant::getId, id -> Degrassi.getVariantCodec(type.get(), id), "Machine component variant").aliases("varient");
    }

    ResourceLocation getId();

    NamedCodec<? extends IComponentVariant> getCodec();
}
