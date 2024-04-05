package es.degrassi.forge.core.common.conduit;

import es.degrassi.forge.Degrassi;
import es.degrassi.common.conduit.ConduitItemFactory;
import es.degrassi.common.conduit.ConduitTypes;
import es.degrassi.forge.core.common.conduit.common.init.ConduitBlockEntities;
import es.degrassi.forge.core.common.conduit.common.init.ConduitBlocks;
import es.degrassi.forge.core.common.conduit.common.init.ConduitItems;
import es.degrassi.forge.core.common.conduit.common.init.ConduitLang;
import es.degrassi.forge.core.common.conduit.common.init.ConduitMenus;
import es.degrassi.forge.core.common.conduit.common.init.EnderConduitTypes;
import es.degrassi.forge.core.common.conduit.common.integrations.Integrations;
import es.degrassi.forge.core.common.conduit.common.items.ConduitBlockItem;
import es.degrassi.forge.core.common.conduit.common.lang.DegrassiLang;
import es.degrassi.forge.core.common.conduit.common.network.ConduitNetwork;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber(modid = Degrassi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DegrassiConduits {

  @SubscribeEvent
  public static void onConstruct(FMLConstructModEvent event) {
    System.out.println("================ Cables construct ==================");
    IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
    ConduitTypes.register(bus);
    EnderConduitTypes.register();
    ConduitBlockEntities.register();
    ConduitMenus.register();
    ConduitBlocks.register();
    ConduitItems.register();
    Integrations.register();
    ConduitNetwork.register();
    DegrassiLang.register();
    ConduitLang.register();
    ConduitItemFactory.setFactory((type, properties) -> new ConduitBlockItem(type, ConduitBlocks.CONDUIT.get(), properties));
  }
}
