package es.degrassi.comon.util;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.RoundingMode;
import java.text.CompactNumberFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

@SuppressWarnings("unchecked")
public class Utils {
  public static final CompactNumberFormat COMPACT_NUMBER_FORMAT;
  static {
    COMPACT_NUMBER_FORMAT = new CompactNumberFormat(
      "#0.0#",
      new DecimalFormatSymbols(new Locale("en", "us")),
      new String[] { "0", "00", "000", "0k", "00k", "000k", "0M", "00M", "000M", "0G", "00G", "000G", "0T", "00T", "000T" }
    );
    COMPACT_NUMBER_FORMAT.setRoundingMode(RoundingMode.FLOOR);
    COMPACT_NUMBER_FORMAT.setMaximumFractionDigits(2);
  }
  @Nullable
  public static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> p_152133_, BlockEntityType<E> p_152134_, BlockEntityTicker<? super E> p_152135_) {
    return p_152134_ == p_152133_ ? (BlockEntityTicker<A>) p_152135_ : null;
  }

  public static @NotNull String format(int number) {
    return format((long) number);
  }

  public static @NotNull String format(long number) {
    return format((double) number);
  }

  public static @NotNull String format(double number) {
    return format((float) number);
  }

  public static @NotNull String format(@NotNull String n) {
    float number = Float.parseFloat(n.contains(",") ? n.replaceAll(",", ".") : n);
    return format(number);
  }

  public static @NotNull String format(float number) {
    return COMPACT_NUMBER_FORMAT.format(number);
  }
}
