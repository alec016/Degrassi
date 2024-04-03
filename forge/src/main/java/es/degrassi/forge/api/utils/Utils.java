package es.degrassi.forge.api.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.Nullable;

public class Utils {
  public static final DecimalFormat decimalFormat = new DecimalFormat("#0.0#");
  @SuppressWarnings("unchecked")
  @Nullable
  public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
    BlockEntityType<A> p_152133_,
    BlockEntityType<E> p_152134_,
    BlockEntityTicker<? super E> p_152135_
  ) {
    return p_152134_ == p_152133_ ? (BlockEntityTicker<A>) p_152135_ : null;
  }

  public static String format(long value) {
    return decimalFormat.format(value).replaceAll(",", ".");
  }

  public static String format(double value) {
    return decimalFormat.format(value).replaceAll(",", ".");
  }

  public static String addCommas(long value) {
    return NumberFormat.getInstance(Locale.ROOT).format(value);
  }
}
