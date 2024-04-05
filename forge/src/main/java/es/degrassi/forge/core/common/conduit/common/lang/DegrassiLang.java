package es.degrassi.forge.core.common.conduit.common.lang;

import es.degrassi.forge.Degrassi;
import es.degrassi.common.misc.ApiLang;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class DegrassiLang {
    private static final String mod = Degrassi.MODID;

    public static final Component CONDUIT_CHANNEL = Component.translatable("gui." + mod + ".conduit_channel", "Conduit-Channel");
    public static final Component REDSTONE_CHANNEL = Component.translatable("gui." + mod + ".redstone_channel");
    public static final Component REDSTONE_MODE = Component.translatable("gui." + mod + ".redstone.mode");
    public static final Component REDSTONE_ALWAYS_ACTIVE = Component.translatable("gui." + mod + ".redstone.always_active");
    public static final Component REDSTONE_ACTIVE_WITH_SIGNAL = Component.translatable("gui." + mod + ".redstone.active_with_signal");
    public static final Component REDSTONE_ACTIVE_WITHOUT_SIGNAL = Component.translatable("gui." + mod + ".redstone.active_without_signal");
    public static final Component REDSTONE_NEVER_ACTIVE = Component.translatable("gui." + mod + ".redstone.never_active");
    public static final Component ROUND_ROBIN_ENABLED = Component.translatable("gui." + mod + ".round_robin.enabled");
    public static final Component ROUND_ROBIN_DISABLED = Component.translatable("gui." + mod + ".round_robin.disabled");
    public static final Component SELF_FEED_ENABLED = Component.translatable("gui." + mod + ".self_feed.enabled");
    public static final Component SELF_FEED_DISABLED = Component.translatable("gui." + mod + ".self_feed.disabled");
    public static final Component FLUID_CONDUIT_CHANGE_FLUID1 = Component.translatable("gui." + mod + ".fluid_conduit.change_fluid1");
    public static final Component FLUID_CONDUIT_CHANGE_FLUID2 = Component.translatable("gui." + mod + ".fluid_conduit.change_fluid2");
    public static final MutableComponent FLUID_CONDUIT_CHANGE_FLUID3 = Component.translatable("gui." + mod + ".fluid_conduit.change_fluid3");

    public static void register() {
        ApiLang.REDSTONE_ACTIVE_WITH_SIGNAL = REDSTONE_ACTIVE_WITH_SIGNAL;
        ApiLang.REDSTONE_NEVER_ACTIVE = REDSTONE_NEVER_ACTIVE;
        ApiLang.REDSTONE_ALWAYS_ACTIVE = REDSTONE_ALWAYS_ACTIVE;
        ApiLang.REDSTONE_ACTIVE_WITHOUT_SIGNAL = REDSTONE_ACTIVE_WITHOUT_SIGNAL;
    }
}
