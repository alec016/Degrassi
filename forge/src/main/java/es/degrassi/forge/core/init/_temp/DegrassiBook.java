package es.degrassi.forge.core.init._temp;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import vazkii.patchouli.common.book.Book;
import vazkii.patchouli.xplat.XplatModContainer;

public class DegrassiBook extends Book {
  private static final JsonObject book = new JsonObject();

  static {
    book.addProperty("name", "Testing With PatchouliAPI books");
  }

  public DegrassiBook(XplatModContainer owner, ResourceLocation id, boolean external) {
    super(book, owner, id, external);
  }
}
