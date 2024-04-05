package es.degrassi.forge.core.common.conduit.common.init;

import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.util.entry.MenuEntry;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.common.conduit.client.gui.ConduitScreen;
import es.degrassi.forge.core.common.conduit.common.menu.ConduitMenu;

public class ConduitMenus {
    private static final Registrate REGISTRATE = Degrassi.registrate();

    public static final MenuEntry<ConduitMenu> CONDUIT_MENU = REGISTRATE.menu("conduit", ConduitMenu::factory, () -> ConduitScreen::new).register();

    public static void register() {}
}
