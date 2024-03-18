package es.degrassi.forge;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.platform.forge.EventBuses;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.core.client.DegrassiResourcePack;
import es.degrassi.forge.core.client.model.SolarPanelModel;
import es.degrassi.forge.core.client.model.loader.CableModelLoader;
import es.degrassi.forge.core.common.cables.block.FacadeBlockColor;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.init.ContainerRegistration;
import es.degrassi.forge.core.init.ItemRegistration;
import es.degrassi.forge.core.init.Registration;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.zeith.hammerlib.client.adapter.ResourcePackAdapter;

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
    ResourcePackAdapter.registerResourcePack(DegrassiResourcePack.getPackInstance());
  }

  public static void clientInit() {
    LifecycleEvent.SETUP.register(ContainerRegistration::registerScreens);
  }

  @SubscribeEvent
  public static void clientSetup (FMLClientSetupEvent event) {
    event.enqueueWork(DegrassiForge::registerRenderers);
  }

  @SubscribeEvent
  public static void registerLoaders(ModelEvent.RegisterGeometryLoaders event) {
    CableModelLoader.register(event);
  }

  @SubscribeEvent
  public static void registerBlockColor(RegisterColorHandlersEvent.Block event) {
    event.register(new FacadeBlockColor(), BlockRegistration.CABLE_FACADE.get());
  }

  @SubscribeEvent
  @OnlyIn(Dist.CLIENT)
  public static void modelBake(ModelEvent.BakingCompleted event) {
    BlockRegistration.listPanels().forEach(panel ->
      event.getModelManager().bakedRegistry.put(new ModelResourceLocation(panel.getId(), ""), new SolarPanelModel(panel.get()))
    );
  }

  public static void registerRenderers() {
  }

  @SubscribeEvent
  public static void register (final RegisterEvent event) {
    event.register(Registries.CREATIVE_MODE_TAB, helper ->
      helper.register(MACHINES, CreativeModeTab.builder().title(Component.translatable("degrassi.tabs.machines")).displayItems((params, output) -> {
        output.accept(new ItemStack(ItemRegistration.IRON_FURNACE.get()));
        output.accept(new ItemStack(ItemRegistration.GOLD_FURNACE.get()));
        output.accept(new ItemStack(ItemRegistration.DIAMOND_FURNACE.get()));
        output.accept(new ItemStack(ItemRegistration.EMERALD_FURNACE.get()));
        output.accept(new ItemStack(ItemRegistration.NETHERITE_FURNACE.get()));
        output.accept(new ItemStack(ItemRegistration.SP1.get()));
        output.accept(new ItemStack(ItemRegistration.SP2.get()));
        output.accept(new ItemStack(ItemRegistration.SP3.get()));
        output.accept(new ItemStack(ItemRegistration.SP4.get()));
        output.accept(new ItemStack(ItemRegistration.SP5.get()));
        output.accept(new ItemStack(ItemRegistration.SP6.get()));
        output.accept(new ItemStack(ItemRegistration.SP7.get()));
        output.accept(new ItemStack(ItemRegistration.SP8.get()));
        output.accept(new ItemStack(ItemRegistration.CABLE_FACADE.get()));
        output.accept(new ItemStack(ItemRegistration.BASIC_ENERGY_CABLE.get()));
        output.accept(new ItemStack(ItemRegistration.ADVANCE_ENERGY_CABLE.get()));
        output.accept(new ItemStack(ItemRegistration.EXTREME_ENERGY_CABLE.get()));
    }).withSearchBar().icon(() -> new ItemStack(ItemRegistration.IRON_FURNACE.get())).build()));
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
      entries.put(new ItemStack(ItemRegistration.SP1.get()), vis);
      entries.put(new ItemStack(ItemRegistration.SP2.get()), vis);
      entries.put(new ItemStack(ItemRegistration.SP3.get()), vis);
      entries.put(new ItemStack(ItemRegistration.SP4.get()), vis);
      entries.put(new ItemStack(ItemRegistration.SP5.get()), vis);
      entries.put(new ItemStack(ItemRegistration.SP6.get()), vis);
      entries.put(new ItemStack(ItemRegistration.SP7.get()), vis);
      entries.put(new ItemStack(ItemRegistration.SP8.get()), vis);
      entries.put(new ItemStack(ItemRegistration.CABLE_FACADE.get()), vis);
      entries.put(new ItemStack(ItemRegistration.BASIC_ENERGY_CABLE.get()), vis);
      entries.put(new ItemStack(ItemRegistration.ADVANCE_ENERGY_CABLE.get()), vis);
      entries.put(new ItemStack(ItemRegistration.EXTREME_ENERGY_CABLE.get()), vis);
    }
  }
}