package es.degrassi.forge;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.platform.forge.EventBuses;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.core.init.ContainerRegistration;
import es.degrassi.forge.core.init.ItemRegistration;
import es.degrassi.forge.core.init.Registration;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;

@Mod(Degrassi.MODID)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Degrassi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DegrassiForge {
  public static final ResourceKey<CreativeModeTab> MACHINES = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new DegrassiLocation("machines"));

  public DegrassiForge() {
    EventBuses.registerModEventBus(Degrassi.MODID, FMLJavaModLoadingContext.get().getModEventBus());

    Degrassi.init();
    IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

    Registration.register(bus);

    EnvExecutor.runInEnv(Env.CLIENT, () -> DegrassiForge::clientInit);

    MinecraftForge.EVENT_BUS.register(DegrassiForge.class);
  }

  public static void clientInit() {
    LifecycleEvent.SETUP.register(ContainerRegistration::registerScreens);
  }

  @SubscribeEvent
  public static void registerRenderers(final EntityRenderersEvent.@NotNull RegisterRenderers event){
  }

  @SubscribeEvent
  public static void register (final RegisterEvent event) {
    event.register(Registries.CREATIVE_MODE_TAB, helper -> {
      helper.register(MACHINES, CreativeModeTab.builder().title(Component.translatable("degrassi.tabs.machines")).displayItems((params, output) -> {
        output.accept(new ItemStack(ItemRegistration.IRON_FURNACE.get()));
        output.accept(new ItemStack(ItemRegistration.GOLD_FURNACE.get()));
        output.accept(new ItemStack(ItemRegistration.DIAMOND_FURNACE.get()));
        output.accept(new ItemStack(ItemRegistration.EMERALD_FURNACE.get()));
        output.accept(new ItemStack(ItemRegistration.NETHERITE_FURNACE.get()));
      }).withSearchBar().icon(() -> new ItemStack(ItemRegistration.IRON_FURNACE.get())).build());
    });
  }

  @SubscribeEvent
  public static void registerTab(final BuildCreativeModeTabContentsEvent event) {
    var entries = event.getEntries();
    var vis = CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;
    if (event.getTabKey() == MACHINES) {
      entries.put(new ItemStack(ItemRegistration.IRON_FURNACE.get()), vis);
      entries.put(new ItemStack(ItemRegistration.GOLD_FURNACE.get()), vis);
      entries.put(new ItemStack(ItemRegistration.DIAMOND_FURNACE.get()), vis);
      entries.put(new ItemStack(ItemRegistration.EMERALD_FURNACE.get()), vis);
      entries.put(new ItemStack(ItemRegistration.NETHERITE_FURNACE.get()), vis);
    }
  }
}