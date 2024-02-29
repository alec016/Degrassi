package es.degrassi.forge.init.registration;

import dev.architectury.registry.registries.Registrar;
import es.degrassi.forge.api.core.network.DataType;

public class DataRegistration {
  public static final Registrar<DataType<?, ?>> DATA_REGISTRY = Register.REGISTRIES.builder(DataType.REGISTRY_KEY.location(), new DataType<?, ?>[]{}).build();

}
