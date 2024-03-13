package es.degrassi.forge.core.data.lang;

import es.degrassi.forge.core.data.DegrassiLangProvider;
import es.degrassi.forge.core.init.BlockRegistration;
import net.minecraft.data.PackOutput;

public class EN extends DegrassiLangProvider {
  public EN(PackOutput output) {
    super(output, "en_us");
  }

  @Override
  protected void addItems() {

  }

  @Override
  protected void addBlocks() {
    addBlock(BlockRegistration.IRON_FURNACE, "Iron Furnace");
    addBlock(BlockRegistration.GOLD_FURNACE, "Gold Furnace");
    addBlock(BlockRegistration.DIAMOND_FURNACE, "Diamond Furnace");
    addBlock(BlockRegistration.EMERALD_FURNACE, "Emerald Furnace");
    addBlock(BlockRegistration.NETHERITE_FURNACE, "Netherite Furnace");

  }

  @Override
  protected void addEntities() {

  }

  @Override
  protected void addItemGroups() {
//    addItemGroup("common", "Degrassi");
    addItemGroup("machines", "Degrassi Machines");
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
