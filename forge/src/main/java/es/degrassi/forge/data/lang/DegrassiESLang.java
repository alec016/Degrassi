package es.degrassi.forge.data.lang;

import es.degrassi.forge.data.DegrassiLangProvider;
import es.degrassi.forge.init.registration.BlockRegister;
import es.degrassi.forge.init.registration.ItemRegister;
import net.minecraft.data.DataGenerator;

public class DegrassiESLang extends DegrassiLangProvider {
  public DegrassiESLang(DataGenerator gen) {
    super(gen, "es_es");
  }

  protected void addFluids() {
    add(BlockRegister.MOLTEN_RED_MATTER_BLOCK.get(), "Materia Roja Fundida");
    add(ItemRegister.MOLTEN_RED_MATTER_BUCKET.get(), "Cubo Materia Roja Fundida");
    addFluid("molten_red_matter", "Materia Roja Fundida");
    addFluidType("molten_red_matter", "Materia Roja Fundida");
  }

  protected void addItems() {
    add(ItemRegister.GOLD_COIN.get(), "Moneda de Oro");
    add(ItemRegister.EFFICIENCY_UPGRADE.get(), "Mejora de Eficiencia");
    add(ItemRegister.TRANSFER_UPGRADE.get(), "Mejora de Transferencia");
    add(ItemRegister.CAPACITY_UPGRADE.get(), "Mejora de Capacidad");
    add(ItemRegister.GENERATION_UPGRADE.get(), "Mejora de Generación");
    add(ItemRegister.SPEED_UPGRADE.get(), "Mejora de velocidad");
    add(ItemRegister.ENERGY_UPGRADE.get(), "Mejora de Energía");
    add(ItemRegister.UPGRADE_BASE.get(), "Base de Mejora");
    add(ItemRegister.MODIFIER_BASE.get(), "Base de Modificación");
    add(ItemRegister.BLACK_PEARL.get(), "Perla Negra");
    add(ItemRegister.RED_MATTER.get(), "Materia Roja");
    add(ItemRegister.CIRCUIT.get(), "Circuit");
    add(ItemRegister.PHOTOVOLTAIC_CELL_I.get(), "Placa Fotovoltaica I");
    add(ItemRegister.PHOTOVOLTAIC_CELL_II.get(), "Placa Fotovoltaica II");
    add(ItemRegister.PHOTOVOLTAIC_CELL_III.get(), "Placa Fotovoltaica III");
    add(ItemRegister.PHOTOVOLTAIC_CELL_IV.get(), "Placa Fotovoltaica IV");
    add(ItemRegister.PHOTOVOLTAIC_CELL_V.get(), "Placa Fotovoltaica V");
    add(ItemRegister.PHOTOVOLTAIC_CELL_VI.get(), "Placa Fotovoltaica VI");
    add(ItemRegister.PHOTOVOLTAIC_CELL_VII.get(), "Placa Fotovoltaica VII");
    add(ItemRegister.PHOTOVOLTAIC_CELL_VIII.get(), "Placa Fotovoltaica VIII");
  }

  protected void addBlocks() {
    add(BlockRegister.MACHINE_CASING.get(), "Machine Casing");
    add(BlockRegister.SP1_BLOCK.get(), "Panel Solar Tier I");
    add(BlockRegister.SP2_BLOCK.get(), "Panel Solar Tier II");
    add(BlockRegister.SP3_BLOCK.get(), "Panel Solar Tier III");
    add(BlockRegister.SP4_BLOCK.get(), "Panel Solar Tier IV");
    add(BlockRegister.SP5_BLOCK.get(), "Panel Solar Tier V");
    add(BlockRegister.SP6_BLOCK.get(), "Panel Solar Tier VI");
    add(BlockRegister.SP7_BLOCK.get(), "Panel Solar Tier VII");
    add(BlockRegister.SP8_BLOCK.get(), "Panel Solar Tier VIII");
    add(BlockRegister.MELTER_BLOCK.get(), "Fundidora");
    add(BlockRegister.IRON_FURNACE_BLOCK.get(), "Horno de Hierro");
    add(BlockRegister.GOLD_FURNACE_BLOCK.get(), "Horno de Oro");
    add(BlockRegister.DIAMOND_FURNACE_BLOCK.get(), "Horno de Diamante");
    add(BlockRegister.EMERALD_FURNACE_BLOCK.get(), "Horno de Esmeralda");
    add(BlockRegister.NETHERITE_FURNACE_BLOCK.get(), "Horno de Netherite");
    add(BlockRegister.UPGRADE_MAKER.get(), "Fábrica de Mejoras");
    add(BlockRegister.JEWELRY_GENERATOR.get(), "Generador por Joyas");
  }

