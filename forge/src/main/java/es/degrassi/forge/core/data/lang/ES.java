package es.degrassi.forge.core.data.lang;

import es.degrassi.forge.core.data.DegrassiLangProvider;
import es.degrassi.forge.core.init.BlockRegistration;
import net.minecraft.data.PackOutput;

public class ES extends DegrassiLangProvider {
  public ES(PackOutput output) {
    super(output, "es_es");
  }

  @Override
  protected void addItems() {

  }

  @Override
  protected void addBlocks() {
    addBlock(BlockRegistration.IRON_FURNACE, "Horno de Hierro");
    addBlock(BlockRegistration.GOLD_FURNACE, "Horno de Oro");
    addBlock(BlockRegistration.DIAMOND_FURNACE, "Horno de Diamante");
    addBlock(BlockRegistration.EMERALD_FURNACE, "Horno de Esmeralda");
    addBlock(BlockRegistration.NETHERITE_FURNACE, "Horno de Netherite");
  }

  @Override
  protected void addEntities() {

  }

  @Override
  protected void addItemGroups() {
    addItemGroup("machines", "Maquinaria Degrassi");
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
