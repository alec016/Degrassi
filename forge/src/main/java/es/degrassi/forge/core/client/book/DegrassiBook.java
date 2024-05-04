package es.degrassi.forge.core.client.book;

import es.degrassi.forge.core.common.conduit.common.init.ConduitItems;
import es.degrassi.forge.core.init.BlockRegistration;
import es.degrassi.forge.core.init.ItemRegistration;
import es.degrassi.forge.core.tiers.Chest;
import es.degrassi.forge.core.tiers.Furnace;
import es.degrassi.forge.core.tiers.MultiblockPartStorage;
import es.degrassi.forge.core.tiers.SolarPanel;
import es.degrassi.forge.lib.client.wiki.Icon;
import es.degrassi.forge.lib.client.wiki.Wiki;
import es.degrassi.forge.lib.client.wiki.page.GridPage;
import es.degrassi.forge.lib.client.wiki.page.Info;
import es.degrassi.forge.lib.client.wiki.page.panel.CraftingPanel;
import es.degrassi.forge.lib.client.wiki.page.panel.EnergyPanel;
import es.degrassi.forge.lib.client.wiki.page.panel.FluidPanel;
import es.degrassi.forge.lib.client.wiki.page.panel.ItemPanel;
import es.degrassi.forge.lib.client.wiki.page.panel.RedstonePanel;
import es.degrassi.forge.lib.client.wiki.page.panel.WelcomePanel;
import java.util.List;

public class DegrassiBook {
  public static final Wiki WIKI = new Wiki();
  static {
    WIKI
      .e(
        "storage_transfer",
        new Icon(ConduitItems.ENERGY.get()),
        ae -> ae.s(
          as -> as.p(
            new GridPage(as).e(
              "energy_conduit",
              e -> e.s(
                s -> s.p(
                  new Info(IMG.ENERGY_CABLE, s),
                  new EnergyPanel<>(s).next(new CraftingPanel<>(s))
                )
              )
            ).e(
              "fluid_conduit",
              new Icon(ConduitItems.ADVANCED_FLUID.get()),
              e -> e.s(
                s -> s.p(
                  new Info(IMG.ENERGY_CABLE, s),
                  new FluidPanel<>(List.of(
                    ConduitItems.BASIC_FLUID,
                    ConduitItems.ADVANCED_FLUID,
                    ConduitItems.EXTREME_FLUID
                  ), s).next(new CraftingPanel<>(s))
                )
              )
            ).e(
              "item_conduit",
              e -> e.s(
                s -> s.p(
                  new Info(IMG.ENERGY_CABLE, s),
                  new RedstonePanel<>(s).next(new CraftingPanel<>(s))
                )
              )
            ).e(
              "redstone_conduit",
              e -> e.s(
                s -> s.p(
                  new Info(IMG.ENERGY_CABLE, s),
                  new RedstonePanel<>(s).next(new CraftingPanel<>(s))
                )
              )
            ).e(
              "energy_cell",
              e -> e.s(
                s -> s.p(
                  new Info(IMG.ENERGY_CABLE, s),
                  new EnergyPanel<>(s).next(new CraftingPanel<>(s))
                )
              )
            ).e(
              "fluid_tank",
              e -> e.s(
                s -> s.p(
                  new Info(IMG.ENERGY_CABLE, s),
                  new FluidPanel<>(s).next(new CraftingPanel<>(s))
                )
              )
            ),
            new WelcomePanel(as)
          )
        )
      )
      .e(
        "generators",
        new Icon(BlockRegistration.SP.get(SolarPanel.T1)),
        ae -> ae.s(
          as -> as.p(
            new GridPage(as).e(
              "sp",
              e -> e.s(
                s -> s.p(
                  new Info(s),
                  new EnergyPanel<>(s).next(new CraftingPanel<>(s))
                )
              )
            ),
            new WelcomePanel(as)
          )
        )
      ).e(
        "machines",
        new Icon(BlockRegistration.FURNACE.get(Furnace.IRON)),
        ae -> ae.s(
          as -> as.p(
            new GridPage(as).e(
              "furnace",
              new Icon(BlockRegistration.FURNACE.get(Furnace.NETHERITE)),
              e -> e.s(
                s -> s.p(
                  new Info(s),
                  new EnergyPanel<>(s).next(new CraftingPanel<>(s))
                )
              )
            ).e(
              "chest",
              new Icon(BlockRegistration.CHEST.get(Chest.NETHERITE)),
              e -> e.s(
                s -> s.p(
                  new Info(s),
                  new EnergyPanel<>(s).next(new CraftingPanel<>(s))
                )
              )
            ),
            new WelcomePanel(as)
          )
        )
      ).e(
        "items",
        new Icon(ItemRegistration.MACHINE_CASING.get()),
        ae -> ae.s(
          as -> as.p(
            new GridPage(as).e(
              "wrench",
              e -> e.s(
                s -> s.p(
                  new Info(s),
                  new ItemPanel<>(s).next(new CraftingPanel<>(s))
                )
              )
            ).e(
              "photovoltaic_cell",
              e -> e.s(
                s -> s.p(
                  new Info(s),
                  new ItemPanel<>(s).next(new CraftingPanel<>(s))
                )
              )
            ).e(
              "red_matter",
              e -> e.s(
                s -> s.p(
                  new Info(s),
                  new ItemPanel<>(s).next(new CraftingPanel<>(s))
                )
              )
            ).e(
              "black_pearl",
              e -> e.s(
                s -> s.p(
                  new Info(s),
                  new ItemPanel<>(s).next(new CraftingPanel<>(s))
                )
              )
            ),
            new WelcomePanel(as)
          )
        )
      ).e(
        "multiblocks_storages",
        new Icon(BlockRegistration.ENERGY_HATCH.get(MultiblockPartStorage.Energy.ADVANCED)),
        ae -> ae.s(
          as -> as.p(
            new GridPage(as).e(
              "energy_hatch",
              e -> e.s(
                s -> s.p(
                  new Info(s),
                  new EnergyPanel<>(s).next(new CraftingPanel<>(s))
                )
              )
            ).e(
              "fluid_input_tank",
              e -> e.s(
                s -> s.p(
                  new Info(s),
                  new FluidPanel<>(s).next(new CraftingPanel<>(s))
                )
              )
            ).e(
              "fluid_output_tank",
              e -> e.s(
                s -> s.p(
                  new Info(s),
                  new FluidPanel<>(s).next(new CraftingPanel<>(s))
                )
              )
            ).e(
              "input_bus",
              e -> e.s(
                s -> s.p(
                  new Info(s),
                  new RedstonePanel<>(s).next(new CraftingPanel<>(s))
                )
              )
            ).e(
              "output_bus",
              e -> e.s(
                s -> s.p(
                  new Info(s),
                  new RedstonePanel<>(s).next(new CraftingPanel<>(s))
                )
              )
            ),
            new WelcomePanel(as)
          )
        )
      );
  }

  public static void register() {
  }
}
