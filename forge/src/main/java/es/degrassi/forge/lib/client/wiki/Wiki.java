package es.degrassi.forge.lib.client.wiki;

import dev.architectury.event.events.client.ClientRecipeUpdateEvent;
import dev.architectury.platform.Platform;
import es.degrassi.forge.Degrassi;
import es.degrassi.common.utils.DegrassiLogger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.jetbrains.annotations.Nullable;

@Getter
public class Wiki {
  private static boolean initialized = false;
  public static final Marker MARKER = new MarkerManager.Log4jMarker("Wiki");
  public static final Map<String, Wiki> WIKIS = new HashMap<>();
  private final List<Entry> categories = new ArrayList<>();
  private final Map<ItemLike, List<Recipe<?>>> crafting = new HashMap<>();
  private final Map<ItemLike, List<Recipe<?>>> smelting = new HashMap<>();
  private final String modId;

  public Wiki() {
    this.modId = Degrassi.MODID;
    WIKIS.put(this.modId, this);
  }

  public Wiki e(String name, Consumer<Entry> consumer) {
    return e(name, null, consumer);
  }

  public Wiki e(String name, @Nullable Icon icon, Consumer<Entry> consumer) {
    Entry entry = new Entry(name, icon, this);
    entry.setMain(true);
    entry.setParent(entry);
    consumer.accept(entry);
    register(entry);
    return this;
  }

  public Entry register(Entry entry) {
    this.categories.add(entry);
    return entry;
  }

  public String getModName() {
    return Platform.getMod(this.modId).getName();
  }

  public String getModVersion() {
    return Platform.getMod(this.modId).getVersion();
  }

  private static void init(RecipeManager recipeManager) {
    var registryAccess = Minecraft.getInstance().level.registryAccess();

    StopWatch watch = StopWatch.createStarted();
    DegrassiLogger.INSTANCE.info(MARKER, "Started wikis recipes collecting...");
    WIKIS.forEach((s, wiki) -> {
      BuiltInRegistries.ITEM.stream().filter(i -> BuiltInRegistries.ITEM.getKey(i).getNamespace().equals(Degrassi.MODID)).forEach(item -> {
        List<Recipe<?>> crafting = new ArrayList<>();
        recipeManager.getAllRecipesFor(RecipeType.CRAFTING).forEach(recipe -> {
          if (recipe.getResultItem(registryAccess).is(item)) {
            crafting.add(recipe);
          }
        });
        wiki.crafting.put(item, crafting);
        List<Recipe<?>> smelting = new ArrayList<>();
        recipeManager.getAllRecipesFor(RecipeType.CRAFTING).forEach(recipe -> {
          if (recipe.getResultItem(registryAccess).is(item)) {
            smelting.add(recipe);
          }
        });
        wiki.smelting.put(item, smelting);
      });
    });
    watch.stop();
    DegrassiLogger.INSTANCE.info(MARKER, "Wiki recipes collecting completed in: {} ms", watch.getTime());
  }

  static {
    ClientRecipeUpdateEvent.EVENT.register(Wiki::init);
    if (!initialized && !WIKIS.isEmpty()) {
      init(Minecraft.getInstance().level.getRecipeManager());
      initialized = true;
    }
  }
}
