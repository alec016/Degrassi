package es.degrassi.forge.core.init;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import es.degrassi.common.registry.IVariant;
import es.degrassi.common.registry.VarReg;
import es.degrassi.forge.Degrassi;
import es.degrassi.forge.core.common.machines.item.PhotovoltaicCellItem;
import es.degrassi.forge.core.common.machines.item.WrenchItem;
import es.degrassi.forge.core.common.machines.item.BookItem;
import es.degrassi.forge.core.common.machines.multiblock.parts.item.MelterFrameItem;
import es.degrassi.forge.core.tiers.PhotovoltaicCell;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class ItemRegistration {
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Degrassi.MODID, Registries.ITEM);

  // ITEMS
  public static final RegistrySupplier<WrenchItem> WRENCH = ITEMS.register("wrench", WrenchItem::new);
  public static final RegistrySupplier<BookItem> BOOK = ITEMS.register("book", () -> new BookItem(new Item.Properties().stacksTo(1)));
  public static final RegistrySupplier<Item> RED_MATTER = ITEMS.register("red_matter", () -> new Item(new Item.Properties()));
  public static final RegistrySupplier<Item> BLACK_PEARL = ITEMS.register("black_pearl", () -> new Item(new Item.Properties()));
  public static final VarReg<PhotovoltaicCell, Item> PHOTOVOLTAIC_CELL = register("photovoltaic_cell", PhotovoltaicCellItem::new, PhotovoltaicCell.getNormalizedVariants());

  // BLOCK ITEMS
  public static final RegistrySupplier<BlockItem> MACHINE_CASING = ITEMS.register("machine_casing", () -> new BlockItem(BlockRegistration.MACHINE_CASING.get(), new Item.Properties()));
  public static final RegistrySupplier<BlockItem> MELTER_FRAME = ITEMS.register("melter_frame", () -> new MelterFrameItem(BlockRegistration.MELTER_FRAME.get(), new Item.Properties()));

  // multiblock controller item
  public static final RegistrySupplier<BlockItem> MELTER_CONTROLLER = ITEMS.register("melter_controller", () -> new BlockItem(BlockRegistration.MELTER_CONTROLLER.get(), new Item.Properties()));

  // Digital Storage
  public static final RegistrySupplier<BlockItem> DIGITAL_CONTROLLER = ITEMS.register("controller", () -> new BlockItem(BlockRegistration.DIGITAL_CONTROLLER.get(), new Item.Properties()));

  private static <V extends Enum<V> & IVariant<V>> VarReg<V, Item> register(String name, VarReg.VariantConstructor<V, Item> ctor, V[] variants) {
    return new VarReg<>(ITEMS, name, ctor, variants);
  }
}
