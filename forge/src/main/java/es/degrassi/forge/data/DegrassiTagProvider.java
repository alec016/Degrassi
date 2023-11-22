package es.degrassi.forge.data;

import es.degrassi.forge.Degrassi;
import es.degrassi.forge.data.tags.DegrassiBlockTags;
import es.degrassi.forge.data.tags.DegrassiFluidTags;
import es.degrassi.forge.data.tags.DegrassiItemTags;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.TagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class DegrassiTagProvider<T> extends TagsProvider<T> {
  protected DegrassiTagProvider(DataGenerator arg, Registry<T> arg2, @Nullable ExistingFileHelper existingFileHelper) {
    super(arg, arg2, Degrassi.MODID, existingFileHelper);
  }

  public static void generate(boolean includeServer, @NotNull DataGenerator generator, ExistingFileHelper fileHelper) {
    generator.addProvider(includeServer, new DegrassiBlockTags(generator, fileHelper));
    generator.addProvider(includeServer, new DegrassiItemTags(generator, fileHelper));
    generator.addProvider(includeServer, new DegrassiFluidTags(generator, fileHelper));
  }
}
