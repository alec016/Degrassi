package es.degrassi.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import es.degrassi.common.DegrassiLocation;
import es.degrassi.forge.core.client.DegrassiResourcePack;
import es.degrassi.forge.core.client.model.DegrassiLayerDefinition;
import es.degrassi.forge.core.client.model.SolarPanelModel;
import es.degrassi.forge.core.client.renderer.ChestEntityRenderer;
import es.degrassi.forge.core.common.conduit.DegrassiConduits;
import es.degrassi.forge.core.common.storage.fluid.entity.renderer.FluidTankRenderer;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.init.ContainerRegistration;
import es.degrassi.forge.core.init.EntityRegistration;
import es.degrassi.forge.core.init.ItemRegistration;
import es.degrassi.forge.core.init.Registration;
import es.degrassi.forge.core.tiers.Chest;
import es.degrassi.forge.core.tiers.Furnace;
import es.degrassi.forge.core.tiers.MultiblockPartStorage;
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
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.client.adapter.ResourcePackAdapter;

@Mod(Degrassi.MODID)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Degrassi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DegrassiForge {
  public static final ResourceKey<CreativeModeTab> MACHINES = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new DegrassiLocation("machines"));
  public static final ResourceKey<CreativeModeTab> DIGITAL = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new DegrassiLocation("digital"));
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
//    MinecraftForge.EVENT_BUS.addGenericListener(BlockEntity.class, this::attachBeCaps);
  }

  public static void clientInit(final IEventBus bus) {
    DegrassiLayerDefinition.register();
    bus.addListener(DegrassiForge::clientSetup);
  }

  public static void clientSetup (final FMLClientSetupEvent event) {
    event.enqueueWork(ContainerRegistration::registerScreens);
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

  @SubscribeEvent
  public static void registerRenderers(final EntityRenderersEvent.@NotNull RegisterRenderers event) {
    event.registerBlockEntityRenderer(EntityRegistration.CHEST.get(), ChestEntityRenderer::new);
    event.registerBlockEntityRenderer(EntityRegistration.FLUID_TANK.get(), FluidTankRenderer::new);
  }

  @SubscribeEvent
  public static void register (final @NotNull RegisterEvent event) {
    event.register(Registries.CREATIVE_MODE_TAB, helper -> {
      helper.register(MACHINES, CreativeModeTab.builder().title(Component.translatable("degrassi.tabs.machines")).displayItems(
        (params, output) -> {
          output.accept(new ItemStack(BlockRegistration.MACHINE_CASING.get()));
          output.accept(new ItemStack(BlockRegistration.MELTER_FRAME.get()));
          output.accept(new ItemStack(BlockRegistration.MELTER_CONTROLLER.get()));
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
          for (MultiblockPartStorage.Energy variant : MultiblockPartStorage.Energy.values()) {
            output.accept(new ItemStack(BlockRegistration.ENERGY_HATCH.get(variant)));
          }
          for (MultiblockPartStorage.Fluid.Input variant : MultiblockPartStorage.Fluid.Input.values()) {
            output.accept(new ItemStack(BlockRegistration.FLUID_INPUT_TANK.get(variant)));
          }
          for (MultiblockPartStorage.Fluid.QuadrupleInput variant : MultiblockPartStorage.Fluid.QuadrupleInput.values()) {
            output.accept(new ItemStack(BlockRegistration.QUADRUPLE_FLUID_INPUT_TANK.get(variant)));
          }
          for (MultiblockPartStorage.Fluid.Output variant : MultiblockPartStorage.Fluid.Output.values()) {
            output.accept(new ItemStack(BlockRegistration.FLUID_OUTPUT_TANK.get(variant)));
          }
          for (MultiblockPartStorage.Item.Input variant : MultiblockPartStorage.Item.Input.values()) {
            output.accept(new ItemStack(BlockRegistration.INPUT_BUS.get(variant)));
          }
          for (MultiblockPartStorage.Item.Output variant : MultiblockPartStorage.Item.Output.values()) {
            output.accept(new ItemStack(BlockRegistration.OUTPUT_BUS.get(variant)));
          }
        }).icon(() -> new ItemStack(ItemRegistration.MACHINE_CASING.get())).build());

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
      ).icon(() -> new ItemStack(ItemRegistration.RED_MATTER.get())).build());

      helper.register(DIGITAL, CreativeModeTab.builder().title(Component.translatable("degrassi.tabs.digital")).displayItems(
        (params, output) -> {
          output.accept(new ItemStack(BlockRegistration.DIGITAL_CONTROLLER.get()));
        }
      ).icon(() -> new ItemStack(ItemRegistration.DIGITAL_CONTROLLER.get())).build());
    });
  }
}