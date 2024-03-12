package es.degrassi.forge.core.data;

import es.degrassi.forge.Degrassi;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Degrassi.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

  @SubscribeEvent
  public static void gatherData(GatherDataEvent event) {
    DataGenerator generator = event.getGenerator();
    PackOutput packOutput = generator.getPackOutput();
    ExistingFileHelper fileHelper = event.getExistingFileHelper();
    CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

    generator.addProvider(event.includeServer(), new DegrassiRecipeProvider(packOutput));
    generator.addProvider(event.includeServer(), DegrassiLootTableProvider.create(packOutput));

    generator.addProvider(event.includeClient(), new DegrassiBlockStateProvider(packOutput, fileHelper));
    generator.addProvider(event.includeClient(), new DegrassiItemModelProvider(packOutput, fileHelper));

    DegrassiBlockTagProvider blockTagProvider = generator.addProvider(
      event.includeServer(),
      new DegrassiBlockTagProvider(packOutput, lookupProvider, fileHelper)
    );
    generator.addProvider(
      event.includeServer(),
      new DegrassiItemTagProvider(packOutput, lookupProvider, blockTagProvider.contentsGetter(), fileHelper)
    );
  }
}
