package es.degrassi.forge.requirements;

import es.degrassi.forge.Degrassi;
import es.degrassi.forge.api.codec.NamedCodec;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class RequirementType<T extends IRequirement<?>> {

    public static final ResourceKey<Registry<RequirementType<? extends IRequirement<?>>>> REGISTRY_KEY = ResourceKey.createRegistryKey(Degrassi.rl("requirement_type"));

    public static <T extends IRequirement<?>> RequirementType<T> world(NamedCodec<T> codec) {
        return new RequirementType<>(codec, true);
    }

    public static <T extends IRequirement<?>> RequirementType<T> inventory(NamedCodec<T> codec) {
        return new RequirementType<>(codec, false);
    }

    private final NamedCodec<T> codec;
    private final boolean isWorldRequirement;

    private RequirementType(NamedCodec<T> codec, boolean isWorldRequirement) {
        this.codec = codec;
        this.isWorldRequirement = isWorldRequirement;
    }

    public NamedCodec<T> getCodec() {
        return this.codec;
    }

    public boolean isWorldRequirement() {
        return isWorldRequirement;
    }

    public ResourceLocation getId() {
        return Degrassi.requirementRegistrar().getId(this);
    }

    /**
     * Used to display the name of this requirement to the player, either in a gui or in the log.
     * @return A text component representing the name of this requirement.
     */
    public Component getName() {
        if(getId() == null)
            return Component.literal("unknown");
        return Component.translatable("requirement." + getId().getNamespace() + "." + getId().getPath());
    }
}
