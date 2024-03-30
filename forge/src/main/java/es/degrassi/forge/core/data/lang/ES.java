package es.degrassi.forge.core.data.lang;

import es.degrassi.forge.core.data.DegrassiLangProvider;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.init.ItemRegistration;
import es.degrassi.forge.core.tiers.CableTier;
import es.degrassi.forge.core.tiers.SolarPanel;
import net.minecraft.data.PackOutput;

public class ES extends DegrassiLangProvider {
  public ES(PackOutput output) {
    super(output, "es_es");
  }

  @Override
  protected void addItems() {
    addItem(ItemRegistration.WRENCH, "Wrench");

    addItem(ItemRegistration.BOOK, "Degrassi Manual");
    addItem(ItemRegistration.RED_MATTER, "Red Matter");
    addItem(ItemRegistration.BLACK_PEARL, "Black Pearl");
    addItem(ItemRegistration.PHOTOVOLTAIC_CELL_I, "Photovoltaic Cell I");
    addItem(ItemRegistration.PHOTOVOLTAIC_CELL_II, "Photovoltaic Cell II");
    addItem(ItemRegistration.PHOTOVOLTAIC_CELL_III, "Photovoltaic Cell III");
    addItem(ItemRegistration.PHOTOVOLTAIC_CELL_IV, "Photovoltaic Cell IV");
    addItem(ItemRegistration.PHOTOVOLTAIC_CELL_V, "Photovoltaic Cell V");
    addItem(ItemRegistration.PHOTOVOLTAIC_CELL_VI, "Photovoltaic Cell VI");
    addItem(ItemRegistration.PHOTOVOLTAIC_CELL_VII, "Photovoltaic Cell VII");
    addItem(ItemRegistration.PHOTOVOLTAIC_CELL_VIII, "Photovoltaic Cell VIII");
  }

  @Override
  protected void addBlocks() {
    addBlock(BlockRegistration.MACHINE_CASING, "Machine Casing");
    addBlock(BlockRegistration.IRON_FURNACE, "Horno de Hierro");
    addBlock(BlockRegistration.GOLD_FURNACE, "Horno de Oro");
    addBlock(BlockRegistration.DIAMOND_FURNACE, "Horno de Diamante");
    addBlock(BlockRegistration.EMERALD_FURNACE, "Horno de Esmeralda");
    addBlock(BlockRegistration.NETHERITE_FURNACE, "Horno de Netherite");

    addBlock(() -> BlockRegistration.SP.get(SolarPanel.T1), "Panel Solar I");
    addBlock(() -> BlockRegistration.SP.get(SolarPanel.T2), "Panel Solar II");
    addBlock(() -> BlockRegistration.SP.get(SolarPanel.T3), "Panel Solar III");
    addBlock(() -> BlockRegistration.SP.get(SolarPanel.T4), "Panel Solar IV");
    addBlock(() -> BlockRegistration.SP.get(SolarPanel.T5), "Panel Solar V");
    addBlock(() -> BlockRegistration.SP.get(SolarPanel.T6), "Panel Solar VI");
    addBlock(() -> BlockRegistration.SP.get(SolarPanel.T7), "Panel Solar VII");
    addBlock(() -> BlockRegistration.SP.get(SolarPanel.T8), "Panel Solar VIII");


    addBlock(() -> BlockRegistration.ENERGY_CABLE.get(CableTier.BASIC), "Cable de Energía Básico");
    addBlock(() -> BlockRegistration.ENERGY_CABLE.get(CableTier.ADVANCE), "Cable de Energía Avanzado");
    addBlock(() -> BlockRegistration.ENERGY_CABLE.get(CableTier.EXTREME), "Cable de Energía Extremo");

    addBlock(() -> BlockRegistration.FLUID_CABLE.get(CableTier.BASIC), "Basic Fluid Cable");
    addBlock(() -> BlockRegistration.FLUID_CABLE.get(CableTier.ADVANCE), "Advance Fluid Cable");
    addBlock(() -> BlockRegistration.FLUID_CABLE.get(CableTier.EXTREME), "Extreme Fluid Cable");

    add("degrassi.wrench.mode", "Modo configuración");

    add("degrassi.wrench.mode.change", "Cambiado el modo en: %s, lado: %s, de %s a %s");
  }
  @Override
  protected void addWiki() {
    addWiki("storage_transfer", "Transferencia de almacenamiento (Energía, Items, Líquidos)");
    addWiki("generators", "Generadores");
    addWiki("welcome_back", "Bienvenid@ de vuelta %s");
    addWiki("energy_cable", "Energy Cables");
    addWiki("energy_cable_0", "Cables are used to transfer power between machines.");
    addWiki("fluid_cable", "Fluid Cables");
    addWiki("fluid_cable_0", "Cables are used to transfer fluids between machines.");
    addWiki("sp", "Solar Panels.");
    addWiki("sp_0", "The Solar Panel is an FE generator that generates energy from the sun light.");
  }

  @Override
  protected void addInfos() {
    //wrench
    addInfo("wrench.mode", "Mode: %s");
    addInfo("wrench.mode.config", "Config");
    addInfo("wrench.mode.rotate", "Rotate");
    // wiki info
    addInfo("io.mode", "I/O Mode");
    addInfo("io.mode.all", "Rec/Ext");
    addInfo("io.mode.extract", "Extract");
    addInfo("io.mode.none", "Off");
    addInfo("io.mode.pull", "Pull");
    addInfo("io.mode.push", "Push");
    addInfo("io.mode.receive", "Receive");
    addInfo("redstone", "Redstone");
    addInfo("on", "On");
    addInfo("off", "Off");
    addInfo("ignore", "Ignore");
    addInfo("capacity", "Capacidad");
    addInfo("fe", "%s FE");
    addInfo("mb", "%s mB");
    addInfo("max.io", "Max I/O");
    addInfo("fe.per.tick", "%s FE/t");
    addInfo("mb.per.tick", "%s mB/t");
    addInfo("generation", "Generación");
    addInfo("sp.generation", "%s FE/t");
  }

  @Override
  protected void addEntities() {

  }

  @Override
  protected void addItemGroups() {
    addItemGroup("machines", "Degrassi(Máquinas)");
    addItemGroup("items", "Degrassi(Items)");
  }

  @Override
  protected void addGuiElements() {

  }

  @Override
  protected void addUpgradeTooltips() {

  }

  @Override
  protected void addJeiGuiElements() {

  }

  @Override
  protected void addJeiRecipes() {

  }

  @Override
  protected void addMachineTooltips() {

  }

  @Override
  protected void addFluids() {

  }
}
