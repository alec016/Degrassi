package es.degrassi.comon.init.registration;

import es.degrassi.Degrassi;

public class Registries {
  public static final dev.architectury.registry.registries.Registries REGISTRIES = dev.architectury.registry.registries.Registries.get(Degrassi.MODID);

  public static void register(){
    ElementRegistry.register();
  }
}
