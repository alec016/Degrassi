package es.degrassi.forge.data;

import es.degrassi.forge.Degrassi;
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

//    generator.addProvider(true, new DegrassiRecipeProvider(generator));
    generator.addProvider(true, new DegrassiLootTableProvider(generator));
    generator.addProvider(true, new DegrassiBlocksStateProvider(generator, fileHelper));
    generator.addProvider(true, new DegrassiItemModelProvider(generator, fileHelper));
  }
}
