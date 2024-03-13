package es.degrassi.forge.core.data;

import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.data.lang.EN;
import es.degrassi.forge.core.data.lang.ES;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import org.jetbrains.annotations.NotNull;

public abstract class DegrassiLangProvider extends LanguageProvider {
  public DegrassiLangProvider(PackOutput output, String locale) {
    super(output, Degrassi.MODID, locale);
  }

  public static void generate(GatherDataEvent event, @NotNull DataGenerator generator) {
    generator.addProvider(event.includeClient(), new ES(generator.getPackOutput()));
    generator.addProvider(event.includeClient(), new EN(generator.getPackOutput()));
  }

  @Override
  protected void addTranslations() {
    addItemGroups();
    addItems();
    addBlocks();
    addEntities();
    addFluids();
    addGuiElements();
    addUpgradeTooltips();
    addJei();
    addMachineTooltips();

    add("unit.energy.forge", "FE");
  }

  protected abstract void addItems();
  protected abstract void addBlocks();
  protected abstract void addEntities();
  protected abstract void addItemGroups();
  protected abstract void addGuiElements();
  protected abstract void addUpgradeTooltips();
  protected abstract void addJeiGuiElements();
  protected abstract void addJeiRecipes();
  protected abstract void addMachineTooltips();
  protected abstract void addFluids();

  protected void addJei() {
    addJeiGuiElements();
    addJeiRecipes();
  }

  protected void addFluid(String name, String value) {
    add("fluid." + Degrassi.MODID + "." + name, value);
  }

  protected void addItemGroup(String name, String value) {
    add(Degrassi.MODID + ".tabs." + name, value);
  }

  protected void addGuiElement(String name, String value) {
    add(Degrassi.MODID + ".gui.element." + name, value);
  }

  protected void addUpgradeTooltip(String name, String value) {
    add(Degrassi.MODID + ".upgrades." + name, value);
  }
  protected void addUpgradeShiftTooltip(String name, String value) {
    addUpgradeTooltip("shift." + name, value);
  }

  protected void addJeiGuiElement(String name, String value) {
    add(Degrassi.MODID + ".jei.gui.element." + name, value);
  }

  protected void addJeiRecipe(String name, String value) {
    add(Degrassi.MODID + ".jei.recipe." + name, value);
  }

  protected void addMachineTooltip(String machine, String value) {
    add(Degrassi.MODID + ".machine." + machine + ".tooltip", value);
  }

  protected void addMachineStatus(String status, String value) {
    add(Degrassi.MODID + ".machine." + status, value);
  }

  protected void addJadeConfig(String provider, String value) {
    add("config.jade.plugin_" + Degrassi.MODID + "." + provider, value);
  }

  protected void addFluidType(String fluid, String value) {
    add("fluid_type." + Degrassi.MODID + "." + fluid, value);
  }
}
