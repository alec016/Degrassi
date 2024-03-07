package es.degrassi.forge.api.core.common;

import dev.architectury.registry.registries.Registrar;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.api.codec.NamedCodec;
import es.degrassi.forge.core.init.RequirementRegistration;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class RequirementType<T extends IRequirement<?>> {
  public static final ResourceKey<Registry<RequirementType<? extends IRequirement<?>>>> REGISTRY_KEY = ResourceKey.createRegistryKey(new DegrassiLocation("requirement_type"));

  private final NamedCodec<T> codec;

  public RequirementType(NamedCodec<T> codec) {
    this.codec = codec;
  }

  public NamedCodec<T> getCodec() {
    return codec;
  }

  public ResourceLocation getId () {
    return requirementRegistrar().getId(this);
  }

  public Component getName() {
    if (getId() == null) return Component.literal("unknown");
    return Component.translatable("requirement" + getId().getNamespace() + "." + getId().getPath());
  }
  public static Registrar<RequirementType<?>> requirementRegistrar() {
    return RequirementRegistration.REQUIREMENT_TYPE_REGISTRY;
  }
}
