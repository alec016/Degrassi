package es.degrassi.forge;

import dev.architectury.platform.forge.EventBuses;
import es.degrassi.forge.init.entity.renderer.MelterRenderer;
import es.degrassi.forge.init.registration.EntityRegister;
import es.degrassi.forge.init.registration.Register;
import es.degrassi.forge.util.DegrassiLogger;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static es.degrassi.forge.integration.config.DegrassiConfig.SPEC;

@Mod(Degrassi.MODID)
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = Degrassi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DegrassiForge {

  public DegrassiForge() {
    ModLoadingContext.get().registerConfig(Type.COMMON, SPEC, "degrassi.toml");
    EventBuses.registerModEventBus(Degrassi.MODID, FMLJavaModLoadingContext.get().getModEventBus());
    Degrassi.init();
    Register.register();

    MinecraftForge.EVENT_BUS.register(DegrassiForge.class);
  }


  @SubscribeEvent
  public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event){
    DegrassiLogger.INSTANCE.info("registering melter renderer");
    event.registerBlockEntityRenderer(EntityRegister.MELTER.get(), MelterRenderer::new);
  }
}