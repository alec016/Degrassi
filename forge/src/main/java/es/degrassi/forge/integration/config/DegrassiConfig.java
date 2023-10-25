package es.degrassi.forge.integration.config;

import es.degrassi.forge.init.item.upgrade.UpgradeType;
import es.degrassi.forge.util.Fetcher;
import net.minecraftforge.common.ForgeConfigSpec;

public class DegrassiConfig {

  public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
  public static final ForgeConfigSpec SPEC;

  // general config

  // Solar Panels
  // sp1
  public static final ForgeConfigSpec.ConfigValue<Integer> sp1_generation;
  public static final ForgeConfigSpec.ConfigValue<Integer> sp1_loose;
  public static final ForgeConfigSpec.ConfigValue<Integer> sp1_capacity;
  public static final ForgeConfigSpec.ConfigValue<Integer> sp1_transfer;

  // sp2
  public static final ForgeConfigSpec.ConfigValue<Integer> sp2_generation;
  public static final ForgeConfigSpec.ConfigValue<Integer> sp2_loose;
  public static final ForgeConfigSpec.ConfigValue<Integer> sp2_capacity;
  public static final ForgeConfigSpec.ConfigValue<Integer> sp2_transfer;

  // sp3
  public static final ForgeConfigSpec.ConfigValue<Integer> sp3_generation;
  public static final ForgeConfigSpec.ConfigValue<Integer> sp3_loose;
  public static final ForgeConfigSpec.ConfigValue<Integer> sp3_capacity;
  public static final ForgeConfigSpec.ConfigValue<Integer> sp3_transfer;

  // sp4
  public static final ForgeConfigSpec.ConfigValue<Integer> sp4_generation;
  public static final ForgeConfigSpec.ConfigValue<Integer> sp4_loose;
  public static final ForgeConfigSpec.ConfigValue<Integer> sp4_capacity;
  public static final ForgeConfigSpec.ConfigValue<Integer> sp4_transfer;

  // sp5
  public static final ForgeConfigSpec.ConfigValue<Integer> sp5_generation;
  public static final ForgeConfigSpec.ConfigValue<Integer> sp5_loose;
  public static final ForgeConfigSpec.ConfigValue<Integer> sp5_capacity;
  public static final ForgeConfigSpec.ConfigValue<Integer> sp5_transfer;

  // sp6
  public static final ForgeConfigSpec.ConfigValue<Integer> sp6_generation;
  public static final ForgeConfigSpec.ConfigValue<Integer> sp6_loose;
  public static final ForgeConfigSpec.ConfigValue<Integer> sp6_capacity;
  public static final ForgeConfigSpec.ConfigValue<Integer> sp6_transfer;

  // sp7
  public static final ForgeConfigSpec.ConfigValue<Integer> sp7_generation;
  public static final ForgeConfigSpec.ConfigValue<Integer> sp7_loose;
  public static final ForgeConfigSpec.ConfigValue<Integer> sp7_capacity;
  public static final ForgeConfigSpec.ConfigValue<Integer> sp7_transfer;

  // sp8
  public static final ForgeConfigSpec.ConfigValue<Integer> sp8_generation;
  public static final ForgeConfigSpec.ConfigValue<Integer> sp8_loose;
  public static final ForgeConfigSpec.ConfigValue<Integer> sp8_capacity;
  public static final ForgeConfigSpec.ConfigValue<Integer> sp8_transfer;

  // lunar panels
  // lp1
  public static final ForgeConfigSpec.ConfigValue<Integer> lp1_generation;
  public static final ForgeConfigSpec.ConfigValue<Integer> lp1_loose;

  // lp2
  public static final ForgeConfigSpec.ConfigValue<Integer> lp2_generation;
  public static final ForgeConfigSpec.ConfigValue<Integer> lp2_loose;

  // lp3
  public static final ForgeConfigSpec.ConfigValue<Integer> lp3_generation;
  public static final ForgeConfigSpec.ConfigValue<Integer> lp3_loose;

  // passive panels
  // pp1
  public static final ForgeConfigSpec.ConfigValue<Integer> pp1_generation;
  public static final ForgeConfigSpec.ConfigValue<Integer> pp1_loose;

  // pp2
  public static final ForgeConfigSpec.ConfigValue<Integer> pp2_generation;
  public static final ForgeConfigSpec.ConfigValue<Integer> pp2_loose;

