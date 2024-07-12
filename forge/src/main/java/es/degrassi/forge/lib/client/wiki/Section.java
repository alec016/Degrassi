package es.degrassi.forge.lib.client.wiki;

import es.degrassi.forge.lib.client.wiki.page.panel.Panel;
import lombok.Getter;

public class Section {
  private final Entry parent;
  @Getter
  private Page page;
  @Getter
  private Page panel;
  public Section(Entry parent) {
    this.parent = parent;
  }

  public Section p(Page page, Panel panel) {
    this.page = page;
    this.panel = panel;
    return this;
  }

  public Entry getEntry() {
    return parent;
  }

}
