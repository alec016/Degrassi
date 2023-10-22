package es.degrassi.forge;

import dev.architectury.platform.forge.EventBuses;
import es.degrassi.Degrassi;
import es.degrassi.forge.init.registration.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static es.degrassi.forge.integration.config.DegrassiConfig.SPEC;

@Mod(Degrassi.MODID)
public class DegrassiForge {

  public DegrassiForge() {
    ModLoadingContext.get().registerConfig(Type.COMMON, SPEC, "degrassi.toml");
    EventBuses.registerModEventBus(Degrassi.MODID, FMLJavaModLoadingContext.get().getModEventBus());
    Degrassi.init();
    Register.register();
  }

  /**
   * Registering logger after config setup
   */
  @SubscribeEvent
  public void onInit (FMLLoadCompleteEvent event) {
  }
}