  //furnaces
  //iron furnace
  public static final ForgeConfigSpec.ConfigValue<Integer> iron_furnace_capacity;
  public static final ForgeConfigSpec.ConfigValue<Integer> iron_furnace_transfer;
  public static final ForgeConfigSpec.ConfigValue<Integer> iron_furnace_xp;
  public static final ForgeConfigSpec.ConfigValue<Integer> iron_furnace_speed;
  public static final ForgeConfigSpec.ConfigValue<Integer> iron_furnace_energy;

  //gold furnace
  public static final ForgeConfigSpec.ConfigValue<Integer> gold_furnace_capacity;
  public static final ForgeConfigSpec.ConfigValue<Integer> gold_furnace_transfer;
  public static final ForgeConfigSpec.ConfigValue<Integer> gold_furnace_xp;
  public static final ForgeConfigSpec.ConfigValue<Integer> gold_furnace_speed;
  public static final ForgeConfigSpec.ConfigValue<Integer> gold_furnace_energy;

  //diamond furnace
  public static final ForgeConfigSpec.ConfigValue<Integer> diamond_furnace_capacity;
  public static final ForgeConfigSpec.ConfigValue<Integer> diamond_furnace_transfer;
  public static final ForgeConfigSpec.ConfigValue<Integer> diamond_furnace_xp;
  public static final ForgeConfigSpec.ConfigValue<Integer> diamond_furnace_speed;
  public static final ForgeConfigSpec.ConfigValue<Integer> diamond_furnace_energy;

  //emerald furnace
  public static final ForgeConfigSpec.ConfigValue<Integer> emerald_furnace_capacity;
  public static final ForgeConfigSpec.ConfigValue<Integer> emerald_furnace_transfer;
  public static final ForgeConfigSpec.ConfigValue<Integer> emerald_furnace_xp;
  public static final ForgeConfigSpec.ConfigValue<Integer> emerald_furnace_speed;
  public static final ForgeConfigSpec.ConfigValue<Integer> emerald_furnace_energy;

  //netherite furnace
  public static final ForgeConfigSpec.ConfigValue<Integer> netherite_furnace_capacity;
  public static final ForgeConfigSpec.ConfigValue<Integer> netherite_furnace_transfer;
  public static final ForgeConfigSpec.ConfigValue<Integer> netherite_furnace_xp;
  public static final ForgeConfigSpec.ConfigValue<Integer> netherite_furnace_speed;
  public static final ForgeConfigSpec.ConfigValue<Integer> netherite_furnace_energy;

  // melter
  public static final ForgeConfigSpec.ConfigValue<Integer> melter_capacity;
  public static final ForgeConfigSpec.ConfigValue<Integer> melter_transfer;

  // efficiency upgrade
  public static final ForgeConfigSpec.ConfigValue<Integer> eff_augment;
  public static final ForgeConfigSpec.EnumValue<UpgradeType> eff_type;

  // transfer upgrade
  public static final ForgeConfigSpec.ConfigValue<Integer> trans_augment;
  public static final ForgeConfigSpec.EnumValue<UpgradeType> trans_type;

  // generation upgrade
  public static final ForgeConfigSpec.ConfigValue<Integer> gen_augment;
  public static final ForgeConfigSpec.EnumValue<UpgradeType> gen_type;

  // capacity upgrade
  public static final ForgeConfigSpec.ConfigValue<Integer> cap_augment;
  public static final ForgeConfigSpec.EnumValue<UpgradeType> cap_type;

  // speed upgrade
  public static final ForgeConfigSpec.ConfigValue<Integer> speed_augment;
  public static final ForgeConfigSpec.ConfigValue<Integer> speed_energy_augment;

  // energy upgrade
  public static final ForgeConfigSpec.ConfigValue<Integer> energy_augment;

  // general config
//  static {
//    BUILDER.push("General");
//    debugLevel = BUILDER
//      .comment(" debug level - default info")
//      .defineEnum("level", LoggingLevel.INFO);
//    BUILDER.pop();
//  }

