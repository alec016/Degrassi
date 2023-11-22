package es.degrassi.forge.data;

import es.degrassi.forge.Degrassi;
import es.degrassi.forge.data.lang.DegrassiESLang;
import es.degrassi.forge.data.lang.DegrassiUSLang;
import es.degrassi.forge.data.tags.DegrassiBlockTags;
import es.degrassi.forge.data.tags.DegrassiFluidTags;
import es.degrassi.forge.data.tags.DegrassiItemTags;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Degrassi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

  @SubscribeEvent
  public static void gatherData(GatherDataEvent event) {
    DataGenerator generator = event.getGenerator();
    ExistingFileHelper fileHelper = event.getExistingFileHelper();

    generator.addProvider(event.includeServer(), new DegrassiLootTableProvider(generator));
    generator.addProvider(event.includeClient(), new DegrassiBlocksStateProvider(generator, fileHelper));
    generator.addProvider(event.includeClient(), new DegrassiItemModelProvider(generator, fileHelper));
    DegrassiTagProvider.generate(event.includeServer(), generator, fileHelper);
    DegrassiLangProvider.generate(event, generator);
  }
}
