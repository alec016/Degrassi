package es.degrassi.forge.core.tiers;

import es.degrassi.common.registry.IVariant;
import java.util.Locale;
import lombok.Getter;
import net.minecraft.util.StringRepresentable;

public abstract class MultiblockPartStorage {
  public interface MultiblockPartTieredSerialization<V extends Enum<V> & IVariant<V>> {
    String serializeNBT();
    V deserializeNBT(String nbt);
  }

  @Getter
  public enum Energy implements IVariant<Energy>, MultiblockPartTieredSerialization<Energy> {
    BASIC(25_000),
    ADVANCED(100_000),
    EXTREME(500_000);

    private final int capacity;

    Energy(int capacity) {
      this.capacity = capacity;
    }

    @Override
    public Energy[] getVariants() {
      return values();
    }

    public static Energy[] getNormalVariants() {
      return new Energy[] { BASIC, ADVANCED, EXTREME };
    }

    @Override
    public String serializeNBT() {
      return nameL();
    }

    @Override
    public Energy deserializeNBT(String nbt) {
      return switch (nbt.toLowerCase(Locale.ROOT)) {
        case "advanced" -> ADVANCED;
        case "extreme" -> EXTREME;
        default -> BASIC;
      };
    }
  }

  public static abstract class Fluid {
    @Getter
    public enum Input implements IVariant<Input>, MultiblockPartTieredSerialization<Input> {
      BASIC(16_000),
      ADVANCED(64_000),
      EXTREME(128_000);

      /**
       * Capacity in mB
       */
      private final int capacity;

      Input(int capacity) {
        this.capacity = capacity;
      }

      @Override
      public Input[] getVariants() {
        return values();
      }

      public static Input[] getNormalVariants() {
        return new Input[] { BASIC, ADVANCED, EXTREME };
      }

      @Override
      public String serializeNBT() {
        return nameL();
      }

      @Override
      public Input deserializeNBT(String nbt) {
        return switch (nbt.toLowerCase(Locale.ROOT)) {
          case "advanced" -> ADVANCED;
          case "extreme" -> EXTREME;
          default -> BASIC;
        };
      }
    }

    @Getter
    public enum Output implements IVariant<Output>, MultiblockPartTieredSerialization<Output> {
      BASIC(16_000),
      ADVANCED(64_000),
      EXTREME(128_000);

      /**
       * Capacity in mB
       */
      private final int capacity;

      Output(int capacity) {
        this.capacity = capacity;
      }
      @Override
      public Output[] getVariants() {
        return values();
      }

      public static Output[] getNormalVariants() {
        return new Output[] { BASIC, ADVANCED, EXTREME };
      }

      @Override
      public String serializeNBT() {
        return nameL();
      }

      @Override
      public Output deserializeNBT(String nbt) {
        return switch (nbt.toLowerCase(Locale.ROOT)) {
          case "advanced" -> ADVANCED;
          case "extreme" -> EXTREME;
          default -> BASIC;
        };
      }
    }
  }

  public static abstract class Item {
    @Getter
    public enum Input implements IVariant<Input>, MultiblockPartTieredSerialization<Input> {
      BASIC(1, 1),
      ADVANCED(2, 2),
      EXTREME(3, 3);

      private final int rows, cols;

      Input(int rows, int cols) {
        this.cols = cols;
        this.rows = rows;
      }

      @Override
      public Input[] getVariants() {
        return values();
      }

      public static Input[] getNormalVariants() {
        return new Input[] { BASIC, ADVANCED, EXTREME };
      }

      public int getTotalSlots() {
        return rows * cols;
      }

      @Override
      public String serializeNBT() {
        return nameL();
      }

      @Override
      public Input deserializeNBT(String nbt) {
        return switch (nbt.toLowerCase(Locale.ROOT)) {
          case "advanced" -> ADVANCED;
          case "extreme" -> EXTREME;
          default -> BASIC;
        };
      }
    }

    @Getter
    public enum Output implements IVariant<Output>, MultiblockPartTieredSerialization<Output> {
      BASIC(1, 1),
      ADVANCED(2, 2),
      EXTREME(3, 3);

      private final int rows, cols;

      Output(int rows, int cols) {
        this.cols = cols;
        this.rows = rows;
      }

      @Override
      public Output[] getVariants() {
        return values();
      }

      public static Output[] getNormalVariants() {
        return new Output[] { BASIC, ADVANCED, EXTREME };
      }

      public int getTotalSlots() {
        return rows * cols;
      }

      @Override
      public String serializeNBT() {
        return nameL();
      }

      @Override
      public Output deserializeNBT(String nbt) {
        return switch (nbt.toLowerCase(Locale.ROOT)) {
          case "advanced" -> ADVANCED;
          case "extreme" -> EXTREME;
          default -> BASIC;
        };
      }
    }
  }
}