  // solar panels config
  static {
    BUILDER.push("Solar Panels");

    BUILDER.push("Solar Panel tier I");
    sp1_generation = BUILDER
      .comment(" base generation - default 1")
      .define("generation", 1);
    sp1_loose = BUILDER
      .comment(" percentage of energy loose on break - default 5")
      .define("loose", 5);
    sp1_capacity = BUILDER
      .comment(" Energy Capacity - default 10000")
      .define("capacity", 10000);
    sp1_transfer = BUILDER
      .comment(" Max RF/t transfer - default 5")
      .define("transfer", 5);
    BUILDER.pop();

    BUILDER.push("Solar Panel tier II");
    sp2_generation = BUILDER
      .comment(" base generation - default 8")
      .define("generation", 8);
    sp2_loose = BUILDER
      .comment(" percentage of energy loose on break - default 5")
      .define("loose", 5);
    sp2_capacity = BUILDER
      .comment(" Energy Capacity - default 60000")
      .define("capacity", 60000);
    sp2_transfer = BUILDER
      .comment(" Max RF/t transfer - default 16")
      .define("transfer", 16);
    BUILDER.pop();

    BUILDER.push("Solar Panel tier III");
    sp3_generation = BUILDER
      .comment(" base generation - default 16")
      .define("generation", 16);
    sp3_loose = BUILDER
      .comment(" percentage of energy loose on break - default 5")
      .define("loose", 5);
    sp3_capacity = BUILDER
      .comment(" Energy Capacity - default 100000")
      .define("capacity", 100000);
    sp3_transfer = BUILDER
      .comment(" Max RF/t transfer - default 64")
      .define("transfer", 64);
    BUILDER.pop();

    BUILDER.push("Solar Panel tier IV");
    sp4_generation = BUILDER
      .comment(" base generation - default 48")
      .define("generation", 48);
    sp4_loose = BUILDER
      .comment(" percentage of energy loose on break - default 5")
      .define("loose", 5);
    sp4_capacity = BUILDER
      .comment(" Energy Capacity - default 500000")
      .define("capacity", 500000);
    sp4_transfer = BUILDER
      .comment(" Max RF/t transfer - default 128")
      .define("transfer", 128);
    BUILDER.pop();

    BUILDER.push("Solar Panel tier V");
    sp5_generation = BUILDER
      .comment(" base generation - default 128")
      .define("generation", 128);
    sp5_loose = BUILDER
      .comment(" percentage of energy loose on break - default 5")
      .define("loose", 5);
    sp5_capacity = BUILDER
      .comment(" Energy Capacity - default 1000000")
      .define("capacity", 1000000);
    sp5_transfer = BUILDER
      .comment(" Max RF/t transfer - default 512")
      .define("transfer", 512);
    BUILDER.pop();

    BUILDER.push("Solar Panel tier VI");
    sp6_generation = BUILDER
      .comment(" base generation - default 256")
      .define("generation", 256);
    sp6_loose = BUILDER
      .comment(" percentage of energy loose on break - default 5")
      .define("loose", 5);
    sp6_capacity = BUILDER
      .comment(" Energy Capacity - default 2000000")
      .define("capacity", 2000000);
    sp6_transfer = BUILDER
      .comment(" Max RF/t transfer - default 1024")
      .define("transfer", 1024);
    BUILDER.pop();

    BUILDER.push("Solar Panel tier VII");
    sp7_generation = BUILDER
      .comment(" base generation - default 1024")
      .define("generation", 1024);
    sp7_loose = BUILDER
      .comment(" percentage of energy loose on break - default 5")
      .define("loose", 5);
    sp7_capacity = BUILDER
      .comment(" Energy Capacity - default 5000000")
      .define("capacity", 5000000);
    sp7_transfer = BUILDER
      .comment(" Max RF/t transfer - default 2048")
      .define("transfer", 2048);
    BUILDER.pop();

    BUILDER.push("Solar Panel tier VIII");
    sp8_generation = BUILDER
      .comment(" base generation - default 32048")
      .define("generation", 32048);
    sp8_loose = BUILDER
      .comment(" percentage of energy loose on break - default 5")
      .define("loose", 5);
    sp8_capacity = BUILDER
      .comment(" Energy Capacity - default 10000000")
      .define("capacity", 10000000);
    sp8_transfer = BUILDER
      .comment(" Max RF/t transfer - default 48072")
      .define("transfer", 48072);
    BUILDER.pop();

    BUILDER.pop();
  }

