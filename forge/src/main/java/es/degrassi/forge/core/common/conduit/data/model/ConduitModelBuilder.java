package es.degrassi.forge.core.common.conduit.data.model;

import es.degrassi.forge.Degrassi;
import net.minecraftforge.client.model.generators.CustomLoaderBuilder;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ConduitModelBuilder<T extends ModelBuilder<T>> extends CustomLoaderBuilder<T> {

  public static <T extends ModelBuilder<T>> ConduitModelBuilder<T> begin(T parent, ExistingFileHelper existingFileHelper) {
    return new ConduitModelBuilder<>(parent, existingFileHelper);
  }

  protected ConduitModelBuilder(T parent, ExistingFileHelper existingFileHelper) {
    super(Degrassi.rl("conduit"), parent, existingFileHelper);
  }
}
