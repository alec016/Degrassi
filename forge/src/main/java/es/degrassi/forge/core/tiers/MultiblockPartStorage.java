package es.degrassi.forge.core.tiers;

import es.degrassi.common.registry.IVariant;
import lombok.Getter;

public abstract class MultiblockPartStorage {
  @Getter
  public enum Energy implements IVariant<Energy> {
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
  }

  public static abstract class Fluid {
    @Getter
    public enum Input implements IVariant<Input> {
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
    }

    @Getter
    public enum Output implements IVariant<Output> {
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
    }
  }

  public static abstract class Item {
    @Getter
    public enum Input implements IVariant<Input> {
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
    }

    @Getter
    public enum Output implements IVariant<Output> {
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
    }
  }
}