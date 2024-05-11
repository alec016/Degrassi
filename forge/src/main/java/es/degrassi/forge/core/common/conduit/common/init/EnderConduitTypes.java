package es.degrassi.forge.core.common.conduit.common.init;

import es.degrassi.forge.Degrassi;
import es.degrassi.common.conduit.ConduitTypes;
import es.degrassi.common.conduit.IConduitType;
import es.degrassi.common.misc.Vector2i;
import es.degrassi.forge.core.common.conduit.common.types.energy.EnergyConduitType;
import es.degrassi.forge.core.common.conduit.common.types.fluid.FluidConduitType;
import es.degrassi.forge.core.common.conduit.common.types.heat.HeatConduitType;
import es.degrassi.forge.core.common.conduit.common.types.item.ItemConduitType;
import es.degrassi.forge.core.common.conduit.common.types.redstone.RedstoneConduitType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

public class EnderConduitTypes {

    public static final ResourceLocation ICON_TEXTURE = Degrassi.rl("textures/gui/conduit_icon.png");
    public static final RegistryObject<? extends IConduitType<?>> ENERGY = ConduitTypes.CONDUIT_TYPES.register("energy_conduit", EnergyConduitType::new);
    public static final RegistryObject<FluidConduitType> FLUID = fluidConduit("basic_fluid_conduit", 100, false, new Vector2i(0, 120));
    public static final RegistryObject<FluidConduitType> FLUID2 = fluidConduit("advanced_fluid_conduit", 1_000, false, new Vector2i(0, 144));
    public static final RegistryObject<FluidConduitType> FLUID3 = fluidConduit("extreme_fluid_conduit", 10_000, true, new Vector2i(0, 168));
    public static final RegistryObject<? extends IConduitType<?>> REDSTONE = ConduitTypes.CONDUIT_TYPES.register("redstone_conduit", RedstoneConduitType::new);
    public static final RegistryObject<? extends IConduitType<?>> ITEM = ConduitTypes.CONDUIT_TYPES.register("item_conduit", ItemConduitType::new);
    public static final RegistryObject<? extends IConduitType<?>> HEAT = ConduitTypes.CONDUIT_TYPES.register("heat_conduit", HeatConduitType::new);

    private static RegistryObject<FluidConduitType> fluidConduit(String name, int tier, boolean isMultiFluid, Vector2i iconPos) {
        return ConduitTypes.CONDUIT_TYPES.register(name,
            () -> new FluidConduitType(Degrassi.rl("block/conduit/" + name), tier, isMultiFluid, ICON_TEXTURE, iconPos));
    }

    public static void register() {}
}
