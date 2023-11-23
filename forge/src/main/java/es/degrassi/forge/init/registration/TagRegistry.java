package es.degrassi.forge.init.registration;

import es.degrassi.forge.Degrassi;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Locale;
import java.util.Objects;

import static es.degrassi.forge.init.registration.TagRegistry.NameSpace.MOD;

@SuppressWarnings("unused")
public class TagRegistry {
  public static <T> @NotNull TagKey<T> optionalTag(@NotNull IForgeRegistry<T> registry, ResourceLocation id) {
    return Objects.requireNonNull(registry.tags())
      .createOptionalTagKey(id, Collections.emptySet());
  }

  public static <T> @NotNull TagKey<T> forgeTag(IForgeRegistry<T> registry, String path) {
    return optionalTag(registry, new ResourceLocation("forge", path));
  }

  public static @NotNull TagKey<Block> forgeBlockTag(String path) {
    return forgeTag(ForgeRegistries.BLOCKS, path);
  }

  public static @NotNull TagKey<Item> forgeItemTag(String path) {
    return forgeTag(ForgeRegistries.ITEMS, path);
  }

  public static @NotNull TagKey<Fluid> forgeFluidTag(String path) {
    return forgeTag(ForgeRegistries.FLUIDS, path);
  }

  public enum NameSpace {
    MOD(Degrassi.MODID, false, true),
    FORGE("forge");

    public final String id;
    public final boolean optionalDefault;
    public final boolean alwaysDatagenDefault;
    NameSpace(String id) {
      this(id, true, false);
    }
    NameSpace(String id, boolean optionalDefault, boolean alwaysDatagenDefault) {
      this.id = id;
      this.optionalDefault = optionalDefault;
      this.alwaysDatagenDefault = alwaysDatagenDefault;
    }
  }

  public enum AllBlockTags {
    PISTONS(NameSpace.FORGE),
    BLOCKS,
    FURNACES,
    MACHINES,
    SOLAR_PANELS(MOD, "panels/solar_panels");

    public final TagKey<Block> tag;
    public final boolean allwaysDatagen;
    AllBlockTags() {
      this(MOD);
    }
    AllBlockTags(NameSpace namespace) {
      this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
    }
    AllBlockTags(NameSpace namespace, String path) {
      this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
    }

    AllBlockTags(NameSpace namespace, boolean optionalDefault, boolean alwaysDatagen) {
      this(namespace, null, optionalDefault, alwaysDatagen);
    }
    AllBlockTags(@NotNull NameSpace nameSpace, String path, boolean optional, boolean alwaysDatagen) {
      ResourceLocation id = new ResourceLocation(nameSpace.id, path == null ? name().toLowerCase(Locale.ROOT) : path);
      if (optional) tag = optionalTag(ForgeRegistries.BLOCKS, id);
      else tag = BlockTags.create(id);
      this.allwaysDatagen = alwaysDatagen;
    }

    @SuppressWarnings("deprecation")
    public boolean matches(@NotNull Block block) {
      return block.builtInRegistryHolder().is(tag);
    }

    public boolean matches(ItemStack stack) {
      return stack != null && stack.getItem() instanceof BlockItem blockItem && matches(blockItem.getBlock());
    }

    public boolean matches(@NotNull BlockState state) {
      return state.is(tag);
    }

    private static void init() {

    }
  }

  public enum AllItemTags {
    PISTONS(NameSpace.FORGE),
    ITEMS,
    PHOTOVOLTAIC_CELLS(MOD, "panels/photovoltaic_cells"),
    UPGRADES,
    MACHINES,
    SOLAR_PANELS(MOD, "panels/solar_panels"),
    FURNACES;

    public final TagKey<Item> tag;
    public final boolean alwaysDatagen;

    AllItemTags() {
      this(MOD);
    }

    AllItemTags(NameSpace namespace) {
      this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
    }

    AllItemTags(NameSpace namespace, String path) {
      this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
    }

    AllItemTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
      this(namespace, null, optional, alwaysDatagen);
    }

    AllItemTags(@NotNull NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
      ResourceLocation id = new ResourceLocation(namespace.id, path == null ? name().toLowerCase(Locale.ROOT) : path);
      if (optional) {
        tag = optionalTag(ForgeRegistries.ITEMS, id);
      } else {
        tag = ItemTags.create(id);
      }
      this.alwaysDatagen = alwaysDatagen;
    }

    @SuppressWarnings("deprecation")
    public boolean matches(@NotNull Item item) {
      return item.builtInRegistryHolder()
        .is(tag);
    }

    public boolean matches(@NotNull ItemStack stack) {
      return stack.is(tag);
    }
    private static void init() {

    }
  }

  public enum AllFluidTags {
    FLUIDS;

    public final TagKey<Fluid> tag;
    public final boolean alwaysDatagen;

    AllFluidTags() {
      this(MOD);
    }

    AllFluidTags(NameSpace namespace) {
      this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
    }

    AllFluidTags(NameSpace namespace, String path) {
      this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
    }

    AllFluidTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
      this(namespace, null, optional, alwaysDatagen);
    }

    AllFluidTags(@NotNull NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
      ResourceLocation id = new ResourceLocation(namespace.id, path == null ? name().toLowerCase(Locale.ROOT) : path);
      if (optional) {
        tag = optionalTag(ForgeRegistries.FLUIDS, id);
      } else {
        tag = FluidTags.create(id);
      }
      this.alwaysDatagen = alwaysDatagen;
    }

    @SuppressWarnings("deprecation")
    public boolean matches(@NotNull Fluid fluid) {
      return fluid.is(tag);
    }

    public boolean matches(@NotNull FluidState state) {
      return state.is(tag);
    }

    private static void init() {
    }
  }

  public static void register() {
    AllBlockTags.init();
    AllItemTags.init();
    AllFluidTags.init();}
}