  // lunar panels config
  static {
    BUILDER.push("Lunar Panels");

    BUILDER.push("Lunar Panel tier I");
    lp1_generation = BUILDER
      .comment(" base generation - default 1")
      .define("generation", 1);
    lp1_loose = BUILDER
      .comment(" percentage of energy loose on break - default 5")
      .define("loose", 5);
    BUILDER.pop();

    BUILDER.push("Lunar Panel tier II");
    lp2_generation = BUILDER
      .comment(" base generation - default 1")
      .define("generation", 1);
    lp2_loose = BUILDER
      .comment(" percentage of energy loose on break - default 5")
      .define("loose", 5);
    BUILDER.pop();

    BUILDER.push("Lunar Panel tier III");
    lp3_generation = BUILDER
      .comment(" base generation - default 1")
      .define("generation", 1);
    lp3_loose = BUILDER
      .comment(" percentage of energy loose on break - default 5")
      .define("loose", 5);
    BUILDER.pop();

    BUILDER.pop();
  }

  // passive panels config
  static {
    BUILDER.push("Passive Panels");

    BUILDER.push("Passive Panel tier I");
    pp1_generation = BUILDER
      .comment(" base generation - default 1")
      .define("generation", 1);
    pp1_loose = BUILDER
      .comment(" percentage of energy loose on break - default 5")
      .define("loose", 5);
    BUILDER.pop();

    BUILDER.push("Passive Panel tier II");
    pp2_generation = BUILDER
      .comment(" base generation - default 1")
      .define("generation", 1);
    pp2_loose = BUILDER
      .comment(" percentage of energy loose on break - default 5")
      .define("loose", 5);
    BUILDER.pop();

    BUILDER.pop();
  }

  // furnaces config
  static {
    BUILDER.push("Furnaces");

    BUILDER.push("Iron Furnace");
    iron_furnace_capacity = BUILDER
      .comment("energy capacity of iron furnace - Default 10000")
      .define("capacity", 10000);
    iron_furnace_transfer = BUILDER
      .comment("energy transfer of iron furnace - Default 20")
      .define("transfer", 20);
    iron_furnace_xp = BUILDER
      .comment("xp augment production on recipe in percentage - Default 1")
      .define("xp", 1);
    iron_furnace_speed = BUILDER
      .comment("speed recipe reduction in percentage - Default 5")
      .define("speed", 5);
    iron_furnace_energy = BUILDER
      .comment("energy recipe augment in percentage - Default 0")
      .define("energy", 0);
    BUILDER.pop();

    BUILDER.push("Gold Furnace");
    gold_furnace_capacity = BUILDER
      .comment("energy capacity of gold furnace - Default 50000")
      .define("capacity", 50000);
    gold_furnace_transfer = BUILDER
      .comment("energy transfer of gold furnace - Default 20")
      .define("transfer", 100);
    gold_furnace_xp = BUILDER
      .comment("xp augment production on recipe in percentage - Default 1")
      .define("xp", 1);
    gold_furnace_speed = BUILDER
      .comment("speed recipe reduction in percentage - Default 20")
      .define("speed", 20);
    gold_furnace_energy = BUILDER
      .comment("energy recipe augment in percentage - Default 20")
      .define("energy", 20);
    BUILDER.pop();

    BUILDER.push("Diamond Furnace");
    diamond_furnace_capacity = BUILDER
      .comment("energy capacity of diamond furnace - Default 100000")
      .define("capacity", 100000);
    diamond_furnace_transfer = BUILDER
      .comment("energy transfer of diamond furnace - Default 200")
      .define("transfer", 200);
    diamond_furnace_xp = BUILDER
      .comment("xp augment production on recipe in percentage - Default 1")
      .define("xp", 1);
    diamond_furnace_speed = BUILDER
      .comment("speed recipe reduction in percentage - Default 40")
      .define("speed", 40);
    diamond_furnace_energy = BUILDER
      .comment("energy recipe augment in percentage - Default 40")
      .define("energy", 40);
    BUILDER.pop();

    BUILDER.push("emerald Furnace");
    emerald_furnace_capacity = BUILDER
      .comment("energy capacity of iron furnace - Default 500000")
      .define("capacity", 500000);
    emerald_furnace_transfer = BUILDER
      .comment("energy transfer of emerald furnace - Default 1000")
      .define("transfer", 1000);
    emerald_furnace_xp = BUILDER
      .comment("xp augment production on recipe in percentage - Default 1")
      .define("xp", 1);
    emerald_furnace_speed = BUILDER
      .comment("speed recipe reduction in percentage - Default 60")
      .define("speed", 60);
    emerald_furnace_energy = BUILDER
      .comment("energy recipe augment in percentage - Default 60")
      .define("energy", 60);
    BUILDER.pop();

    BUILDER.push("Netherite Furnace");
    netherite_furnace_capacity = BUILDER
      .comment("energy capacity of netherite furnace - Default 1000000")
      .define("capacity", 1000000);
    netherite_furnace_transfer = BUILDER
      .comment("energy transfer of netherite furnace - Default 10000")
      .define("transfer", 10000);
    netherite_furnace_xp = BUILDER
      .comment("xp augment production on recipe in percentage - Default 1")
      .define("xp", 1);
    netherite_furnace_speed = BUILDER
      .comment("speed recipe reduction in percentage - Default 90")
      .define("speed", 90);
    netherite_furnace_energy = BUILDER
      .comment("energy recipe augment in percentage - Default 90")
      .define("energy", 90);
    BUILDER.pop();

    BUILDER.pop();
  }

