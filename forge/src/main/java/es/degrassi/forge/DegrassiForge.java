package es.degrassi.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.core.client.DegrassiResourcePack;
import es.degrassi.forge.core.client.model.DegrassiLayerDefinition;
import es.degrassi.forge.core.client.model.SolarPanelModel;
import es.degrassi.forge.core.client.renderer.ChestEntityRenderer;
import es.degrassi.forge.core.common.conduit.DegrassiConduits;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.init.ContainerRegistration;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.init.ItemRegistration;
import es.degrassi.forge.core.init.Registration;
import es.degrassi.forge.core.tiers.Chest;
import es.degrassi.forge.core.tiers.Furnace;
import es.degrassi.forge.core.tiers.PhotovoltaicCell;
import es.degrassi.forge.core.tiers.SolarPanel;
import es.degrassi.forge.core.tiers.Storage;
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
    IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    EventBuses.registerModEventBus(Degrassi.MODID, bus);

    Degrassi.init();

    Registration.register(bus);

    EnvExecutor.runInEnv(Env.CLIENT, () -> () -> clientInit(bus));

    MinecraftForge.EVENT_BUS.register(DegrassiForge.class);
    MinecraftForge.EVENT_BUS.register(DegrassiConduits.class);
    ResourcePackAdapter.registerResourcePack(DegrassiResourcePack.getPackInstance());
  }

  public static void clientInit(IEventBus bus) {
    DegrassiLayerDefinition.register();
    bus.addListener(DegrassiForge::clientSetup);
  }

  public static void clientSetup (FMLClientSetupEvent event) {
    event.enqueueWork(ContainerRegistration::registerScreens);
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
    BlockEntityRendererRegistry.register(EntityRegistration.CHEST.get(), ChestEntityRenderer::new);
  }

  @SubscribeEvent
  public static void register (final RegisterEvent event) {
    event.register(Registries.CREATIVE_MODE_TAB, helper -> {
      helper.register(MACHINES, CreativeModeTab.builder().title(Component.translatable("degrassi.tabs.machines")).displayItems(
        (params, output) -> {
          output.accept(new ItemStack(BlockRegistration.MACHINE_CASING.get()));
          for (Furnace tier : Furnace.values()) {
            output.accept(new ItemStack(BlockRegistration.FURNACE.get(tier)));
          }
          for (Chest tier : Chest.values()) {
            output.accept(new ItemStack(BlockRegistration.CHEST.get(tier)));
          }
          for (SolarPanel tier : SolarPanel.values()) {
            output.accept(new ItemStack(BlockRegistration.SP.get(tier)));
          }
          for (Storage.Energy tier : Storage.Energy.values()) {
            output.accept(new ItemStack(BlockRegistration.ENERGY_CELL.get(tier)));
          }
          for (Storage.Fluid tier : Storage.Fluid.values()) {
            output.accept(new ItemStack(BlockRegistration.FLUID_TANK.get(tier)));
          }
        }).withSearchBar().icon(() -> new ItemStack(ItemRegistration.MACHINE_CASING.get())).build());
      helper.register(ITEMS, CreativeModeTab.builder().title(Component.translatable("degrassi.tabs.items")).displayItems(
        (params, output) -> {
          output.accept(new ItemStack(ItemRegistration.WRENCH.get()));
          output.accept(new ItemStack(ItemRegistration.BOOK.get()));
          output.accept(new ItemStack(ItemRegistration.RED_MATTER.get()));
          output.accept(new ItemStack(ItemRegistration.BLACK_PEARL.get()));
          for (PhotovoltaicCell cell : PhotovoltaicCell.values()) {
            output.accept(new ItemStack(ItemRegistration.PHOTOVOLTAIC_CELL.get(cell)));
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
      for (Furnace tier : Furnace.values()) {
        entries.put(new ItemStack(BlockRegistration.FURNACE.get(tier)), vis);
      }
      for (SolarPanel tier : SolarPanel.values()) {
        entries.put(new ItemStack(BlockRegistration.SP.get(tier)), vis);
      }
      for (Chest tier : Chest.values()) {
        entries.put(new ItemStack(BlockRegistration.CHEST.get(tier)), vis);
      }
      for (Storage.Energy tier : Storage.Energy.values()) {
        entries.put(new ItemStack(BlockRegistration.ENERGY_CELL.get(tier)), vis);
      }
      for (Storage.Fluid tier : Storage.Fluid.values()) {
        entries.put(new ItemStack(BlockRegistration.FLUID_TANK.get(tier)), vis);
      }
    } else if (event.getTabKey() == ITEMS) {
      entries.put(new ItemStack(ItemRegistration.WRENCH.get()), vis);
      entries.put(new ItemStack(ItemRegistration.BOOK.get()), vis);
      entries.put(new ItemStack(ItemRegistration.RED_MATTER.get()), vis);
      entries.put(new ItemStack(ItemRegistration.BLACK_PEARL.get()), vis);
      for (PhotovoltaicCell cell : PhotovoltaicCell.values()) {
        entries.put(new ItemStack(ItemRegistration.PHOTOVOLTAIC_CELL.get(cell)), vis);
      }
    }
  }
}