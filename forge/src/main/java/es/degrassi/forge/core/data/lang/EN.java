package es.degrassi.forge.core.data.lang;

import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.common.conduit.common.init.ConduitBlocks;
import es.degrassi.forge.core.common.conduit.common.init.ConduitItems;
import es.degrassi.forge.core.data.DegrassiLangProvider;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.init.ItemRegistration;
import es.degrassi.forge.core.tiers.Chest;
import es.degrassi.forge.core.tiers.Furnace;
import es.degrassi.forge.core.tiers.MultiblockPartStorage;
import es.degrassi.forge.core.tiers.PhotovoltaicCell;
import es.degrassi.forge.core.tiers.SolarPanel;
import es.degrassi.forge.core.tiers.Storage;
import net.minecraft.data.PackOutput;

public class EN extends DegrassiLangProvider {
  private static final String mod = Degrassi.MODID;
  public EN(PackOutput output) {
    super(output, "en_us");
  }

  @Override
  protected void addOthers() {
    add("degrassi.wrench.mode", "Config mode");
    add("degrassi.wrench.mode.change", "Changed mode in pos: %s, side: %s, from %s to %s");
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

    addItem(ConduitItems.ENERGY, "Energy Cable");
    addItem(ConduitItems.HEAT, "Heat Cable");
    addItem(ConduitItems.ITEM, "Item Cable");
    addItem(ConduitItems.REDSTONE, "Redstone Cable");
    addItem(ConduitItems.BASIC_FLUID, "Basic Fluid Cable");
    addItem(ConduitItems.ADVANCED_FLUID, "Advanced Fluid Cable");
    addItem(ConduitItems.EXTREME_FLUID, "Extreme Fluid Cable");
  }

  @Override
  protected void addBlocks() {
    addBlock(BlockRegistration.MACHINE_CASING, "Machine Casing");
    addBlock(BlockRegistration.MELTER_FRAME, "Melter Frame");
    addBlock(BlockRegistration.MELTER_CONTROLLER, "Melter Controller");

    addBlock(() -> BlockRegistration.FURNACE.get(Furnace.IRON), "Iron Furnace");
    addBlock(() -> BlockRegistration.FURNACE.get(Furnace.GOLD), "Gold Furnace");
    addBlock(() -> BlockRegistration.FURNACE.get(Furnace.DIAMOND), "Diamond Furnace");
    addBlock(() -> BlockRegistration.FURNACE.get(Furnace.EMERALD), "Emerald Furnace");
    addBlock(() -> BlockRegistration.FURNACE.get(Furnace.NETHERITE), "Netherite Furnace");

    addBlock(() -> BlockRegistration.CHEST.get(Chest.IRON), "Iron Chest");
    addBlock(() -> BlockRegistration.CHEST.get(Chest.GOLD), "Gold Chest");
    addBlock(() -> BlockRegistration.CHEST.get(Chest.DIAMOND), "Diamond Chest");
    addBlock(() -> BlockRegistration.CHEST.get(Chest.EMERALD), "Emerald Chest");
    addBlock(() -> BlockRegistration.CHEST.get(Chest.NETHERITE), "Netherite Chest");

    addBlock(() -> BlockRegistration.SP.get(SolarPanel.T1), "Solar Panel I");
    addBlock(() -> BlockRegistration.SP.get(SolarPanel.T2), "Solar Panel II");
    addBlock(() -> BlockRegistration.SP.get(SolarPanel.T3), "Solar Panel III");
    addBlock(() -> BlockRegistration.SP.get(SolarPanel.T4), "Solar Panel IV");
    addBlock(() -> BlockRegistration.SP.get(SolarPanel.T5), "Solar Panel V");
    addBlock(() -> BlockRegistration.SP.get(SolarPanel.T6), "Solar Panel VI");
    addBlock(() -> BlockRegistration.SP.get(SolarPanel.T7), "Solar Panel VII");
    addBlock(() -> BlockRegistration.SP.get(SolarPanel.T8), "Solar Panel VIII");

    addBlock(ConduitBlocks.CONDUIT, "Cable");
    addBlock(BlockRegistration.DIGITAL_CONTROLLER, "Controller");

    addBlock(() -> BlockRegistration.ENERGY_CELL.get(Storage.Energy.BASIC), "Basic Energy Cell");
    addBlock(() -> BlockRegistration.ENERGY_CELL.get(Storage.Energy.ADVANCED), "Advanced Energy Cell");
    addBlock(() -> BlockRegistration.ENERGY_CELL.get(Storage.Energy.EXTREME), "Extreme Energy Cell");
    addBlock(() -> BlockRegistration.ENERGY_CELL.get(Storage.Energy.CREATIVE), "Creative Energy Cell");

    addBlock(() -> BlockRegistration.FLUID_TANK.get(Storage.Fluid.BASIC), "Basic Fluid Tank");
    addBlock(() -> BlockRegistration.FLUID_TANK.get(Storage.Fluid.ADVANCED), "Advanced Fluid Tank");
    addBlock(() -> BlockRegistration.FLUID_TANK.get(Storage.Fluid.EXTREME), "Extreme Fluid Tank");
    addBlock(() -> BlockRegistration.FLUID_TANK.get(Storage.Fluid.CREATIVE), "Creative Fluid Tank");

    addBlock(() -> BlockRegistration.ENERGY_HATCH.get(MultiblockPartStorage.Energy.BASIC), "Basic Energy Hatch");
    addBlock(() -> BlockRegistration.ENERGY_HATCH.get(MultiblockPartStorage.Energy.ADVANCED), "Advanced Energy Hatch");
    addBlock(() -> BlockRegistration.ENERGY_HATCH.get(MultiblockPartStorage.Energy.EXTREME), "Extreme Energy Hatch");

    addBlock(() -> BlockRegistration.FLUID_INPUT_TANK.get(MultiblockPartStorage.Fluid.Input.BASIC), "Basic Fluid Input Tank");
    addBlock(() -> BlockRegistration.FLUID_INPUT_TANK.get(MultiblockPartStorage.Fluid.Input.ADVANCED), "Advanced Fluid Input Tank");
    addBlock(() -> BlockRegistration.FLUID_INPUT_TANK.get(MultiblockPartStorage.Fluid.Input.EXTREME), "Extreme Fluid Input Tank");

    addBlock(() -> BlockRegistration.QUADRUPLE_FLUID_INPUT_TANK.get(MultiblockPartStorage.Fluid.QuadrupleInput.BASIC), "Basic Quadruple Fluid Input Tank");
    addBlock(() -> BlockRegistration.QUADRUPLE_FLUID_INPUT_TANK.get(MultiblockPartStorage.Fluid.QuadrupleInput.ADVANCED), "Advanced Quadruple Fluid Input Tank");
    addBlock(() -> BlockRegistration.QUADRUPLE_FLUID_INPUT_TANK.get(MultiblockPartStorage.Fluid.QuadrupleInput.EXTREME), "Extreme Quadruple Fluid Input Tank");

    addBlock(() -> BlockRegistration.FLUID_OUTPUT_TANK.get(MultiblockPartStorage.Fluid.Output.BASIC), "Basic Fluid Output Tank");
    addBlock(() -> BlockRegistration.FLUID_OUTPUT_TANK.get(MultiblockPartStorage.Fluid.Output.ADVANCED), "Advanced Fluid Output Tank");
    addBlock(() -> BlockRegistration.FLUID_OUTPUT_TANK.get(MultiblockPartStorage.Fluid.Output.EXTREME), "Extreme Fluid Output Tank");

    addBlock(() -> BlockRegistration.INPUT_BUS.get(MultiblockPartStorage.Item.Input.BASIC), "Basic Input Bus");
    addBlock(() -> BlockRegistration.INPUT_BUS.get(MultiblockPartStorage.Item.Input.ADVANCED), "Advanced Input Bus");
    addBlock(() -> BlockRegistration.INPUT_BUS.get(MultiblockPartStorage.Item.Input.EXTREME), "Extreme Input Bus");

    addBlock(() -> BlockRegistration.OUTPUT_BUS.get(MultiblockPartStorage.Item.Output.BASIC), "Basic Output Bus");
    addBlock(() -> BlockRegistration.OUTPUT_BUS.get(MultiblockPartStorage.Item.Output.ADVANCED), "Advanced Output Bus");
    addBlock(() -> BlockRegistration.OUTPUT_BUS.get(MultiblockPartStorage.Item.Output.EXTREME), "Extreme Output Bus");
  }

