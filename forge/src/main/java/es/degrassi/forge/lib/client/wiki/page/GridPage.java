package es.degrassi.forge.lib.client.wiki.page;

import es.degrassi.forge.lib.client.screen.Texture;
import es.degrassi.forge.lib.client.screen.widget.IconButton;
import es.degrassi.forge.lib.client.screen.wiki.WikiScreen;
import es.degrassi.forge.lib.client.wiki.Entry;
import es.degrassi.forge.lib.client.wiki.MC;
import es.degrassi.forge.lib.client.wiki.Section;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class GridPage extends EntriesPage {
  public GridPage(Section parent) {
    this("", parent);
  }

  public GridPage(String name, Section parent) {
    super(name, parent);
  }

  @Override
  public void init(int x, int y, WikiScreen screen) {
    super.init(x, y, screen);
    for (int i = 0; i < 8; ++i) {
      for (int j = 0; j < 6; ++j) {
        int index = j + i * 6;
        if (index < this.entries.size()) {
          Entry e = this.entries.get(index);
          screen.addButton2(new IconButton(27 + x + 13 + j * 29, 17 + y + 13 + i * 29, e.getStack(), Texture.WIKI_FRM, button -> {
            MC.open(new WikiScreen(e.getSections(0)));
          }, screen).setTooltip(Component.translatable(e.getTransKey())));
        } else
          break;
      }
    }
  }

  @Override
  public void render(GuiGraphics gui, int x, int y, int mx, int my, float pt, Font font, WikiScreen screen) {
    gui.drawString(font, Component.translatable(getSection().getEntry().getTransKey()), x + 10, y + 10, 0x444444, false);
  }
}
