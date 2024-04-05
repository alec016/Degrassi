package es.degrassi.forge.core.common.conduit.common.init;

import es.degrassi.forge.Degrassi;
import net.minecraft.network.chat.Component;

public class ConduitLang {
    public static final Component CONDUIT_INSERT = Component.translatable("gui." + Degrassi.MODID + ".conduit.insert");
    public static final Component CONDUIT_EXTRACT = Component.translatable("gui." + Degrassi.MODID + ".conduit.extract");

    public static void register() {
    }
}
