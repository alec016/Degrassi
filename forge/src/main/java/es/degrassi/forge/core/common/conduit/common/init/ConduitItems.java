package es.degrassi.forge.core.common.conduit.common.init;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.ItemEntry;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.DegrassiForge;
import es.degrassi.common.conduit.ConduitItemFactory;
import es.degrassi.common.conduit.IConduitType;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ConduitItems {
    private static final Registrate REGISTRATE = Degrassi.registrate();

    public static final ItemEntry<Item> ENERGY = createConduitItem(EnderConduitTypes.ENERGY, "energy");
    public static final ItemEntry<Item> BASIC_FLUID = createConduitItem(EnderConduitTypes.FLUID, "basic_fluid");
    public static final ItemEntry<Item> ADVANCED_FLUID = createConduitItem(EnderConduitTypes.FLUID2, "advanced_fluid");
    public static final ItemEntry<Item> EXTREME_FLUID = createConduitItem(EnderConduitTypes.FLUID3, "extreme_fluid");
    public static final ItemEntry<Item> REDSTONE = createConduitItem(EnderConduitTypes.REDSTONE, "redstone");
    public static final ItemEntry<Item> ITEM = createConduitItem(EnderConduitTypes.ITEM, "item");

    private static ItemEntry<Item> createConduitItem(Supplier<? extends IConduitType<?>> type, String itemName) {
        return REGISTRATE.item(itemName + "_conduit",
            properties -> ConduitItemFactory.build(type, properties))
            .tab(DegrassiForge.ITEMS)
            .model((ctx, prov) -> prov.withExistingParent(itemName+"_conduit", Degrassi.rl("item/conduit")).texture("0", type.get().getItemTexture()))
            .register();
    }

    public static void register() {}
}