  // melter config
  static {
    BUILDER.push("Melter");

    melter_capacity = BUILDER
      .comment("energy capacity - Default 100000")
      .define("capacity", 100000);
    melter_transfer = BUILDER
      .comment("transfer rate - Default 10000")
      .define("transfer", 10000);

    BUILDER.pop();
  }

  // upgrades config
  static {
    BUILDER.push("Upgrades");

    BUILDER.push("Efficiency");
    eff_augment = BUILDER
      .comment("efficiency augment - default 5")
      .define("augment", 5);
    eff_type = BUILDER
      .comment("efficiency type - Default ADD")
      .comment("ADD -> adds a % of default efficiency")
      .comment("MULTIPLY -> multiply the default efficiency by a positive integer")
      .comment("EXPONENTIAL -> increasingly dramatically the default efficiency by a positive integer certain of times")
      .defineEnum("type", UpgradeType.ADD);
    BUILDER.pop();

    BUILDER.push("Transfer");
    trans_augment = BUILDER
      .comment("transfer augment - default 5")
      .define("augment", 5);
    trans_type = BUILDER
      .comment("transfer type - Default ADD")
      .comment("ADD -> adds a % of default transfer rate")
      .comment("MULTIPLY -> multiply the default transfer rate by a positive integer")
      .comment("EXPONENTIAL -> increasingly dramatically the default transfer rate by a positive integer certain of times")
      .defineEnum("type", UpgradeType.ADD);
    BUILDER.pop();

    BUILDER.push("Generation");
    gen_augment = BUILDER
      .comment("generation augment - default 5")
      .define("augment", 5);
    gen_type = BUILDER
      .comment("generation type - Default ADD")
      .comment("ADD -> adds a % of default generation")
      .comment("MULTIPLY -> multiply the default generation by a positive integer")
      .comment("EXPONENTIAL -> increasingly dramatically the default generation by a positive integer certain of times")
      .defineEnum("type", UpgradeType.ADD);
    BUILDER.pop();

    BUILDER.push("Capacity");
    cap_augment = BUILDER
      .comment("capacity augment - default 5")
      .define("augment", 5);
    cap_type = BUILDER
      .comment("capacity type - Default ADD")
      .comment("ADD -> adds a % of default capacity")
      .comment("MULTIPLY -> multiply the default capacity by a positive integer")
      .comment("EXPONENTIAL -> increasingly dramatically the default capacity by a positive integer certain of times")
      .defineEnum("type", UpgradeType.ADD);
    BUILDER.pop();

    BUILDER.push("Speed");
    speed_augment = BUILDER
      .comment("speed augment in percentage - default 1")
      .define("augment", 1);
    speed_energy_augment = BUILDER
      .comment("speed energy augment in percentage - default 5")
      .define("energy augment", 5);
    BUILDER.pop();

    BUILDER.push("Energy");
    energy_augment = BUILDER
      .comment("energy reduction in percentage - default 1")
      .define("reduction", 1);
    BUILDER.pop();

    BUILDER.pop();
  }

  // creation of spec
  static {
    SPEC = BUILDER.build();
  }

}