  protected void addEntities() {}

  protected void addItemGroups() {
    addItemGroup("common", "Degrassi");
    addItemGroup("machines", "Maquinaria Degrassi");
  }

  @Override
  protected void addGuiElements() {
    addGuiElement("energy.tooltip", "Energía: %s%s/%s%s");
    addGuiElement("energy.stored","Almacenada: %s%s/%s%s");
    addGuiElement("energy.generation","Generando: %s%s/t");
    addGuiElement("energy.input","Requiere");
    addGuiElement("energy.output","Produce");
    addGuiElement("energy.jei", "%s%s%s");
    addGuiElement("energy.jei.total", "%s%s");
    addGuiElement("energy.jei.perTick", "%s%s%s");
    addGuiElement("efficiency", "Eficiencia: %s");
    addGuiElement("fluid.tooltip", "%smB/%smB");
    addGuiElement("fluid.tooltip.amount", "%smB");
    addGuiElement("fluid.empty", "Vacío: %smB/%smB");
    addGuiElement("transfer", "Capacidad de traspaso: %s%s/t");
    addGuiElement("progress", "Mostrar Recetas");
  }

  @Override
  protected void addUpgradeTooltips() {
    addUpgradeTooltip("capacity.tooltips", "Aumenta la capacidad en x%s");
    addUpgradeTooltip("efficiency.tooltips", "Aumenta la eficiencia en x%s");
    addUpgradeTooltip("generation.tooltips", "Aumenta la generación en x%s");
    addUpgradeTooltip("transfer.tooltips", "Aumenta la transferencia en x%s");
    addUpgradeTooltip("speed.tooltip", "Aumenta la velocidad de la máquina en x%s");
    addUpgradeTooltip("speed.energy.tooltip", "Aumenta la energía requerida por la máquina en x%s");
    addUpgradeTooltip("energy.tooltip", "Reduce la energía requerida por la máquina en x%s");
    addUpgradeTooltip("type", "Tipo actual: %s");

    addUpgradeShiftTooltip("tooltip", "%s %s %s");
    addUpgradeShiftTooltip("press.tooltip", "Presiona");
    addUpgradeShiftTooltip("more.tooltip", "para mas información");
  }

  @Override
  protected void addJeiGuiElements() {
    addJeiGuiElement("sp", "Este Panel Solar %s%s");
    addJeiGuiElement("energy.generation", "generata hasta %s%s/t");
    addJeiGuiElement("sp.solar_position", " en función de la posición del sol.");

    addJeiGuiElement("upgrades.transfer", "Modifica la transferencia de la máquina por x%s");
    addJeiGuiElement("upgrades.generation", "Modifica la generacion de la máquina por x%s");
    addJeiGuiElement("upgrades.efficiency", "Modifica la eficiencia de la máquina por x%s");
    addJeiGuiElement("upgrades.capacity", "Modifica la capacidad de energía de la máquina por  x%s");
  }

  @Override
  protected void addJeiRecipes() {
    addJeiRecipe("time", "Duración: %s ticks");
    addJeiRecipe("instant", "Crafteo instantaneo");
    addJeiRecipe("id", "ID de la Receta: %s");
  }

  @Override
  protected void addMachineTooltips() {
    addMachineTooltip("sp", "Genera %s%s/t");
    addMachineStatus("running", "En Ejecución");
    addMachineStatus("idle", "En Espera");
    addJadeConfig("melter_component_provider", "Melter Component Provider");
    addJadeConfig("furnace_component_provider", "Furnace Component Provider");
    addJadeConfig("upgrade_maker_component_provider", "Upgrade Maker Component Provider");
  }
}