  @Override
  protected void addWiki() {
    addWiki("storage_transfer", "Storage & Transfer (Energy, Item, Fluid)");
    addWiki("generators", "Generators");
    addWiki("welcome_back", "Welcome back %s");
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
    addInfo("config.mode", "Changed %s, %s");
    addInfo("config.mode.from", "From: %s");
    addInfo("config.mode.to", "To: %s");
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
    addInfo("capacity.energy", "Energy Capacity");
    addInfo("capacity.fluid", "Fluid Capacity");
    addInfo("capacity.fluid.quadruple", "Fluid Capacity Per Tank");
    addInfo("capacity.xp", "Experience Capacity");
    addInfo("fe", "%s FE");
    addInfo("xp", "%s XP");
    addInfo("mb", "%s mB");
    addInfo("max.io", "Max I/O");
    addInfo("max.i", "Max Input");
    addInfo("max.o", "Max Output");
    addInfo("fe.per.tick", "%s FE/t");
    addInfo("items.per.tick", "%s Items/t");
    addInfo("mb.per.tick", "%s mB/t");
    addInfo("generation", "Generation");
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
    addItemGroup("machines", "Degrassi(Machines)");
    addItemGroup("digital", "Degrassi(Digital Storage)");
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
    addJeiRecipe("furnace", "Degrassi Furnace");
    addJeiRecipe("melter", "Melting Factory");
    // progress element
    addJeiRecipe("time", "Duration: %s ticks");
    addJeiRecipe("instant", "Instant craft");
    addJeiRecipe("id", "Recipe ID: %s");
    // energy element
    addJeiRecipe("energy.input", "Requires");
    addJeiRecipe("energy.output", "Produces");
    addJeiRecipe("energy", "%s %s %s/t");
    addJeiRecipe("energy.total", "%s RF");
    // fluid element
    addJeiRecipe("fluid.input", "Requires");
    addJeiRecipe("fluid.output", "Produces");
    addJeiRecipe("fluid", "%s %s %s/t");
    addJeiRecipe("fluid.total", "%s mB");
    // experience element
    addJeiRecipe("experience.input", "Requires");
    addJeiRecipe("experience.output", "Produces");
    addJeiRecipe("experience", "%s %s %s/t");
    addJeiRecipe("experience.total", "%s XP");
  }

  @Override
  protected void addMachineTooltips() {

  }

  @Override
  protected void addFluids() {

  }
}
