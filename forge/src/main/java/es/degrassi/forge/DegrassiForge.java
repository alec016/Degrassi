package es.degrassi.forge;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.platform.forge.EventBuses;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.core.client.DegrassiResourcePack;
import es.degrassi.forge.core.client.model.DegrassiLayerDefinition;
import es.degrassi.forge.core.client.model.SolarPanelModel;
import es.degrassi.forge.core.client.renderer.EnergyCableRenderer;
import es.degrassi.forge.core.client.renderer.FluidCableRenderer;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.init.ContainerRegistration;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.init.ItemRegistration;
import es.degrassi.forge.core.init.Registration;
import es.degrassi.forge.core.tiers.CableTier;
import es.degrassi.forge.core.tiers.SolarPanel;
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
  public static final ResourceKey<CreativeModeTab> ITEMS = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new DegrassiLocation("items"));

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
    DegrassiLayerDefinition.register();
  }

  @SubscribeEvent
  public static void clientSetup (FMLClientSetupEvent event) {
    event.enqueueWork(DegrassiForge::registerRenderers);
  }

  @SubscribeEvent
  public static void registerLoaders(ModelEvent.RegisterGeometryLoaders event) {
  }

  @SubscribeEvent
  public static void registerBlockColor(RegisterColorHandlersEvent.Block event) {
  }

  @SubscribeEvent
  @OnlyIn(Dist.CLIENT)
  public static void modelBake(ModelEvent.BakingCompleted event) {
    BlockRegistration.listPanels().forEach(panel ->
      event.getModelManager().bakedRegistry.put(
        new ModelResourceLocation(new DegrassiLocation("sp_" + panel.getVariant().getName()), ""), new SolarPanelModel(panel)
      )
    );
  }

  public static void registerRenderers() {
    BlockEntityRendererRegistry.register(EntityRegistration.ENERGY_CABLE.get(), EnergyCableRenderer::new);
    BlockEntityRendererRegistry.register(EntityRegistration.FLUID_CABLE.get(), FluidCableRenderer::new);
  }

  @SubscribeEvent
  public static void register (final RegisterEvent event) {
    event.register(Registries.CREATIVE_MODE_TAB, helper -> {
      helper.register(MACHINES, CreativeModeTab.builder().title(Component.translatable("degrassi.tabs.machines")).displayItems(
        (params, output) -> {
          output.accept(new ItemStack(BlockRegistration.MACHINE_CASING.get()));
          output.accept(new ItemStack(BlockRegistration.IRON_FURNACE.get()));
          output.accept(new ItemStack(BlockRegistration.GOLD_FURNACE.get()));
          output.accept(new ItemStack(BlockRegistration.DIAMOND_FURNACE.get()));
          output.accept(new ItemStack(BlockRegistration.EMERALD_FURNACE.get()));
          output.accept(new ItemStack(BlockRegistration.NETHERITE_FURNACE.get()));
          for (SolarPanel tier : SolarPanel.values()) {
            output.accept(new ItemStack(BlockRegistration.SP.get(tier)));
          }
        }).withSearchBar().icon(() -> new ItemStack(ItemRegistration.MACHINE_CASING.get())).build());
      helper.register(ITEMS, CreativeModeTab.builder().title(Component.translatable("degrassi.tabs.items")).displayItems(
        (params, output) -> {
          output.accept(new ItemStack(ItemRegistration.WRENCH.get()));
          output.accept(new ItemStack(ItemRegistration.BOOK.get()));
          output.accept(new ItemStack(ItemRegistration.RED_MATTER.get()));
          output.accept(new ItemStack(ItemRegistration.BLACK_PEARL.get()));
          output.accept(new ItemStack(ItemRegistration.PHOTOVOLTAIC_CELL_I.get()));
          output.accept(new ItemStack(ItemRegistration.PHOTOVOLTAIC_CELL_II.get()));
          output.accept(new ItemStack(ItemRegistration.PHOTOVOLTAIC_CELL_III.get()));
          output.accept(new ItemStack(ItemRegistration.PHOTOVOLTAIC_CELL_IV.get()));
          output.accept(new ItemStack(ItemRegistration.PHOTOVOLTAIC_CELL_V.get()));
          output.accept(new ItemStack(ItemRegistration.PHOTOVOLTAIC_CELL_VI.get()));
          output.accept(new ItemStack(ItemRegistration.PHOTOVOLTAIC_CELL_VII.get()));
          output.accept(new ItemStack(ItemRegistration.PHOTOVOLTAIC_CELL_VIII.get()));
          for (CableTier tier : CableTier.values()) {
            output.accept(new ItemStack(BlockRegistration.ENERGY_CABLE.get(tier)));
            output.accept(new ItemStack(BlockRegistration.FLUID_CABLE.get(tier)));
          }
        }
      ).withSearchBar().icon(() -> new ItemStack(ItemRegistration.RED_MATTER.get())).build());
    });
  }

  @SubscribeEvent
  public static void registerTab(final BuildCreativeModeTabContentsEvent event) {
    var entries = event.getEntries();
    var vis = CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;
    if (event.getTabKey() == MACHINES) {
      entries.put(new ItemStack(ItemRegistration.MACHINE_CASING.get()), vis);
      entries.put(new ItemStack(ItemRegistration.IRON_FURNACE.get()), vis);
      entries.put(new ItemStack(ItemRegistration.GOLD_FURNACE.get()), vis);
      entries.put(new ItemStack(ItemRegistration.DIAMOND_FURNACE.get()), vis);
      entries.put(new ItemStack(ItemRegistration.EMERALD_FURNACE.get()), vis);
      entries.put(new ItemStack(ItemRegistration.NETHERITE_FURNACE.get()), vis);
      for (SolarPanel tier : SolarPanel.values()) {
        entries.put(new ItemStack(BlockRegistration.SP.get(tier)), vis);
      }
    } else if (event.getTabKey() == ITEMS) {
      entries.put(new ItemStack(ItemRegistration.WRENCH.get()), vis);
      entries.put(new ItemStack(ItemRegistration.BOOK.get()), vis);
      entries.put(new ItemStack(ItemRegistration.RED_MATTER.get()), vis);
      entries.put(new ItemStack(ItemRegistration.BLACK_PEARL.get()), vis);
      entries.put(new ItemStack(ItemRegistration.PHOTOVOLTAIC_CELL_I.get()), vis);
      entries.put(new ItemStack(ItemRegistration.PHOTOVOLTAIC_CELL_II.get()), vis);
      entries.put(new ItemStack(ItemRegistration.PHOTOVOLTAIC_CELL_III.get()), vis);
      entries.put(new ItemStack(ItemRegistration.PHOTOVOLTAIC_CELL_IV.get()), vis);
      entries.put(new ItemStack(ItemRegistration.PHOTOVOLTAIC_CELL_V.get()), vis);
      entries.put(new ItemStack(ItemRegistration.PHOTOVOLTAIC_CELL_VI.get()), vis);
      entries.put(new ItemStack(ItemRegistration.PHOTOVOLTAIC_CELL_VII.get()), vis);
      entries.put(new ItemStack(ItemRegistration.PHOTOVOLTAIC_CELL_VIII.get()), vis);
      for (CableTier tier : CableTier.values()) {
        entries.put(new ItemStack(BlockRegistration.ENERGY_CABLE.get(tier)), vis);
        entries.put(new ItemStack(BlockRegistration.FLUID_CABLE.get(tier)), vis);
      }
    }
  }
}