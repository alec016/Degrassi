package es.degrassi.forge.core.data.lang;

import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.common.conduit.common.init.ConduitBlocks;
import es.degrassi.forge.core.common.conduit.common.init.ConduitItems;
import es.degrassi.forge.core.data.DegrassiLangProvider;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.init.ItemRegistration;
import es.degrassi.forge.core.tiers.Chest;
import es.degrassi.forge.core.tiers.Furnace;
import es.degrassi.forge.core.tiers.PhotovoltaicCell;
import es.degrassi.forge.core.tiers.SolarPanel;
import net.minecraft.data.PackOutput;

public class ES extends DegrassiLangProvider {
  private static final String mod = Degrassi.MODID;
  public ES(PackOutput output) {
    super(output, "es_es");
  }

  @Override
  protected void addOthers() {
    add("degrassi.wrench.mode", "Modo configuración");
    add("degrassi.wrench.mode.change", "Cambiado el modo en: %s, lado: %s, de %s a %s");
    addEnderIO();
  }

  protected void addEnderIO() {
    // DegrassiLang
    add("gui." + mod + ".conduit_channel", "Conduit-Channel");
    add("gui." + mod + ".redstone_channel", "Redstone-Channel");
    add("gui." + mod + ".redstone.mode", "Redstone Mode");
    add("gui." + mod + ".redstone.always_active", "Always active");
    add("gui." + mod + ".redstone.active_with_signal", "Active with signal");
    add("gui." + mod + ".redstone.active_without_signal", "Active without signal");
    add("gui." + mod + ".redstone.never_active", "Never active");
    add("gui." + mod + ".round_robin.enabled", "Round Robin Enabled");
    add("gui." + mod + ".round_robin.disabled", "Round Robin Disabled");
    add("gui." + mod + ".self_feed.enabled", "Self Feed Enabled");
    add("gui." + mod + ".self_feed.disabled", "Self Feed Disabled");
    add("gui." + mod + ".fluid_conduit.change_fluid1", "Locked Fluid:");
    add("gui." + mod + ".fluid_conduit.change_fluid2", "Click to reset!");
    add("gui." + mod + ".fluid_conduit.change_fluid3", "Fluid: %s");
    // ConduitLang
    add("gui." + mod + ".conduit.insert", "Insert");
    add("gui." + mod + ".conduit.extract", "Extract");
  }

  @Override
  protected void addItems() {
    addItem(ItemRegistration.WRENCH, "Wrench");

    addItem(ItemRegistration.BOOK, "Degrassi Manual");
    addItem(ItemRegistration.RED_MATTER, "Red Matter");
    addItem(ItemRegistration.BLACK_PEARL, "Black Pearl");

    addItem(() -> ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.I), "Photovoltaic Cell I");
    addItem(() -> ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.II), "Photovoltaic Cell II");
    addItem(() -> ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.III), "Photovoltaic Cell III");
    addItem(() -> ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.IV), "Photovoltaic Cell IV");
    addItem(() -> ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.V), "Photovoltaic Cell V");
    addItem(() -> ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.VI), "Photovoltaic Cell VI");
    addItem(() -> ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.VII), "Photovoltaic Cell VII");
    addItem(() -> ItemRegistration.PHOTOVOLTAIC_CELL.get(PhotovoltaicCell.VIII), "Photovoltaic Cell VIII");

    addItem(ConduitItems.ENERGY, "Energy Conduit");
    addItem(ConduitItems.ITEM, "Item Conduit");
    addItem(ConduitItems.REDSTONE, "Redstone Conduit");
    addItem(ConduitItems.BASIC_FLUID, "Basic Fluid Conduit");
    addItem(ConduitItems.ADVANCED_FLUID, "Advanced Fluid Conduit");
    addItem(ConduitItems.EXTREME_FLUID, "Extreme Fluid Conduit");
  }

  @Override
  protected void addBlocks() {
    addBlock(BlockRegistration.MACHINE_CASING, "Machine Casing");
    addBlock(() -> BlockRegistration.FURNACE.get(Furnace.IRON), "Horno de Hierro");
    addBlock(() -> BlockRegistration.FURNACE.get(Furnace.GOLD), "Horno de Oro");
    addBlock(() -> BlockRegistration.FURNACE.get(Furnace.DIAMOND), "Horno de Diamante");
    addBlock(() -> BlockRegistration.FURNACE.get(Furnace.EMERALD), "Horno de Esmeralda");
    addBlock(() -> BlockRegistration.FURNACE.get(Furnace.NETHERITE), "Horno de Netherite");

    addBlock(() -> BlockRegistration.CHEST.get(Chest.IRON), "Cofre de Hierro");
    addBlock(() -> BlockRegistration.CHEST.get(Chest.GOLD), "Cofre de Oro");
    addBlock(() -> BlockRegistration.CHEST.get(Chest.DIAMOND), "Cofre de Diamante");
    addBlock(() -> BlockRegistration.CHEST.get(Chest.EMERALD), "Cofre de Esmeralda");
    addBlock(() -> BlockRegistration.CHEST.get(Chest.NETHERITE), "Cofre de Netherite");

    addBlock(() -> BlockRegistration.SP.get(SolarPanel.T1), "Panel Solar I");
    addBlock(() -> BlockRegistration.SP.get(SolarPanel.T2), "Panel Solar II");
    addBlock(() -> BlockRegistration.SP.get(SolarPanel.T3), "Panel Solar III");
    addBlock(() -> BlockRegistration.SP.get(SolarPanel.T4), "Panel Solar IV");
    addBlock(() -> BlockRegistration.SP.get(SolarPanel.T5), "Panel Solar V");
    addBlock(() -> BlockRegistration.SP.get(SolarPanel.T6), "Panel Solar VI");
    addBlock(() -> BlockRegistration.SP.get(SolarPanel.T7), "Panel Solar VII");
    addBlock(() -> BlockRegistration.SP.get(SolarPanel.T8), "Panel Solar VIII");

    addBlock(ConduitBlocks.CONDUIT, "Conduit");
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
    addInfo("io.mode.prev", "From: %s");
    addInfo("io.mode.next", "To: %s");
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
    addInfo("capacity.energy", "Capacidad de Energía");
    addInfo("capacity.xp", "Capacidad de Experiencia");
    addInfo("fe", "%s FE");
    addInfo("xp", "%s XP");
    addInfo("mb", "%s mB");
    addInfo("max.io", "Max I/O");
    addInfo("fe.per.tick", "%s FE/t");
    addInfo("items.per.tick", "%s Items/t");
    addInfo("mb.per.tick", "%s mB/t");
    addInfo("generation", "Generación");
    addInfo("sp.generation", "%s FE/t");
    // chest info
    addInfo("chest.rows", "Rows");
    addInfo("chest.rows.number", "%s");
    addInfo("chest.cols", "Columns");
    addInfo("chest.cols.number", "%s");
    addInfo("chest.slot", "Slots");
    addInfo("chest.slot.number", "%s total slots");
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
    addGuiElement("generation", "Generating: %s/%sFE/t");
    addGuiElement("energy.stored", "Energy Stored: %sFE");
    addGuiElement("energy.capacity", "Energy Capacity: %sFE");
    addGuiElement("transfer", "Transfer: %sFE/t");
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
